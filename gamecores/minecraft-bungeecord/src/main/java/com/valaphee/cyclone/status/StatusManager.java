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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.valaphee.cyclone.communication.Communication;
import com.valaphee.cyclone.serialization.Serializer;
import com.valaphee.cyclone.status.data.StatusData;
import com.valaphee.cyclone.status.data.StatusRepository;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import redis.clients.jedis.JedisPool;

/**
 * Default
 *
 * @author valaphee
 */
public final class StatusManager
{
	private final StatusRepository repository;
	private final List<Gamespy3ProtocolHandler> ut3Handlers = new ArrayList<>();
	private StatusData status;
	private JsonObject genericResponse;
	private String legacyResponse;
	private String legacyColoredResponse;

	public StatusManager(final JedisPool jedisPool, final Serializer serializer)
	{
		repository = new StatusRepository(jedisPool, serializer);

		updateStatus();
	}

	public void listenUT3(final SocketAddress address)
	{
		final Gamespy3ProtocolHandler gamespy3Handler = new Gamespy3ProtocolHandler(15_000L, "MINECRAFT", "MMP", ((InetSocketAddress) address).getHostString(), ((InetSocketAddress) address).getPort());
		new Bootstrap().group(Communication.getGroup()).channel(getDatagramChannel()).handler(gamespy3Handler).bind(address).syncUninterruptibly();
		ut3Handlers.add(gamespy3Handler);
	}

	private void updateStatus()
	{
		status = repository.getStatus();
	}

	public String getName()
	{
		return status.name;
	}

	public String getVersion()
	{
		return status.version;
	}

	public List<String> getSamples()
	{
		return status.samples;
	}

	public String getDescription()
	{
		return status.description;
	}

	public String getShortDescription()
	{
		return status.shortDescription;
	}

	public byte[] getFavicon()
	{
		return status.favicon;
	}

	public int getPlayerLimit()
	{
		return status.playerLimit;
	}

	public int getPlayerCount()
	{
		return status.playerCount;
	}

	public boolean isMaintenance()
	{
		return status.maintenance;
	}

	public String getGenericResponse(final int protocolVersion)
	{
		if (getVersion() == null)
		{
			final JsonObject version = genericResponse.getAsJsonObject("version");
			version.addProperty("name", "Cyclone");
			version.addProperty("protocol", protocolVersion);
			genericResponse.add("version", version);
		}

		return genericResponse.toString();
	}

	public String getLegacyResponse()
	{
		return legacyResponse;
	}

	public String getLegacyColoredResponse()
	{
		return legacyColoredResponse;
	}

	private void buildGenericResponse()
	{
		final JsonObject version = new JsonObject();
		if (getVersion() != null)
		{
			version.addProperty("name", getVersion());
			version.addProperty("protocol", -1);
		}

		final JsonArray playerSamples = new JsonArray();
		if (getSamples() != null)
		{
			getSamples().stream().forEachOrdered((sample) ->
			{
				final JsonObject playerSample = new JsonObject();
				playerSample.addProperty("id", UUID.nameUUIDFromBytes(sample.getBytes()).toString());
				playerSample.addProperty("name", sample);
				playerSamples.add(playerSample);
			});
		}
		final JsonObject players = new JsonObject();
		players.addProperty("max", getPlayerLimit());
		players.addProperty("online", getPlayerCount());
		players.add("sample", playerSamples);

		final JsonArray modinfoModList = new JsonArray();
		final JsonObject modinfo = new JsonObject();
		modinfo.addProperty("type", "BUKKIT");
		modinfo.addProperty("clientModsAllowed", "true");
		modinfo.add("modList", modinfoModList);

		genericResponse = new JsonObject();
		genericResponse.add("version", version);
		genericResponse.add("players", players);
		genericResponse.add("modinfo", modinfo);
		genericResponse.addProperty("description", getDescription());
		if (getFavicon() != null)
		{
			final StringBuilder favicon = new StringBuilder("data:image/png;base64,");
			favicon.append(Base64.getEncoder().encodeToString(getFavicon()));
			genericResponse.addProperty("favicon", favicon.toString());
		}
	}

	private void buildLegacyResponse()
	{
		final StringBuilder response = new StringBuilder();
		response.append(getName());
		response.append('\u00A7');
		response.append(getPlayerCount());
		response.append('\u00A7');
		response.append(getPlayerLimit());
		legacyResponse = response.toString();
	}

	private void buildLegacyColoredResponse()
	{
		final StringBuilder response = new StringBuilder();
		response.append("\u00A71\0");
		response.append("127");
		response.append('\0');
		response.append(getVersion());
		response.append('\0');
		response.append(getShortDescription());
		response.append('\0');
		response.append(getPlayerCount());
		response.append('\0');
		response.append(getPlayerLimit());
		legacyColoredResponse = response.toString();
	}

	public void update()
	{
		updateStatus();
		ut3Handlers.stream().forEach((ut3Handler) ->
		{
			ut3Handler.update(this);
		});
		buildGenericResponse();
		buildLegacyResponse();
		buildLegacyColoredResponse();
	}

	private static Class<? extends Channel> getDatagramChannel()
	{
		return Communication.hasEpoll() ? EpollDatagramChannel.class : NioDatagramChannel.class;
	}
}
