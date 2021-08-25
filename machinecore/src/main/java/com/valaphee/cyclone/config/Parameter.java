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

import static com.valaphee.cyclone.Cyclone.SYSERR;
import static com.valaphee.cyclone.Cyclone.SYSOUT;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * Default
 *
 * @author valaphee
 */
public final class Parameter
{
	public static final OptionParser PARSER = new OptionParser();
	public static final OptionSpec<File> CONFIG = PARSER.accepts("config", "Configuration file").withRequiredArg().ofType(File.class).defaultsTo(new File("cyclone.conf")).describedAs("This is the path to the configuration which will be loaded.");
	public static final OptionSpec<String> ID = PARSER.accepts("id", "Identification").withRequiredArg().describedAs("The id which will be known as this server.");
	public static final OptionSpec<String> LISTENER_HOST = PARSER.acceptsAll(Arrays.asList("L", "listener-host"), "Host to listen").withRequiredArg().describedAs("The host to listen. Can be a ip or name.");
	public static final OptionSpec<Integer> LISTENER_PORT = PARSER.acceptsAll(Arrays.asList("l", "listener-port"), "Port to listen").withRequiredArg().ofType(Integer.class).describedAs("The port to listen. Can be any number between 1024 and 65535.");
	public static final OptionSpec<String> CONNECTOR_HOST = PARSER.acceptsAll(Arrays.asList("C", "connector-host"), "Host to connect").withRequiredArg().describedAs("The host to connect. Can be a ip or name.");
	public static final OptionSpec<Integer> CONNECTOR_PORT = PARSER.acceptsAll(Arrays.asList("c", "connector-port"), "Port to connect").withRequiredArg().ofType(Integer.class).describedAs("The port to connect. Can be any number between 1024 and 65535.");
	private File config;
	private UUID id;
	private String listenerHost;
	private Integer listenerPort;
	private String connectorHost;
	private Integer connectorPort;

	public boolean parse(final String[] arguments)
	{
		OptionSet parameter = null;
		try
		{
			parameter = PARSER.parse(arguments);
		}
		catch (final OptionException ex)
		{
			SYSERR.println(ex.getMessage());
		}
		if ((parameter == null) || parameter.has("?"))
		{
			try
			{
				PARSER.printHelpOn(SYSOUT);
			}
			catch (final IOException ex)
			{
				SYSERR.println(ex.getMessage());
			}

			return false;
		}

		config = parameter.valueOf(CONFIG);
		final String id = parameter.valueOf(ID);
		if ((id != null) && (!id.isEmpty()))
		{
			try
			{
				this.id = UUID.fromString(parameter.valueOf(ID));
			}
			catch (final IllegalArgumentException ignore)
			{}
		}
		listenerHost = parameter.valueOf(LISTENER_HOST);
		listenerPort = parameter.valueOf(LISTENER_PORT);
		connectorHost = parameter.valueOf(CONNECTOR_HOST);
		connectorPort = parameter.valueOf(CONNECTOR_PORT);

		return true;
	}

	public File getConfig()
	{
		return config;
	}

	public UUID getId()
	{
		return id;
	}

	public String getListenerHost()
	{
		return listenerHost;
	}

	public Integer getListenerPort()
	{
		return listenerPort;
	}

	public String getConnectorHost()
	{
		return connectorHost;
	}

	public Integer getConnectorPort()
	{
		return connectorPort;
	}
}
