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

package com.valaphee.cyclone.config;

import org.ini4j.Ini;
import org.ini4j.Profile;

/**
 * Default
 *
 * @author valaphee
 */
public final class TeamSpeakConfig
{
	private String host;
	private Integer port;
	private String username;
	private String password;
	private String nickname;
	private Integer virtualServer;

	public String getHost()
	{
		if (host == null)
		{
			host = "127.0.0.1";
		}

		return host;
	}

	public void setHost(final String host)
	{
		this.host = host;
	}

	public Integer getPort()
	{
		if (port == null)
		{
			port = 10011;
		}

		return port;
	}

	public void setPort(final Integer port)
	{
		this.port = port;
	}

	public String getUsername()
	{
		if (username == null)
		{
			username = "test";
		}

		return username;
	}

	public void setUsername(final String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(final String password)
	{
		this.password = password;
	}

	public String getNickname()
	{
		if (nickname == null)
		{
			nickname = "test";
		}

		return nickname;
	}

	public void setNickname(final String nickname)
	{
		this.nickname = nickname;
	}

	public Integer getVirtualServer()
	{
		if (virtualServer == null)
		{
			virtualServer = 1;
		}

		return virtualServer;
	}

	public void setVirtualServer(final Integer virtualServer)
	{
		this.virtualServer = virtualServer;
	}

	public void load(final Ini configuration)
	{
		final Profile.Section section = configuration.get("teamspeak");
		if (section != null)
		{
			if (section.containsKey("host"))
			{
				setHost(section.get("host"));
			}
			if (section.containsKey("port"))
			{
				setPort(section.get("port", Integer.class));
			}
			if (section.containsKey("username"))
			{
				setUsername(section.get("username"));
			}
			if (section.containsKey("password"))
			{
				setPassword(section.get("password"));
			}
			if (section.containsKey("nickname"))
			{
				setNickname(section.get("nickname"));
			}
			if (section.containsKey("virtual-server"))
			{
				setVirtualServer(section.get("virtual-server", Integer.class));
			}
		}
	}

	public void save(final Ini configuration)
	{
		Profile.Section section = configuration.get("teamspeak");
		if (section == null)
		{
			section = configuration.add("teamspeak");
		}

		if (getHost() != null)
		{
			section.put("host", getHost());
		}
		if (getPort() != null)
		{
			section.put("port", getPort());
		}
		if (getUsername() != null)
		{
			section.put("username", getUsername());
		}
		if (getPassword() != null)
		{
			section.put("password", getPassword());
		}
		if (getNickname() != null)
		{
			section.put("nickname", getNickname());
		}
		if (getVirtualServer() != null)
		{
			section.put("virtual-server", getVirtualServer());
		}
	}
}
