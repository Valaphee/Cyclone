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
import com.valaphee.cyclone.account.data.UserPunishmentData;
import com.valaphee.cyclone.command.CommandException;
import com.valaphee.cyclone.command.CommandHandler;
import com.valaphee.cyclone.command.CommandSender;
import com.valaphee.cyclone.command.SystemCommandSender;
import com.valaphee.cyclone.communication.ProtocolHandler;
import com.valaphee.cyclone.communication.packet.*;
import static com.valaphee.cyclone.language.I18n.tl;
import com.valaphee.cyclone.reflect.ThrowablePrinter;
import com.valaphee.cyclone.server.LocalServer;
import com.valaphee.cyclone.status.StatusManager;
import com.valaphee.cyclone.util.TimestampUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Default
 *
 * @author valaphee
 */
public final class ServerSubscriber
		implements PacketProtocolSubscriber
{
	private static final Logger LOGGER = LogManager.getLogger(ServerSubscriber.class);
	private final LocalServer server;
	private final AccountManager accountManager;
	private final CommandHandler commandHandler;
	private final StatusManager statusManager;

	public ServerSubscriber(final LocalServer server, final AccountManager accountManager, final CommandHandler commandHandler, final StatusManager statusManager)
	{
		this.server = server;
		this.accountManager = accountManager;
		this.commandHandler = commandHandler;
		this.statusManager = statusManager;
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
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void negate(final NegotiationPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void serverScore(final ServerScorePacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void serverStart(final ServerStartPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void serverRegister(final ServerRegisterPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void serverStopAndUnregister(final ServerStopAndUnregisterPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void serverUpdate(final ServerUpdatePacket packet)
	{
		server.setPlayerCount(packet.getPlayerCount());
		server.setTicksPerSecond(packet.getTicksPerSecond());
		server.setState(packet.getState());
	}

	@Override
	public void playerProclamate(final PlayerProclamationPacket packet)
	{
		Long userId = accountManager.findAndSynchronizeUserIdByAssociation(packet.getRequestName(), packet.getRequestAssociation());
		if (userId == null)
		{
			userId = accountManager.secureGetNewUserId(packet.getRequestName(), TimestampUtil.getCurrentTimestamp());
			accountManager.addAssociation(userId, packet.getRequestAssociation());
		}

		packet.setAnswer(userId);
		server.getConnection().sendPacket(packet);
	}

	@Override
	public void playerAdmission(final PlayerAdmissionPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in the server.");
	}

	@Override
	public void playerMessage(final PlayerMessagePacket packet)
	{
		switch (packet.getType())
		{
		case STRAIGHT:
			final CommandSender sender = packet.getSenderOrReceiver() != 0 ? ((CommandSender) accountManager.getUser(packet.getSenderOrReceiver())) : SystemCommandSender.getInstance();
			final String message = packet.getMessage();
			try
			{
				if (!commandHandler.dispatch(sender, message))
				{
					sender.sendMessage(tl("cyclone.commandnotfound", message));
				}
			}
			catch (final CommandException ex)
			{
				sender.sendMessage(ThrowablePrinter.print(ex));
			}

			break;
		case PRIVATE_MESSAGE:
			final User user = accountManager.getUser(accountManager.findUserId(packet.getReceiverOrChannel()));
			if (user != null)
			{
				if (user instanceof CommandSender)
				{
					((CommandSender) user).sendMessage(packet.getMessage());
				}
			}

			break;
		case CHANNEL_MESSAGE:
		}
	}

	@Override
	public void playerKick(final PlayerKickPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in the server.");
	}

	@Override
	public void playerCrash(final PlayerCrashPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in the server.");
	}

	@Override
	public void playerUpdate(final PlayerUpdatePacket packet)
	{
		if (packet.getState().equals(PlayerUpdatePacket.State.JOINED))
		{
			final Player player = (Player) accountManager.getUser(packet.getPlayer());
			player.setName(packet.getName());
			player.setIp(packet.getIp());
			player.setProxyServer(server);

			if (statusManager.isMaintenance() && (!player.hasPermission("cyclone.maintenance")))
			{
				final StringBuilder message = new StringBuilder();
				message.append(tl("cyclone.user.disconnect.header")).append('\n');
				message.append(tl("cyclone.user.disconnect.title.unknown")).append("\n\n");
				message.append(tl("cyclone.user.disconnect.cause.maintenance")).append('\n');
				message.append(tl("cyclone.user.disconnect.footer"));
				player.kick(message.toString());

				return;
			}

			if ((statusManager.getPlayerCount() + 1) > statusManager.getPlayerLimit())
			{
				final StringBuilder message = new StringBuilder();
				message.append(tl("cyclone.user.disconnect.header")).append('\n');
				message.append(tl("cyclone.user.disconnect.title.unknown")).append("\n\n");
				message.append(tl("cyclone.user.disconnect.cause.noslots")).append("\n\n");
				message.append(tl("cyclone.user.disconnect.info.support")).append('\n');
				message.append(tl("cyclone.user.disconnect.footer"));
				player.kick(message.toString());

				return;
			}

			final UserPunishmentData punishment = player.getActivePunishment(UserPunishmentData.Type.BAN);
			if (punishment != null)
			{
				if (TimestampUtil.getCurrentTimestamp().after(punishment.expire))
				{
					punishment.active = false;
				}
				else
				{
					final StringBuilder message = new StringBuilder();
					message.append(tl("cyclone.user.disconnect.header")).append('\n');
					message.append(tl("cyclone.user.disconnect.title.ban", TimestampUtil.formatTimestamp(punishment.createdAt, punishment.expire))).append("\n\n");
					message.append(tl("cyclone.user.disconnect.by", punishment.createdBy != null ? "#" + punishment.createdBy : "Konsole")).append('\n');
					message.append(tl("cyclone.user.disconnect.remainingtime", TimestampUtil.formatTimestamp(TimestampUtil.getCurrentTimestamp(), punishment.expire))).append('\n');
					if (punishment.reason != null)
					{
						message.append(tl("cyclone.user.disconnect.reason", punishment.reason)).append('\n');
					}
					message.append('\n').append(tl("cyclone.user.disconnect.info.request")).append('\n');
					message.append(tl("cyclone.user.disconnect.footer"));
					player.kick(message.toString());

					return;
				}
			}

			player.setJoined(TimestampUtil.getCurrentTimestamp());
			if (!player.spread("spigot:hub"))
			{
				final StringBuilder message = new StringBuilder();
				message.append(tl("cyclone.user.disconnect.header")).append('\n');
				message.append(tl("cyclone.user.disconnect.title.unknown")).append("\n\n");
				message.append(tl("cyclone.user.disconnect.cause.nohubs")).append('\n');
				message.append(tl("cyclone.user.disconnect.footer"));
				player.kick(message.toString());
			}
		}
		else if (packet.getState().equals(PlayerUpdatePacket.State.LEFT))
		{
			accountManager.removeUser(packet.getPlayer());
		}
	}
}
