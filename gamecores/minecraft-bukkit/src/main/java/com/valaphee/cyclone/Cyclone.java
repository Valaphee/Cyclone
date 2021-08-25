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

package com.valaphee.cyclone;

import com.valaphee.cyclone.account.AccountListener;
import com.valaphee.cyclone.account.AccountManager;
import com.valaphee.cyclone.account.PermissionHandler;
import com.valaphee.cyclone.account.PermissionListener;
import com.valaphee.cyclone.communication.Communication;
import com.valaphee.cyclone.config.CacheConfig;
import com.valaphee.cyclone.config.CommunicationConfig;
import com.valaphee.cyclone.config.Config;
import com.valaphee.cyclone.config.Parameter;
import com.valaphee.cyclone.connection.ConnectionManager;
import com.valaphee.cyclone.serialization.Serializer;
import com.valaphee.cyclone.server.data.ServerData;
import com.valaphee.cyclone.server.data.ServerData.State;
import com.valaphee.cyclone.status.StatusManager;
import com.valaphee.cyclone.status.UnproxiedBlocker;
import com.valaphee.cyclone.util.TpSMeasurement;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Default
 *
 * @author valaphee
 */
public final class Cyclone
		extends JavaPlugin
{
	public static final Logger LOGGER = Logger.getLogger(Cyclone.class.getName());
	private static Cyclone instance;
	private Parameter parameter;
	private Config config;
	private Serializer serializer;
	private JedisPool redisPool;
	private Communication communication;
	private AccountManager accountManager;
	private PermissionHandler permissionHandler;
	private ConnectionManager connectionManager;
	private StatusManager statusManager;
	private UnproxiedBlocker unproxiedBlocker;
	private ServerData.State state = ServerData.State.NOT_SPECIFIED;
	private String stateDescription = "";

	public static Cyclone getInstance()
	{
		return instance;
	}

	public Cyclone()
	{
		instance = this;

		System.setIn(null);
		final org.apache.logging.log4j.core.Logger root = (org.apache.logging.log4j.core.Logger) org.apache.logging.log4j.LogManager.getRootLogger();
		root.getAppenders().values().stream().filter((appender) -> (!(appender instanceof org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender))).forEachOrdered((appender) ->
		{
			root.removeAppender(appender);
		});
	}

	@Override
	public void onLoad()
	{
		LOGGER.setParent(getLogger());
		LOGGER.log(Level.INFO,
				"\n  ______            _\n"
				+ " / _____)          | |\n"
				+ "| /     _   _  ____| | ___  ____   ____\n"
				+ "| |    | | | |/ ___) |/ _ \\|  _ \\ / _  )\n"
				+ "| \\____| |_| ( (___| | |_| | | | ( (/ / \n"
				+ " \\______)__  |\\____)_|\\___/|_| |_|\\____)\n"
		);

		parameter = new Parameter();
		if (parameter.getId() == null)
		{
			LOGGER.log(Level.SEVERE, "Required parameters are not given.");
			getServer().shutdown();
		}

		config = new Config();
		config.load();
	}

	@Override
	public void onEnable()
	{
		final long started = System.currentTimeMillis();

		serializer = new Serializer();
		serializer.appendClassLoader(getClass().getClassLoader());
		final JedisPoolConfig redisPoolConfig = new JedisPoolConfig();
		final CacheConfig cacheConfig = config.getCache();
		redisPoolConfig.setMaxTotal(cacheConfig.getConnectionPoolSize());
		redisPool = new JedisPool(redisPoolConfig, cacheConfig.getHost(), cacheConfig.getPort(), 0, cacheConfig.getAuth(), 0, config.getCommunication().getId().toString());

		communication = new Communication();

		accountManager = new AccountManager(this);
		permissionHandler = new PermissionHandler(accountManager);

		final CommunicationConfig communicationConfig = config.getCommunication();
		UUID nodeId = communicationConfig.getId();
		if (parameter.getNodeId() != null)
		{
			nodeId = parameter.getNodeId();
		}
		String nodeHost = communicationConfig.getHost();
		if (parameter.getNodeHost() != null)
		{
			nodeHost = parameter.getNodeHost();
		}
		int nodePort = communicationConfig.getPort();
		if (parameter.getNodePort() != null)
		{
			nodePort = parameter.getNodePort();
		}
		final SocketAddress nodeAddress = new InetSocketAddress(nodeHost, nodePort);

		connectionManager = new ConnectionManager(this, parameter.getId(), communicationConfig.getAuth().toCharArray(), null);
		connectionManager.outgoing(nodeId, nodeAddress);

		statusManager = new StatusManager(redisPool, serializer);

		unproxiedBlocker = new UnproxiedBlocker(this);

		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			private int tick = 0;

			@Override
			public void run()
			{
				communication.update();
				accountManager.update();
				if ((tick % 100) == 0)
				{
					statusManager.update();
					unproxiedBlocker.update();
				}
				tick++;
			}
		}, 0L, 1L);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new TpSMeasurement(), 0L, 1L);

		getServer().getPluginManager().registerEvents(new AccountListener(this), this);
		getServer().getPluginManager().registerEvents(new PermissionListener(this), this);

		LOGGER.log(Level.INFO, "Started in just {0} milliseconds!", System.currentTimeMillis() - started);
	}

	@Override
	public void onDisable()
	{
		setState(State.RESTARTING);

		getServer().getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);

		if (communication != null)
		{
			communication.shutdown();
			communication = null;
		}

		if (redisPool != null)
		{
			redisPool.close();
			redisPool = null;
		}
	}

	public Parameter getParameter()
	{
		return parameter;
	}

	public Config getConfig0()
	{
		return config;
	}

	public Serializer getSerializer()
	{
		return serializer;
	}

	public JedisPool getRedisPool()
	{
		return redisPool;
	}

	public Communication getCommunication()
	{
		return communication;
	}

	public AccountManager getAccountManager()
	{
		return accountManager;
	}

	public PermissionHandler getPermissionHandler()
	{
		return permissionHandler;
	}

	public ConnectionManager getConnectionManager()
	{
		return connectionManager;
	}

	public StatusManager getStatusManager()
	{
		return statusManager;
	}

	public UnproxiedBlocker getUnproxiedBlocker()
	{
		return unproxiedBlocker;
	}

	public ServerData.State getState()
	{
		return state;
	}

	public void setState(final ServerData.State state)
	{
		this.state = state;
	}

	public String getStateDescription()
	{
		return stateDescription;
	}

	public void setStateDescription(final String stateDescription)
	{
		this.stateDescription = stateDescription;
	}
}
