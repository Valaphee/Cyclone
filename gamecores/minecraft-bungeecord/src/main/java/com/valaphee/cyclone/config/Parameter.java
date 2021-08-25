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

import java.util.UUID;

/**
 * Default
 *
 * @author valaphee
 */
public final class Parameter
{
	private UUID id;
	private UUID nodeId;
	private String nodeHost;
	private Integer nodePort;

	public Parameter()
	{
		if (System.getProperty("com.valaphee.cyclone.id") != null)
		{
			try
			{
				id = UUID.fromString(System.getProperty("com.valaphee.cyclone.id"));
			}
			catch (final IllegalArgumentException ignore)
			{}
		}
		if (System.getProperty("com.valaphee.cyclone.nodeId") != null)
		{
			try
			{
				nodeId = UUID.fromString(System.getProperty("com.valaphee.cyclone.nodeId"));
			}
			catch (final IllegalArgumentException ignore)
			{}
		}
		if (System.getProperty("com.valaphee.cyclone.nodeHost") != null)
		{
			nodeHost = System.getProperty("com.valaphee.cyclone.nodeHost");
		}
		if (System.getProperty("com.valaphee.cyclone.nodePort") != null)
		{
			try
			{
				nodePort = Integer.parseInt(System.getProperty("com.valaphee.cyclone.nodePort"));
			}
			catch (final NumberFormatException ignore)
			{}
		}
	}

	public UUID getId()
	{
		return id;
	}

	public UUID getNodeId()
	{
		return nodeId;
	}

	public String getNodeHost()
	{
		return nodeHost;
	}

	public Integer getNodePort()
	{
		return nodePort;
	}
}
