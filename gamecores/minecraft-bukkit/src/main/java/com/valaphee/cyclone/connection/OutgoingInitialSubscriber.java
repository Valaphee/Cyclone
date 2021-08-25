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
import com.valaphee.cyclone.communication.CryptographicDecoder;
import com.valaphee.cyclone.communication.CryptographicEncoder;
import com.valaphee.cyclone.communication.ProtocolHandler;
import com.valaphee.cyclone.communication.packet.*;
import com.valaphee.cyclone.security.Secret;
import io.netty.channel.ChannelFuture;
import java.security.SecureRandom;
import java.util.Random;
import java.util.logging.Level;

/**
 * Default
 *
 * @author valaphee
 */
public final class OutgoingInitialSubscriber
		implements PacketProtocolSubscriber
{
	private static final Random RANDOM = new SecureRandom();
	private final ConnectionManager manager;
	private final byte[] salt;
	private ProtocolHandler handler;
	private volatile int step = 0;

	public OutgoingInitialSubscriber(final ConnectionManager manager)
	{
		this.manager = manager;
		salt = new byte[32];
		RANDOM.nextBytes(salt);
	}

	@Override
	public void initialize(final ProtocolHandler handler)
	{
		if (step != 0)
		{
			Cyclone.LOGGER.log(Level.WARNING, "Initial handler is used more than one time.");
			step = -1;

			return;
		}

		this.handler = handler;
		this.handler.sendPacket(new HandshakePacket(HandshakePacket.Mode.SLAVE, manager.getId(), handler.getAddress(), false), (ChannelFuture future) ->
		{
			step = 1;
		});
	}

	@Override
	public void update()
	{
		if (step == -1)
		{
			Cyclone.LOGGER.log(Level.SEVERE, "Initial process failed. Closing channel.");

			handler.getChannel().close();
		}
		else if (step == 3)
		{
			manager.outgoingFinished(handler);
		}
	}

	@Override
	public void disconnected()
	{
		Cyclone.LOGGER.log(Level.WARNING, "Disconnected.");
		step = -1;
	}

	@Override
	public void handshake(final HandshakePacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in the client.");
	}

	@Override
	public void handshakeFinish(final HandshakeFinalPacket packet)
	{
		if (step != 2)
		{
			Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this state.");
			step = -1;

			return;
		}

		if (packet.getStatus().equals(HandshakeFinalPacket.Status.ACCEPTED))
		{
			step = 3;

			return;
		}

		Cyclone.LOGGER.log(Level.WARNING, "Login has failed for %s", handler.getAddress());
		step = -1;
	}

	@Override
	public void cryptographicRequest(final CryptographicRequestPacket packet)
	{
		if (step != 1)
		{
			Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this state.");
			step = -1;

			return;
		}

		final Secret secret = Secret.generate(manager.getKey(), salt);
		handler.sendPacket(new CryptographicResponsePacket(packet.getCertificate().encrypt(salt), secret.encrypt(packet.getChallenge())), (ChannelFuture future) ->
		{
			handler.getChannel().pipeline().addBefore("frame-prepender", "frame-encryptor", new CryptographicEncoder(secret.getEncryptCipher()));
			handler.getChannel().pipeline().addBefore("frame-splitter", "frame-decryptor", new CryptographicDecoder(secret.getDecryptCipher()));
			step = 2;
		});
	}

	@Override
	public void cryptographicResponse(final CryptographicResponsePacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in the client.");
	}

	@Override
	public void determine(final DeterminationPacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void negate(final NegotiationPacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void serverScore(final ServerScorePacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void serverStart(final ServerStartPacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void serverRegister(final ServerRegisterPacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void serverStopAndUnregister(final ServerStopAndUnregisterPacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void serverUpdate(final ServerUpdatePacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void playerProclamate(final PlayerProclamationPacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void playerAdmission(final PlayerAdmissionPacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void playerMessage(final PlayerMessagePacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void playerKick(final PlayerKickPacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void playerCrash(final PlayerCrashPacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}

	@Override
	public void playerUpdate(final PlayerUpdatePacket packet)
	{
		Cyclone.LOGGER.log(Level.WARNING, "Can't process packet in this mode.");
	}
}
