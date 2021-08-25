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

package com.valaphee.cyclone.serialization.template;

import com.valaphee.cyclone.serialization.stream.SerializationReader;
import com.valaphee.cyclone.serialization.stream.SerializationWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

/**
 * Default
 *
 * @author valaphee
 */
public final class ShortBufferTemplate
		implements Template<ShortBuffer>
{
	private static final ShortBufferTemplate INSTANCE = new ShortBufferTemplate();

	public static ShortBufferTemplate getInstance()
	{
		return INSTANCE;
	}

	@Override
	public ShortBuffer read(final SerializationReader reader, ShortBuffer target)
			throws IOException
	{
		if (reader.readNull())
		{
			return null;
		}

		return ByteBuffer.wrap(reader.readBytes()).asShortBuffer();
	}

	@Override
	public void write(final SerializationWriter writer, final ShortBuffer value)
			throws IOException
	{
		if (value == null)
		{
			writer.writeNull();

			return;
		}

		final ByteBuffer buffer = ByteBuffer.allocate(value.remaining() * Short.BYTES);
		buffer.asShortBuffer().put(value);
		writer.writeBytes(buffer.array());
	}

	private ShortBufferTemplate()
	{}
}
