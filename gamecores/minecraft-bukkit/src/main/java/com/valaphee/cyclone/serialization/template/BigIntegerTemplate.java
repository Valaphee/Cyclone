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
import java.math.BigInteger;

/**
 * Default
 *
 * @author valaphee
 */
public final class BigIntegerTemplate
		implements Template<BigInteger>
{
	private static final BigIntegerTemplate INSTANCE = new BigIntegerTemplate();

	public static BigIntegerTemplate getInstance()
	{
		return INSTANCE;
	}

	@Override
	public BigInteger read(final SerializationReader reader, BigInteger target)
			throws IOException
	{
		if (reader.readNull())
		{
			return null;
		}

		return new BigInteger(reader.readBytes());
	}

	@Override
	public void write(final SerializationWriter writer, final BigInteger value)
			throws IOException
	{
		if (value == null)
		{
			writer.writeNull();

			return;
		}

		writer.writeBytes(value.toByteArray());
	}

	private BigIntegerTemplate()
	{}
}
