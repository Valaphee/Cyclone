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
import java.util.HashSet;
import java.util.Set;

/**
 * Default
 *
 * @author valaphee
 */
public final class SetTemplate<E>
		implements Template<Set<E>>
{
	private final Template<E> elementTemplate;

	public SetTemplate(final Template<E> elementTemplate)
	{
		this.elementTemplate = elementTemplate;
	}

	@Override
	public Set<E> read(final SerializationReader reader, Set<E> target)
			throws IOException
	{
		if (reader.readNull())
		{
			return null;
		}

		final int size = reader.readListBegin();
		if (target == null)
		{
			target = new HashSet<>(size);
		}
		for (int i = 0; i < size; ++i)
		{
			target.add(elementTemplate.read(reader, null));
		}
		reader.readListEnd();

		return target;
	}

	@Override
	public void write(final SerializationWriter writer, final Set<E> value)
			throws IOException
	{
		if (value == null)
		{
			writer.writeNull();

			return;
		}

		writer.writeListBegin(value.size());
		for (final E element : value)
		{
			elementTemplate.write(writer, element);
		}
		writer.writeListEnd();
	}
}
