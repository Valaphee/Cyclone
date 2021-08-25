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

import com.valaphee.cyclone.CycloneConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

/**
 * Default
 *
 * @author valaphee
 */
public final class LogStream
		extends ByteArrayOutputStream
{
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private final Logger logger;
	private final Level level;

	public LogStream(final Logger logger, final Level level)
	{
		this.logger = logger;
		this.level = level;
	}

	@Override
	public void flush()
			throws IOException
	{
		synchronized (this)
		{
			super.flush();

			final String record = new String(toByteArray(), CHARSET);
			if ((!record.isEmpty()) && (!record.equals(CycloneConstants.LINE_SEPARATOR)))
			{
				logger.log(level, record);
			}

			super.reset();
		}
	}
}
