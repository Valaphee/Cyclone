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

package com.valaphee.cyclone.status;

import com.valaphee.cyclone.util.CharsetUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Default
 *
 * @author valaphee
 */
public final class LANBroadcaster
		extends ChannelOutboundHandlerAdapter
{
	private final String ad;
	private ChannelHandlerContext context;
	private InetSocketAddress address;

	public LANBroadcaster(final String ad)
	{
		this.ad = ad;
	}

	@Override
	public void bind(final ChannelHandlerContext ctx, final SocketAddress localAddress, final ChannelPromise promise)
			throws Exception
	{
		context = ctx;
		context.bind(localAddress, promise);
		address = (InetSocketAddress) localAddress;
	}

	public void update(final StatusManager manager)
	{
		final ByteBuf buffer = context.alloc().buffer();
		final StringBuffer message = new StringBuffer();
		message.append("[MOTD]").append(manager.getName()).append("[/MOTD]");
		message.append("[AD]").append(ad).append("[/AD]");
		buffer.writeBytes(message.toString().getBytes(CharsetUtil.UTF_8));
		context.writeAndFlush(new DatagramPacket(null, address));
	}
}
