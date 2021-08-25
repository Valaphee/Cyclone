/*
 * Copyright (c) by Valaphee 2019.
 *
 * Licensed under the 4-clause BSD license (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      https://deploy.valaphee.com/license/BSD-4-Clause.txt
 *
 * THIS SOFTWARE IS PROVIDED BY VALAPHEE "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.
 */

package com.valaphee.cyclone.communication;

import com.valaphee.cyclone.connection.ConnectionManager;
import com.valaphee.cyclone.connection.IncomingInitialSubscriber;
import com.valaphee.cyclone.util.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ResourceLeakDetector;
import java.net.SocketAddress;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Default
 *
 * @author valaphee
 */
public final class Communication
{
	static final Logger LOGGER = LogManager.getLogger(Communication.class);
	private static final boolean EPOLL;
	private static final EventLoopGroup BOSS_GROUP;
	private static final EventLoopGroup WORKER_GROUP;
	private final List<Channel> endpoints = Collections.synchronizedList(new ArrayList<>());
	private final List<ProtocolHandler> handlers = Collections.synchronizedList(new ArrayList<>());
	private final CommunicationMetricsHandler metrics = new CommunicationMetricsHandler();

	public ProtocolHandler connect(final SocketAddress address, final ProtocolSubscriber initial)
	{
		final ProtocolHandler handler = new ProtocolHandler(initial);
		new Bootstrap().group(WORKER_GROUP).channel(getChannel()).handler(new ChannelInitializer<SocketChannel>()
		{
			@Override
			protected void initChannel(final SocketChannel channel)
					throws Exception
			{
				channel.pipeline().addLast("metrics", metrics);
				channel.pipeline().addLast("frame-prepender", new LengthFieldPrepender());
				channel.pipeline().addLast("packet-encoder", new ProtocolEncoder());
				channel.pipeline().addLast("frame-splitter", new LengthSplitter());
				channel.pipeline().addLast("packet-decoder", new ProtocolDecoder());
				channel.pipeline().addLast("packet-handler", handler);
				handlers.add(handler);
			}
		}).option(ChannelOption.SO_KEEPALIVE, false).option(ChannelOption.TCP_NODELAY, true).remoteAddress(address).connect().addListener((ChannelFuture future) ->
		{
			if (!future.isSuccess())
			{
				LOGGER.log(Level.WARN, "Address {} is not reachable or invalid.", address.toString());
			}
		}).syncUninterruptibly();

		return handler;
	}

	public void listen(final SocketAddress address, final ConnectionManager connectionManager)
	{
		new ServerBootstrap().group(BOSS_GROUP, WORKER_GROUP).channel(getServerChannel()).childHandler(new ChannelInitializer<SocketChannel>()
		{
			@Override
			protected void initChannel(final SocketChannel channel)
					throws Exception
			{
				final ProtocolHandler handler = new ProtocolHandler(new IncomingInitialSubscriber(connectionManager));
				channel.pipeline().addLast("metrics", metrics);
				channel.pipeline().addLast("frame-prepender", new LengthFieldPrepender());
				channel.pipeline().addLast("packet-encoder", new ProtocolEncoder());
				channel.pipeline().addLast("frame-splitter", new LengthSplitter());
				channel.pipeline().addLast("packet-decoder", new ProtocolDecoder());
				channel.pipeline().addLast("packet-handler", handler);
				handlers.add(handler);
			}
		}).option(ChannelOption.SO_BACKLOG, 64).childOption(ChannelOption.SO_KEEPALIVE, false).childOption(ChannelOption.TCP_NODELAY, true).localAddress(address).bind().addListener((ChannelFuture future) ->
		{
			if (!future.isSuccess())
			{
				LOGGER.log(Level.FATAL, "Address {} is already in use or invalid.", address.toString());
			}
			else
			{
				endpoints.add(future.channel());
			}
		}).syncUninterruptibly();
	}

	public void update()
	{
		synchronized (handlers)
		{
			final Iterator<ProtocolHandler> iterator = handlers.iterator();
			while (iterator.hasNext())
			{
				final ProtocolHandler handler = iterator.next();
				if (!handler.update())
				{
					iterator.remove();
				}
			}
		}
	}

	public void shutdown()
	{
		endpoints.forEach((endpoint) ->
		{
			try
			{
				endpoint.close().sync();
			}
			catch (final InterruptedException ignore)
			{}
		});
		endpoints.clear();

		WORKER_GROUP.shutdownGracefully();
		try
		{
			WORKER_GROUP.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		}
		catch (final InterruptedException ignore)
		{}

		BOSS_GROUP.shutdownGracefully();
		try
		{
			BOSS_GROUP.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		}
		catch (final InterruptedException ignore)
		{}
	}

	public CommunicationMetrics getMetrics()
	{
		return metrics;
	}

	private static Class<? extends Channel> getChannel()
	{
		return EPOLL ? EpollSocketChannel.class : NioSocketChannel.class;
	}

	private static Class<? extends ServerChannel> getServerChannel()
	{
		return EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
	}

	static
	{
		Security.setProperty("networkaddress.cache.ttl", "32");
		Security.setProperty("networkaddress.cache.negative.ttl", "16");
		System.setProperty("io.netty.selectorAutoRebuildThreshold", "0");
		if (System.getProperty("io.netty.leakDetectionLevel") == null)
		{
			ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
		}

		EPOLL = Epoll.isAvailable();
		BOSS_GROUP = EPOLL ? new EpollEventLoopGroup(0, Executors.newCachedThreadPool(new ThreadFactoryBuilder().setName("Network Listener #%1$d").setDeamon(true).build())) : new NioEventLoopGroup(0, Executors.newCachedThreadPool(new ThreadFactoryBuilder().setName("Network Listener #%1$d").setDeamon(true).build()));
		WORKER_GROUP = EPOLL ? new EpollEventLoopGroup(0, Executors.newCachedThreadPool(new ThreadFactoryBuilder().setName("Network Handler #%1$d").setDeamon(true).build())) : new NioEventLoopGroup(0, Executors.newCachedThreadPool(new ThreadFactoryBuilder().setName("Network Handler #%1$d").setDeamon(true).build()));
	}
}
