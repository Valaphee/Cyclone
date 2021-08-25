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
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Default
 *
 * @author valaphee
 */
public final class PermissionListener
		implements Listener
{
	private final AccountManager accountManager;
	private final PermissionHandler permissionHandler;

	public PermissionListener(final Cyclone cyclone)
	{
		this.accountManager = cyclone.getAccountManager();
		this.permissionHandler = cyclone.getPermissionHandler();
	}

	@EventHandler
	public void on(final PermissionCheckEvent event)
	{
		if (event.getPermission().equalsIgnoreCase("bungeecord.nopermissions"))
		{
			event.setHasPermission(true);
		}
		else if (event.getSender() instanceof ProxiedPlayer)
		{
			event.setHasPermission(permissionHandler.has(accountManager.getUser(accountManager.findUserId(((ProxiedPlayer) event.getSender()).getUniqueId())), event.getPermission()));
		}
	}

}
