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
import java.util.HashMap;
import java.util.Map;

/**
 * Default
 *
 * @author valaphee
 */
public final class MapTemplate<K, V>
		implements Template<Map<K, V>>
{
	private final Template<K> keyTemplate;
	private final Template<V> valueTemplate;

	public MapTemplate(final Template<K> keyTemplate, final Template<V> valueTemplate)
	{
		this.keyTemplate = keyTemplate;
		this.valueTemplate = valueTemplate;
	}

	@Override
	public Map<K, V> read(final SerializationReader reader, Map<K, V> target)
			throws IOException
	{
		if (reader.readNull())
		{
			return null;
		}

		final int size = reader.readMapBegin();
		if (target == null)
		{
			target = new HashMap<>(size);
		}
		for (int i = 0; i < size; ++i)
		{
			target.put(keyTemplate.read(reader, null), valueTemplate.read(reader, null));
		}
		reader.readMapEnd();

		return target;
	}

	@Override
	public void write(final SerializationWriter writer, final Map<K, V> value)
			throws IOException
	{
		if (value == null)
		{
			writer.writeNull();

			return;
		}

		writer.writeMapBegin(value.size());
		for (final Map.Entry<K, V> element : value.entrySet())
		{
			keyTemplate.write(writer, element.getKey());
			valueTemplate.write(writer, element.getValue());
		}
		writer.writeMapEnd();
	}
}
