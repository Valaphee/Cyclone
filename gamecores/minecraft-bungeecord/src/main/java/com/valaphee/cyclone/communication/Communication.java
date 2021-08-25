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

import com.valaphee.cyclone.Cyclone;
import com.valaphee.cyclone.util.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ResourceLeakDetector;
import java.net.SocketAddress;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Default
 *
 * @author valaphee
 */
public final class Communication
{
	private static final boolean EPOLL;
	private static final EventLoopGroup GROUP;
	private final List<ProtocolHandler> handlers = Collections.synchronizedList(new ArrayList<>());

	public ProtocolHandler connect(final SocketAddress address, final ProtocolSubscriber initial)
	{
		final ProtocolHandler handler = new ProtocolHandler(initial);
		new Bootstrap().group(GROUP).channel(getChannel()).handler(new ChannelInitializer<SocketChannel>()
		{
			@Override
			protected void initChannel(final SocketChannel channel)
					throws Exception
			{
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
				Cyclone.LOGGER.log(Level.WARNING, "Address {0} is not reachable or invalid.", address.toString());
			}
		}).syncUninterruptibly();

		return handler;
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
		GROUP.shutdownGracefully();
		try
		{
			GROUP.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		}
		catch (final InterruptedException ignore)
		{}
	}

	public static boolean hasEpoll()
	{
		return EPOLL;
	}

	private static Class<? extends Channel> getChannel()
	{
		return EPOLL ? EpollSocketChannel.class : NioSocketChannel.class;
	}

	public static EventLoopGroup getGroup()
	{
		return GROUP;
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
		GROUP = EPOLL ? new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setName("Network Handler #%1$d").setDeamon(true).build()) : new NioEventLoopGroup(0, new ThreadFactoryBuilder().setName("Network Handler #%1$d").setDeamon(true).build());
	}
}
