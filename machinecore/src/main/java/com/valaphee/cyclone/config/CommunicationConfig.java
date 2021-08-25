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

import com.valaphee.cyclone.util.CharsetUtil;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

/**
 * Default
 *
 * @author valaphee
 */
public final class CommunicationConfig
{
	private String host;
	private Integer port;
	private UUID id;
	private String auth;

	public String getHost()
	{
		if (host == null)
		{
			try
			{
				host = Inet6Address.getLocalHost().getHostAddress();
			}
			catch (final UnknownHostException ex)
			{
				try
				{
					host = Inet4Address.getLocalHost().getHostAddress();
				}
				catch (final UnknownHostException ignore)
				{
					host = "localhost";
				}
			}
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
			port = 1024;
		}

		return port;
	}

	public void setPort(final Integer port)
	{
		this.port = port;
	}

	public UUID getId()
	{
		if (id == null)
		{
			try
			{
				id = UUID.nameUUIDFromBytes(InetAddress.getLocalHost().getHostName().getBytes(CharsetUtil.UTF_8));
			}
			catch (final UnknownHostException ignore)
			{
				id = UUID.randomUUID();
			}
		}

		return id;
	}

	public void setId(final UUID id)
	{
		this.id = id;
	}

	public String getAuth()
	{
		if (auth == null)
		{
			final byte[] auth = new byte[64];
			new SecureRandom().nextBytes(auth);
			this.auth = Base64.getEncoder().encodeToString(auth);
		}

		return auth;
	}

	public void setAuth(final String auth)
	{
		this.auth = auth;
	}

	public void load(final Ini configuration)
	{
		final Section section = configuration.get("communication");
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
			if (section.containsKey("id"))
			{
				try
				{
					setId(UUID.fromString(section.get("id")));
				}
				catch (final IllegalArgumentException ignore)
				{}
			}
			if (section.containsKey("auth"))
			{
				setAuth(section.get("auth"));
			}
		}
	}

	public void save(final Ini configuration)
	{
		Section section = configuration.get("communication");
		if (section == null)
		{
			section = configuration.add("communication");
		}

		if (getHost() != null)
		{
			section.put("host", getHost());
		}
		if (getPort() != null)
		{
			section.put("port", getPort());
		}
		if (getId() != null)
		{
			section.put("id", getId());
		}
		if (getAuth() != null)
		{
			section.put("auth", getAuth());
		}
	}
}
