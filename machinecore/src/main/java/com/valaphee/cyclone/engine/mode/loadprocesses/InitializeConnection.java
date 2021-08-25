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

package com.valaphee.cyclone.engine.mode.loadprocesses;

import com.valaphee.cyclone.config.CommunicationConfig;
import com.valaphee.cyclone.config.Config;
import com.valaphee.cyclone.config.Parameter;
import com.valaphee.cyclone.connection.ConnectionManager;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.engine.mode.LoadProcess;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Default
 *
 * @author valaphee
 */
public final class InitializeConnection
		implements LoadProcess
{
	private final ConnectionManager manager;
	private final SocketAddress connectorAddress;

	public InitializeConnection(final Context context)
	{
		manager = context.get(ConnectionManager.class);

		final Parameter parameter = context.get(Parameter.class);
		final CommunicationConfig communicationConfig = context.get(Config.class).getCommunication();
		String connectorHost = communicationConfig.getHost();
		if (parameter.getConnectorHost() != null)
		{
			connectorHost = parameter.getConnectorHost();
		}
		int connectorPort = communicationConfig.getPort();
		if (parameter.getConnectorPort() != null)
		{
			connectorPort = parameter.getConnectorPort();
		}
		connectorAddress = new InetSocketAddress(connectorHost, connectorPort);
	}

	@Override
	public boolean step()
	{
		manager.outgoing(manager.getId(), connectorAddress, false);

		return true;
	}

	@Override
	public float getProgress()
	{
		return 1.0f;
	}

	@Override
	public int getExpectedCost()
	{
		return 1;
	}
}
