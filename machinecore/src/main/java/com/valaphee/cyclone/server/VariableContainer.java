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

package com.valaphee.cyclone.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default
 *
 * @author valaphee
 */
public final class VariableContainer
		implements Cloneable
{
	private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{(.+?)\\}");
	private static final Pattern ARGUMENTS_PATTERN = Pattern.compile(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	private final Map<String, String> variables = new HashMap<>();

	public VariableContainer()
	{}

	public VariableContainer(final Map<String, String> variables)
	{
		this.variables.putAll(variables);
	}

	public Map<String, String> getVariables()
	{
		return Collections.unmodifiableMap(variables);
	}

	public String getVariable(final String name)
	{
		return variables.get(name);
	}

	public void addVariable(final String name, final String content)
	{
		if (variables.containsKey(name))
		{
			variables.remove(name);
		}
		variables.put(name, content);
	}

	public boolean removeVariable(final String name)
	{
		if (variables.containsKey(name))
		{
			variables.remove(name);

			return true;
		}

		return false;
	}

	public String[] parse(final String text)
	{
		final StringBuilder parsed = new StringBuilder();

		int i = 0;
		for (final Matcher matcher = VARIABLE_PATTERN.matcher(text); matcher.find(); i = matcher.end())
		{
			final String variable = variables.get(matcher.group(1));
			parsed.append(text.substring(i, matcher.start()));
			parsed.append(variable != null ? variable : matcher.group(0));
		}
		parsed.append(text.substring(i, text.length()));

		return ARGUMENTS_PATTERN.split(parsed);
	}

	@Override
	@SuppressWarnings("CloneDoesntCallSuperClone")
	public VariableContainer clone()
	{
		return new VariableContainer(variables);
	}
}
