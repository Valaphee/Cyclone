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

import com.valaphee.cyclone.reflect.Accessors;
import com.valaphee.cyclone.reflect.ConstructorAccessor;
import com.valaphee.cyclone.reflect.FieldAccessor;
import com.valaphee.cyclone.serialization.stream.SerializationReader;
import com.valaphee.cyclone.serialization.stream.SerializationWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Default
 *
 * @author valaphee
 */
public final class BigDecimalTemplate
		implements Template<BigDecimal>
{
	private static final BigDecimalTemplate INSTANCE = new BigDecimalTemplate();
	private static final ConstructorAccessor BIG_DECIMAL = Accessors.getConstructorAccessor(BigDecimal.class, BigInteger.class, long.class, int.class, int.class);
	private static final FieldAccessor BIG_DECIMAL_INT_VAL = Accessors.getFieldAccessor(BigDecimal.class, "intVal");

	public static BigDecimalTemplate getInstance()
	{
		return INSTANCE;
	}

	@Override
	public BigDecimal read(final SerializationReader reader, BigDecimal target)
			throws IOException
	{
		if (reader.readNull())
		{
			return null;
		}

		reader.readListBegin();
		target = (BigDecimal) BIG_DECIMAL.invoke(new BigInteger(reader.readBytes()), 0, reader.readInt(), reader.readInt());
		reader.readListEnd();

		return target;
	}

	@Override
	public void write(final SerializationWriter writer, final BigDecimal value)
			throws IOException
	{
		if (value == null)
		{
			writer.writeNull();

			return;
		}

		writer.writeListBegin(3);
		writer.writeBytes(((BigInteger) BIG_DECIMAL_INT_VAL.get(value)).toByteArray());
		writer.writeInt(value.scale());
		writer.writeInt(value.precision());
		writer.writeListEnd();
	}

	private BigDecimalTemplate()
	{}
}
