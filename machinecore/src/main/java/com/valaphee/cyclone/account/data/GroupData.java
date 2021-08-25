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

package com.valaphee.cyclone.account.data;

import com.valaphee.cyclone.serialization.annotation.Index;
import java.util.List;
import java.util.Map;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("PublicField")
public final class GroupData
{
	@Index(0)
	public long id;
	@Index(1)
	public String name;
	@Index(2)
	public Map<String, String> variables;
	@Index(3)
	public List<Long> inheritances;
	@Index(4)
	public List<String> permissions;

	@Override
	public boolean equals(final Object object)
	{
		if (object == this)
		{
			return true;
		}
		if (object instanceof GroupData)
		{
			final GroupData other = (GroupData) object;

			return (id == other.id);
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		return (int) (id ^ (id >>> 32));
	}
}
