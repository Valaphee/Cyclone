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

import com.valaphee.cyclone.Cyclone;
import com.valaphee.cyclone.communication.packet.PlayerMessagePacket;
import com.valaphee.cyclone.communication.packet.PlayerUpdatePacket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * Default
 *
 * @author valaphee
 */
public final class AccountListener
		implements Listener
{
	private final Cyclone cyclone;
	private final List<UUID> ignore = new ArrayList<>();

	public AccountListener(final Cyclone cyclone)
	{
		this.cyclone = cyclone;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void on(final PreLoginEvent event)
	{
		if ((cyclone.getConnectionManager().getOutgoingConnection() == null) || (!cyclone.getConnectionManager().getOutgoingConnection().getChannel().isOpen()))
		{
			event.setCancelReason(new TranslatableComponent("disconnect.loginFailedInfo", "There is no connection to the machine core."));
			event.setCancelled(true);
		}

		final PendingConnection connection = event.getConnection();
		if ((cyclone.getProxy().getPlayer(connection.getName()) != null) || (cyclone.getProxy().getPlayer(connection.getUniqueId()) != null))
		{
			ignore.add(connection.getUniqueId());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void on(final PostLoginEvent event)
	{
		final ProxiedPlayer player = event.getPlayer();
		final long userId = cyclone.getAccountManager().proclamate(player.getName(), player.getUniqueId());
		cyclone.getConnectionManager().getOutgoingConnection().sendPacket(new PlayerUpdatePacket(userId, PlayerUpdatePacket.State.JOINED, player.getName(), player.getAddress().getHostString()));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(final PlayerDisconnectEvent event)
	{
		final ProxiedPlayer player = event.getPlayer();
		final User user = cyclone.getAccountManager().getUser(cyclone.getAccountManager().findUserId(player.getUniqueId()));
		if (ignore.contains(player.getUniqueId()))
		{
			ignore.remove(player.getUniqueId());
		}
		else if (user != null)
		{
			cyclone.getConnectionManager().getOutgoingConnection().sendPacket(new PlayerUpdatePacket(user.getId(), PlayerUpdatePacket.State.LEFT));
			cyclone.getAccountManager().release(user.getId());
		}
	}

	@EventHandler
	public void on(final ServerConnectEvent event)
	{
		if ((cyclone.getConnectionManager().getOutgoingConnection() != null) && cyclone.getConnectionManager().getOutgoingConnection().getChannel().isOpen())
		{
			if (event.getTarget().getName().equals("none"))
			{
				try
				{
					cyclone.getAccountManager().connect(cyclone.getAccountManager().findUserId(event.getPlayer().getUniqueId()));
				}
				catch (final IllegalStateException ex)
				{
					event.getPlayer().disconnect(new TranslatableComponent("disconnect.loginFailedInfo", "The login process has failed due to high latency."));
				}
				event.setCancelled(true);
			}
		}
		else
		{
			event.getPlayer().disconnect(new TranslatableComponent("disconnect.loginFailedInfo", "There is no connection to the machine core."));
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void on(final ChatEvent event)
	{
		String message = event.getMessage();
		if (message.startsWith("+"))
		{
			message = message.substring(1, message.length()).trim();
			if (!message.isEmpty())
			{
				cyclone.getConnectionManager().getOutgoingConnection().sendPacket(new PlayerMessagePacket(cyclone.getAccountManager().findUserId(((ProxiedPlayer) event.getSender()).getUniqueId()), message));
			}
			event.setCancelled(true);
		}
	}
}
