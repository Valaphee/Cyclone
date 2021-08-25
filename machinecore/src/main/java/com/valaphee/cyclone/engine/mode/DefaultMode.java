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

package com.valaphee.cyclone.engine.mode;

import com.valaphee.cyclone.account.AccountManager;
import com.valaphee.cyclone.account.PermissionHandler;
import com.valaphee.cyclone.account.command.*;
import com.valaphee.cyclone.command.AbortCommand;
import com.valaphee.cyclone.command.CommandHandler;
import com.valaphee.cyclone.command.ConfirmCommand;
import com.valaphee.cyclone.command.HelpCommand;
import com.valaphee.cyclone.config.Config;
import com.valaphee.cyclone.connection.ConnectionManager;
import com.valaphee.cyclone.connection.command.ConnectionCommand;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.engine.Engine;
import com.valaphee.cyclone.engine.EngineMode;
import com.valaphee.cyclone.server.LocalServer;
import com.valaphee.cyclone.server.ServerManager;
import com.valaphee.cyclone.server.command.*;
import com.valaphee.cyclone.status.StatusManager;
import com.valaphee.cyclone.status.command.MaintenanceCommand;
import com.valaphee.cyclone.status.command.StatusCommand;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Default
 *
 * @author valaphee
 */
public final class DefaultMode
		implements EngineMode
{
	static final Logger LOGGER = LogManager.getLogger(Engine.class);
	private final Config config;
	private final AccountManager accountManager;
	private final PermissionHandler permissionHandler;
	private final ConnectionManager connectionManager;
	private final ServerManager serverManager;
	private final StatusManager statusManager;
	private final CommandHandler commandHandler;
	private int tick;

	public DefaultMode(final Context context)
	{
		config = context.get(Config.class);
		accountManager = context.get(AccountManager.class);
		permissionHandler = context.get(PermissionHandler.class);
		connectionManager = context.get(ConnectionManager.class);
		serverManager = context.get(ServerManager.class);
		statusManager = context.get(StatusManager.class);
		commandHandler = context.get(CommandHandler.class);
	}

	@Override
	public void startup(final Engine engine)
	{
		commandHandler.register(new ConfirmCommand(commandHandler));
		commandHandler.register(new AbortCommand(commandHandler));
		commandHandler.register(new HelpCommand(commandHandler));
		commandHandler.register(new GroupListCommand(accountManager));
		commandHandler.register(new GroupPermissionAddCommand(accountManager));
		commandHandler.register(new GroupPermissionRemoveCommand(accountManager));
		commandHandler.register(new GroupPermissionListCommand(accountManager));
		commandHandler.register(new GroupInheritanceAddCommand(accountManager));
		commandHandler.register(new GroupInheritanceRemoveCommand(accountManager));
		commandHandler.register(new GroupInheritanceListCommand(accountManager));
		commandHandler.register(new UserListCommand(accountManager));
		commandHandler.register(new UserKickCommand(accountManager, permissionHandler));
		commandHandler.register(new UserMuteCommand(accountManager, permissionHandler));
		commandHandler.register(new UserUnmuteCommand(accountManager, permissionHandler));
		commandHandler.register(new UserBanCommand(accountManager, permissionHandler));
		commandHandler.register(new UserUnbanCommand(accountManager, permissionHandler));
		commandHandler.register(new UserCrashCommand(accountManager, permissionHandler));
		commandHandler.register(new UserPunishCommand(accountManager, permissionHandler));
		commandHandler.register(new UserWhoCommand(accountManager));
		commandHandler.register(new UserWhereCommand(accountManager));
		commandHandler.register(new UserPromoteCommand(accountManager, permissionHandler));
		commandHandler.register(new UserDemoteCommand(accountManager, permissionHandler));
		commandHandler.register(new ConnectionCommand(connectionManager));
		commandHandler.register(new ServerListCommand(serverManager));
		commandHandler.register(new ServerStartCommand(serverManager));
		commandHandler.register(new ServerRestartCommand(serverManager));
		commandHandler.register(new ServerStopCommand(serverManager));
		commandHandler.register(new ServerSwitchCommand(serverManager, accountManager));
		commandHandler.register(new ServerHubCommand(serverManager, accountManager));
		commandHandler.register(new ServerPersistentCommand(serverManager));
		commandHandler.register(new ServerBroadcastCommand(accountManager));
		commandHandler.register(new ServerCategoryListCommand(serverManager));
		commandHandler.register(new StatusCommand(statusManager));
		commandHandler.register(new MaintenanceCommand(statusManager, accountManager, permissionHandler));
		commandHandler.register(new EndCommand(commandHandler, engine));
	}

	@Override
	public void update(final float delta)
	{
		accountManager.update();
		serverManager.update();
		if ((tick % 50) == 0)
		{
			statusManager.update();
		}

		tick++;
	}

	@Override
	public void shutdown()
	{
		serverManager.getServers(true).stream().filter((server) -> (server instanceof LocalServer)).forEachOrdered((server) ->
		{
			LOGGER.log(Level.INFO, "Server still running, killing it {}.", server.getId());
			server.stop();
		});
	}
}
