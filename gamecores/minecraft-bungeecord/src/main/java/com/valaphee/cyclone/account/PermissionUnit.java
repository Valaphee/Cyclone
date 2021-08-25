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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Default
 *
 * @author valaphee
 */
public abstract class PermissionUnit
{
	protected final AccountManager manager;
	private boolean modified;
	private boolean sorted;

	public PermissionUnit(final AccountManager manager)
	{
		this.manager = manager;
	}

	public abstract Map<String, String> getVariables();

	public abstract String getVariable(final String name);

	public final Boolean getVariableAsBoolean(final String name)
	{
		final String content = getVariable(name);

		return content != null ? content.equalsIgnoreCase("true") || content.equalsIgnoreCase("yes") : null;
	}

	public final Long getVariableAsLong(final String name)
	{
		try
		{
			final String content = getVariable(name);

			return content != null ? Long.parseLong(content) : null;
		}
		catch (final NumberFormatException ex)
		{
			return 0L;
		}
	}

	public final Double getVariableAsDouble(final String name)
	{
		try
		{
			final String content = getVariable(name);

			return content != null ? Double.parseDouble(content) : null;
		}
		catch (final NumberFormatException ex)
		{
			return Double.NaN;
		}
	}

	public abstract void addVariable(final String name, final String content);

	public abstract boolean removeVariable(final String name);

	public abstract Collection<String> getPermissions();

	public abstract void addPermission(final String permission);

	public abstract boolean removePermission(final String permission);

	public boolean isModified()
	{
		return modified;
	}

	public boolean isSorted()
	{
		return sorted;
	}

	public void flagAsModified()
	{
		modified = true;
		sorted = false;
	}

	public void flagAsSaved()
	{
		modified = false;
	}

	public void sortPermissions()
	{
		if (!sorted)
		{
			Collections.sort((List<String>) getPermissions(), PermissionComparator.getInstance());

			sorted = true;
		}
	}
}
