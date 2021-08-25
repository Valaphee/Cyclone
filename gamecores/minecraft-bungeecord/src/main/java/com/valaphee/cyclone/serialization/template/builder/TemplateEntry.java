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

import com.valaphee.cyclone.reflect.DefaultFieldAccessor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Default
 *
 * @author valaphee
 */
public final class TemplateEntry
		extends DefaultFieldAccessor
{
	public TemplateEntry(final Field field)
	{
		super(field);
	}

	public String getName()
	{
		return field.getName();
	}

	public Class<?> getType()
	{
		return field.getType();
	}

	public Type getGenericType()
	{
		return field.getGenericType();
	}
}
