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

package com.valaphee.cyclone;

import com.valaphee.cyclone.config.Parameter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import jline.Terminal;
import jline.TerminalFactory;
import jline.UnsupportedTerminal;
import jline.console.ConsoleReader;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

/**
 * Default
 *
 * @author valaphee
 */
public final class Cyclone
{
	private static final char[] ERASE = Ansi.ansi().cursorToColumn(0).eraseLine().toString().toCharArray(), RESET = Ansi.ansi().a(Ansi.Attribute.RESET).toString().toCharArray();
	public static final PrintStream SYSOUT = System.out, SYSERR = System.err;
	public static final Terminal TERMINAL = TerminalFactory.get();
	private static boolean ansi;
	private static ConsoleReader reader;
	private static PrintWriter writer;

	public static boolean ansi()
	{
		return ansi;
	}

	public static ConsoleReader reader()
	{
		return reader;
	}

	public static PrintWriter writer()
	{
		return writer;
	}

	public static void main(final String[] arguments)
	{
		final Parameter parameter = new Parameter();
		if (!parameter.parse(arguments))
		{
			return;
		}

		ansi = !UnsupportedTerminal.class.getName().equals(System.getProperty("jline.terminal"));
		if (System.console() == null)
		{
			ansi = false;
		}

		if (ansi)
		{
			AnsiConsole.systemInstall();
		}

		try
		{
			reader = new ConsoleReader(Cyclone.class.getName(), System.in, System.out, TERMINAL);
			reader.setPrompt("+");
		}
		catch (final IOException thrown)
		{
			try
			{
				System.setProperty("jline.terminal", UnsupportedTerminal.class.getName());
				System.setProperty("user.language", "en");
				reader = new ConsoleReader(Cyclone.class.getName(), System.in, System.out, TERMINAL);
				ansi = false;
			}
			catch (final IOException ignore)
			{}
		}
		finally
		{
			if (reader != null)
			{
				reader.setExpandEvents(false);
				reader.setHandleLitteralNext(false);
			}
		}
		writer = new PrintWriter(reader.getOutput());

		new Cyclone().launch(parameter);

		if (ansi)
		{
			writer.write(ERASE);
			writer.flush();
			AnsiConsole.systemUninstall();
		}

		System.exit(0);
	}

	private void launch(final Parameter parameter)
	{
		initializeSecurity();

		SYSOUT.println();
		SYSOUT.print(
				"  ______            _\n"
				+ " / _____)          | |\n"
				+ "| /     _   _  ____| | ___  ____   ____\n"
				+ "| |    | | | |/ ___) |/ _ \\|  _ \\ / _  )\n"
				+ "| \\____| |_| ( (___| | |_| | | | ( (/ / \n"
				+ " \\______)__  |\\____)_|\\___/|_| |_|\\____)\n"
				+ "       (____/"
		);
	}

	private void initializeSecurity()
	{
		System.setSecurityManager(new CycloneSecurityManager());
	}
}
