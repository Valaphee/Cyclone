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
import java.util.UUID;

/**
 * Default
 *
 * @author valaphee
 */
public final class UUIDTemplate
		implements Template<UUID>
{
	private static final UUIDTemplate INSTANCE = new UUIDTemplate();

	public static UUIDTemplate getInstance()
	{
		return INSTANCE;
	}

	@Override
	public UUID read(final SerializationReader reader, UUID target)
			throws IOException
	{
		if (reader.readNull())
		{
			return null;
		}

		reader.readListBegin();
		target = new UUID(reader.readLong(), reader.readLong());
		reader.readListEnd();

		return target;
	}

	@Override
	public void write(final SerializationWriter writer, final UUID value)
			throws IOException
	{
		if (value == null)
		{
			writer.writeNull();

			return;
		}

		writer.writeListBegin(2);
		writer.writeLong(value.getMostSignificantBits());
		writer.writeLong(value.getLeastSignificantBits());
		writer.writeListEnd();
	}

	private UUIDTemplate()
	{}
}
