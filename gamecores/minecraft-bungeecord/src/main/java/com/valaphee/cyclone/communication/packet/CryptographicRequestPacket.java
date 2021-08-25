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
import com.valaphee.cyclone.security.PublicIdentityCertificate;
import java.math.BigInteger;

/**
 * Default
 *
 * @author valaphee
 */
public final class CryptographicRequestPacket
		implements Packet<PacketProtocolSubscriber>
{
	private PublicIdentityCertificate certificate;
	private byte[] challenge;

	public CryptographicRequestPacket()
	{}

	public CryptographicRequestPacket(final PublicIdentityCertificate certificate, final byte[] challenge)
	{
		this.certificate = certificate;
		this.challenge = challenge;
	}

	public PublicIdentityCertificate getCertificate()
	{
		return certificate;
	}

	public byte[] getChallenge()
	{
		return challenge;
	}

	@Override
	public void read(final PacketBuffer buffer)
	{
		final byte[] modulus = new byte[buffer.readUnsignedShort()];
		buffer.readBytes(modulus);
		final byte[] exponent = new byte[buffer.readUnsignedShort()];
		buffer.readBytes(exponent);
		final byte[] signature = new byte[buffer.readUnsignedShort()];
		buffer.readBytes(signature);
		certificate = new PublicIdentityCertificate(new BigInteger(modulus), new BigInteger(exponent), new BigInteger(signature));
		challenge = new byte[buffer.readUnsignedShort()];
		buffer.readBytes(challenge);
	}

	@Override
	public void write(final PacketBuffer buffer)
	{
		final byte[] modulus = certificate.getModulus().toByteArray();
		buffer.writeShort(modulus.length);
		buffer.writeBytes(modulus);
		final byte[] exponent = certificate.getExponent().toByteArray();
		buffer.writeShort(exponent.length);
		buffer.writeBytes(exponent);
		final byte[] signature = certificate.getSignature().toByteArray();
		buffer.writeShort(signature.length);
		buffer.writeBytes(signature);
		buffer.writeShort(challenge.length);
		buffer.writeBytes(challenge);
	}

	@Override
	public void handle(final PacketProtocolSubscriber subscriber)
	{
		subscriber.cryptographicRequest(this);
	}
}
