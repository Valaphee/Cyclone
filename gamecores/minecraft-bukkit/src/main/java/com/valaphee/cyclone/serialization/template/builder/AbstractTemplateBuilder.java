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

import com.valaphee.cyclone.serialization.annotation.Ignore;
import com.valaphee.cyclone.serialization.annotation.Index;
import com.valaphee.cyclone.serialization.template.Template;
import com.valaphee.cyclone.serialization.template.TemplateRegistry;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("unchecked")
public abstract class AbstractTemplateBuilder
		implements TemplateBuilder
{
	protected final TemplateRegistry registry;

	protected AbstractTemplateBuilder(final TemplateRegistry registry)
	{
		this.registry = registry;
	}

	@Override
	public <T> Template<T> build(final Type type)
	{
		final Class<T> clazz = (Class<T>) type;
		if (clazz.isInterface())
		{
			throw new TemplateBuilderException("Cannot build a template for the interface " + clazz.getSimpleName() + ".");
		}
		if (Modifier.isAbstract(clazz.getModifiers()))
		{
			throw new TemplateBuilderException("Cannot build a template for the abstract class " + clazz.getSimpleName() + ".");
		}
		if (clazz.isArray())
		{
			throw new TemplateBuilderException("Cannot build a template for a array.");
		}
		if (clazz.isPrimitive())
		{
			throw new TemplateBuilderException("Cannot build a template for a primitive.");
		}

		return build(clazz, getFieldEntries(clazz));
	}

	protected TemplateEntry[] getFieldEntries(final Class<?> clazz)
	{
		int currentIndex = -1;
		final Field[] fields = getFields(clazz);
		final List<TemplateEntry> ordered = new ArrayList<>();
		for (final Field field : fields)
		{
			if (isFieldIgnored(field))
			{
				continue;
			}

			final int index = getFieldIndex(field, currentIndex);
			if ((ordered.size() > index) && (ordered.get(index) != null))
			{
				throw new TemplateBuilderException("Index " + index + " is already used.");
			}
			if (index < 0)
			{
				throw new TemplateBuilderException("Index " + index + " is invalid.");
			}
			while (ordered.size() <= index)
			{
				ordered.add(null);
			}
			ordered.set(index, new TemplateEntry(field));
			if (currentIndex < index)
			{
				currentIndex = index;
			}
		}

		final TemplateEntry[] entries = new TemplateEntry[(currentIndex + 1)];
		for (int i = ordered.size() - 1; i >= 0; --i)
		{
			entries[i] = ordered.get(i);
		}

		return entries;
	}

	private Field[] getFields(final Class<?> clazz)
	{
		final List<Field> fields = new LinkedList<>();
		for (Class<?> superclass = clazz; superclass != Object.class; superclass = superclass.getSuperclass())
		{
			fields.addAll(Arrays.asList(superclass.getDeclaredFields()));
		}

		return fields.toArray(new Field[fields.size()]);
	}

	private boolean isFieldIgnored(final Field field)
	{
		return field.isAnnotationPresent(Ignore.class);
	}

	private int getFieldIndex(final Field field, final int currentIndex)
	{
		final Index annotation = field.getAnnotation(Index.class);
		if (annotation == null)
		{
			return currentIndex + 1;
		}

		return annotation.value();
	}
}
