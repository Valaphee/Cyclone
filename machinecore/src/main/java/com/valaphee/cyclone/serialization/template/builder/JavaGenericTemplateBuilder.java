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
import com.valaphee.cyclone.serialization.template.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings(
		{
			"rawtypes", "unchecked"
		})
public final class JavaGenericTemplateBuilder
		extends AbstractTemplateBuilder
{
	public JavaGenericTemplateBuilder(final TemplateRegistry registry)
	{
		super(registry);
	}

	@Override
	public void appendClassLoader(final ClassLoader classLoader)
	{}

	@Override
	public boolean match(final Type type)
	{
		return type instanceof GenericArrayType ? true : ((Class<?>) type).isArray();
	}

	@Override
	public <T> Template<T> build(final Type type)
	{
		Type elementType;
		Class<?> elementClass;
		int dimension = 1;
		if (type instanceof GenericArrayType)
		{
			elementType = ((GenericArrayType) type).getGenericComponentType();
			while (elementType instanceof GenericArrayType)
			{
				elementType = ((GenericArrayType) elementType).getGenericComponentType();
				dimension += 1;
			}
			if (elementType instanceof ParameterizedType)
			{
				elementClass = (Class<?>) ((ParameterizedType) elementType).getRawType();
			}
			else
			{
				elementClass = (Class<?>) elementType;
			}
		}
		else
		{
			elementClass = ((Class<?>) type).getComponentType();
			while (elementClass.isArray())
			{
				elementClass = elementClass.getComponentType();
				dimension += 1;
			}
			elementType = elementClass;
		}

		return build(type, dimension, elementType, elementClass);
	}

	@Override
	public <T> Template<T> build(final Class<?> clazz, final TemplateEntry[] entries)
	{
		throw new UnsupportedOperationException(clazz.getName());
	}

	private Template build(final Type type, final int dimension, final Type elementType, final Class<?> elementClass)
	{
		if (dimension == 1)
		{
			if (elementClass == boolean.class)
			{
				return BooleanArrayTemplate.getInstance();
			}
			else if (elementClass == byte.class)
			{
				return ByteArrayTemplate.getInstance();
			}
			else if (elementClass == short.class)
			{
				return ShortArrayTemplate.getInstance();
			}
			else if (elementClass == int.class)
			{
				return IntegerArrayTemplate.getInstance();
			}
			else if (elementClass == long.class)
			{
				return LongArrayTemplate.getInstance();
			}
			else if (elementClass == float.class)
			{
				return FloatArrayTemplate.getInstance();
			}
			else if (elementClass == double.class)
			{
				return DoubleArrayTemplate.getInstance();
			}
			else if (elementClass == char.class)
			{
				return CharacterArrayTemplate.getInstance();
			}
		}
		else
		{
			return new GenericTemplate(Array.newInstance(elementClass, 0).getClass(), build(type, dimension - 1, elementType, elementClass));
		}

		return null;
	}

	private static class GenericTemplate
			implements Template<Object>
	{
		private final Class<?> clazz;
		private final Template<Object> template;

		public GenericTemplate(final Class<?> clazz, final Template<Object> template)
		{
			this.clazz = clazz;
			this.template = template;
		}

		@Override
		public Object read(final SerializationReader reader, Object target)
				throws IOException
		{
			if (reader.readNull())
			{
				return null;
			}

			final Object[] elements = (Object[]) Array.newInstance(clazz, reader.readListBegin());
			for (int i = 0; i < elements.length; ++i)
			{
				elements[i] = template.read(reader, null);
			}
			reader.readListEnd();

			return elements;
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

			final Object[] elements = (Object[]) value;
			writer.writeListBegin(elements.length);
			for (final Object element : elements)
			{
				template.write(writer, element);
			}
			writer.writeListEnd();
		}
	}
}
