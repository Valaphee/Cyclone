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
public final class ThrowablePrinter
{
	public static String print(final Throwable throwable)
	{
		return print(throwable, 3);
	}

	public static String print(final Throwable throwable, final int hierachyDepth)
	{
		if (throwable == null)
		{
			throw new IllegalArgumentException("Throwable cannot be null.");
		}

		final StringBuilder result = new StringBuilder();
		print(result, throwable, hierachyDepth);

		return result.toString();
	}

	private static void print(final StringBuilder result, final Throwable cause, final int hierachyIndex)
	{
		result.append(cause.getClass().getName()).append(": ").append(cause.getLocalizedMessage()).append('\n');
		for (final StackTraceElement stackTraceElement : cause.getStackTrace())
		{
			result.append("\t at ").append(stackTraceElement.getClassName()).append('.').append(stackTraceElement.getMethodName()).append('(').append(stackTraceElement.getFileName()).append(':').append(stackTraceElement.getLineNumber()).append(')').append('\n');
		}

		if (hierachyIndex <= 0)
		{
			result.append("And some more...");
		}
		else if (cause.getCause() != null)
		{
			result.append("Caused by ");

			print(result, cause.getCause(), hierachyIndex - 1);
		}
	}

	private ThrowablePrinter()
	{}
}
