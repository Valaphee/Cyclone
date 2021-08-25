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

import com.valaphee.cyclone.util.CharsetUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.WrappedByteBuf;
import java.util.UUID;

/**
 * Default
 *
 * @author valaphee
 */
public final class PacketBuffer
		extends WrappedByteBuf
{
	public PacketBuffer(final ByteBuf buffer)
	{
		super(buffer);
	}

	public UUID readGUID()
	{
		return new UUID(readLong(), readLong());
	}

	public void writeGUID(final UUID value)
	{
		writeLong(value.getMostSignificantBits());
		writeLong(value.getLeastSignificantBits());
	}

	public String readString()
	{
		final byte[] bytes = new byte[readUnsignedShort()];
		readBytes(bytes);

		return new String(bytes, CharsetUtil.UTF_8);
	}

	public void writeString(final String value)
	{
		final byte[] bytes = value.getBytes(CharsetUtil.UTF_8);
		writeShort(bytes.length);
		writeBytes(bytes);
	}
}
