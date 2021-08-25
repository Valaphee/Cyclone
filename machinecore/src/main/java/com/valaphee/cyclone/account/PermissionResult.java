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

/**
 * Default
 *
 * @author valaphee
 */
public final class PermissionResult
{
	private final String permission;
	private String accessLevel;
	private Type type = Type.NOT_FOUND;

	public PermissionResult(final String permission)
	{
		accessLevel = this.permission = permission;
	}

	public String getPermission()
	{
		return permission;
	}

	public String getAccessLevel()
	{
		return accessLevel;
	}

	public void setAccessLevel(final String accessLevel)
	{
		this.accessLevel = accessLevel;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(final Type type)
	{
		this.type = type;
	}

	public static enum Type
	{
		EXCEPTION, NEGATION, FOUND, NOT_FOUND
	}
}
