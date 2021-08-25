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

package com.valaphee.cyclone.util;

/**
 * Default
 *
 * @author valaphee
 */
public final class StringUtil
{
	public static String padLeft(final String value, final int count, final char pad)
	{
		final StringBuilder padLeft = new StringBuilder();
		for (int i = count - 1; i >= 0; --i)
		{
			padLeft.append(pad);
		}

		return padLeft.append(value).toString();
	}

	public static String padRight(final String value, final int count, final char pad)
	{
		final StringBuilder padRight = new StringBuilder().append(value);
		for (int i = count - 1; i >= 0; --i)
		{
			padRight.append(pad);
		}

		return padRight.toString();
	}

	public static String center(final String value, final int width, final char pad)
	{
		return center(value, width, pad, pad);
	}

	public static String center(final String value, final int width, final char leftPad, final char rightPad)
	{
		final int leftPadding = (width - value.length()) / 2;
		final int rightPadding = width - value.length() - leftPadding;

		return padLeft((rightPad != ' ' ? padRight(value, rightPadding, rightPad) : ""), leftPadding, leftPad);
	}

	public static String repeat(final int count, final String value)
	{
		final StringBuilder repeat = new StringBuilder();
		for (int i = count; i > 0; --i)
		{
			repeat.append(value);
		}

		return repeat.toString();
	}

	private StringUtil()
	{}
}
