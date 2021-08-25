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

package com.valaphee.cyclone.engine.mode.loadprocesses;

import com.valaphee.cyclone.account.AccountManager;
import com.valaphee.cyclone.account.Group;
import com.valaphee.cyclone.account.PermissionHandler;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.engine.mode.LoadProcess;
import java.util.Collection;
import java.util.Iterator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Default
 *
 * @author valaphee
 */
public final class InitializeAndSetupAccountManager
		implements LoadProcess
{
	private static final Logger LOGGER = LogManager.getLogger(InitializeAndSetupAccountManager.class);
	private final AccountManager manager;
	private final PermissionHandler permissionHandler;
	private final int groups;
	private final Iterator<Long> groupIdIterator;
	private int groupsLoaded;

	public InitializeAndSetupAccountManager(final Context context)
	{
		context.set(AccountManager.class, manager = new AccountManager(context));
		context.set(PermissionHandler.class, permissionHandler = new PermissionHandler(manager));

		final Collection<Long> groupIds = manager.getGroupRepository().getAllGroups();
		this.groups = groupIds.size();
		groupIdIterator = groupIds.iterator();
	}

	@Override
	public boolean step()
	{
		final Group group = manager.getGroup(groupIdIterator.next());
		if (permissionHandler.getVariableAsBoolean(group, "default", false))
		{
			manager.setDefaultGroup(group);
			LOGGER.log(Level.INFO, "Default group setted to {}", group.getName());
		}
		groupsLoaded++;
		LOGGER.log(Level.INFO, "Group {} loaded successfully.", group.getName());

		return !groupIdIterator.hasNext();
	}

	@Override
	public float getProgress()
	{
		return groupsLoaded / (float) groups;
	}

	@Override
	public int getExpectedCost()
	{
		return groups;
	}
}
