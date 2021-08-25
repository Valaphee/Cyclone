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
import com.valaphee.cyclone.command.AliasingCommand;
import com.valaphee.cyclone.communication.Communication;
import com.valaphee.cyclone.config.CacheConfig;
import com.valaphee.cyclone.config.CommunicationConfig;
import com.valaphee.cyclone.config.Config;
import com.valaphee.cyclone.config.Parameter;
import com.valaphee.cyclone.connection.ConnectionManager;
import com.valaphee.cyclone.reflect.Accessors;
import com.valaphee.cyclone.reflect.FieldAccessor;
import com.valaphee.cyclone.serialization.Serializer;
import com.valaphee.cyclone.status.DenialOfServiceBlocker;
import com.valaphee.cyclone.status.ProtocolInitializer;
import com.valaphee.cyclone.status.StatusManager;
import com.valaphee.cyclone.util.ThreadFactoryBuilder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.GroupedThreadFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("deprecation")
public final class Cyclone
		extends Plugin
{
	public static final Logger LOGGER = Logger.getLogger(Cyclone.class.getName());
	public static final long TICK_IN_MILLISECONDS = 1_000L / 20L;
	private static Cyclone instance;
	private final ThreadFactory threadFactory = new GroupedThreadFactory(this, Cyclone.class.getName());
	private Parameter parameter;
	private Config config;
	private Serializer serializer;
	private JedisPool redisPool;
	private Communication communication;
	private AccountManager accountManager;
	private PermissionHandler permissionHandler;
	private ConnectionManager connectionManager;
	private StatusManager statusManager;
	private DenialOfServiceBlocker denialOfServiceBlocker;

	public static Cyclone getInstance()
	{
		return instance;
	}

	public Cyclone()
	{
		instance = this;

		createDeadlock();

		System.setIn(null);
		final Logger logger = ProxyServer.getInstance().getLogger();
		logger.setUseParentHandlers(false);
		for (final Handler handler : logger.getHandlers())
		{
			if (!(handler instanceof FileHandler))
			{
				logger.removeHandler(handler);
			}
		}
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
			getProxy().stop();
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

		denialOfServiceBlocker = new DenialOfServiceBlocker(getProxy().getScheduler(), 120_000L, 20);

		getProxy().getScheduler().schedule(this, new Runnable()
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
				}
				tick++;
			}
		}, 0L, TICK_IN_MILLISECONDS, TimeUnit.MILLISECONDS);

		getProxy().setReconnectHandler(null);
		setPipelineInitializer(new ProtocolInitializer(this));
		getProxy().getConfig().getListeners().stream().filter((listenerInfo) -> (!listenerInfo.isQueryEnabled())).forEach((listenerInfo) ->
		{
			statusManager.listenUT3(listenerInfo.getHost());
		});

		getProxy().getScheduler().schedule(this, () ->
		{
			cancelTimer("saveThread");
			cancelTimer("metricsThread");
		}, 1_500L, TimeUnit.MILLISECONDS);
		getProxy().getPluginManager().registerListener(this, new AccountListener(this));
		getProxy().getPluginManager().registerListener(this, new PermissionListener(this));

		config.getAlias().getAliases().forEach((alias, aliasOf) ->
		{
			LOGGER.log(Level.INFO, "Registered alias {0} as alias of {1}.", new Object[]
			{
				alias, aliasOf
			});

			getProxy().getPluginManager().registerCommand(this, new AliasingCommand(this, alias, aliasOf));
		});

		LOGGER.log(Level.INFO, "Started in just {0} milliseconds!", System.currentTimeMillis() - started);
	}

	@Override
	public void onDisable()
	{
		getProxy().getScheduler().cancel(this);
		getProxy().getPluginManager().unregisterListeners(this);

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

	public Config getConfig()
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

	public DenialOfServiceBlocker getDenialOfServiceBlocker()
	{
		return denialOfServiceBlocker;
	}

	private void setPipelineInitializer(final ChannelInitializer<SocketChannel> pipelineInitializer)
	{
		try
		{
			final Class<?> pipelineUtils = Class.forName("net.md_5.bungee.netty.PipelineUtils");

			final FieldAccessor fieldModifiers = Accessors.getFieldAccessor(Field.class, "modifiers");
			final FieldAccessor pipelineUtilsServerChild = Accessors.getFieldAccessor(pipelineUtils, "SERVER_CHILD");
			fieldModifiers.set(pipelineUtilsServerChild.getField(), pipelineUtilsServerChild.getField().getModifiers() & ~Modifier.FINAL);
			pipelineUtilsServerChild.set(null, pipelineInitializer);
			fieldModifiers.set(pipelineUtilsServerChild.getField(), pipelineUtilsServerChild.getField().getModifiers() | Modifier.FINAL);

			LOGGER.log(Level.INFO, "Pipeline Initializer changed to {0}.", pipelineInitializer.getClass().getName());
		}
		catch (final ClassNotFoundException ignore)
		{}
	}

	private void cancelTimer(final String name)
	{
		((Timer) Accessors.getFieldAccessor(getProxy().getClass(), name).get(getProxy())).cancel();

		LOGGER.log(Level.INFO, "Timer {0} cancelled.", name);
	}

	private void createDeadlock()
	{
		new ThreadFactoryBuilder().setName("Deadlock").setThreadFactory(threadFactory).setDeamon(false).build().newThread(() ->
		{
			try
			{
				final Object lock = new Object();
				synchronized (lock)
				{
					LOGGER.log(Level.INFO, "Deadlock created.");

					lock.wait();
				}
			}
			catch (final InterruptedException ignore)
			{}
		}).start();
	}
}
