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

import com.valaphee.cyclone.communication.Communication;
import com.valaphee.cyclone.communication.ProtocolHandler;
import com.valaphee.cyclone.communication.packet.NegotiationPacket;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.security.CertificatePair;
import java.net.SocketAddress;
import java.util.UUID;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Default
 *
 * @author valaphee
 */
public final class ConnectionManager
{
	private static final Logger LOGGER = LogManager.getLogger(IncomingInitialSubscriber.class);
	final Context context;
	private final UUID id;
	private final SocketAddress address;
	private final char[] key;
	private final CertificatePair certificatePair;
	private OutgoingSubscriber outgoingSubscriber;
	private IncomingSubscriber incomingSubscriber;
	private UUID outgoingId;
	private ProtocolHandler outgoingConnection;
	private UUID incomingId;
	private ProtocolHandler incomingConnection;

	public ConnectionManager(final Context context, final UUID id, final SocketAddress address, final char[] key, final CertificatePair certificatePair)
	{
		this.context = context;
		this.id = id;
		this.address = address;
		this.key = key;
		this.certificatePair = certificatePair;
	}

	public UUID getId()
	{
		return id;
	}

	public SocketAddress getAddress()
	{
		return address;
	}

	public char[] getKey()
	{
		return key;
	}

	public CertificatePair getCertificatePair()
	{
		return certificatePair;
	}

	public Communication getCommunication()
	{
		return context.get(Communication.class);
	}

	public UUID getOutgoingId()
	{
		return outgoingId;
	}

	public ProtocolHandler getOutgoingConnection()
	{
		return outgoingConnection;
	}

	public void outgoing(final UUID id, final SocketAddress address, final boolean negotiated)
	{
		outgoingId = id;
		if ((outgoingConnection != null) && (outgoingConnection.getChannel() != null) && (outgoingConnection.getChannel().isOpen()))
		{
			outgoingConnection.getChannel().close();
		}
		outgoingConnection = getCommunication().connect(address, new OutgoingInitialSubscriber(this, negotiated));
	}

	public void outgoingFinished(final ProtocolHandler handler)
	{
		if (outgoingSubscriber == null)
		{
			outgoingSubscriber = new OutgoingSubscriber(this);
		}
		handler.setSubscriber(outgoingSubscriber);

		LOGGER.log(Level.INFO, "Frontend constellation changed (Frontend: {}, Backbone: {}).", outgoingId, incomingId);
	}

	public UUID getIncomingId()
	{
		return incomingId;
	}

	public ProtocolHandler getIncomingConnection()
	{
		return incomingConnection;
	}

	public void incoming(final UUID id, final SocketAddress address, final ProtocolHandler handler, final boolean negotiated)
	{
		if (!negotiated)
		{
			if (incomingConnection == null)
			{
				incomingConnection = handler;
			}
			incomingConnection.sendPacket(new NegotiationPacket(id, address));
		}

		incomingId = id;
		if ((incomingConnection != null) && (incomingConnection.getChannel() != null) && (incomingConnection.getChannel().isOpen()))
		{
			incomingConnection.getChannel().close();
		}
		incomingConnection = handler;

		if (incomingSubscriber == null)
		{
			incomingSubscriber = new IncomingSubscriber(this);
		}
		handler.setSubscriber(incomingSubscriber);

		LOGGER.log(Level.INFO, "Backbone constellation changed (Frontend: {}, Backbone: {}).", outgoingId, incomingId);
	}
}
