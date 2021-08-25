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

import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.Permission;

/**
 * Default
 *
 * @author valaphee
 */
public final class CycloneSecurityManager
		extends SecurityManager
{
	@Override
	public void checkPermission(final Permission permission)
	{
		if (permission instanceof RuntimePermission)
		{
			if (permission.getName().equals("setIO"))
			{
				tryBypass(new AccessControlException("You cannot do that.", permission));
			}
			else if (permission.getName().equals("setSecurityManager"))
			{
				tryBypass(new AccessControlException("You cannot do that.", permission));
			}
		}
	}

	@Override
	public void checkPermission(final Permission permission, final Object context)
	{
		if (context instanceof AccessControlContext)
		{
			checkPermission(permission);
		}
		else
		{
			throw new SecurityException();
		}
	}

	@Override
	public void checkExit(final int status)
	{
		tryBypass(new SecurityException("You cannot exit the VM."));
	}

	private void tryBypass(final RuntimeException exception)
	{
		final Class<?>[] classContext = getClassContext();
		for (int i = 2; i < classContext.length; ++i)
		{
			final ClassLoader classLoader = classContext[i].getClassLoader();
			if (ClassLoader.getSystemClassLoader().equals(classLoader) || ((classLoader == null) && (!classContext[i].getName().startsWith("java"))))
			{
				break;
			}
			else if (classLoader != null)
			{
				throw exception;
			}
		}
	}
}
