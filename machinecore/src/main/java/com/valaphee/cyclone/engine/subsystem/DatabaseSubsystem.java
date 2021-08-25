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

package com.valaphee.cyclone.engine.subsystem;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.valaphee.cyclone.account.data.*;
import com.valaphee.cyclone.config.Config;
import com.valaphee.cyclone.config.DatabaseConfig;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.engine.EngineSubsystem;
import com.valaphee.cyclone.server.data.ServerCategoryScoreCriteriaTable;
import com.valaphee.cyclone.server.data.ServerCategoryTable;
import com.valaphee.cyclone.server.data.ServerCategoryVariableTable;
import com.valaphee.cyclone.server.data.ServerTable;
import org.avaje.datasource.DataSourceConfig;

/**
 * Default
 *
 * @author valaphee
 */
public final class DatabaseSubsystem
		implements EngineSubsystem
{
	private EbeanServer database;

	@Override
	public String getName()
	{
		return "Database";
	}

	@Override
	public void preStartup(final Context context)
	{
		final DatabaseConfig config = context.get(Config.class).getDatabase();
		final DataSourceConfig dataSourceConfig = new DataSourceConfig();
		dataSourceConfig.setUrl(String.format("jdbc:mysql://%s:%s/%s?collation=utf8_bin&serverTimezone=UTC", config.getHost(), config.getPort(), config.getSchema()));
		dataSourceConfig.setUsername(config.getUsername());
		dataSourceConfig.setPassword(config.getPassword());
		dataSourceConfig.setDriver("com.mysql.cj.jdbc.Driver");
		dataSourceConfig.setHeartbeatSql("SELECT 1 FROM dual");
		dataSourceConfig.setMaxConnections(config.getConnectionPoolSize());
		dataSourceConfig.setWaitTimeoutMillis(30_000);
		final ServerConfig serverConfig = new ServerConfig();
		serverConfig.setName("cyclone");
		serverConfig.setDataSourceConfig(dataSourceConfig);
		serverConfig.setDdlGenerate(true);
		serverConfig.setDdlRun(false);
		serverConfig.addClass(ServerCategoryScoreCriteriaTable.class);
		serverConfig.addClass(ServerCategoryVariableTable.class);
		serverConfig.addClass(ServerCategoryTable.class);
		serverConfig.addClass(ServerTable.class);
		serverConfig.addClass(GroupPermissionTable.class);
		serverConfig.addClass(GroupInheritanceTable.class);
		serverConfig.addClass(GroupVariableTable.class);
		serverConfig.addClass(GroupTable.class);
		serverConfig.addClass(UserAssociationTable.class);
		serverConfig.addClass(UserPunishmentTable.class);
		serverConfig.addClass(UserIpTable.class);
		serverConfig.addClass(UserVariableTable.class);
		serverConfig.addClass(UserTable.class);
		context.set(EbeanServer.class, database = EbeanServerFactory.create(serverConfig));
	}

	@Override
	public void shutdown()
	{
		if (database != null)
		{
			database.shutdown(true, true);
		}
	}
}
