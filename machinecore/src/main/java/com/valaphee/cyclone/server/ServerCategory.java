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

package com.valaphee.cyclone.server;

import com.avaje.ebean.EbeanServer;
import com.valaphee.cyclone.serialization.Serializer;
import com.valaphee.cyclone.server.data.ServerRepository;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import redis.clients.jedis.JedisPool;

/**
 * Default
 *
 * @author valaphee
 */
public class ServerCategory
{
	private final ServerManager root;
	private final ServerCategory parent;
	private final String name;
	private final String urn;
	private final ServerRepository repository;
	private final File path;
	private final String mask;
	protected final VariableContainer variableContainer;
	protected final ScoreCriteriaContainer scoreCriteriaContainer;
	protected final Set<ServerCategory> subcategories = Collections.newSetFromMap(new ConcurrentHashMap<>());
	protected final Set<Server> servers = Collections.newSetFromMap(new ConcurrentHashMap<>());

	ServerCategory()
	{
		root = (ServerManager) this;
		parent = null;
		name = null;
		urn = calculateUrn(parent, name, root);
		repository = null;
		path = null;
		mask = null;
		variableContainer = new VariableContainer();
		scoreCriteriaContainer = new ScoreCriteriaContainer();
	}

	public ServerCategory(final ServerCategory parent, final String name, final String mask, final VariableContainer variableContainer, final ScoreCriteriaContainer scoreCriteriaContainer)
	{
		root = parent.getRoot();
		this.parent = parent;
		this.name = name;
		urn = calculateUrn(this.parent, this.name, root);
		repository = new ServerRepository(root.context.get(JedisPool.class), root.context.get(Serializer.class), root.context.get(EbeanServer.class), urn);
		path = new File(root.getSharePath(), urn.replace(":", "_"));
		this.mask = mask;
		this.variableContainer = variableContainer;
		this.scoreCriteriaContainer = scoreCriteriaContainer;
	}

	public ServerManager getRoot()
	{
		return root;
	}

	public ServerCategory getParent()
	{
		return parent;
	}

	public String getName()
	{
		return name;
	}

	public String getUrn()
	{
		return urn;
	}

	public ServerRepository getRepository()
	{
		return repository;
	}

	public File getPath()
	{
		return path;
	}

	public String getMask()
	{
		return mask;
	}

	public VariableContainer getVariableContainer()
	{
		return variableContainer;
	}

	public ScoreCriteriaContainer getScoreCriteriaContainer()
	{
		return scoreCriteriaContainer;
	}

	public Collection<ServerCategory> getSubcategories()
	{
		return subcategories;
	}

	public ServerCategory getSubcategory(final String urn)
	{
		final String[] splitted = urn.split(":");
		ServerCategory current = this;
		for (int i = 0; i < splitted.length; ++i)
		{
			if (current.getSubcategories().isEmpty())
			{
				break;
			}

			for (final ServerCategory subcategory : current.getSubcategories())
			{
				if (subcategory.getName().equalsIgnoreCase(splitted[i]))
				{
					current = subcategory;
				}
			}
		}

		return current;
	}

	public Collection<Server> getServers()
	{
		return servers;
	}

	public Collection<Server> getServers(final boolean includeSubcategories)
	{
		final List<Server> founded = new ArrayList<>();

		final Stack<ServerCategory> stacked = new Stack<>();
		final List<ServerCategory> visited = new ArrayList<>();
		stacked.push(this);
		visited.add(this);
		while (!stacked.isEmpty())
		{
			final ServerCategory current = stacked.pop();
			if (includeSubcategories)
			{
				current.getSubcategories().stream().filter((subcategory) -> (!visited.contains(subcategory))).forEach((subcategory) ->
				{
					stacked.add(subcategory);
					visited.add(subcategory);
				});
			}

			current.getServers().forEach((server) ->
			{
				founded.add(server);
			});
		}

		return Collections.unmodifiableList(founded);
	}

	public Server getServer(final UUID id)
	{
		for (final Server server : getServers())
		{
			if (server.getId().equals(id))
			{
				return server;
			}
		}

		return null;
	}

	public Server getServer(final UUID id, final boolean includeSubcategories)
	{
		final Stack<ServerCategory> stacked = new Stack<>();
		final List<ServerCategory> visited = new ArrayList<>();
		stacked.push(this);
		visited.add(this);
		while (!stacked.isEmpty())
		{
			final ServerCategory current = stacked.pop();
			if (includeSubcategories)
			{
				current.getSubcategories().stream().filter((subcategory) -> (!visited.contains(subcategory))).forEach((subcategory) ->
				{
					stacked.add(subcategory);
					visited.add(subcategory);
				});
			}

			for (final Server server : current.getServers())
			{
				if (server.getId().equals(id))
				{
					return server;
				}
			}
		}

		return null;
	}

	public void update()
	{
		subcategories.forEach((subcategory) ->
		{
			subcategory.update();
		});

		servers.forEach((server) ->
		{
			server.update();
		});
	}

	private static String calculateUrn(final ServerCategory begin, final String name, final ServerCategory end)
	{
		final List<ServerCategory> path = new ArrayList<>();
		for (ServerCategory parent = begin; (parent != null) && (parent != end); parent = parent.getParent())
		{
			path.add(parent);
		}

		final StringBuilder urn = new StringBuilder();
		for (final ListIterator<ServerCategory> iterator = path.listIterator(path.size()); iterator.hasPrevious();)
		{
			final ServerCategory child = iterator.previous();
			if ((child.getName() != null) && (!child.getName().isEmpty()))
			{
				urn.append(child.getName()).append(':');
			}
		}
		if ((name != null) && (!name.isEmpty()))
		{
			urn.append(name);
		}
		else if (urn.length() > 0)
		{
			urn.setLength(urn.length() - 1);
		}

		return urn.toString();
	}
}
