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
import java.lang.reflect.Type;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("unchecked")
public final class TemplateReference<T>
		implements Template<T>
{
	private final TemplateRegistry registry;
	private final Type type;
	private Template<T> reference;

	TemplateReference(final TemplateRegistry registry, final Type type)
	{
		this.registry = registry;
		this.type = type;
	}

	@Override
	public T read(final SerializationReader reader, T target)
			throws IOException
	{
		if (reference == null)
		{
			reference = (Template<T>) registry.registry.get(type);
		}

		return reference.read(reader, target);
	}

	@Override
	public void write(final SerializationWriter writer, final T value)
			throws IOException
	{
		if (reference == null)
		{
			reference = (Template<T>) registry.registry.get(type);
		}

		reference.write(writer, value);
	}
}
