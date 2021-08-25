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

import static com.valaphee.cyclone.Cyclone.SYSOUT;
import static com.valaphee.cyclone.Cyclone.TERMINAL;

/**
 * Default
 *
 * @author valaphee
 */
public final class Progress
{
	private static final String FORMAT = "\r%3d%% %s> %c";
	private static final char[] WORKING =
	{
		'|', '/', '-', '\\'
	};
	private boolean active = false;
	private int progressBarWidth;
	private StringBuilder progressBar;
	private int percent;
	private int updates;

	public boolean isActive()
	{
		return active;
	}

	public void setActive(final boolean active)
	{
		this.active = active;
		if (this.active)
		{
			progressBarWidth = TERMINAL.getWidth() - 8;
			progressBar = new StringBuilder(progressBarWidth);
			percent = 0;
			updates = 0;
		}
	}

	public void update(final double percent)
	{
		this.percent = (int) (percent * 100);
		final int proceed = ((int) (percent * progressBarWidth)) - progressBar.length();
		for (int i = proceed; i > 0; --i)
		{
			this.progressBar.append('=');
		}
		updates++;
	}

	public void resetProgress()
	{
		if (active)
		{
			SYSOUT.printf(FORMAT, percent, progressBar, WORKING[(updates % WORKING.length)]);
		}
	}
}
