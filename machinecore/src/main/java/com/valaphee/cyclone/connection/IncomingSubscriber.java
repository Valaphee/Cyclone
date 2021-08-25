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

import com.valaphee.cyclone.account.AccountManager;
import com.valaphee.cyclone.account.Player;
import com.valaphee.cyclone.account.User;
import com.valaphee.cyclone.command.CommandSender;
import com.valaphee.cyclone.communication.ProtocolHandler;
import com.valaphee.cyclone.communication.packet.*;
import com.valaphee.cyclone.server.ServerManager;
import java.util.UUID;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Default
 *
 * @author valaphee
 */
public final class IncomingSubscriber
		implements PacketProtocolSubscriber
{
	private static final Logger LOGGER = LogManager.getLogger(IncomingSubscriber.class);
	private final ConnectionManager manager;

	public IncomingSubscriber(final ConnectionManager manager)
	{
		this.manager = manager;
	}

	@Override
	public void initialize(final ProtocolHandler handler)
	{}

	@Override
	public void update()
	{}

	@Override
	public void disconnected()
	{}

	@Override
	public void handshake(final HandshakePacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void handshakeFinish(final HandshakeFinalPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void cryptographicRequest(final CryptographicRequestPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void cryptographicResponse(final CryptographicResponsePacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void determine(final DeterminationPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in the frontend.");
	}

	@Override
	public void negate(final NegotiationPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in the frontend.");
	}

	@Override
	public void serverScore(final ServerScorePacket packet)
	{
		if (!packet.getAddressee().equals(manager.getId()))
		{
			final int score = manager.context.get(ServerManager.class).getSubcategory(packet.getCategory()).getScoreCriteriaContainer().compute();
			if (score > packet.getBestScore())
			{
				packet.setBest(manager.getId());
				packet.setBestScore(score);
			}
			manager.getOutgoingConnection().sendPacket(packet);
		}
		else
		{
			manager.getOutgoingConnection().sendPacket(new ServerStartPacket(manager.getId(), UUID.randomUUID(), packet.getCategory(), packet.getBest()));
		}
	}

	@Override
	public void serverStart(final ServerStartPacket packet)
	{
		if (!packet.getNode().equals(manager.getId()))
		{
			if (!packet.getAddressee().equals(manager.getId()))
			{
				manager.getOutgoingConnection().sendPacket(packet);
			}
		}
		else
		{
			manager.context.get(ServerManager.class).startInternal(packet.getNode(), packet.getServer(), manager.context.get(ServerManager.class).getSubcategory(packet.getCategory()));
		}
	}

	@Override
	public void serverRegister(final ServerRegisterPacket packet)
	{
		if (!packet.getAddressee().equals(manager.getId()))
		{
			manager.context.get(ServerManager.class).registerInternal(packet.getAddressee(), packet.getServer(), manager.context.get(ServerManager.class).getSubcategory(packet.getCategory()));
			manager.getOutgoingConnection().sendPacket(packet);
		}
	}

	@Override
	public void serverStopAndUnregister(final ServerStopAndUnregisterPacket packet)
	{
		if (!packet.getAddressee().equals(manager.getId()))
		{
			manager.context.get(ServerManager.class).stopInternal(packet.getServer());
			manager.getOutgoingConnection().sendPacket(packet);
		}
	}

	@Override
	public void serverUpdate(final ServerUpdatePacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void playerProclamate(final PlayerProclamationPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void playerAdmission(final PlayerAdmissionPacket packet)
	{
		if (packet.getNode() != null)
		{
			if (!packet.getNode().equals(manager.getId()))
			{
				manager.getOutgoingConnection().sendPacket(packet);
			}
			else
			{
				User user = manager.context.get(AccountManager.class).getUser(packet.getPlayer());
				if (user != null)
				{
					((Player) user).admission(packet.getAddress());
				}
			}
		}
	}

	@Override
	public void playerMessage(final PlayerMessagePacket packet)
	{
		if (PlayerMessagePacket.Type.JUMPING.equals(packet.getType()))
		{
			if (!packet.getNode().equals(manager.getId()))
			{
				manager.getOutgoingConnection().sendPacket(packet);
			}
			else
			{
				User user = manager.context.get(AccountManager.class).getUser(packet.getSenderOrReceiver());
				if (user != null)
				{
					((CommandSender) user).sendMessage(packet.getMessage());
				}
			}
		}
	}

	@Override
	public void playerKick(final PlayerKickPacket packet)
	{
		if (packet.getNode() != null)
		{
			if (!packet.getNode().equals(manager.getId()))
			{
				manager.getOutgoingConnection().sendPacket(packet);
			}
			else
			{
				User user = manager.context.get(AccountManager.class).getUser(packet.getPlayer());
				if (user != null)
				{
					((Player) user).kick(packet.getReason());
				}
			}
		}
	}

	@Override
	public void playerCrash(final PlayerCrashPacket packet)
	{
		if (packet.getNode() != null)
		{
			if (!packet.getNode().equals(manager.getId()))
			{
				manager.getOutgoingConnection().sendPacket(packet);
			}
			else
			{
				User user = manager.context.get(AccountManager.class).getUser(packet.getPlayer());
				if (user != null)
				{
					((Player) user).crash(packet.isImmediately());
				}
			}
		}
	}

	@Override
	public void playerUpdate(final PlayerUpdatePacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}
}
