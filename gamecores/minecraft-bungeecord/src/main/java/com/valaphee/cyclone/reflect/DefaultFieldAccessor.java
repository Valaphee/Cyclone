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

package com.valaphee.cyclone.reflect;

import java.lang.reflect.Field;

/**
 * Default
 *
 * @author valaphee
 */
public class DefaultFieldAccessor
		implements FieldAccessor
{
	protected final Field field;

	public DefaultFieldAccessor(final Field field)
	{
		this.field = field;
		if (!this.field.isAccessible())
		{
			this.field.setAccessible(true);
		}
	}

	@Override
	public Field getField()
	{
		return field;
	}

	@Override
	public Object get(final Object instance)
	{
		try
		{
			return field.get(instance);
		}
		catch (final IllegalArgumentException ex)
		{
			throw ex;
		}
		catch (final IllegalAccessException ex)
		{
			throw new IllegalStateException("Could not access the field.", ex);
		}
	}

	@Override
	public void set(final Object instance, final Object value)
	{
		try
		{
			field.set(instance, value);
		}
		catch (final IllegalArgumentException ex)
		{
			throw ex;
		}
		catch (final IllegalAccessException ex)
		{
			throw new IllegalStateException("Could not access the field.", ex);
		}
	}
}
