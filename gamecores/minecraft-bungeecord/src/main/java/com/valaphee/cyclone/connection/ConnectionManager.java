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

package com.valaphee.cyclone.connection;

import com.valaphee.cyclone.Cyclone;
import com.valaphee.cyclone.communication.Communication;
import com.valaphee.cyclone.communication.ProtocolHandler;
import com.valaphee.cyclone.security.PublicIdentityCertificate;
import java.net.SocketAddress;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Default
 *
 * @author valaphee
 */
public final class ConnectionManager
{
	final Cyclone cyclone;
	private final UUID id;
	private final char[] key;
	private final PublicIdentityCertificate certificate;
	private OutgoingSubscriber outgoingSubscriber;
	private UUID outgoingId;
	private ProtocolHandler outgoingConnection;

	public ConnectionManager(final Cyclone cyclone, final UUID id, final char[] key, final PublicIdentityCertificate certificate)
	{
		this.cyclone = cyclone;
		this.id = id;
		this.key = key;
		this.certificate = certificate;
	}

	public UUID getId()
	{
		return id;
	}

	public char[] getKey()
	{
		return key;
	}

	public PublicIdentityCertificate getCertificate()
	{
		return certificate;
	}

	public Communication getCommunication()
	{
		return cyclone.getCommunication();
	}

	public UUID getOutgoingId()
	{
		return outgoingId;
	}

	public ProtocolHandler getOutgoingConnection()
	{
		return outgoingConnection;
	}

	public void outgoing(final UUID id, final SocketAddress address)
	{
		outgoingId = id;
		if ((outgoingConnection != null) && (outgoingConnection.getChannel() != null) && (outgoingConnection.getChannel().isOpen()))
		{
			outgoingConnection.getChannel().close();
		}
		outgoingConnection = getCommunication().connect(address, new OutgoingInitialSubscriber(this));
	}

	public void outgoingFinished(final ProtocolHandler handler)
	{
		if (outgoingSubscriber == null)
		{
			outgoingSubscriber = new OutgoingSubscriber(this);
		}
		handler.setSubscriber(outgoingSubscriber);

		Cyclone.LOGGER.log(Level.INFO, "Frontend constellation changed (Frontend: {0}).", outgoingId);
	}
}
