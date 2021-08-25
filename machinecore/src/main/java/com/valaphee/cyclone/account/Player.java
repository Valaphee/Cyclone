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

package com.valaphee.cyclone.account;

import com.valaphee.cyclone.account.data.UserData;
import com.valaphee.cyclone.command.CommandSender;
import com.valaphee.cyclone.communication.packet.PlayerAdmissionPacket;
import com.valaphee.cyclone.communication.packet.PlayerCrashPacket;
import com.valaphee.cyclone.communication.packet.PlayerKickPacket;
import com.valaphee.cyclone.communication.packet.PlayerMessagePacket;
import com.valaphee.cyclone.connection.ConnectionManager;
import static com.valaphee.cyclone.language.I18n.tl;
import com.valaphee.cyclone.server.LocalServer;
import com.valaphee.cyclone.server.Server;
import com.valaphee.cyclone.server.ServerManager;
import com.valaphee.cyclone.server.data.ServerData;
import com.valaphee.cyclone.util.TimestampUtil;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.sql.Timestamp;

/**
 * Default
 *
 * @author valaphee
 */
public class Player
		extends User
		implements CommandSender
{
	private Server proxyServer;
	private Server server;

	public Player(final AccountManager manager, final UserData data)
	{
		super(manager, data);
	}

	@Override
	public void addKickPunishment(final String reason, final User createdBy, final Timestamp createdAt)
	{
		super.addKickPunishment(reason, createdBy, createdAt);

		final StringBuilder message = new StringBuilder();
		message.append(tl("cyclone.user.disconnect.header")).append('\n');
		message.append(tl("cyclone.user.disconnect.title.kick")).append("\n\n");
		message.append(tl("cyclone.user.disconnect.by", createdBy != null ? "#" + createdBy.getId() : "Konsole")).append('\n');
		if (reason != null)
		{
			message.append(tl("cyclone.user.disconnect.reason", reason)).append('\n');
		}
		message.append(tl("cyclone.user.disconnect.footer"));
		kick(message.toString());
	}

	@Override
	public void addBanPunishment(final String reason, final User createdBy, final Timestamp createdAt, final Timestamp expire)
	{
		super.addBanPunishment(reason, createdBy, createdAt, expire);

		final StringBuilder message = new StringBuilder();
		message.append(tl("cyclone.user.disconnect.header")).append('\n');
		message.append(tl("cyclone.user.disconnect.title.ban", TimestampUtil.formatTimestamp(createdAt, expire))).append("\n\n");
		message.append(tl("cyclone.user.disconnect.by", createdBy != null ? "#" + createdBy.getId() : "Konsole")).append('\n');
		message.append(tl("cyclone.user.disconnect.remainingtime", TimestampUtil.formatTimestamp(TimestampUtil.getCurrentTimestamp(), expire))).append('\n');
		if (reason != null)
		{
			message.append(tl("cyclone.user.disconnect.reason", reason)).append('\n');
		}
		message.append('\n').append(tl("cyclone.user.disconnect.info.request")).append('\n');
		message.append(tl("cyclone.user.disconnect.footer"));
		kick(message.toString());
	}

	@Override
	public void addCrashPunishment(final String reason, final User createdBy, final Timestamp createdAt, boolean immediately)
	{
		super.addCrashPunishment(reason, createdBy, createdAt, immediately);

		crash(immediately);
	}

	public void setProxyServer(final Server proxyServer)
	{
		data.proxyServer = (this.proxyServer = proxyServer).getId();
		flagAsModified();
	}

	public boolean setServer(final Server server)
	{
		if ((server.getState() != null) && server.getState().equals(ServerData.State.LOBBY) && ((!server.isFull()) || hasPermission("cyclone.joinfullserver")))
		{
			data.server = (this.server = server).getId();
			flagAsModified();

			admission(new InetSocketAddress(this.server.getHost(), this.server.getPort()));

			return true;
		}

		return false;
	}

	public boolean spread(final String category)
	{
		double bestTicksPerSecond = 0.0D;
		Server best = null;
		for (final Server server : manager.context.get(ServerManager.class).getSubcategory(category).getServers(true))
		{
			if ((bestTicksPerSecond < server.getTicksPerSecond()) && ((!server.isFull()) || hasPermission("cyclone.joinfullserver")))
			{
				bestTicksPerSecond = server.getTicksPerSecond();
				best = server;
			}
		}
		if (best != null)
		{
			return setServer(best);
		}

		return false;
	}

	public void admission(final SocketAddress address)
	{
		if (data.proxyServer != null)
		{
			if (proxyServer instanceof LocalServer)
			{
				((LocalServer) proxyServer).getConnection().sendPacket(new PlayerAdmissionPacket(getId(), address));
			}
			else
			{
				manager.context.get(ConnectionManager.class).getOutgoingConnection().sendPacket(new PlayerAdmissionPacket(manager.context.get(ServerManager.class).getServer(data.proxyServer, true).getNode(), getId(), address));
			}
		}
	}

	@Override
	public void sendMessage(final String message)
	{
		if (data.proxyServer != null)
		{
			if (proxyServer instanceof LocalServer)
			{
				((LocalServer) proxyServer).getConnection().sendPacket(new PlayerMessagePacket(getId(), message));
			}
			else
			{
				manager.context.get(ConnectionManager.class).getOutgoingConnection().sendPacket(new PlayerMessagePacket(manager.context.get(ServerManager.class).getServer(data.proxyServer, true).getNode(), getId(), message));
			}
		}
	}

	public void kick(final String reason)
	{
		if (data.proxyServer != null)
		{
			if (proxyServer instanceof LocalServer)
			{
				((LocalServer) proxyServer).getConnection().sendPacket(new PlayerKickPacket(getId(), reason));
			}
			else
			{
				manager.context.get(ConnectionManager.class).getOutgoingConnection().sendPacket(new PlayerKickPacket(manager.context.get(ServerManager.class).getServer(data.proxyServer, true).getNode(), getId(), reason));
			}
		}
	}

	public void crash(final boolean immediately)
	{
		if (data.server != null)
		{
			if (server instanceof LocalServer)
			{
				((LocalServer) server).getConnection().sendPacket(new PlayerCrashPacket(getId(), immediately));
			}
			else
			{
				manager.context.get(ConnectionManager.class).getOutgoingConnection().sendPacket(new PlayerCrashPacket(manager.context.get(ServerManager.class).getServer(data.server, true).getNode(), getId(), immediately));
			}
		}
	}

	@Override
	public boolean hasPermission(final String permission)
	{
		return manager.context.get(PermissionHandler.class).has(this, permission);
	}
}
