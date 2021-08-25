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

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Default
 *
 * @author valaphee
 */
@ChannelHandler.Sharable
public final class CommunicationMetricsHandler
		extends ChannelHandlerAdapter
		implements CommunicationMetrics
{
	private final AtomicInteger receivedPackets = new AtomicInteger();
	private final AtomicLong receivedBytes = new AtomicLong();
	private final AtomicInteger sentPackets = new AtomicInteger();
	private final AtomicLong sentBytes = new AtomicLong();

	@Override
	public void channelRead(final ChannelHandlerContext ctx, final Object msg)
			throws Exception
	{
		super.channelRead(ctx, msg);

		receivedPackets.incrementAndGet();
		receivedBytes.addAndGet(((ByteBuf) msg).readableBytes());
	}

	@Override
	public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise)
			throws Exception
	{
		super.write(ctx, msg, promise);

		sentPackets.incrementAndGet();
		sentBytes.addAndGet(((ByteBuf) msg).readableBytes());
	}

	@Override
	public int getReceivedPackets()
	{
		return receivedPackets.get();
	}

	@Override
	public long getReceivedBytes()
	{
		return receivedBytes.get();
	}

	@Override
	public int getSentPackets()
	{
		return sentPackets.get();
	}

	@Override
	public long getSentBytes()
	{
		return sentBytes.get();
	}
}
