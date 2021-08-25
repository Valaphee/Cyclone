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

package com.valaphee.cyclone.serialization.stream;

import java.io.EOFException;

/**
 * Default
 *
 * @author valaphee
 */
public final class BalanceStack
{
	private static final byte LIST = 0x01, MAP = 0x02;
	private final byte[] typeStack = new byte[256];
	private final int[] countStack = new int[256];
	private int pointer = -1;

	public void pop()
	{
		--pointer;
	}

	public void pushList(final int size)
	{
		++pointer;
		typeStack[pointer] = LIST;
		countStack[pointer] = size;
	}

	public void pushMap(final int size)
	{
		++pointer;
		typeStack[pointer] = MAP;
		countStack[pointer] = size * 2;
	}

	public boolean isPointingToList()
	{
		return typeStack[pointer] == LIST;
	}

	public boolean isPointingToMap()
	{
		return typeStack[pointer] == MAP;
	}

	public int getRemaining()
	{
		return countStack[pointer];
	}

	public void decrease()
	{
		if (pointer >= 0)
		{
			countStack[pointer]--;
		}
	}

	public void validate()
			throws EOFException
	{
		if ((pointer >= 0) && (countStack[pointer] <= 0))
		{
			throw new EOFException("No elements left.");
		}
	}
}
