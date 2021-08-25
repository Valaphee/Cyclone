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

import com.valaphee.cyclone.communication.Communication;
import com.valaphee.cyclone.config.CommunicationConfig;
import com.valaphee.cyclone.config.Config;
import com.valaphee.cyclone.config.Parameter;
import com.valaphee.cyclone.connection.ConnectionManager;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.engine.mode.LoadProcess;
import com.valaphee.cyclone.security.CertificatePair;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;

/**
 * Default
 *
 * @author valaphee
 */
public final class InitializeConnectionManager
		implements LoadProcess
{
	private final ConnectionManager manager;
	private final Communication communication;
	private final SocketAddress listenerAddress;

	public InitializeConnectionManager(final Context context)
	{
		communication = context.get(Communication.class);

		final Parameter parameter = context.get(Parameter.class);
		final CommunicationConfig communicationConfig = context.get(Config.class).getCommunication();
		UUID id = communicationConfig.getId();
		if (parameter.getId() != null)
		{
			id = parameter.getId();
		}
		String listenerHost = communicationConfig.getHost();
		if (parameter.getListenerHost() != null)
		{
			listenerHost = parameter.getListenerHost();
		}
		int listenerPort = communicationConfig.getPort();
		if (parameter.getListenerPort() != null)
		{
			listenerPort = parameter.getListenerPort();
		}
		listenerAddress = new InetSocketAddress(listenerHost, listenerPort);

		context.set(ConnectionManager.class, manager = new ConnectionManager(context, id, listenerAddress, communicationConfig.getAuth().toCharArray(), CertificatePair.generateSelfSigned()));
	}

	@Override
	public boolean step()
	{
		communication.listen(listenerAddress, manager);

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
