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

package com.valaphee.cyclone.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public abstract class PaginatedCommand<T>
		extends Command
{
	private int itemsPerPage;

	public PaginatedCommand()
	{
		setArgumentsRange(0, 2);
		setItemsPerPage(6);
	}

	public int getItemsPerPage()
	{
		return itemsPerPage;
	}

	protected final void setItemsPerPage(final int itemsPerPage)
	{
		this.itemsPerPage = itemsPerPage;
	}

	public Pagination getPagination(final CommandSender sender, final List<String> arguments, final Collection<T> collection)
	{
		final Pagination pagination = new Pagination(collection);
		if (arguments.size() == (getMaximumArguments() - 1))
		{
			try
			{
				pagination.page = Integer.parseInt(arguments.get(getMaximumArguments() - 2));
			}
			catch (final NumberFormatException ex)
			{
				pagination.filter = arguments.get((getMaximumArguments() - 2));
			}
		}
		else if (arguments.size() == getMaximumArguments())
		{
			try
			{
				pagination.page = Integer.parseInt(arguments.get(getMaximumArguments() - 2));
				pagination.filter = arguments.get(getMaximumArguments() - 1);
			}
			catch (final NumberFormatException ex)
			{
				pagination.filter = arguments.get(getMaximumArguments() - 2);
				try
				{
					pagination.page = Integer.parseInt(arguments.get(getMaximumArguments() - 1));
				}
				catch (final NumberFormatException ignore)
				{}
			}
		}

		pagination.items = run(sender, pagination);
		pagination.pages = (pagination.items.size() + itemsPerPage - 1) / itemsPerPage;
		if (pagination.page > pagination.pages)
		{
			pagination.page = pagination.pages;
		}
		if (pagination.page <= 0)
		{
			pagination.page = 1;
		}

		return pagination;
	}

	public abstract List<T> run(CommandSender sender, Pagination pagination);

	public String format(final T item)
	{
		return item.toString();
	}

	public void display(final CommandSender sender, final Pagination pagination)
	{
		final int startItem = (pagination.page - 1) * itemsPerPage;
		final int endItem = startItem + (startItem + itemsPerPage > pagination.items.size() ? pagination.items.size() - startItem : itemsPerPage);
		for (int i = startItem; i < endItem; ++i)
		{
			sender.sendMessage(format(pagination.items.get(i)));
		}
	}

	protected final class Pagination
	{
		private final Collection<T> collection;
		int page = 1;
		String filter = "";
		List<T> items;
		int pages;

		private Pagination(final Collection<T> collection)
		{
			this.collection = collection;
		}

		public Collection<T> getCollection()
		{
			return Collections.unmodifiableCollection(collection);
		}

		public int getPage()
		{
			return page;
		}

		public String getFilter()
		{
			return filter;
		}

		public List<T> getItems()
		{
			return Collections.unmodifiableList(items);
		}

		public int getPages()
		{
			return pages;
		}
	}
}
