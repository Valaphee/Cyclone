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
import java.util.UUID;

/**
 * Default
 *
 * @author valaphee
 */
public final class PlayerMessagePacket
		implements Packet<PacketProtocolSubscriber>
{
	private Type type;
	private UUID node;
	private long senderOrReceiver;
	private String receiverOrChannel;
	private String message;

	public PlayerMessagePacket()
	{}

	public PlayerMessagePacket(final long senderOrReceiver, final String message)
	{
		type = Type.STRAIGHT;
		this.senderOrReceiver = senderOrReceiver;
		this.message = message;
	}

	public PlayerMessagePacket(final UUID node, final long senderOrReceiver, final String message)
	{
		type = Type.JUMPING;
		this.node = node;
		this.senderOrReceiver = senderOrReceiver;
		this.message = message;
	}

	public PlayerMessagePacket(final Type type, final long sender, final String receiverOrChannel, final String message)
	{
		this.type = type;
		this.senderOrReceiver = sender;
		this.receiverOrChannel = receiverOrChannel;
		this.message = message;
	}

	public Type getType()
	{
		return type;
	}

	public UUID getNode()
	{
		return node;
	}

	public long getSenderOrReceiver()
	{
		return senderOrReceiver;
	}

	public String getReceiverOrChannel()
	{
		return receiverOrChannel;
	}

	public String getMessage()
	{
		return message;
	}

	@Override
	public void read(final PacketBuffer buffer)
	{
		type = Type.values()[buffer.readUnsignedByte()];
		if (Type.JUMPING.equals(type))
		{
			node = buffer.readGUID();
		}
		senderOrReceiver = buffer.readLong();
		if (type.equals(Type.PRIVATE_MESSAGE) || type.equals(Type.CHANNEL_MESSAGE))
		{
			receiverOrChannel = buffer.readString();
		}
		message = buffer.readString();
	}

	@Override
	public void write(final PacketBuffer buffer)
	{
		buffer.writeByte(type.ordinal());
		if (Type.JUMPING.equals(type))
		{
			buffer.writeGUID(node);
		}
		buffer.writeLong(senderOrReceiver);
		if (Type.PRIVATE_MESSAGE.equals(type) || Type.PRIVATE_MESSAGE.equals(type))
		{
			buffer.writeString(receiverOrChannel);
		}
		buffer.writeString(message);
	}

	@Override
	public void handle(final PacketProtocolSubscriber subscriber)
	{
		subscriber.playerMessage(this);
	}

	public static enum Type
	{
		STRAIGHT, JUMPING, PRIVATE_MESSAGE, CHANNEL_MESSAGE
	}
}
