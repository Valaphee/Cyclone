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

package com.valaphee.cyclone.connection;

import com.valaphee.cyclone.Cyclone;
import com.valaphee.cyclone.account.AccountManager;
import com.valaphee.cyclone.communication.ProtocolHandler;
import com.valaphee.cyclone.communication.packet.*;
import com.valaphee.cyclone.server.data.ServerData;
import com.valaphee.cyclone.text.MinecraftLegacyTextTranscoder;
import com.valaphee.cyclone.text.TextTranscoder;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.logging.Level;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Default
 *
 * @author valaphee
 */
public final class OutgoingSubscriber
		implements PacketProtocolSubscriber
{
	private final ConnectionManager manager;
	private final ProxyServer server;
	private final AccountManager accountManager;
	private final TextTranscoder<String> transcoder = new MinecraftLegacyTextTranscoder();
	private volatile int tick;

	public OutgoingSubscriber(final ConnectionManager manager)
	{
		this.manager = manager;
		this.server = this.manager.cyclone.getProxy();
		this.accountManager = this.manager.cyclone.getAccountManager();
	}

	@Override
	public void initialize(final ProtocolHandler handler)
	{}

	@Override
	public void update()
	{
		if ((tick % 40) == 39)
		{
			manager.getOutgoingConnection().sendPacket(new ServerUpdatePacket(Cyclone.getInstance().getProxy().getOnlineCount(), 0.0D, ServerData.State.NOT_SPECIFIED, ""));
		}

		tick++;
	}

	@Override
	public void disconnected()
	{}

	@Override
	public void handshake(final HandshakePacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void handshakeFinish(final HandshakeFinalPacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void cryptographicRequest(final CryptographicRequestPacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void cryptographicResponse(final CryptographicResponsePacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void determine(final DeterminationPacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void negate(final NegotiationPacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void serverScore(final ServerScorePacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void serverStart(final ServerStartPacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void serverRegister(final ServerRegisterPacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void serverStopAndUnregister(final ServerStopAndUnregisterPacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void serverUpdate(final ServerUpdatePacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in the server.");
	}

	@Override
	public void playerProclamate(final PlayerProclamationPacket packet)
	{
		accountManager.proclamated(UUID.fromString(packet.getRequestAssociation()), packet.getAnswer());
	}

	@Override
	public void playerAdmission(final PlayerAdmissionPacket packet)
	{
		final ProxiedPlayer player = server.getPlayer(accountManager.findAssociation(packet.getPlayer()));
		if (player != null)
		{
			player.connect(server.constructServerInfo("game", (InetSocketAddress) packet.getAddress(), null, true));

			accountManager.connected(packet.getPlayer());
		}
	}

	@Override
	public void playerMessage(final PlayerMessagePacket packet)
	{
		if (PlayerMessagePacket.Type.STRAIGHT.equals(packet.getType()))
		{
			final ProxiedPlayer player = server.getPlayer(accountManager.findAssociation(packet.getSenderOrReceiver()));
			if (player != null)
			{
				player.sendMessage(TextComponent.fromLegacyText(transcoder.transcode(packet.getMessage())));
			}
		}
	}

	@Override
	public void playerKick(final PlayerKickPacket packet)
	{
		final ProxiedPlayer player = server.getPlayer(accountManager.findAssociation(packet.getPlayer()));
		if (player != null)
		{
			player.disconnect(TextComponent.fromLegacyText(transcoder.transcode(packet.getReason())));
			accountManager.connected(packet.getPlayer());
		}
	}

	@Override
	public void playerCrash(final PlayerCrashPacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in the server.");
	}

	@Override
	public void playerUpdate(final PlayerUpdatePacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in the server.");
	}
}
