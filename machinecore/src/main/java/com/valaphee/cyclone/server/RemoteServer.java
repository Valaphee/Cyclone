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

import com.valaphee.cyclone.communication.packet.ServerStartPacket;
import com.valaphee.cyclone.communication.packet.ServerStopAndUnregisterPacket;
import com.valaphee.cyclone.connection.ConnectionManager;
import com.valaphee.cyclone.server.data.ServerData;

/**
 * Default
 *
 * @author valaphee
 */
public final class RemoteServer
		extends Server
{
	public RemoteServer(final ServerCategory category, final ServerData data)
	{
		super(category, data);
	}

	@Override
	public void start()
	{
		getCategory().getServers().add(this);
		getCategory().getRoot().context.get(ConnectionManager.class).getOutgoingConnection().sendPacket(new ServerStartPacket(getCategory().getRoot().context.get(ConnectionManager.class).getId(), getId(), getCategory().getUrn(), getNode()));
	}

	@Override
	public void stop()
	{
		getCategory().getServers().remove(this);
		getCategory().getRoot().context.get(ConnectionManager.class).getOutgoingConnection().sendPacket(new ServerStopAndUnregisterPacket(getCategory().getRoot().context.get(ConnectionManager.class).getId(), getId()));
	}

	@Override
	public void update()
	{
		if (data != null)
		{
			data = getCategory().getRepository().getServer(getId());
		}
	}
}
