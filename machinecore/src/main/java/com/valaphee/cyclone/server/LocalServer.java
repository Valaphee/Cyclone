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

package com.valaphee.cyclone.server;

import com.valaphee.cyclone.account.AccountManager;
import com.valaphee.cyclone.account.Player;
import com.valaphee.cyclone.command.CommandHandler;
import com.valaphee.cyclone.communication.ProtocolHandler;
import com.valaphee.cyclone.communication.packet.ServerRegisterPacket;
import com.valaphee.cyclone.communication.packet.ServerStopAndUnregisterPacket;
import com.valaphee.cyclone.connection.ConnectionManager;
import com.valaphee.cyclone.connection.ServerSubscriber;
import com.valaphee.cyclone.server.data.ServerData;
import com.valaphee.cyclone.status.StatusManager;
import com.valaphee.cyclone.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Stack;

/**
 * Default
 *
 * @author valaphee
 */
public final class LocalServer
		extends Server

{
	private final PortMap portMap;
	private final VariableContainer variableContainer;
	private final File path;
	private Process process;
	private ProtocolHandler connection;

	public LocalServer(final ServerCategory category, final ServerData data, final PortMap portMap)
	{
		super(category, data);

		this.portMap = portMap;
		variableContainer = category.getVariableContainer().clone();
		variableContainer.addVariable("id", getId().toString());
		variableContainer.addVariable("host", getHost());
		variableContainer.addVariable("path", (path = new File(getCategory().getRoot().getRunPath(), getId().toString())).getPath());
	}

	public File getPath()
	{
		return path;
	}

	public Process getProcess()
	{
		return process;
	}

	public ProtocolHandler getConnection()
	{
		return connection;
	}

	public void setConnection(final ProtocolHandler connection)
	{
		this.connection = connection;
		this.connection.setSubscriber(new ServerSubscriber(this, getCategory().getRoot().context.get(AccountManager.class), getCategory().getRoot().context.get(CommandHandler.class), getCategory().getRoot().context.get(StatusManager.class)));
	}

	@Override
	public void start()
	{
		String mask = null;

		final Stack<ServerCategory> stacked = new Stack<>();
		for (ServerCategory parent = getCategory(); parent != null; parent = parent.getParent())
		{
			stacked.push(parent);
		}

		if (!path.exists())
		{
			path.mkdirs();
		}

		for (final ServerCategory child : stacked)
		{
			if ((child.getPath() != null) && child.getPath().exists())
			{
				try
				{
					FileUtil.recursiveCopy(child.getPath(), path);
				}
				catch (final IOException ignore)
				{}
			}
			if (child.getMask() != null)
			{
				mask = child.getMask();
			}
		}

		if (mask != null)
		{
			if (variableContainer.getVariable("port") == null)
			{
				variableContainer.addVariable("port", Integer.toString(data.port = portMap.occupyPort()));
			}
			else
			{
				portMap.occupyPort(data.port = Short.parseShort(variableContainer.getVariable("port")));
			}

			try
			{
				process = new ProcessBuilder(variableContainer.parse(mask)).directory(path).start();
			}
			catch (final IOException ignore)
			{}
		}

		getCategory().getServers().add(this);
		getCategory().getRepository().setServer(data);

		getCategory().getRoot().context.get(ConnectionManager.class).getOutgoingConnection().sendPacket(new ServerRegisterPacket(getCategory().getRoot().context.get(ConnectionManager.class).getId(), getId(), getCategory().getUrn(), new InetSocketAddress(getHost(), getPort())));
	}

	@Override
	public void stop()
	{
		getCategory().getRoot().context.get(AccountManager.class).getUsers().stream().filter((user) -> (getId().equals(user.getServer()))).forEachOrdered((user) ->
		{
			((Player) user).spread("spigot:hub");
		});

		getCategory().getServers().remove(this);
		getCategory().getRepository().removeServer(getId());

		if (process != null)
		{
			process.destroy();
		}

		portMap.freePort(data.port);
		if ((!isPersistent()) && path.exists())
		{
			try
			{
				FileUtil.recursiveDelete(path);
			}
			catch (final IOException ignore)
			{}
		}

		getCategory().getRoot().context.get(ConnectionManager.class).getOutgoingConnection().sendPacket(new ServerStopAndUnregisterPacket(getCategory().getRoot().context.get(ConnectionManager.class).getId(), getId()));
	}

	@Override
	public void update()
	{
		getCategory().getRepository().setServer(data);

		if ((data.state != null) && data.state.equals(ServerData.State.RESTARTING))
		{
			stop();
		}
	}
}
