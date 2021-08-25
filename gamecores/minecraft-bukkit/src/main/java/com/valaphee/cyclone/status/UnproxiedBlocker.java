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
import com.valaphee.cyclone.reflect.FieldAccessor;
import com.valaphee.cyclone.reflect.MethodAccessor;
import com.valaphee.cyclone.server.data.ServerRepository;
import io.netty.channel.Channel;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Default
 *
 * @author valaphee
 */
public final class UnproxiedBlocker
{
	private static MethodAccessor MINECRAFT_SERVER_GET_SERVER;
	private static FieldAccessor MINECRAFT_SERVER_SERVER_CONNECTION;
	private static FieldAccessor SERVER_CONNECTION_NETWORK_MANAGERS;
	private static FieldAccessor NETWORK_MANAGER_SPOOFED_UUID;
	private static FieldAccessor NETWORK_MANAGER_CHANNEL;
	private final ServerRepository proxyRepository;
	private final List<String> proxies = new ArrayList<>();

	public UnproxiedBlocker(final Cyclone cyclone)
	{
		proxyRepository = new ServerRepository(cyclone.getRedisPool(), cyclone.getSerializer(), "bungeecord");
	}

	@SuppressWarnings("unchecked")
	public boolean validate(final UUID player)
	{
		final Object networkManager = ((Collection<Object>) SERVER_CONNECTION_NETWORK_MANAGERS.get(MINECRAFT_SERVER_SERVER_CONNECTION.get(MINECRAFT_SERVER_GET_SERVER.invoke(null)))).stream().filter((element) -> player.equals(NETWORK_MANAGER_SPOOFED_UUID.get(element))).findFirst().orElse(null);
		final Channel channel = (Channel) NETWORK_MANAGER_CHANNEL.get(networkManager);

		return proxies.contains(((InetSocketAddress) channel.remoteAddress()).getHostString());
	}

	public void update()
	{
		proxyRepository.getCachedServers().values().forEach((proxy) ->
		{
			proxies.add(proxy.host);
		});
	}

	static
	{
		try
		{
			final Class<?> minecraftServer = Class.forName("net.minecraft.server.v1_14_R1.MinecraftServer");
			final Class<?> serverConnection = Class.forName("net.minecraft.server.v1_14_R1.ServerConnection");
			final Class<?> networkManager = Class.forName("net.minecraft.server.v1_14_R1.NetworkManager");

			MINECRAFT_SERVER_GET_SERVER = Accessors.getMethodAccessorOrNull(minecraftServer, "getServer");
			MINECRAFT_SERVER_SERVER_CONNECTION = Accessors.getFieldAccessorOrNull(minecraftServer, "serverConnection");
			SERVER_CONNECTION_NETWORK_MANAGERS = Accessors.getFieldAccessorOrNull(serverConnection, "g");
			NETWORK_MANAGER_SPOOFED_UUID = Accessors.getFieldAccessorOrNull(networkManager, "spoofedUUID");
			NETWORK_MANAGER_CHANNEL = Accessors.getFieldAccessorOrNull(networkManager, "channel");
		}
		catch (final ClassNotFoundException ignore)
		{
		}
	}
}
