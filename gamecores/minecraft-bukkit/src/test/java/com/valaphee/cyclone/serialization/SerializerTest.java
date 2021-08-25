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

import com.valaphee.cyclone.serialization.stream.SerializationReader;
import com.valaphee.cyclone.serialization.stream.SerializationWriter;
import com.valaphee.cyclone.serialization.template.Template;
import java.io.IOException;
import java.util.*;
import junit.framework.TestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Default
 *
 * @author valaphee
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class SerializerTest
		extends TestCase
{
	private Serializer serializer;

	@Override
	public void setUp()
	{
		serializer = new Serializer();
	}

	@Override
	public void tearDown()
	{}

	@Test
	public void testStage1AppendClassLoader()
	{
		serializer.appendClassLoader(getClass().getClassLoader());
	}

	@Test
	public void testStage2RegisterTemplate()
	{
		serializer.register(Simple.class, SimpleTemplate.getInstance());
	}

	@Test
	public void testStage3RegisterClass()
	{
		//serializer.register(Complex.class);
	}

	@Test
	public void testStage4SerializeAndDeserialize()
	{
		final Simple simple = new Simple();
		simple.integerField = 1928;
		simple.booleanField = false;

		Object result = serializer.deserialize(Simple.class, serializer.serialize(Simple.class, simple));
		assertEquals(simple, result);

		final Complex complex = new Complex();
		complex.integerArrayField = new int[]
		{
			1, 33, 7, 1337, 133337
		};
		complex.mapField = new HashMap<>();
		complex.mapField.put(UUID.randomUUID(), "Valaphee");
		complex.mapField.put(UUID.randomUUID(), "Basti");

		result = serializer.deserialize(Complex.class, serializer.serialize(Complex.class, complex));
		assertEquals(complex, result);
	}

	@Test
	public void testStage5Unregister()
	{
		boolean result = serializer.unregister(Simple.class);
		assertEquals(false, result);

		result = serializer.unregister(Complex.class);
		assertEquals(false, result);
	}

	public static final class Simple
	{
		public int integerField;
		public boolean booleanField;

		@Override
		public boolean equals(final Object object)
		{
			if (object == this)
			{
				return true;
			}
			if (object instanceof Simple)
			{
				final Simple other = (Simple) object;

				return (integerField == other.integerField) && (booleanField == other.booleanField);
			}

			return false;
		}

		@Override
		public int hashCode()
		{
			int hash = 5;
			hash = 29 * hash + integerField;
			hash = 29 * hash + (booleanField ? 1 : 0);

			return hash;
		}
	}

	public static final class SimpleTemplate
			implements Template<Simple>
	{
		private static final SimpleTemplate INSTANCE = new SimpleTemplate();

		public static SimpleTemplate getInstance()
		{
			return INSTANCE;
		}

		@Override
		public Simple read(final SerializationReader reader, Simple target)
				throws IOException
		{
			if (reader.readNull())
			{
				return null;
			}

			final Simple simple;
			if (target == null)
			{
				simple = new Simple();
			}
			else
			{
				simple = target;
			}

			reader.readListBegin();
			simple.integerField = reader.readInt();
			simple.booleanField = reader.readBoolean();
			reader.readListEnd();

			return simple;
		}

		@Override
		public void write(final SerializationWriter writer, final Simple value)
				throws IOException
		{
			if (value == null)
			{
				writer.writeNull();

				return;
			}

			writer.writeListBegin(2);
			writer.writeInt(value.integerField);
			writer.writeBoolean(value.booleanField);
			writer.writeListEnd();
		}

		private SimpleTemplate()
		{}
	}

	public static final class Complex
	{
		public int[] integerArrayField;
		public Map<UUID, String> mapField;

		@Override
		public boolean equals(final Object object)
		{
			if (object == this)
			{
				return true;
			}
			if (object instanceof Complex)
			{
				final Complex other = (Complex) object;

				return Arrays.equals(integerArrayField, other.integerArrayField) && Objects.equals(mapField, other.mapField);
			}

			return false;
		}

		@Override
		public int hashCode()
		{
			int hash = 7;
			hash = 73 * hash + Arrays.hashCode(integerArrayField);
			hash = 73 * hash + Objects.hashCode(mapField);

			return hash;
		}
	}
}
