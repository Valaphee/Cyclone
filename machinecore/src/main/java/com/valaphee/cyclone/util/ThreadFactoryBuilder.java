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

package com.valaphee.cyclone.util;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Default
 *
 * @author valaphee
 */
public final class ThreadFactoryBuilder
{
	private String name;
	private Boolean deamon;
	private Integer priority;
	private UncaughtExceptionHandler uncaughtExceptionHandler;
	private ThreadFactory threadFactory;

	public ThreadFactoryBuilder setName(final String name)
	{
		this.name = name;

		return this;
	}

	public ThreadFactoryBuilder setDeamon(final boolean deamon)
	{
		this.deamon = deamon;

		return this;
	}

	public ThreadFactoryBuilder setPriority(final int priority)
	{
		this.priority = priority;

		return this;
	}

	public ThreadFactoryBuilder setUncaughtExceptionHandler(final UncaughtExceptionHandler uncaughtExceptionHandler)
	{
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;

		return this;
	}

	public ThreadFactoryBuilder setThreadFactory(final ThreadFactory threadFactory)
	{
		this.threadFactory = threadFactory;

		return this;
	}

	public ThreadFactory build()
	{
		final AtomicLong counter = name != null ? new AtomicLong() : null;
		return (runnable) ->
		{
			final Thread thread = (threadFactory != null ? threadFactory : Executors.defaultThreadFactory()).newThread(runnable);
			if (name != null)
			{
				thread.setName(String.format(name, counter.getAndIncrement()));
			}
			if (deamon != null)
			{
				thread.setDaemon(deamon);
			}
			if (priority != null)
			{
				thread.setPriority(priority);
			}
			if (uncaughtExceptionHandler != null)
			{
				thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
			}

			return thread;
		};
	}
}
