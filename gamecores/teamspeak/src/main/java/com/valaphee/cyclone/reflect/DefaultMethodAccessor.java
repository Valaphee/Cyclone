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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Default
 *
 * @author valaphee
 */
public class DefaultMethodAccessor
		implements MethodAccessor
{
	protected final Method method;

	public DefaultMethodAccessor(final Method method)
	{
		this.method = method;
		if (!this.method.isAccessible())
		{
			this.method.setAccessible(true);
		}
	}

	@Override
	public Method getMethod()
	{
		return method;
	}

	@Override
	public Object invoke(final Object instance, final Object... arguments)
	{
		try
		{
			return method.invoke(instance, arguments);
		}
		catch (final IllegalAccessException ex)
		{
			throw new IllegalStateException("Could not access the method.", ex);
		}
		catch (final IllegalArgumentException ex)
		{
			throw ex;
		}
		catch (final InvocationTargetException ex)
		{
			throw new IllegalStateException("Could not call the method.", ex);
		}
	}
}
