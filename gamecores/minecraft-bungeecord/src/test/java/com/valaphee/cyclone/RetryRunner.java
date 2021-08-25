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

package com.valaphee.cyclone;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * Default
 *
 * @author valaphee
 */
public class RetryRunner
		extends BlockJUnit4ClassRunner
{
	public RetryRunner(final Class<?> test)
			throws InitializationError
	{
		super(test);
	}

	@Override
	public Statement methodInvoker(final FrameworkMethod method, final Object test)
	{
		final Statement statement = super.methodInvoker(method, test);

		return new Statement()
		{
			@Override
			public void evaluate()
					throws Throwable
			{
				final Retry retry = method.getAnnotation(Retry.class);
				if (retry == null)
				{
					statement.evaluate();
				}
				else
				{
					Throwable failure = null;
					for (int i = retry.value(); i != 0; --i)
					{
						try
						{
							statement.evaluate();

							break;
						}
						catch (final Throwable thrown)
						{
							failure = thrown;
						}
					}

					if (failure != null)
					{
						throw failure;
					}
				}
			}
		};
	}
}
