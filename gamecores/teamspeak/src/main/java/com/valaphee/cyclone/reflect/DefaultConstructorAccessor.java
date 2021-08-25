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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Default
 *
 * @author valaphee
 */
public class DefaultConstructorAccessor
		implements ConstructorAccessor
{
	protected final Constructor<?> constructor;

	public DefaultConstructorAccessor(final Constructor<?> constructor)
	{
		this.constructor = constructor;
		if (!this.constructor.isAccessible())
		{
			this.constructor.setAccessible(true);
		}
	}

	@Override
	public Constructor<?> getConstructor()
	{
		return constructor;
	}

	@Override
	public Object invoke(final Object... arguments)
	{
		try
		{
			return constructor.newInstance(arguments);
		}
		catch (final InstantiationException ex)
		{
			throw new IllegalStateException("Could instantiate the object.", ex);
		}
		catch (final IllegalAccessException ex)
		{
			throw new IllegalStateException("Could not access the constructor.", ex);
		}
		catch (final IllegalArgumentException ex)
		{
			throw ex;
		}
		catch (final InvocationTargetException ex)
		{
			throw new IllegalStateException("Could not call the constructor.", ex);
		}
	}
}
