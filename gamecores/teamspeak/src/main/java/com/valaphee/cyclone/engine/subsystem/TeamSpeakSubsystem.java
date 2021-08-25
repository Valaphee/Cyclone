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

package com.valaphee.cyclone.engine.subsystem;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.TS3Query.FloodRate;
import com.valaphee.cyclone.config.Config;
import com.valaphee.cyclone.config.TeamSpeakConfig;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.engine.EngineSubsystem;

/**
 * Default
 *
 * @author valaphee
 */
public final class TeamSpeakSubsystem
		implements EngineSubsystem
{
	private TS3Api teamspeak;

	@Override
	public String getName()
	{
		return "TeamSpeak";
	}

	@Override
	public void preStartup(final Context context)
	{
		final TeamSpeakConfig config = context.get(Config.class).getTeamSpeak();
		final TS3Config ts3Config = new TS3Config();
		ts3Config.setHost(config.getHost());
		ts3Config.setQueryPort(config.getPort());
		ts3Config.setFloodRate(FloodRate.custom(250));
		final TS3Query ts3Query = new TS3Query(ts3Config);
		ts3Query.connect();
		context.set(TS3Api.class, teamspeak = ts3Query.getApi());
		teamspeak.login(config.getUsername(), config.getPassword());
		teamspeak.selectVirtualServerById(config.getVirtualServer());
		teamspeak.setNickname(config.getNickname());
	}

	@Override
	public void shutdown()
	{
		if (teamspeak != null)
		{
			teamspeak.unregisterAllEvents();
			teamspeak.logout();
		}
	}
}
