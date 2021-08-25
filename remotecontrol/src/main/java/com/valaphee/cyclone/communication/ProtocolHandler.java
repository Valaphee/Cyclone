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

import io.netty.channel.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Default
 *
 * @author valaphee
 */
public final class ProtocolHandler
		extends SimpleChannelInboundHandler<Packet<ProtocolSubscriber>>
{
	private final Queue<PacketWithListeners> queued = new ConcurrentLinkedQueue<>();
	private Channel channel;
	private SocketAddress address;
	private ProtocolSubscriber subscriber;

	public ProtocolHandler(final ProtocolSubscriber subscriber)
	{
		this.subscriber = subscriber;
	}

	public Channel getChannel()
	{
		return channel;
	}

	public SocketAddress getAddress()
	{
		return address;
	}

	public ProtocolSubscriber getSubscriber()
	{
		return subscriber;
	}

	public void setSubscriber(final ProtocolSubscriber subscriber)
	{
		this.subscriber = subscriber;
		subscriber.initialize(this);
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx)
			throws Exception
	{
		super.channelActive(ctx);

		channel = ctx.channel();
		address = channel.remoteAddress();
		subscriber.initialize(this);
	}

	@Override
	public void channelInactive(final ChannelHandlerContext ctx)
			throws Exception
	{
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause)
			throws Exception
	{
		super.exceptionCaught(ctx, cause);
	}

	@Override
	protected void messageReceived(final ChannelHandlerContext ctx, final Packet<ProtocolSubscriber> message)
			throws Exception
	{
		message.handle(subscriber);
	}

	public void sendPacket(final Packet<?> packet, final GenericFutureListener<?>... listeners)
	{
		if ((channel != null) && channel.isOpen())
		{
			flushQueue();
			dispatchPacket(packet, listeners);
		}
		else
		{
			queuePacket(packet, listeners);
		}
	}

	private void queuePacket(final Packet<?> packet, final GenericFutureListener<?>[] listeners)
	{
		queued.add(new PacketWithListeners(packet, listeners));
	}

	@SuppressWarnings("unchecked")
	private void dispatchPacket(final Packet<?> packet, final GenericFutureListener<?>[] listeners)
	{
		if (channel.eventLoop().inEventLoop())
		{
			final ChannelFuture future = channel.writeAndFlush(packet);
			if (listeners != null)
			{
				future.addListeners((GenericFutureListener<? extends Future<? super Void>>[]) listeners);
			}
			future.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
		}
		else
		{
			channel.eventLoop().execute(() ->
			{
				final ChannelFuture future = channel.writeAndFlush(packet);
				if (listeners != null)
				{
					future.addListeners((GenericFutureListener<? extends Future<? super Void>>[]) listeners);
				}
				future.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
			});
		}
	}

	private void flushQueue()
	{
		while (!queued.isEmpty())
		{
			final PacketWithListeners packetWithListeners = queued.poll();
			dispatchPacket(packetWithListeners.packet, packetWithListeners.listeners);
		}
	}

	public boolean update()
	{
		if ((channel != null) && (subscriber != null))
		{
			if (channel.isOpen())
			{
				subscriber.update();
				flushQueue();
			}
			else
			{
				subscriber.disconnected();
			}
			channel.flush();
		}

		return channel != null ? channel.isOpen() : false;
	}

	private static final class PacketWithListeners
	{
		final Packet<?> packet;
		final GenericFutureListener<?>[] listeners;

		private PacketWithListeners(final Packet<?> packet, final GenericFutureListener<?>... listeners)
		{
			this.packet = packet;
			this.listeners = listeners;
		}
	}
}
