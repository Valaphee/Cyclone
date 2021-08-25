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

import com.valaphee.cyclone.reflect.Accessors;
import com.valaphee.cyclone.reflect.ConstructorAccessor;
import com.valaphee.cyclone.serialization.stream.SerializationReader;
import com.valaphee.cyclone.serialization.stream.SerializationWriter;
import com.valaphee.cyclone.serialization.template.Template;
import com.valaphee.cyclone.serialization.template.TemplateRegistry;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("unchecked")
public final class ReflectiveTemplateBuilder
		extends AbstractTemplateBuilder
{
	public ReflectiveTemplateBuilder(final TemplateRegistry registry)
	{
		super(registry);
	}

	@Override
	public void appendClassLoader(final ClassLoader classLoader)
	{}

	@Override
	public boolean match(final Type type)
	{
		return (!((Class<?>) type).isEnum()) && (!((Class<?>) type).isInterface());
	}

	@Override
	public <T> Template<T> build(final Class<?> clazz, final TemplateEntry[] entries)
	{
		for (final TemplateEntry entry : entries)
		{
			if (entry == null)
			{
				continue;
			}

			final Field field = entry.getField();
			if (!Modifier.isPublic(field.getModifiers()))
			{
				field.setAccessible(true);
			}
		}

		final ReflectionFieldTemplate[] templates = new ReflectionFieldTemplate[entries.length];
		for (int i = 0; i < entries.length; ++i)
		{
			templates[i] = new ReflectionFieldTemplate(entries[i], registry.lookup(entries[i].getField().getGenericType()));
		}

		return new ReflectionClassTemplate<>(Accessors.getConstructorAccessor(clazz), templates);
	}

	private static class ReflectionClassTemplate<T>
			implements Template<T>
	{
		private final ConstructorAccessor clazz;
		private final ReflectionFieldTemplate[] templates;

		public ReflectionClassTemplate(final ConstructorAccessor clazz, final ReflectionFieldTemplate[] templates)
		{
			this.clazz = clazz;
			this.templates = templates;
		}

		@Override
		public T read(final SerializationReader reader, T target)
				throws IOException
		{
			if (reader.readNull())
			{
				return null;
			}

			reader.readListBegin();
			if (target == null)
			{
				target = (T) clazz.invoke();
			}
			for (final ReflectionFieldTemplate template : templates)
			{
				if (!reader.readNull())
				{
					template.read(reader, target);
				}
			}
			reader.readListEnd();

			return target;
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

			writer.writeListBegin(templates.length);
			for (final ReflectionFieldTemplate template : templates)
			{
				template.write(writer, template.entry.get(value));
			}
			writer.writeListEnd();
		}
	}

	private static class ReflectionFieldTemplate
			implements Template<Object>
	{
		protected final TemplateEntry entry;
		private final Template<Object> template;

		public ReflectionFieldTemplate(final TemplateEntry entry, final Template<Object> template)
		{
			this.entry = entry;
			this.template = template;
		}

		@Override
		public Object read(final SerializationReader reader, Object target)
				throws IOException
		{
			final Object value = template.read(reader, entry.get(target));
			if (value != entry.get(target))
			{
				entry.set(target, value);
			}

			return value;
		}

		@Override
		public void write(final SerializationWriter writer, final Object value)
				throws IOException
		{
			if (value == null)
			{
				writer.writeNull();

				return;
			}

			template.write(writer, value);
		}
	}
}
