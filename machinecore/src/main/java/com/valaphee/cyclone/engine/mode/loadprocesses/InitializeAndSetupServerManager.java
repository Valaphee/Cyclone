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

import com.avaje.ebean.EbeanServer;
import com.valaphee.cyclone.config.Config;
import com.valaphee.cyclone.config.MainConfig;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.engine.mode.LoadProcess;
import com.valaphee.cyclone.server.*;
import com.valaphee.cyclone.server.data.ServerCategoryScoreCriteriaTable;
import com.valaphee.cyclone.server.data.ServerCategoryTable;
import com.valaphee.cyclone.server.data.ServerCategoryVariableTable;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Default
 *
 * @author valaphee
 */
public final class InitializeAndSetupServerManager
		implements LoadProcess
{
	private static final Logger LOGGER = LogManager.getLogger(InitializeAndSetupAccountManager.class);
	private final ServerManager manager;
	private final EbeanServer database;
	private final int serverCategories;
	private final List<Integer> serverCategoryIds;
	private Iterator<Integer> serverCategoryIdIterator;
	private ServerCategory currentCategory;
	private ServerCategoryTable currentServerCategory;
	private int serverCategoriesLoaded;

	public InitializeAndSetupServerManager(final Context context)
	{
		database = context.get(EbeanServer.class);

		final Config config = context.get(Config.class);
		final MainConfig mainConfig = config.getMain();
		context.set(ServerManager.class, manager = new ServerManager(context, new MultipleRangePortMap(12000, 13000, 25565, 25566), mainConfig.getSharePath(), mainConfig.getRunPath()));

		serverCategoryIds = database.find(ServerCategoryTable.class).findIds();
		serverCategories = serverCategoryIds.size();
		serverCategoryIdIterator = serverCategoryIds.iterator();
		currentCategory = manager;
	}

	@Override
	public boolean step()
	{
		final ServerCategoryTable serverCategory = database.find(ServerCategoryTable.class, serverCategoryIdIterator.next());
		if (((serverCategory.getParent() == null) && (currentServerCategory == null)) || ((serverCategory.getParent() != null) && (serverCategory.getParent().getId() == currentServerCategory.getId())))
		{
			VariableContainer variableContainer = currentCategory.getVariableContainer();
			if (!serverCategory.getVariables().isEmpty())
			{
				variableContainer = variableContainer.clone();
				for (final ServerCategoryVariableTable variable : serverCategory.getVariables())
				{
					variableContainer.addVariable(variable.getName(), variable.getContent());
				}
			}

			ScoreCriteriaContainer scoreCriteriaContainer = currentCategory.getScoreCriteriaContainer();
			if (!serverCategory.getScoreCriterias().isEmpty())
			{
				scoreCriteriaContainer = scoreCriteriaContainer.clone();
				for (final ServerCategoryScoreCriteriaTable scoreCriteria : serverCategory.getScoreCriterias())
				{
					scoreCriteriaContainer.addCriteria(ScoreCriteriaContainer.REGISTRY.get(scoreCriteria.getName()), scoreCriteria.getPrescaler(), scoreCriteria.getRemainder());
				}
			}

			final ServerCategory subcategory = new ServerCategory(currentCategory, serverCategory.getName(), serverCategory.getMask(), variableContainer, scoreCriteriaContainer);
			currentCategory.getSubcategories().add(subcategory);
			currentCategory = subcategory;
			currentServerCategory = serverCategory;
			serverCategoryIdIterator.remove();
			serverCategoriesLoaded++;
			LOGGER.log(Level.INFO, "Server category {} loaded successfully.", subcategory.getUrn());
		}
		if (!serverCategoryIdIterator.hasNext())
		{
			serverCategoryIdIterator = serverCategoryIds.iterator();
			currentCategory = currentCategory.getParent();
			currentServerCategory = currentServerCategory.getParent();
		}

		return !serverCategoryIdIterator.hasNext();
	}

	@Override
	public float getProgress()
	{
		return serverCategoriesLoaded / (float) serverCategories;
	}

	@Override
	public int getExpectedCost()
	{
		return serverCategories;
	}
}
