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

import com.valaphee.cyclone.Cyclone;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.*;

/**
 * Default
 *
 * @author valaphee
 */
public final class ProtocolInterceptor
		extends MessageToMessageDecoder<PacketWrapper>
{
	private int protocolVersion;
	private volatile int step = 0;

	@Override
	protected void decode(final ChannelHandlerContext ctx, final PacketWrapper msg, final List<Object> out)
			throws Exception
	{
		if ((msg == null) || (msg.packet == null))
		{
			return;
		}
		else if (msg.packet instanceof Handshake)
		{
			if (step != 0)
			{
				ctx.close();

				return;
			}

			final Handshake packet = (Handshake) msg.packet;
			protocolVersion = packet.getProtocolVersion();
			if (packet.getRequestedProtocol() != 1)
			{
				ctx.channel().pipeline().remove(ProtocolInitializer.LEGACY_KICKER);
				ctx.channel().pipeline().remove(ProtocolInitializer.PACKET_INTERCEPTOR);
			}

			step = 1;
		}
		else if (msg.packet instanceof StatusRequest)
		{
			if (step != 1)
			{
				ctx.close();

				return;
			}

			ctx.pipeline().writeAndFlush(new StatusResponse(Cyclone.getInstance().getStatusManager().getGenericResponse(protocolVersion)));

			step = 2;

			return;
		}
		else if (msg.packet instanceof LegacyPing)
		{
			final LegacyPing packet = (LegacyPing) msg.packet;
			if (packet.isV1_5())
			{
				ctx.pipeline().writeAndFlush(Cyclone.getInstance().getStatusManager().getLegacyColoredResponse());
			}
			else
			{
				ctx.pipeline().writeAndFlush(Cyclone.getInstance().getStatusManager().getLegacyResponse());
			}
			ctx.close();

			return;
		}
		else if (msg.packet instanceof PingPacket)
		{
			if (step != 2)
			{
				ctx.close();

				return;
			}

			ctx.writeAndFlush(msg.packet);
			ctx.close();

			return;
		}

		out.add(msg);
	}
}
