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
import java.nio.DoubleBuffer;

/**
 * Default
 *
 * @author valaphee
 */
public final class DoubleBufferTemplate
		implements Template<DoubleBuffer>
{
	private static final DoubleBufferTemplate INSTANCE = new DoubleBufferTemplate();

	public static DoubleBufferTemplate getInstance()
	{
		return INSTANCE;
	}

	@Override
	public DoubleBuffer read(final SerializationReader reader, DoubleBuffer target)
			throws IOException
	{
		if (reader.readNull())
		{
			return null;
		}

		return ByteBuffer.wrap(reader.readBytes()).asDoubleBuffer();
	}

	@Override
	public void write(final SerializationWriter writer, final DoubleBuffer value)
			throws IOException
	{
		if (value == null)
		{
			writer.writeNull();

			return;
		}

		final ByteBuffer buffer = ByteBuffer.allocate(value.remaining() * Double.BYTES);
		buffer.asDoubleBuffer().put(value);
		writer.writeBytes(buffer.array());
	}

	private DoubleBufferTemplate()
	{}
}
