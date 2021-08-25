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

import com.valaphee.cyclone.communication.ProtocolHandler;
import com.valaphee.cyclone.communication.packet.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Default
 *
 * @author valaphee
 */
public final class OutgoingSubscriber
		implements PacketProtocolSubscriber
{
	private static final Logger LOGGER = LogManager.getLogger(OutgoingSubscriber.class);
	private final ConnectionManager manager;

	public OutgoingSubscriber(final ConnectionManager manager)
	{
		this.manager = manager;
	}

	@Override
	public void initialize(final ProtocolHandler handler)
	{}

	@Override
	public void update()
	{}

	@Override
	public void disconnected()
	{}

	@Override
	public void handshake(final HandshakePacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void handshakeFinish(final HandshakeFinalPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void cryptographicRequest(final CryptographicRequestPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void cryptographicResponse(final CryptographicResponsePacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void determine(final DeterminationPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process unimplemented packet.");
	}

	@Override
	public void negate(final NegotiationPacket packet)
	{
		manager.outgoing(packet.getNode(), packet.getAddress(), true);
	}

	@Override
	public void serverScore(final ServerScorePacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in the backbone.");
	}

	@Override
	public void serverStart(final ServerStartPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in the backbone.");
	}

	@Override
	public void serverRegister(final ServerRegisterPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in the backbone.");
	}

	@Override
	public void serverStopAndUnregister(final ServerStopAndUnregisterPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in the backbone.");
	}

	@Override
	public void serverUpdate(final ServerUpdatePacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in the backbone.");
	}

	@Override
	public void playerProclamate(final PlayerProclamationPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void playerAdmission(final PlayerAdmissionPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void playerMessage(final PlayerMessagePacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void playerKick(final PlayerKickPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void playerCrash(final PlayerCrashPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void playerUpdate(final PlayerUpdatePacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}
}
