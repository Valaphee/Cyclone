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
import org.ini4j.Profile.Section;

/**
 * Default
 *
 * @author valaphee
 */
public final class CacheConfig
{
	private String host;
	private Integer port;
	private String auth;
	private Integer connectionPoolSize;

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
			port = 6379;
		}

		return port;
	}

	public void setPort(final Integer port)
	{
		this.port = port;
	}

	public String getAuth()
	{
		return auth;
	}

	public void setAuth(final String auth)
	{
		this.auth = auth;
	}

	public Integer getConnectionPoolSize()
	{
		if (connectionPoolSize == null)
		{
			connectionPoolSize = 4;
		}

		return connectionPoolSize;
	}

	public void setConnectionPoolSize(final Integer connectionPoolSize)
	{
		this.connectionPoolSize = connectionPoolSize;
	}

	public void load(final Ini configuration)
	{
		final Section section = configuration.get("cache");
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
			if (section.containsKey("auth"))
			{
				setAuth(section.get("auth"));
			}
			if (section.containsKey("connection-pool-size"))
			{
				setConnectionPoolSize(section.get("connection-pool-size", Integer.class));
			}
		}
	}

	public void save(final Ini configuration)
	{
		Section section = configuration.get("cache");
		if (section == null)
		{
			section = configuration.add("cache");
		}

		if (getHost() != null)
		{
			section.put("host", getHost());
		}
		if (getPort() != null)
		{
			section.put("port", getPort());
		}
		if (getAuth() != null)
		{
			section.put("auth", getAuth());
		}
		if (getConnectionPoolSize() != null)
		{
			section.put("connection-pool-size", getConnectionPoolSize());
		}
	}
}
