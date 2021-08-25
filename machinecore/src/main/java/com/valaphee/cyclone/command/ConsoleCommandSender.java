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

import com.valaphee.cyclone.text.TextTranscoder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

/**
 * Default
 *
 * @author valaphee
 */
public final class ConsoleCommandSender
		implements CommandSender
{
	private final Logger logger;
	private final TextTranscoder<String> transcoder;

	public ConsoleCommandSender(final Logger logger, final TextTranscoder<String> transcoder)
	{
		this.logger = logger;
		this.transcoder = transcoder;
	}

	@Override
	public String getName()
	{
		return "Console";
	}

	@Override
	public void sendMessage(final String message)
	{
		logger.log(Level.INFO, transcoder.transcode(message));
	}

	@Override
	public boolean hasPermission(final String permission)
	{
		return true;
	}
}
