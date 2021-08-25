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

package com.valaphee.cyclone.account;

import java.util.Comparator;

/**
 * Default
 *
 * @author valaphee
 */
public final class PermissionComparator
		implements Comparator<String>
{
	private static final PermissionComparator INSTANCE = new PermissionComparator();

	public static PermissionComparator getInstance()
	{
		return INSTANCE;
	}

	@Override
	public int compare(final String left, final String right)
	{
		final boolean leftPositive = left.startsWith("+"), rightPositive = right.startsWith("+");
		final boolean leftNegative = left.startsWith("-"), rightNegative = right.startsWith("-");
		if ((!leftPositive) && rightPositive)
		{
			return 1;
		}
		if ((!leftNegative) && rightNegative)
		{
			return 1;
		}
		if (leftPositive && rightPositive)
		{
			return 0;
		}
		if (leftNegative && rightNegative)
		{
			return 0;
		}
		if (leftPositive && (!rightPositive))
		{
			return -1;
		}
		if (leftNegative && (!rightNegative))
		{
			return -1;
		}

		return left.compareToIgnoreCase(right);
	}

	private PermissionComparator()
	{}
}
