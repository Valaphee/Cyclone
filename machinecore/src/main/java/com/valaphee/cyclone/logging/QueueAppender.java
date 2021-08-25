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

package com.valaphee.cyclone.logging;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.Booleans;

/**
 * Default
 *
 * @author valaphee
 */
@Plugin(name = "Queue", category = "Core", elementType = "appender", printObject = true)
public final class QueueAppender
		extends AbstractAppender
{
	private static final BlockingQueue<String> QUEUE = new LinkedBlockingQueue<String>();

	@PluginFactory
	public static QueueAppender createAppender(@PluginAttribute("name") final String name, @PluginElement("Filters") final Filter filter, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginAttribute("ignoreExceptions") final String ignoreExceptions)
	{
		if (name == null)
		{
			LOGGER.log(Level.ERROR, "Name cannot be null.");

			return null;
		}
		if (layout == null)
		{
			layout = PatternLayout.createDefaultLayout();
		}

		return new QueueAppender(name, filter, layout, Booleans.parseBoolean(ignoreExceptions, true));
	}

	public static String getMessage()
	{
		try
		{
			return QUEUE.take();
		}
		catch (final InterruptedException ignore)
		{}

		return null;
	}

	@Override
	public void append(final LogEvent event)
	{
		QUEUE.add(getLayout().toSerializable(event).toString());
	}

	private QueueAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout, final boolean ignoreExceptions)
	{
		super(name, filter, layout, ignoreExceptions);
	}
}
