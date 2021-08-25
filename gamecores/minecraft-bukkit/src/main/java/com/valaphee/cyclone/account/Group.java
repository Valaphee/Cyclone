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

import com.valaphee.cyclone.account.data.GroupData;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Default
 *
 * @author valaphee
 */
public class Group
		extends PermissionUnit
{
	protected GroupData data;

	public Group(final AccountManager manager, final GroupData data)
	{
		super(manager);

		this.data = data;
	}

	public GroupData getData()
	{
		return data;
	}

	void setData(final GroupData data)
	{
		this.data = data;
	}

	public long getId()
	{
		return data.id;
	}

	public String getName()
	{
		return data.name;
	}

	public void setName(final String name)
	{
		data.name = name;
		flagAsModified();
	}

	@Override
	public Map<String, String> getVariables()
	{
		return Collections.unmodifiableMap(data.variables);
	}

	@Override
	public String getVariable(final String name)
	{
		return data.variables.get(name);
	}

	@Override
	public void addVariable(final String name, final String content)
	{
		if (data.variables.containsKey(name))
		{
			data.variables.remove(name);
		}
		data.variables.put(name, content);
		flagAsModified();
	}

	@Override
	public boolean removeVariable(final String name)
	{
		if (data.variables.containsKey(name))
		{
			data.variables.remove(name);
			flagAsModified();

			return true;
		}

		return false;
	}

	@Override
	public Collection<String> getPermissions()
	{
		return Collections.unmodifiableCollection(data.permissions);
	}

	@Override
	public void addPermission(final String permission)
	{
		if (!data.permissions.contains(permission))
		{
			data.permissions.add(permission);
			flagAsModified();
		}
	}

	@Override
	public boolean removePermission(final String permission)
	{
		if (data.permissions.contains(permission))
		{
			data.permissions.remove(permission);
			flagAsModified();

			return true;
		}

		return false;
	}

	public Collection<Long> getInheritances()
	{
		return Collections.unmodifiableCollection(data.inheritances);
	}

	public void addInheritance(final Group inheritance)
	{
		if (!manager.groupExists(inheritance.getId()))
		{
			manager.addGroup(inheritance);
		}

		if (!data.inheritances.contains(inheritance.getId()))
		{
			data.inheritances.add(inheritance.getId());
			flagAsModified();
		}
	}

	public boolean removeInheritance(final Long inheritance)
	{
		if (data.inheritances.contains(inheritance))
		{
			data.inheritances.remove(inheritance);
			flagAsModified();

			return true;
		}

		return false;
	}
}
