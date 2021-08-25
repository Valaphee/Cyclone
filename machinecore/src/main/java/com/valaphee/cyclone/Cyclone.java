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
import com.valaphee.cyclone.engine.EngineBuilder;
import com.valaphee.cyclone.engine.mode.LoadingMode;
import com.valaphee.cyclone.engine.subsystem.*;
import com.valaphee.cyclone.logging.LogDispatcher;
import com.valaphee.cyclone.logging.LogStream;
import com.valaphee.cyclone.logging.Progress;
import com.valaphee.cyclone.logging.QueueAppender;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import jline.Terminal;
import jline.TerminalFactory;
import jline.UnsupportedTerminal;
import jline.console.ConsoleReader;
import jline.console.CursorBuffer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public final class Cyclone
{
	private static final char[] ERASE = Ansi.ansi().cursorToColumn(0).eraseLine().toString().toCharArray(), RESET = Ansi.ansi().a(Ansi.Attribute.RESET).toString().toCharArray();
	public static final PrintStream SYSOUT = System.out, SYSERR = System.err;
	public static final Terminal TERMINAL = TerminalFactory.get();
	public static final Progress PROGRESS = new Progress();
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
		initializeLogging();
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

		final EngineBuilder engine = new EngineBuilder();
		engine.addSubsystem(new SleeperSubsystem(3L));
		engine.addSubsystem(new ConfigSubsystem(parameter));
		engine.addSubsystem(new I18nSubsystem());
		engine.addSubsystem(new CommandSubsystem());
		engine.addSubsystem(new DatabaseSubsystem());
		engine.addSubsystem(new CacheSubsystem());
		engine.addSubsystem(new CommunicationSubsystem());
		engine.setInitialMode(new LoadingMode());
		engine.build().run();
	}

	private void initializeLogging()
	{
		final java.util.logging.Logger global = java.util.logging.Logger.getLogger("");
		global.setUseParentHandlers(false);
		for (final java.util.logging.Handler handler : global.getHandlers())
		{
			global.removeHandler(handler);
		}
		global.addHandler(new LogDispatcher());

		final Logger root = (Logger) LogManager.getRootLogger();
		root.getAppenders().values().stream().filter((appender) -> (appender instanceof ConsoleAppender)).forEach(root::removeAppender);

		final Thread consoleWriter = new Thread(() ->
		{
			for (;;)
			{
				final String message = QueueAppender.getMessage();
				if (ansi)
				{
					final CursorBuffer stashed = reader.getCursorBuffer().copy();
					writer.write(ERASE);
					writer.flush();
					writer.write(message);
					writer.write(RESET);
					try
					{
						reader.resetPromptLine(reader.getPrompt(), stashed.toString(), stashed.cursor);
					}
					catch (final IOException | IndexOutOfBoundsException ex)
					{
						reader.getCursorBuffer().clear();
					}
					PROGRESS.resetProgress();
				}
				else
				{
					writer.write(message);
					writer.flush();
				}
			}
		}, "Console Writer");
		consoleWriter.setDaemon(true);
		consoleWriter.start();

		System.setIn(null);
		System.setOut(new PrintStream(new LogStream(root, Level.INFO), true));
		System.setErr(new PrintStream(new LogStream(root, Level.WARN), true));
	}

	private void initializeSecurity()
	{
		System.setSecurityManager(new CycloneSecurityManager());
	}
}
