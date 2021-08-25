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
public final class ServerScorePacket
		implements Packet<PacketProtocolSubscriber>
{
	private UUID addressee;
	private String category;
	private UUID best;
	private double bestScore;

	public ServerScorePacket()
	{}

	public ServerScorePacket(final UUID addressee, final String category, final UUID best, final double bestScore)
	{
		this.addressee = addressee;
		this.category = category;
		this.best = best;
		this.bestScore = bestScore;
	}

	public UUID getAddressee()
	{
		return addressee;
	}

	public String getCategory()
	{
		return category;
	}

	public void setBest(final UUID best)
	{
		this.best = best;
	}

	public UUID getBest()
	{
		return best;
	}

	public void setBestScore(final double bestScore)
	{
		this.bestScore = bestScore;
	}

	public double getBestScore()
	{
		return bestScore;
	}

	@Override
	public void read(final PacketBuffer buffer)
	{
		addressee = buffer.readGUID();
		category = buffer.readString();
		best = buffer.readGUID();
		bestScore = buffer.readDouble();
	}

	@Override
	public void write(final PacketBuffer buffer)
	{
		buffer.writeGUID(addressee);
		buffer.writeString(category);
		buffer.writeGUID(best);
		buffer.writeDouble(bestScore);
	}

	@Override
	public void handle(final PacketProtocolSubscriber subscriber)
	{
		subscriber.serverScore(this);
	}
}
