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

package com.valaphee.cyclone.serialization;

import com.valaphee.cyclone.serialization.stream.SerializationStreamReader;
import com.valaphee.cyclone.serialization.stream.SerializationStreamWriter;
import com.valaphee.cyclone.serialization.template.GenericTemplate;
import com.valaphee.cyclone.serialization.template.Template;
import com.valaphee.cyclone.serialization.template.TemplateRegistry;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Default
 *
 * @author valaphee
 */
public final class Serializer
{
	private final TemplateRegistry registry = new TemplateRegistry();

	public Serializer()
	{}

	public void appendClassLoader(final ClassLoader classLoader)
	{
		registry.appendClassLoader(classLoader);
	}

	public void register(final Type type, final Template<?> template)
	{
		registry.register(type, template);
	}

	public void registerGeneric(final Type type, final GenericTemplate template)
	{
		registry.registerGeneric(type, template);
	}

	public void register(final Class<?> clazz)
	{
		registry.register(clazz);
	}

	public boolean unregister(final Type type)
	{
		return registry.unregister(type);
	}

	public byte[] serialize(final Type type, final Object object)
			throws SerializationException
	{
		byte[] serialized = null;
		try (final ByteArrayOutputStream stream = new ByteArrayOutputStream(); final SerializationStreamWriter writer = new SerializationStreamWriter(stream))
		{
			final Template<Object> template = registry.lookup(type);
			template.write(writer, object);
			serialized = stream.toByteArray();
		}
		catch (final IOException ex)
		{
			throw new SerializationException("Serialization of data failed.", ex);
		}
		catch (final SerializationException ex)
		{
			throw ex;
		}

		return serialized;
	}

	public Object deserialize(final Type type, final byte[] data, final Object target)
			throws SerializationException
	{
		Object object = null;
		try (final SerializationStreamReader reader = new SerializationStreamReader(new ByteArrayInputStream(data)))
		{
			final Template<Object> template = registry.lookup(type);
			object = template.read(reader, target);
		}
		catch (final IOException ex)
		{
			throw new SerializationException("Serialization of data failed.", ex);
		}
		catch (final SerializationException ex)
		{
			throw ex;
		}

		return object;
	}

	public Object deserialize(final Type type, final byte[] data)
			throws SerializationException
	{
		return deserialize(type, data, null);
	}
}
