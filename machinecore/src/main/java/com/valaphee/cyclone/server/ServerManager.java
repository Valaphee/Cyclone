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

import com.valaphee.cyclone.communication.packet.ServerScorePacket;
import com.valaphee.cyclone.connection.ConnectionManager;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.server.data.ServerData;
import java.io.File;
import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * Default
 *
 * @author valaphee
 */
public final class ServerManager
		extends ServerCategory
{
	final Context context;
	protected final PortMap portMap;
	private final File sharePath;
	private final File runPath;

	public ServerManager(final Context context, final PortMap portMap, final File sharePath, final File runPath)
	{
		this.context = context;
		this.portMap = portMap;
		variableContainer.addVariable("nodeId", context.get(ConnectionManager.class).getId().toString());
		variableContainer.addVariable("nodeHost", (((InetSocketAddress) context.get(ConnectionManager.class).getAddress()).getAddress()).getHostAddress());
		variableContainer.addVariable("nodePort", Integer.toString(((InetSocketAddress) context.get(ConnectionManager.class).getAddress()).getPort()));
		variableContainer.addVariable("sharePath", (this.sharePath = sharePath).getPath());
		variableContainer.addVariable("runPath", (this.runPath = runPath).getPath());
	}

	public File getSharePath()
	{
		return sharePath;
	}

	public File getRunPath()
	{
		return runPath;
	}

	public void create(final ServerCategory category)
	{
		context.get(ConnectionManager.class).getOutgoingConnection().sendPacket(new ServerScorePacket(context.get(ConnectionManager.class).getId(), category.getUrn(), context.get(ConnectionManager.class).getId(), category.getScoreCriteriaContainer().compute()));
	}

	@Deprecated
	public void startInternal(final UUID node, final UUID id, final ServerCategory category)
	{
		Server server = getServer(id);
		if (server == null)
		{
			if (context.get(ConnectionManager.class).getId().equals(node))
			{
				ServerData data = category.getRepository().getServer(id);
				if (data == null)
				{
					data = new ServerData();
					data.id = id;
					data.serverCategory = category.getUrn();
					data.node = context.get(ConnectionManager.class).getId();
					data.host = ((InetSocketAddress) context.get(ConnectionManager.class).getAddress()).getHostString();
					try
					{
						data.playerLimit = Integer.parseInt(category.getVariableContainer().getVariable("playerLimit"));
					}
					catch (final NumberFormatException ex)
					{
						return;
					}
				}
				server = new LocalServer(category, data, portMap);
			}
			else
			{
				server = new RemoteServer(category, category.getRepository().getServer(id));
			}
			category.servers.add(server);
		}

		server.start();
	}

	@Deprecated
	public void registerInternal(final UUID node, final UUID id, final ServerCategory category)
	{
		if (getServer(id) == null)
		{
			category.servers.add(new RemoteServer(category, category.getRepository().getServer(id)));
		}
	}

	@Deprecated
	public void stopInternal(final UUID id)
	{
		final Server server = getServer(id);
		if (server != null)
		{
			server.stop();
		}
	}

	@Deprecated
	public void unregisterInternal()
	{}
}
