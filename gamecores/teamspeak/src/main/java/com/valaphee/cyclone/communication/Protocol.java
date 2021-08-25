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

package com.valaphee.cyclone.communication;

import com.valaphee.cyclone.communication.packet.*;
import com.valaphee.cyclone.reflect.Accessors;
import com.valaphee.cyclone.reflect.ConstructorAccessor;
import java.util.HashMap;
import java.util.Map;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings(
		{
			"unchecked", "rawtypes"
		})
public final class Protocol
{
	private static final Map<Class<?>, Integer> PACKETS = new HashMap<>(0xFF);
	private static final Class<?>[] PACKET_CLASSES = new Class[0xFF];
	private static final ConstructorAccessor[] PACKET_CONSTRUCTORS = new ConstructorAccessor[0xFF];

	public static Packet<?> getPacket(final int id)
	{
		if (id > 0xFF)
		{
			throw new ProtocolException("Id " + id + " is out of range.");
		}
		if (PACKET_CLASSES[id] == null)
		{
			throw new ProtocolException("No packet with id " + id + " found.");
		}

		try
		{
			return (Packet<?>) PACKET_CONSTRUCTORS[id].invoke();
		}
		catch (final IllegalStateException ex)
		{
			throw new ProtocolException("Packet " + PACKET_CLASSES[id].getSimpleName() + " could not be constructed.");
		}
	}

	public static int getPacketId(final Packet<?> packet)
	{
		if (!PACKETS.containsKey(packet.getClass()))
		{
			throw new ProtocolException("Packet " + packet.getClass().getSimpleName() + " is not registered.");
		}

		return PACKETS.get(packet.getClass());
	}

	static void registerPacket(final int id, final Class<? extends Packet<?>> clazz)
	{
		try
		{
			PACKET_CONSTRUCTORS[id] = Accessors.getConstructorAccessor(clazz);
		}
		catch (final IllegalArgumentException ex)
		{
			throw new ProtocolException("Packet " + clazz.getSimpleName() + " has no default constructor.", ex);
		}
		PACKET_CLASSES[id] = clazz;
		PACKETS.put(clazz, id);
	}

	static void unregisterPacket(final int id)
	{
		if (!PACKETS.containsValue(id))
		{
			throw new ProtocolException("No packet with id " + id + " found.");
		}
		PACKETS.remove(PACKET_CLASSES[id]);
		PACKET_CLASSES[id] = null;
		PACKET_CONSTRUCTORS[id] = null;
	}

	static
	{
		registerPacket('H', HandshakePacket.class);
		registerPacket('h', HandshakeFinalPacket.class);
		registerPacket('C', CryptographicRequestPacket.class);
		registerPacket('c', CryptographicResponsePacket.class);
		registerPacket('D', DeterminationPacket.class);
		registerPacket('N', NegotiationPacket.class);
		registerPacket('o', ServerScorePacket.class);
		registerPacket('E', ServerStartPacket.class);
		registerPacket('e', ServerRegisterPacket.class);
		registerPacket('X', ServerStopAndUnregisterPacket.class);
		registerPacket('U', ServerUpdatePacket.class);
		registerPacket('F', PlayerProclamationPacket.class);
		registerPacket('A', PlayerAdmissionPacket.class);
		registerPacket('M', PlayerMessagePacket.class);
		registerPacket('K', PlayerKickPacket.class);
		registerPacket('k', PlayerCrashPacket.class);
		registerPacket('P', PlayerUpdatePacket.class);
	}

	private Protocol()
	{}
}
