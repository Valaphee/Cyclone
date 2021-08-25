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

/**
 * Default
 *
 * @author valaphee
 */
public final class Accessors
{
	public static ConstructorAccessor getConstructorAccessor(final Class<?> clazz, final Class<?>... parameters)
	{
		try
		{
			return new DefaultConstructorAccessor(clazz.getDeclaredConstructor(parameters));
		}
		catch (final NoSuchMethodException ex)
		{
			throw new IllegalArgumentException("Could not find the constructor for " + clazz.getSimpleName() + ".");
		}
		catch (final SecurityException ex)
		{
			throw new IllegalStateException("Could not access the constructor.", ex);
		}
	}

	public static ConstructorAccessor getConstructorAccessorOrNull(final Class<?> clazz, final Class<?>... parameters)
	{
		try
		{
			return getConstructorAccessor(clazz, parameters);
		}
		catch (final IllegalArgumentException ignore)
		{}

		return null;
	}

	public static FieldAccessor getFieldAccessor(final Class<?> clazz, final String field)
	{
		try
		{
			return new DefaultFieldAccessor(clazz.getDeclaredField(field));
		}
		catch (final NoSuchFieldException ex)
		{
			throw new IllegalArgumentException("Could not find the field " + field + " for " + clazz.getSimpleName() + ".");
		}
		catch (final SecurityException ex)
		{
			throw new IllegalStateException("Could not access the field.", ex);
		}
	}

	public static FieldAccessor getFieldAccessorOrNull(final Class<?> clazz, final String field)
	{
		try
		{
			return getFieldAccessor(clazz, field);
		}
		catch (final IllegalArgumentException ignore)
		{}

		return null;
	}

	public static MethodAccessor getMethodAccessor(final Class<?> clazz, final String method, final Class<?>... parameters)
	{
		try
		{
			return new DefaultMethodAccessor(clazz.getDeclaredMethod(method, parameters));
		}
		catch (final NoSuchMethodException ex)
		{
			throw new IllegalArgumentException("Could not find the method " + method + " for " + clazz.getSimpleName() + ".");
		}
		catch (final SecurityException ex)
		{
			throw new IllegalStateException("Could not access the method.", ex);
		}
	}

	public static MethodAccessor getMethodAccessorOrNull(final Class<?> clazz, final String method, final Class<?>... parameters)
	{
		try
		{
			return getMethodAccessor(clazz, method, parameters);
		}
		catch (final IllegalArgumentException ignore)
		{}

		return null;
	}

	private Accessors()
	{}
}
