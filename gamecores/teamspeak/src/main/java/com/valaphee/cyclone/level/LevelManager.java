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

package com.valaphee.cyclone.level;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.valaphee.cyclone.context.Context;

/**
 * Default
 *
 * @author valaphee
 */
public final class LevelManager
{
	final Context context;
	private final TS3Api teamspeak;
	private int[] levels;

	public LevelManager(final Context context)
	{
		this.context = context;
		teamspeak = this.context.get(TS3Api.class);
	}

	public void update()
	{}
}
