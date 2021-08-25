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
public final class DatabaseConfig
{
	private String host;
	private Integer port;
	private String username;
	private String password;
	private String schema;
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
			port = 3306;
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

	public String getSchema()
	{
		if (schema == null)
		{
			schema = "test";
		}

		return schema;
	}

	public void setSchema(final String schema)
	{
		this.schema = schema;
	}

	public Integer getConnectionPoolSize()
	{
		if (connectionPoolSize == null)
		{
			connectionPoolSize = 8;
		}

		return connectionPoolSize;
	}

	public void setConnectionPoolSize(final Integer connectionPoolSize)
	{
		this.connectionPoolSize = connectionPoolSize;
	}

	public void load(final Ini configuration)
	{
		final Section section = configuration.get("database");
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
			if (section.containsKey("schema"))
			{
				setSchema(section.get("schema"));
			}
			if (section.containsKey("connection-pool-size"))
			{
				setConnectionPoolSize(section.get("connection-pool-size", Integer.class));
			}
		}
	}

	public void save(final Ini configuration)
	{
		Section section = configuration.get("database");
		if (section == null)
		{
			section = configuration.add("database");
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
		if (getSchema() != null)
		{
			section.put("schema", getSchema());
		}
		if (getConnectionPoolSize() != null)
		{
			section.put("connection-pool-size", getConnectionPoolSize());
		}
	}
}
