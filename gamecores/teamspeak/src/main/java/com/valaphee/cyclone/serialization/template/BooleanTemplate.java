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
public final class BooleanTemplate
		implements Template<Boolean>
{
	private static final BooleanTemplate INSTANCE = new BooleanTemplate();

	public static BooleanTemplate getInstance()
	{
		return INSTANCE;
	}

	@Override
	public Boolean read(final SerializationReader reader, Boolean target)
			throws IOException
	{
		if (reader.readNull())
		{
			return null;
		}

		return reader.readBoolean();
	}

	@Override
	public void write(final SerializationWriter writer, final Boolean value)
			throws IOException
	{
		if (value == null)
		{
			writer.writeNull();

			return;
		}

		writer.writeBoolean(value);
	}

	private BooleanTemplate()
	{}
}
