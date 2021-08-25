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
import com.valaphee.cyclone.communication.packet.PlayerUpdatePacket;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Default
 *
 * @author valaphee
 */
public final class AccountListener
		implements Listener
{
	private final Cyclone cyclone;

	public AccountListener(final Cyclone cyclone)
	{
		this.cyclone = cyclone;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void on(final AsyncPlayerPreLoginEvent event)
	{
		if ((cyclone.getConnectionManager().getOutgoingConnection() == null) || (!cyclone.getConnectionManager().getOutgoingConnection().getChannel().isOpen()))
		{
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "There is no connection to the machine core.");
		}

		if (!cyclone.getUnproxiedBlocker().validate(event.getUniqueId()))
		{
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, "You are not allowed to connect without a official proxy.");

			return;
		}

		cyclone.getAccountManager().proclamate(event.getName(), event.getUniqueId());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void on(final PlayerLoginEvent event)
	{
		if (PlayerLoginEvent.Result.KICK_FULL.equals(event.getResult()))
		{
			final User user = cyclone.getAccountManager().getUser(cyclone.getAccountManager().findUserId(event.getPlayer().getUniqueId()));
			if (cyclone.getPermissionHandler().has(user, "cyclone.joinfullserver"))
			{
				event.allow();
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void on(final PlayerJoinEvent event)
	{
		cyclone.getConnectionManager().getOutgoingConnection().sendPacket(new PlayerUpdatePacket(cyclone.getAccountManager().findUserId(event.getPlayer().getUniqueId()), PlayerUpdatePacket.State.SWITCHED));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(final PlayerQuitEvent event)
	{
		final User user = cyclone.getAccountManager().getUser(cyclone.getAccountManager().findUserId(event.getPlayer().getUniqueId()));
		if (user != null)
		{
			cyclone.getAccountManager().release(user.getId());
		}
	}
}
