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

import com.valaphee.cyclone.reflect.Accessors;
import com.valaphee.cyclone.reflect.FieldAccessor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Default
 *
 * @author valaphee
 */
public final class LogDispatcher
		extends Handler
{
	private static final FieldAccessor HANDLER_SEALED = Accessors.getFieldAccessorOrNull(Handler.class, "sealed");
	private final Map<String, Logger> loggers = new HashMap<>();

	public LogDispatcher()
	{
		if (HANDLER_SEALED != null)
		{
			HANDLER_SEALED.set(this, false);
		}
		setLevel(Level.INFO);
		setFormatter(new SimpleFormatter());
		if (HANDLER_SEALED != null)
		{
			HANDLER_SEALED.set(this, true);
		}
	}

	private synchronized Logger getLogger(final String name)
	{
		Logger logger = loggers.get(name);
		if (logger == null)
		{
			logger = LogManager.getLogger(name);
			loggers.put(name, logger);
		}

		return logger;
	}

	@Override
	public void publish(final LogRecord record)
	{
		final Logger logger = getLogger(record.getLoggerName());
		if (record.getLevel().equals(Level.CONFIG))
		{
			logger.debug(getFormatter().formatMessage(record), record.getThrown());
		}
		else if (record.getLevel().equals(Level.INFO))
		{
			logger.info(getFormatter().formatMessage(record), record.getThrown());
		}
		else if (record.getLevel().equals(Level.WARNING))
		{
			logger.warn(getFormatter().formatMessage(record), record.getThrown());
		}
		else if (record.getLevel().equals(Level.SEVERE))
		{
			logger.error(getFormatter().formatMessage(record), record.getThrown());
		}
		else
		{
			logger.trace(getFormatter().formatMessage(record), record.getThrown());
		}
	}

	@Override
	public void flush()
	{}

	@Override
	public void close()
			throws SecurityException
	{}
}
