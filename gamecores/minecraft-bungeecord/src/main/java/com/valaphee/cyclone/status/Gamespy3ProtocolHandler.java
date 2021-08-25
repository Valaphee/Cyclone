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

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.valaphee.cyclone.util.CharsetUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import java.util.*;

/**
 * Default
 *
 * @author valaphee
 */
public final class Gamespy3ProtocolHandler
		extends SimpleChannelInboundHandler<DatagramPacket>
{
	private static final int LAST_PACKET_FLAG = 0x80;
	private final long challengeExpire;
	private final Random random = new Random();
	private final Map<Integer, Long> pendingChallenges = new HashMap<>();
	private final Map<String, String> serverInformation = new HashMap<>();
	private final Table<String, String, String> playerInformation = HashBasedTable.create();
	private final Table<String, String, String> teamInformation = HashBasedTable.create();

	public Gamespy3ProtocolHandler(final long challengeExpire, final String gameId, final String gameType, final String ip, final int port)
	{
		this.challengeExpire = challengeExpire;

		setServerInformation("game_id", gameId);
		setServerInformation("gametype", gameType);
		setServerInformation("hostip", ip);
		setServerInformation("hostport", Integer.toString(port));
		setServerInformation("plugins", "Cyclone: Cyclone; some other plugins");
		setServerInformation("map", "(Unavailable)");
		setTeams(Arrays.asList("(Unavailable)"));
	}

	public void setServerInformation(final String name, final String value)
	{
		serverInformation.put(name, value);
	}

	public void setPlayers(final List<String> players)
	{
		players.forEach((player) -> playerInformation.put(player, "player_", player));
	}

	public void setTeams(final List<String> teams)
	{
		teams.forEach((team) -> teamInformation.put(team, "team_", team));
	}

	@Override
	protected void channelRead0(final ChannelHandlerContext ctx, final DatagramPacket message)
			throws Exception
	{
		final ByteBuf request = message.content();
		if (request.readUnsignedByte() != 0xFE || request.readUnsignedByte() != 0xFD)
		{
			ctx.channel().close().awaitUninterruptibly();
		}

		final ByteBuf response = ctx.alloc().buffer();
		final byte procedure = request.readByte();
		final int sequence = request.readInt();
		response.writeByte(procedure);
		response.writeInt(sequence);

		if (procedure == 0x09)
		{
			final int challenge = random.nextInt();
			pendingChallenges.put(challenge, System.currentTimeMillis());
			writeNumber(response, challenge);

			ctx.writeAndFlush(new DatagramPacket(response, message.sender()));
		}
		else if (procedure == 0x00)
		{
			final Long challengeCreated = pendingChallenges.get(request.readInt());
			if ((challengeCreated == null) || (System.currentTimeMillis() - challengeCreated > challengeExpire))
			{
				ctx.channel().close().awaitUninterruptibly();

				return;
			}

			if (request.readableBytes() == 0)
			{
				writeString(response, serverInformation.get("hostname"));
				writeString(response, serverInformation.get("gametype"));
				writeString(response, serverInformation.get("map"));
				writeString(response, serverInformation.get("numplayers"));
				writeString(response, serverInformation.get("maxplayers"));
				response.writeShort(Short.valueOf(serverInformation.get("hostport")));
				writeString(response, serverInformation.get("hostip"));

				ctx.writeAndFlush(new DatagramPacket(response, message.sender()));
			}
			else if (request.readableBytes() == 4)
			{
				final int packets = Math.max(1, request.readByte());
				boolean includeServerInformation = request.readBoolean();
				boolean includePlayerInformation = request.readBoolean();
				final boolean includeTeamInformation = request.readBoolean();
				if (!(includeServerInformation || includePlayerInformation || includeTeamInformation))
				{
					includeServerInformation = includePlayerInformation = true;
				}

				writeString(response, "splitnum");
				response.writeInt(LAST_PACKET_FLAG);
				response.writeInt(packets);

				if (includeServerInformation)
				{
					serverInformation.forEach((key, value) ->
					{
						writeString(response, key);
						writeString(response, value);
					});
					writeString(response, "");
					writeString(response, "");
				}
				if (includePlayerInformation)
				{
					response.writeByte(playerInformation.rowKeySet().size());
					playerInformation.columnKeySet().forEach((column) ->
					{
						writeString(response, column);
					});
					writeString(response, "");
					playerInformation.rowMap().values().forEach((row) -> row.values().forEach((value) ->
					{
						writeString(response, value);
					}));
				}
				if (includeTeamInformation)
				{
					response.writeByte(teamInformation.rowKeySet().size());
					teamInformation.columnKeySet().forEach((column) ->
					{
						writeString(response, column);
					});
					writeString(response, "");
					teamInformation.rowMap().values().forEach((row) -> row.values().forEach((value) ->
					{
						writeString(response, value);
					}));
				}

				ctx.writeAndFlush(new DatagramPacket(response, message.sender()));
			}
		}
	}

	public void update(final StatusManager manager)
	{
		final Iterator<Map.Entry<Integer, Long>> pendingChallengesIterator = pendingChallenges.entrySet().iterator();
		while (pendingChallengesIterator.hasNext())
		{
			final Long challengeCreated = pendingChallengesIterator.next().getValue();
			if (System.currentTimeMillis() - challengeCreated > challengeExpire)
			{
				pendingChallengesIterator.remove();
			}
		}

		setServerInformation("hostname", manager.getName());
		setServerInformation("version", manager.getVersion() != null ? manager.getVersion() : "(Unavailable)");
		setServerInformation("maxplayers", Integer.toString(manager.getPlayerLimit()));
		setServerInformation("numplayers", Integer.toString(manager.getPlayerCount()));
		setPlayers(manager.getSamples() != null ? manager.getSamples() : Arrays.asList("(Unavailable)"));
	}

	private static void writeString(final ByteBuf buffer, final String value)
	{
		buffer.writeBytes(value.getBytes(CharsetUtil.UTF_8));
		buffer.writeByte('\0');
	}

	private static void writeNumber(final ByteBuf buffer, final int value)
	{
		writeString(buffer, Integer.toString(value));
	}
}
