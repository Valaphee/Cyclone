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

/**
 * Default
 *
 * @author valaphee
 */
public final class ByteBufferTemplate
		implements Template<ByteBuffer>
{
	private static final ByteBufferTemplate INSTANCE = new ByteBufferTemplate();

	public static ByteBufferTemplate getInstance()
	{
		return INSTANCE;
	}

	@Override
	public ByteBuffer read(final SerializationReader reader, ByteBuffer target)
			throws IOException
	{
		if (reader.readNull())
		{
			return null;
		}

		return ByteBuffer.wrap(reader.readBytes());
	}

	@Override
	public void write(final SerializationWriter writer, final ByteBuffer value)
			throws IOException
	{
		if (value == null)
		{
			writer.writeNull();

			return;
		}

		writer.writeBytes(value.array());
	}

	private ByteBufferTemplate()
	{}
}
