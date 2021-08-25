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

package com.valaphee.cyclone.communication.packet;

import com.valaphee.cyclone.communication.ProtocolSubscriber;

/**
 * Default
 *
 * @author valaphee
 */
public interface PacketProtocolSubscriber
		extends ProtocolSubscriber
{
	void handshake(HandshakePacket packet);

	void handshakeFinish(HandshakeFinalPacket packet);

	void cryptographicRequest(CryptographicRequestPacket packet);

	void cryptographicResponse(CryptographicResponsePacket packet);

	void determine(DeterminationPacket packet);

	void negate(NegotiationPacket packet);

	void serverScore(ServerScorePacket packet);

	void serverStart(ServerStartPacket packet);

	void serverRegister(ServerRegisterPacket packet);

	void serverStopAndUnregister(ServerStopAndUnregisterPacket packet);

	void serverUpdate(ServerUpdatePacket packet);

	void playerProclamate(PlayerProclamationPacket packet);

	void playerAdmission(PlayerAdmissionPacket packet);

	void playerMessage(PlayerMessagePacket packet);

	void playerKick(PlayerKickPacket packet);

	void playerCrash(PlayerCrashPacket packet);

	void playerUpdate(PlayerUpdatePacket packet);
}
