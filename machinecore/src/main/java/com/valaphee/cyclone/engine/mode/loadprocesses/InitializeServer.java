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

import com.valaphee.cyclone.connection.ConnectionManager;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.engine.mode.LoadProcess;
import com.valaphee.cyclone.server.ServerCategory;
import com.valaphee.cyclone.server.ServerManager;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.UUID;

/**
 * Default
 *
 * @author valaphee
 */
public final class InitializeServer
		implements LoadProcess
{
	private final ServerManager manager;
	private final UUID id;
	private final int categoryCount;
	private final Queue<ServerCategory> categories = new LinkedList<>();

	public InitializeServer(final Context context)
	{
		manager = context.get(ServerManager.class);
		id = context.get(ConnectionManager.class).getId();

		final Stack<ServerCategory> stacked = new Stack<>();
		stacked.push(manager);
		while (!stacked.isEmpty())
		{
			final ServerCategory current = stacked.pop();
			current.getSubcategories().stream().filter((subcategory) -> (!categories.contains(subcategory))).forEach((subcategory) ->
			{
				stacked.add(subcategory);
				categories.add(subcategory);
			});
		}
		categoryCount = categories.size();
	}

	@Override
	public boolean step()
	{
		final ServerCategory category = categories.poll();
		category.getRepository().getCachedServers().values().forEach((cachedServer) ->
		{
			if (cachedServer.node.equals(id))
			{
				category.getRepository().removeServer(id);
			}
			else
			{
				manager.registerInternal(cachedServer.node, id, category);
			}
		});

		return !categories.isEmpty();
	}

	@Override
	public float getProgress()
	{
		return categories.size() / (float) categoryCount;
	}

	@Override
	public int getExpectedCost()
	{
		return categoryCount;
	}
}
