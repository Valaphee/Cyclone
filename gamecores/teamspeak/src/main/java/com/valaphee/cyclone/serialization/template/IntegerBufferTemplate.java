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
import java.nio.IntBuffer;

/**
 * Default
 *
 * @author valaphee
 */
public final class IntegerBufferTemplate
		implements Template<IntBuffer>
{
	private static final IntegerBufferTemplate INSTANCE = new IntegerBufferTemplate();

	public static IntegerBufferTemplate getInstance()
	{
		return INSTANCE;
	}

	@Override
	public IntBuffer read(final SerializationReader reader, IntBuffer target)
			throws IOException
	{
		if (reader.readNull())
		{
			return null;
		}

		return ByteBuffer.wrap(reader.readBytes()).asIntBuffer();
	}

	@Override
	public void write(final SerializationWriter writer, final IntBuffer value)
			throws IOException
	{
		if (value == null)
		{
			writer.writeNull();

			return;
		}

		final ByteBuffer buffer = ByteBuffer.allocate(value.remaining() * Integer.BYTES);
		buffer.asIntBuffer().put(value);
		writer.writeBytes(buffer.array());
	}

	private IntegerBufferTemplate()
	{}
}
