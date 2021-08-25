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
import java.nio.LongBuffer;

/**
 * Default
 *
 * @author valaphee
 */
public final class LongBufferTemplate
		implements Template<LongBuffer>
{
	private static final LongBufferTemplate INSTANCE = new LongBufferTemplate();

	public static LongBufferTemplate getInstance()
	{
		return INSTANCE;
	}

	@Override
	public LongBuffer read(final SerializationReader reader, LongBuffer target)
			throws IOException
	{
		if (reader.readNull())
		{
			return null;
		}

		return ByteBuffer.wrap(reader.readBytes()).asLongBuffer();
	}

	@Override
	public void write(final SerializationWriter writer, final LongBuffer value)
			throws IOException
	{
		if (value == null)
		{
			writer.writeNull();

			return;
		}

		final ByteBuffer buffer = ByteBuffer.allocate(value.remaining() * Long.BYTES);
		buffer.asLongBuffer().put(value);
		writer.writeBytes(buffer.array());
	}

	private LongBufferTemplate()
	{}
}
