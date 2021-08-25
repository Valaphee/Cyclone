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

import com.valaphee.cyclone.communication.Packet;
import com.valaphee.cyclone.communication.PacketBuffer;

/**
 * Default
 *
 * @author valaphee
 */
public final class CryptographicResponsePacket
		implements Packet<PacketProtocolSubscriber>
{
	private byte[] salt;
	private byte[] challenge;

	public CryptographicResponsePacket()
	{}

	public CryptographicResponsePacket(final byte[] salt, final byte[] challenge)
	{
		this.salt = salt;
		this.challenge = challenge;
	}

	public byte[] getSalt()
	{
		return salt;
	}

	public byte[] getChallenge()
	{
		return challenge;
	}

	@Override
	public void read(final PacketBuffer buffer)
	{
		salt = new byte[buffer.readUnsignedShort()];
		buffer.readBytes(salt);
		challenge = new byte[buffer.readUnsignedShort()];
		buffer.readBytes(challenge);
	}

	@Override
	public void write(final PacketBuffer buffer)
	{
		buffer.writeShort(salt.length);
		buffer.writeBytes(salt);
		buffer.writeShort(challenge.length);
		buffer.writeBytes(challenge);
	}

	@Override
	public void handle(final PacketProtocolSubscriber subscriber)
	{
		subscriber.cryptographicResponse(this);
	}
}
