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
import com.valaphee.cyclone.reflect.Accessors;
import com.valaphee.cyclone.reflect.ConstructorAccessor;
import com.valaphee.cyclone.reflect.MethodAccessor;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.AttributeKey;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.protocol.*;

/**
 * Default
 *
 * @author valaphee
 */
public final class ProtocolInitializer
		extends ChannelInitializer<SocketChannel>
{
	private static ConstructorAccessor HANDLER_BOSS;
	private static MethodAccessor HANDLER_BOSS_SET_HANDLER;
	private static ConstructorAccessor INITIAL_HANDLER;
	public static final AttributeKey<ListenerInfo> LISTENER = AttributeKey.valueOf("ListerInfo");
	public static final String TIMEOUT_HANDLER = "timeout";
	public static final String PACKET_DECODER = "packet-decoder";
	public static final String PACKET_ENCODER = "packet-encoder";
	public static final String BOSS_HANDLER = "inbound-boss";
	public static final String FRAME_DECODER = "frame-decoder";
	public static final String FRAME_PREPENDER = "frame-prepender";
	public static final String LEGACY_DECODER = "legacy-decoder";
	public static final String LEGACY_KICKER = "legacy-kick";
	public static final String PACKET_INTERCEPTOR = "packet-interceptor";
	private final Cyclone cyclone;

	public ProtocolInitializer(final Cyclone cyclone)
	{
		this.cyclone = cyclone;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void initChannel(final SocketChannel channel)
			throws Exception
	{
		if (!cyclone.getDenialOfServiceBlocker().available(channel.remoteAddress().getAddress()))
		{
			channel.close().syncUninterruptibly();

			return;
		}

		channel.pipeline().addLast(TIMEOUT_HANDLER, new ReadTimeoutHandler(cyclone.getProxy().getConfig().getTimeout(), TimeUnit.MILLISECONDS));
		channel.pipeline().addLast(LEGACY_KICKER, new KickStringWriter());
		channel.pipeline().addLast(FRAME_PREPENDER, new VarInt21LengthFieldPrepender());
		channel.pipeline().addLast(PACKET_ENCODER, new MinecraftEncoder(Protocol.HANDSHAKE, true, cyclone.getProxy().getProtocolVersion()));
		channel.pipeline().addLast(LEGACY_DECODER, new LegacyDecoder());
		channel.pipeline().addLast(FRAME_DECODER, new VarInt21FrameDecoder());
		channel.pipeline().addLast(PACKET_DECODER, new MinecraftDecoder(Protocol.HANDSHAKE, true, cyclone.getProxy().getProtocolVersion()));
		channel.pipeline().addLast(PACKET_INTERCEPTOR, new ProtocolInterceptor());

		final Object handlerBoss = HANDLER_BOSS.invoke();
		HANDLER_BOSS_SET_HANDLER.invoke(handlerBoss, INITIAL_HANDLER.invoke(cyclone.getProxy(), channel.attr(LISTENER).get()));
		channel.pipeline().addLast(BOSS_HANDLER, (ChannelHandler) handlerBoss);
	}

	static
	{
		try
		{
			final Class<?> handlerBoss = Class.forName("net.md_5.bungee.netty.HandlerBoss");
			final Class<?> packetHandler = Class.forName("net.md_5.bungee.netty.PacketHandler");
			final Class<?> initialHandler = Class.forName("net.md_5.bungee.connection.InitialHandler");
			final Class<?> bungeeCord = Class.forName("net.md_5.bungee.BungeeCord");

			HANDLER_BOSS = Accessors.getConstructorAccessorOrNull(handlerBoss);
			HANDLER_BOSS_SET_HANDLER = Accessors.getMethodAccessorOrNull(handlerBoss, "setHandler", packetHandler);
			INITIAL_HANDLER = Accessors.getConstructorAccessorOrNull(initialHandler, bungeeCord, ListenerInfo.class);
		}
		catch (final ClassNotFoundException ignore)
		{}
	}
}
