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

import com.valaphee.cyclone.communication.CryptographicDecoder;
import com.valaphee.cyclone.communication.CryptographicEncoder;
import com.valaphee.cyclone.communication.ProtocolHandler;
import com.valaphee.cyclone.communication.packet.*;
import com.valaphee.cyclone.security.Secret;
import com.valaphee.cyclone.server.LocalServer;
import com.valaphee.cyclone.server.ServerManager;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Default
 *
 * @author valaphee
 */
public final class IncomingInitialSubscriber
		implements PacketProtocolSubscriber
{
	private static final Logger LOGGER = LogManager.getLogger(IncomingInitialSubscriber.class);
	private static final Random RANDOM = new SecureRandom();
	private final ConnectionManager manager;
	private final byte[] challenge;
	private ProtocolHandler handler;
	private HandshakePacket handshake;
	private volatile int step = 0;

	public IncomingInitialSubscriber(final ConnectionManager manager)
	{
		this.manager = manager;
		challenge = new byte[32];
		RANDOM.nextBytes(challenge);
	}

	@Override
	public void initialize(final ProtocolHandler handler)
	{
		if (step != 0)
		{
			LOGGER.log(Level.WARN, "Initial handler is used more than one time.");
			step = -1;

			return;
		}

		this.handler = handler;
	}

	@Override
	public void update()
	{
		if (step == -1)
		{
			LOGGER.log(Level.ERROR, "Initial process failed. Closing channel.");

			handler.getChannel().close();
		}
		else if (step == 2)
		{
			if (handshake.getMode().equals(HandshakePacket.Mode.MASTER))
			{
				manager.incoming(handshake.getId(), handshake.getAddress(), handler, handshake.isNegotiated());
			}
			else if (handshake.getMode().equals(HandshakePacket.Mode.SLAVE))
			{
				final LocalServer server = (LocalServer) manager.context.get(ServerManager.class).getServer(handshake.getId(), true);
				if (server != null)
				{
					server.setConnection(handler);
				}
				else
				{
					LOGGER.log(Level.WARN, "Unknown server tried to connect.");
					step = -1;
				}
			}
		}
	}

	@Override
	public void disconnected()
	{
		LOGGER.log(Level.WARN, "Disconnected.");
		step = -1;
	}

	@Override
	public void handshake(final HandshakePacket packet)
	{
		if (step != 0)
		{
			LOGGER.log(Level.WARN, "Can't process packet in this state.");
			step = -1;

			return;
		}

		handshake = packet;
		handler.sendPacket(new CryptographicRequestPacket(manager.getCertificatePair().getPublicCertificate(), challenge));
		step = 1;
	}

	@Override
	public void handshakeFinish(final HandshakeFinalPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in the server.");
	}

	@Override
	public void cryptographicRequest(final CryptographicRequestPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in the server.");
	}

	@Override
	public void cryptographicResponse(final CryptographicResponsePacket packet)
	{
		if (step != 1)
		{
			LOGGER.log(Level.WARN, "Can't process packet in this state.");
			step = -1;

			return;
		}

		final Secret secret = Secret.generate(manager.getKey(), manager.getCertificatePair().getPrivateCertificate().decrypt(packet.getSalt()));
		handler.getChannel().pipeline().addBefore("frame-prepender", "frame-encryptor", new CryptographicEncoder(secret.getEncryptCipher()));
		handler.getChannel().pipeline().addBefore("frame-splitter", "frame-decryptor", new CryptographicDecoder(secret.getDecryptCipher()));
		if (Arrays.equals(secret.decrypt(packet.getChallenge()), challenge))
		{
			handler.sendPacket(new HandshakeFinalPacket(HandshakeFinalPacket.Status.ACCEPTED));
			step = 2;

			return;
		}

		handler.sendPacket(new HandshakeFinalPacket(HandshakeFinalPacket.Status.DECLINED));
		LOGGER.log(Level.WARN, "Login has failed for {} on {}", handshake.getId(), handler.getAddress());
		step = -1;
	}

	@Override
	public void determine(final DeterminationPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void negate(final NegotiationPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void serverScore(final ServerScorePacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void serverStart(final ServerStartPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void serverRegister(final ServerRegisterPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void serverStopAndUnregister(final ServerStopAndUnregisterPacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
	}

	@Override
	public void serverUpdate(final ServerUpdatePacket packet)
	{
		LOGGER.log(Level.WARN, "Can't process packet in this mode.");
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
