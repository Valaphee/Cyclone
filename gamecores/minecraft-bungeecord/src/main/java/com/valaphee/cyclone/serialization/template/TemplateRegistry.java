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

import com.valaphee.cyclone.serialization.SerializationException;
import com.valaphee.cyclone.serialization.template.builder.TemplateBuilder;
import com.valaphee.cyclone.serialization.template.builder.TemplateBuilderChain;
import com.valaphee.cyclone.serialization.template.builder.TemplateEntry;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.*;
import java.sql.Timestamp;
import java.util.*;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings(
		{
			"rawtypes", "unchecked"
		})
public class TemplateRegistry
{
	private final TemplateBuilderChain chain = new TemplateBuilderChain(this);
	final Map<Type, Template<Object>> registry = new HashMap<>();
	final Map<Type, GenericTemplate> registryGeneric = new HashMap<>();

	public TemplateRegistry()
	{
		register(Void.class, VoidTemplate.getInstance());
		register(boolean.class, BooleanTemplate.getInstance());
		register(boolean[].class, BooleanArrayTemplate.getInstance());
		register(Boolean.class, BooleanTemplate.getInstance());
		register(byte.class, ByteTemplate.getInstance());
		register(byte[].class, ByteArrayTemplate.getInstance());
		register(Byte.class, ByteTemplate.getInstance());
		register(ByteBuffer.class, ByteBufferTemplate.getInstance());
		register(short.class, ShortTemplate.getInstance());
		register(short[].class, ShortArrayTemplate.getInstance());
		register(Short.class, ShortTemplate.getInstance());
		register(ShortBuffer.class, ShortBufferTemplate.getInstance());
		register(int.class, IntegerTemplate.getInstance());
		register(int[].class, IntegerArrayTemplate.getInstance());
		register(Integer.class, IntegerTemplate.getInstance());
		register(IntBuffer.class, IntegerBufferTemplate.getInstance());
		register(long.class, LongTemplate.getInstance());
		register(long[].class, LongArrayTemplate.getInstance());
		register(Long.class, LongTemplate.getInstance());
		register(LongBuffer.class, LongBufferTemplate.getInstance());
		register(float.class, FloatTemplate.getInstance());
		register(float[].class, FloatArrayTemplate.getInstance());
		register(Float.class, FloatTemplate.getInstance());
		register(FloatBuffer.class, FloatBufferTemplate.getInstance());
		register(double.class, DoubleTemplate.getInstance());
		register(double[].class, DoubleArrayTemplate.getInstance());
		register(Double.class, DoubleTemplate.getInstance());
		register(DoubleBuffer.class, DoubleBufferTemplate.getInstance());
		register(BigInteger.class, BigIntegerTemplate.getInstance());
		register(BigDecimal.class, BigDecimalTemplate.getInstance());
		register(char.class, CharacterTemplate.getInstance());
		register(char[].class, CharacterArrayTemplate.getInstance());
		register(Character.class, CharacterTemplate.getInstance());
		register(String.class, StringTemplate.getInstance());
		register(UUID.class, UUIDTemplate.getInstance());
		register(Timestamp.class, TimestampTemplate.getInstance());
		register(List.class, new ListTemplate<>(new GenericTemplate.AnyTemplate<>(this)));
		registerGeneric(List.class, new GenericCollectionTemplate(this, ListTemplate.class));
		register(Set.class, new ListTemplate<>(new GenericTemplate.AnyTemplate<>(this)));
		registerGeneric(Set.class, new GenericCollectionTemplate(this, SetTemplate.class));
		register(Map.class, new MapTemplate<>(new GenericTemplate.AnyTemplate<>(this), new GenericTemplate.AnyTemplate<>(this)));
		registerGeneric(Map.class, new GenericMapTemplate(this, MapTemplate.class));
	}

	public void appendClassLoader(final ClassLoader classLoader)
	{
		chain.appendClassLoader(classLoader);
	}

	public final synchronized void register(final Type type, final Template template)
	{
		if (type instanceof ParameterizedType)
		{
			registry.put(((ParameterizedType) type).getRawType(), template);
		}
		else
		{
			registry.put(type, template);
		}
	}

	public final synchronized void registerGeneric(final Type type, final GenericTemplate genericTemplate)
	{
		if (type instanceof ParameterizedType)
		{
			registryGeneric.put(((ParameterizedType) type).getRawType(), genericTemplate);
		}
		else
		{
			registryGeneric.put(type, genericTemplate);
		}
	}

	public void register(final Class<?> clazz)
	{
		buildAndRegister(null, clazz, null);
	}

	public void register(final Class<?> clazz, final TemplateEntry[] entries)
	{
		buildAndRegister(null, clazz, entries);
	}

	public synchronized boolean unregister(final Type type)
	{
		return registry.remove(type) != null;
	}

	public Template<Object> lookup(Type type)
	{
		Template<Object> template;

		if (type instanceof ParameterizedType)
		{
			final ParameterizedType parameterizedType = (ParameterizedType) type;
			template = lookupGenericClass(parameterizedType);
			if (template != null)
			{
				return template;
			}

			template = lookupGenericInterface(parameterizedType);
			if (template != null)
			{
				return template;
			}

			template = lookupGenericSuperclass(parameterizedType);
			if (template != null)
			{
				return template;
			}

			type = parameterizedType.getRawType();
		}
		else if (type instanceof GenericArrayType)
		{
			final GenericArrayType genericArrayType = (GenericArrayType) type;
			template = lookupGenericArray(genericArrayType);
			if (template != null)
			{
				return template;
			}
		}

		template = registry.get(type);
		if (template != null)
		{
			return template;
		}

		if ((type instanceof WildcardType) || (type instanceof TypeVariable))
		{
			return new GenericTemplate.AnyTemplate<>(this);
		}

		final Class<?> clazz = (Class<?>) type;

		if (clazz.isInterface())
		{
			return new GenericTemplate.AnyTemplate<>(this);
		}

		template = lookupClass(clazz);
		if (template != null)
		{
			return template;
		}

		template = lookupInterface(clazz);
		if (template != null)
		{
			return template;
		}

		template = lookupSuperclass(clazz);
		if (template != null)
		{
			return template;
		}

		template = lookupSuperclassInterface(clazz);
		if (template != null)
		{
			return template;
		}

		throw new TemplateException("Could not find or create a template for " + clazz.getCanonicalName() + ".");
	}

	private Template<Object> lookupGenericClass(final ParameterizedType parameterizedType)
	{
		final GenericTemplate genericTemplate = registryGeneric.get(parameterizedType.getRawType());
		if (genericTemplate == null)
		{
			return null;
		}

		final Type[] types = parameterizedType.getActualTypeArguments();
		final Template[] templates = new Template[types.length];
		for (int i = types.length - 1; i >= 0; --i)
		{
			templates[i] = lookup(types[i]);
		}

		return genericTemplate.build(templates);
	}

	private Template<Object> lookupGenericInterface(final ParameterizedType parameterizedType)
	{
		try
		{
			final Class<?>[] interfaces = ((Class<?>) parameterizedType.getRawType()).getInterfaces();
			for (final Class<?> interfaze : interfaces)
			{
				final GenericTemplate genericTemplate = registryGeneric.get(interfaze);
				if (genericTemplate == null)
				{
					continue;
				}

				final Type[] types = parameterizedType.getActualTypeArguments();
				final Template[] templates = new Template[types.length];
				for (int i = types.length - 1; i >= 0; --i)
				{
					templates[i] = lookup(types[i]);
				}

				return genericTemplate.build(templates);
			}
		}
		catch (final ClassCastException ignore)
		{}

		return null;
	}

	private Template<Object> lookupGenericSuperclass(final ParameterizedType parameterizedType)
	{
		try
		{
			Class<?> superclass = ((Class<?>) parameterizedType.getRawType()).getSuperclass();
			if (superclass == null)
			{
				return null;
			}

			for (; superclass != Object.class; superclass = superclass.getSuperclass())
			{
				final GenericTemplate genericTemplate = registryGeneric.get(superclass);
				if (genericTemplate == null)
				{
					continue;
				}

				final Type[] types = parameterizedType.getActualTypeArguments();
				final Template[] templates = new Template[types.length];
				for (int i = types.length - 1; i >= 0; --i)
				{
					templates[i] = lookup(types[i]);
				}

				return genericTemplate.build(templates);
			}
		}
		catch (final ClassCastException ignore)
		{}

		return null;
	}

	private Template<Object> lookupGenericArray(final GenericArrayType genericArrayType)
	{
		final String name = genericArrayType.toString();
		final int dimension = name.split("\\[").length - 1;
		if (dimension <= 0)
		{
			throw new TemplateException("Java Signature could not be recognized.");
		}
		if (dimension > 1)
		{
			throw new TemplateException("Only one dimensional arrays are allowed.");
		}

		final String component = genericArrayType.getGenericComponentType().toString();
		final StringBuilder signature = new StringBuilder();
		for (int i = dimension; i != 0; --i)
		{
			signature.append('[');
		}
		switch (component)
		{
		case "byte":
			signature.append('B');
			break;
		case "boolean":
			signature.append('Z');
			break;
		case "short":
			signature.append('S');
			break;
		case "char":
			signature.append('C');
			break;
		case "int":
			signature.append('I');
			break;
		case "long":
			signature.append('J');
			break;
		case "float":
			signature.append('F');
			break;
		case "double":
			signature.append('D');
			break;
		default:
			signature.append('L');
			signature.append(component.substring(6));
			signature.append(';');
			break;
		}

		try
		{
			final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			if (classLoader != null)
			{
				final Class<?> clazz = classLoader.loadClass(signature.toString());
				if (clazz != null)
				{
					return lookupClass(clazz);
				}
			}
		}
		catch (final ClassNotFoundException ignore)
		{}

		try
		{
			final ClassLoader classLoader = getClass().getClassLoader();
			if (classLoader != null)
			{
				final Class<?> clazz = classLoader.loadClass(signature.toString());
				if (clazz != null)
				{
					return lookupClass(clazz);
				}
			}
		}
		catch (final ClassNotFoundException ignore)
		{}

		try
		{
			final Class<?> clazz = Class.forName(signature.toString());
			if (clazz != null)
			{
				return lookupClass(clazz);
			}
		}
		catch (final ClassNotFoundException ignore)
		{}

		return null;
	}

	private Template<Object> lookupClass(final Class<?> clazz)
	{
		final TemplateBuilder builder = chain.getBuilder(clazz);
		Template<Object> template = null;
		if (builder != null)
		{
			template = buildAndRegister(builder, clazz, null);
		}

		return template;
	}

	private Template<Object> lookupInterface(final Class<?> clazz)
	{
		final Class<?>[] interfaces = clazz.getInterfaces();
		for (final Class<?> interfaze : interfaces)
		{
			final Template<Object> template = registry.get(interfaze);
			if (template != null)
			{
				register(clazz, template);

				return template;
			}
		}

		return null;
	}

	private Template<Object> lookupSuperclass(final Class<?> clazz)
	{
		Class<?> superclass = clazz.getSuperclass();
		if (superclass == null)
		{
			return null;
		}

		for (; superclass != Object.class; superclass = superclass.getSuperclass())
		{
			final Template<Object> template = registry.get(superclass);
			if (template != null)
			{
				register(clazz, template);
				return template;
			}
		}

		return null;
	}

	private Template<Object> lookupSuperclassInterface(final Class<?> clazz)
	{
		Class<?> superclass = clazz.getSuperclass();
		if (superclass == null)
		{
			return null;
		}

		for (; superclass != Object.class; superclass = superclass.getSuperclass())
		{
			final Template<Object> template = lookupInterface(superclass);
			if (template != null)
			{
				return template;
			}
		}

		return null;
	}

	private synchronized Template<Object> buildAndRegister(TemplateBuilder builder, final Class<?> clazz, final TemplateEntry[] entries)
	{
		Template<Object> oldTemplate = null;
		Template<Object> template = null;
		try
		{
			oldTemplate = registry.get(clazz);

			register(clazz, new TemplateReference(this, clazz));
			if (builder == null)
			{
				builder = chain.getBuilder(clazz);
			}
			template = entries != null ? builder.build(clazz, entries) : builder.build(clazz);

			return template;
		}
		catch (final Exception ex)
		{
			if (oldTemplate != null)
			{
				register(clazz, oldTemplate);
			}
			else
			{
				unregister(clazz);
			}

			template = null;

			if (ex instanceof SerializationException)
			{
				throw ex;
			}
			else
			{
				throw new SerializationException(ex);
			}
		}
		finally
		{
			if (template != null)
			{
				register(clazz, template);
			}
		}
	}
}
