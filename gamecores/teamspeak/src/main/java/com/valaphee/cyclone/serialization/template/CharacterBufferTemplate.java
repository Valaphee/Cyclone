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
import java.nio.CharBuffer;

/**
 * Default
 *
 * @author valaphee
 */
public final class CharacterBufferTemplate
		implements Template<CharBuffer>
{
	private static final CharacterBufferTemplate INSTANCE = new CharacterBufferTemplate();

	public static CharacterBufferTemplate getInstance()
	{
		return INSTANCE;
	}

	@Override
	public CharBuffer read(final SerializationReader reader, CharBuffer target)
			throws IOException
	{
		if (reader.readNull())
		{
			return null;
		}

		return ByteBuffer.wrap(reader.readBytes()).asCharBuffer();
	}

	@Override
	public void write(final SerializationWriter writer, final CharBuffer value)
			throws IOException
	{
		if (value == null)
		{
			writer.writeNull();

			return;
		}

		final ByteBuffer buffer = ByteBuffer.allocate(value.remaining() * Character.BYTES);
		buffer.asCharBuffer().put(value);
		writer.writeBytes(buffer.array());
	}

	private CharacterBufferTemplate()
	{}
}
