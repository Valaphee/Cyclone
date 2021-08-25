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

/**
 * Default
 *
 * @author valaphee
 */
public final class IntegerArrayTemplate
		implements Template<int[]>
{
	private static final IntegerArrayTemplate INSTANCE = new IntegerArrayTemplate();

	public static IntegerArrayTemplate getInstance()
	{
		return INSTANCE;
	}

	@Override
	public int[] read(final SerializationReader reader, int[] target)
			throws IOException
	{
		if (reader.readNull())
		{
			return null;
		}

		final int length = reader.readListBegin();
		if ((target == null) || (target.length != length))
		{
			target = new int[length];
		}
		for (int i = 0; i < length; ++i)
		{
			target[i] = reader.readInt();
		}
		reader.readListEnd();

		return target;
	}

	@Override
	public void write(final SerializationWriter writer, final int[] value)
			throws IOException
	{
		if (value == null)
		{
			writer.writeNull();

			return;
		}

		writer.writeListBegin(value.length);
		for (final int element : value)
		{
			writer.writeInt(element);
		}
		writer.writeListEnd();
	}

	private IntegerArrayTemplate()
	{}
}
