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

package com.valaphee.cyclone.serialization.template.builder;

import com.valaphee.cyclone.CycloneConstants;
import com.valaphee.cyclone.serialization.template.TemplateRegistry;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public class TemplateBuilderChain
{
	private final List<TemplateBuilder> builders = new ArrayList<>();

	public TemplateBuilderChain(final TemplateRegistry registry)
	{
		boolean javassist = true;
		if (!"Dalvik".equals(CycloneConstants.JAVA_VM_NAME))
		{
			try
			{
				Class.forName("javassist.ClassPool");
			}
			catch (final ClassNotFoundException ignore)
			{
				javassist = false;
			}
		}

		builders.add(new JavaGenericTemplateBuilder(registry));
		builders.add(new JavaEnumerationTemplateBuilder(registry));
		builders.add(javassist ? new CompileTemplateBuilder(registry) : new ReflectiveTemplateBuilder(registry));
	}

	public TemplateBuilder getBuilder(final Type type)
	{
		for (final TemplateBuilder builder : builders)
		{
			if (builder.match(type))
			{
				return builder;
			}
		}

		return null;
	}

	public void appendClassLoader(final ClassLoader classLoader)
	{
		builders.forEach((builder) ->
		{
			builder.appendClassLoader(classLoader);
		});
	}
}
