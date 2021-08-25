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

import com.valaphee.cyclone.text.TextAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Default
 *
 * @author valaphee
 */
public final class PermissionHandler
{
	private final AccountManager manager;

	public PermissionHandler(final AccountManager manager)
	{
		this.manager = manager;
	}

	public boolean has(final User user, final String permission)
	{
		final PermissionResult result = checkUserWithInheritances(user, permission);

		return result.getType().equals(PermissionResult.Type.FOUND);
	}

	private PermissionResult check(final PermissionUnit unit, final String permission)
	{
		final PermissionResult result = new PermissionResult(permission);
		for (final String accessLevel : unit.getPermissions())
		{
			PermissionResult.Type type = PermissionResult.Type.FOUND;
			int accessLevelOffset = 0;
			if (accessLevel.charAt(0) == '+')
			{
				type = PermissionResult.Type.EXCEPTION;
				accessLevelOffset++;
			}
			else if (accessLevel.charAt(0) == '-')
			{
				type = PermissionResult.Type.NEGATION;
				accessLevelOffset++;
			}
			if (accessLevel.equals(permission) || "*".regionMatches(0, accessLevel, accessLevelOffset, accessLevel.length() - accessLevelOffset))
			{
				result.setType(type);
				result.setAccessLevel(accessLevel);

				break;
			}

			int permissionOffset = 0;
			if ((permission.charAt(0) == '+') || (permission.charAt(0) == '-'))
			{
				permissionOffset++;
			}
			if (accessLevel.charAt(accessLevel.length() - 1) == '*')
			{
				if (!accessLevel.regionMatches(true, accessLevelOffset, permission, permissionOffset, accessLevel.length() - accessLevelOffset - 1) || accessLevel.regionMatches(true, accessLevelOffset, permission, permissionOffset, Math.max(accessLevel.length() - accessLevelOffset, permission.length() - permissionOffset)))
				{
					result.setType(PermissionResult.Type.NOT_FOUND);
				}
				else
				{
					result.setAccessLevel(accessLevel);
				}
			}
			else
			{
				if (!accessLevel.regionMatches(true, accessLevelOffset, permission, permissionOffset, Math.max(accessLevel.length() - accessLevelOffset, permission.length() - permissionOffset)))
				{
					result.setType(PermissionResult.Type.NOT_FOUND);
				}
				else
				{
					result.setAccessLevel(accessLevel);
				}
			}
		}

		return result;
	}

	public PermissionResult checkUserWithInheritances(final User user, final String permission)
	{
		PermissionResult result = new PermissionResult(permission);

		final PermissionResult userResult = check(user, permission);
		if (!userResult.getType().equals(PermissionResult.Type.NOT_FOUND))
		{
			userResult.setAccessLevel(permission);
			if (userResult.getType().equals(PermissionResult.Type.EXCEPTION))
			{
				return userResult;
			}
			result = userResult;
		}

		final PermissionResult groupResult = checkGroupWithInheritances(user.getGroup(), permission);
		if (!groupResult.getType().equals(PermissionResult.Type.NOT_FOUND))
		{
			groupResult.setAccessLevel(permission);
			if (groupResult.getType().equals(PermissionResult.Type.EXCEPTION))
			{
				return groupResult;
			}
			if (result.getType().equals(PermissionResult.Type.NOT_FOUND))
			{
				result = groupResult;
			}
		}

		return result;
	}

	public PermissionResult checkGroupWithInheritances(final Group group, final String permission)
	{
		PermissionResult result = new PermissionResult(permission);

		final Stack<Group> stacked = new Stack<>();
		final List<Group> visited = new ArrayList<>();
		stacked.push(group);
		visited.add(group);
		while (!stacked.isEmpty())
		{
			final Group current = stacked.pop();

			final PermissionResult currentResult = check(current, permission);
			if (!currentResult.getType().equals(PermissionResult.Type.NOT_FOUND))
			{
				currentResult.setAccessLevel(permission);
				if (currentResult.getType().equals(PermissionResult.Type.EXCEPTION))
				{
					return currentResult;
				}
				result = currentResult;
			}

			current.getInheritances().stream().map((id) -> manager.getGroup(id)).filter((inheritance) -> ((inheritance != null) && (!visited.contains(inheritance)))).forEachOrdered((inheritance) ->
			{
				stacked.add(inheritance);
				visited.add(inheritance);
			});
		}

		return result;
	}

	public TextAttribute getColor(final User user)
	{
		final String color = user.getVariable("color");
		if ((color != null) && (!color.isEmpty()))
		{
			try
			{
				return TextAttribute.valueOf(color);
			}
			catch (final Throwable ignore)
			{}
		}

		return getColor(user.getGroup());
	}

	public TextAttribute getColor(final Group group)
	{
		try
		{
			return TextAttribute.valueOf(group.getVariable("color"));
		}
		catch (final Throwable ex)
		{
			return TextAttribute.WHITE;
		}
	}

	public String getPrefix(final User user)
	{
		final String prefix = user.getVariable("prefix");
		if ((prefix != null) && (!prefix.isEmpty()))
		{
			return prefix;
		}

		return getPrefix(user.getGroup());
	}

	public String getPrefix(final Group group)
	{
		return group.getVariable("prefix");
	}

	public String getSuffix(final User user)
	{
		final String suffix = user.getVariable("suffix");
		if ((suffix != null) && (!suffix.isEmpty()))
		{
			return suffix;
		}

		return getSuffix(user.getGroup());
	}

	public String getSuffix(final Group group)
	{
		return group.getVariable("suffix");
	}

	public String getVariable(final User user, final String name)
	{
		if ((user == null) || (name == null))
		{
			return null;
		}

		final String content = user.getVariable(name);
		if (content != null)
		{
			return content;
		}

		return getVariable(user.getGroup(), name, true);
	}

	public String getVariable(final Group group, final String name, boolean includeInheritances)
	{
		String content;

		final Stack<Group> stacked = new Stack<>();
		final List<Group> visited = new ArrayList<>();
		stacked.push(group);
		visited.add(group);
		while (!stacked.isEmpty())
		{
			final Group current = stacked.pop();

			content = current.getVariable(name);
			if (content != null)
			{
				return content;
			}

			if (includeInheritances)
			{
				current.getInheritances().stream().map((id) -> manager.getGroup(id)).filter((inheritance) -> ((inheritance != null) && (!visited.contains(inheritance)))).forEachOrdered((inheritance) ->
				{
					stacked.add(inheritance);
					visited.add(inheritance);
				});
			}
		}

		return null;
	}

	public boolean getVariableAsBoolean(final User user, final String name)
	{
		if ((user == null) || (name == null))
		{
			return false;
		}

		final Boolean content = user.getVariableAsBoolean(name);
		if (content != null)
		{
			return content;
		}

		return getVariableAsBoolean(user.getGroup(), name, true);
	}

	public boolean getVariableAsBoolean(final Group group, final String name, final boolean includeInheritances)
	{
		Boolean content;

		final Stack<Group> stacked = new Stack<>();
		final List<Group> visited = new ArrayList<>();
		stacked.push(group);
		visited.add(group);
		while (!stacked.isEmpty())
		{
			final Group current = stacked.pop();

			content = current.getVariableAsBoolean(name);
			if (content != null)
			{
				return content;
			}

			if (includeInheritances)
			{
				current.getInheritances().stream().map((id) -> manager.getGroup(id)).filter((inheritance) -> ((inheritance != null) && (!visited.contains(inheritance)))).forEachOrdered((inheritance) ->
				{
					stacked.add(inheritance);
					visited.add(inheritance);
				});
			}
		}

		return false;
	}

	public long getVariableAsLong(final User user, final String name)
	{
		if ((user == null) || (name == null))
		{
			return -1L;
		}

		final Long content = user.getVariableAsLong(name);
		if (content != null)
		{
			return content;
		}

		return getVariableAsLong(user.getGroup(), name, true);
	}

	public long getVariableAsLong(final Group group, final String name, final boolean includeInheritances)
	{
		Long content;

		final Stack<Group> stacked = new Stack<>();
		final List<Group> visited = new ArrayList<>();
		stacked.push(group);
		visited.add(group);
		while (!stacked.isEmpty())
		{
			final Group current = stacked.pop();

			content = current.getVariableAsLong(name);
			if (content != null)
			{
				return content;
			}

			if (includeInheritances)
			{
				current.getInheritances().stream().map((id) -> manager.getGroup(id)).filter((inheritance) -> ((inheritance != null) && (!visited.contains(inheritance)))).forEachOrdered((inheritance) ->
				{
					stacked.add(inheritance);
					visited.add(inheritance);
				});
			}
		}

		return -1L;
	}

	public double getVariableAsDouble(final User user, final String name)
	{
		if ((user == null) || (name == null))
		{
			return Double.NaN;
		}

		final Double content = user.getVariableAsDouble(name);
		if (content != null)
		{
			return content;
		}

		return getVariableAsDouble(user.getGroup(), name, true);
	}

	public double getVariableAsDouble(final Group group, final String name, final boolean includeInheritances)
	{
		Double content;

		final Stack<Group> stacked = new Stack<>();
		final List<Group> visited = new ArrayList<>();
		stacked.push(group);
		visited.add(group);
		while (!stacked.isEmpty())
		{
			final Group current = stacked.pop();

			content = current.getVariableAsDouble(name);
			if (content != null)
			{
				return content;
			}

			if (includeInheritances)
			{
				current.getInheritances().stream().map((id) -> manager.getGroup(id)).filter((inheritance) -> ((inheritance != null) && (!visited.contains(inheritance)))).forEachOrdered((inheritance) ->
				{
					stacked.add(inheritance);
					visited.add(inheritance);
				});
			}
		}

		return Double.NaN;
	}
}
