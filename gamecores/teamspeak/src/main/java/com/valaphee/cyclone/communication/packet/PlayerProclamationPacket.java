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
 *
 * @author valaphee
 */
public final class PlayerProclamationPacket
		implements Packet<PacketProtocolSubscriber>
{
	private String requestName;
	private String requestAssociation;
	private Long answer;

	public PlayerProclamationPacket()
	{}

	public PlayerProclamationPacket(final String requestName, final String requestAssociation)
	{
		this.requestName = requestName;
		this.requestAssociation = requestAssociation;
	}

	public PlayerProclamationPacket(final long answer)
	{
		this.answer = answer;
	}

	public String getRequestName()
	{
		return requestName;
	}

	public String getRequestAssociation()
	{
		return requestAssociation;
	}

	public Long getAnswer()
	{
		return answer;
	}

	public void setAnswer(final long answer)
	{
		this.answer = answer;
	}

	@Override
	public void read(final PacketBuffer buffer)
	{
		requestName = buffer.readString();
		requestAssociation = buffer.readString();
		if (buffer.isReadable())
		{
			answer = buffer.readLong();
		}
	}

	@Override
	public void write(final PacketBuffer buffer)
	{
		buffer.writeString(requestName);
		buffer.writeString(requestAssociation);
		if (answer != null)
		{
			buffer.writeLong(answer);
		}
	}

	@Override
	public void handle(final PacketProtocolSubscriber subscriber)
	{
		subscriber.playerProclamate(this);
	}
}
