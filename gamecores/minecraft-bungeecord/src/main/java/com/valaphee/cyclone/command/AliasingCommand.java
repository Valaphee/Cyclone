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

package com.valaphee.cyclone.command;

import com.valaphee.cyclone.Cyclone;
import com.valaphee.cyclone.communication.packet.PlayerMessagePacket;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * Default
 *
 * @author valaphee
 */
public final class AliasingCommand
		extends Command
{
	private final Cyclone cyclone;
	private final String aliasOf;

	public AliasingCommand(final Cyclone cyclone, final String alias, final String aliasOf)
	{
		super(alias, null);

		this.cyclone = cyclone;
		this.aliasOf = aliasOf.concat(" ");
	}

	@Override
	public void execute(final CommandSender sender, final String[] args)
	{
		cyclone.getConnectionManager().getOutgoingConnection().sendPacket(new PlayerMessagePacket(cyclone.getAccountManager().findUserId(((ProxiedPlayer) sender).getUniqueId()), aliasOf + String.join(" ", args)));
	}
}
