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

package com.valaphee.cyclone.serialization.template.builder;

import com.valaphee.cyclone.serialization.stream.SerializationReader;
import com.valaphee.cyclone.serialization.stream.SerializationWriter;
import com.valaphee.cyclone.serialization.template.Template;
import com.valaphee.cyclone.serialization.template.TemplateRegistry;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("unchecked")
public final class JavaEnumerationTemplateBuilder
		extends AbstractTemplateBuilder
{
	public JavaEnumerationTemplateBuilder(final TemplateRegistry registry)
	{
		super(registry);
	}

	@Override
	public void appendClassLoader(final ClassLoader classLoader)
	{}

	@Override
	public boolean match(final Type type)
	{
		return ((Class<?>) type).isEnum();
	}

	@Override
	public <T> Template<T> build(final Type type)
	{
		return new EnumerationTemplate<>((Class<T>) type);
	}

	@Override
	public <T> Template<T> build(final Class<?> clazz, final TemplateEntry[] entries)
	{
		throw new UnsupportedOperationException(clazz.getName());
	}

	public static final class EnumerationTemplate<T>
			implements Template<T>
	{
		private final List<T> ordinals;
		private final Map<T, Integer> ordinalsKeyed;

		public EnumerationTemplate(final Class<T> clazz)
		{
			final T[] constants = clazz.getEnumConstants();
			ordinals = new ArrayList<>(constants.length);
			ordinalsKeyed = new HashMap<>(constants.length);
			for (int i = 0; i < constants.length; ++i)
			{
				ordinals.add(constants[i]);
				ordinalsKeyed.put(constants[i], i);
			}
		}

		@Override
		public T read(final SerializationReader reader, T target)
				throws IOException
		{
			if (reader.readNull())
			{
				return null;
			}

			final int ordinal = reader.readInt();
			if (ordinal < ordinals.size())
			{
				return ordinals.get(ordinal);
			}

			return null;
		}

		@Override
		public void write(final SerializationWriter writer, final T value)
				throws IOException
		{
			if (value == null)
			{
				writer.writeNull();

				return;
			}

			writer.writeInt(ordinalsKeyed.get(value));
		}
	}
}
