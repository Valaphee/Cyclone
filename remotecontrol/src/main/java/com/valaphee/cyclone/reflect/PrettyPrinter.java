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

package com.valaphee.cyclone.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Default
 *
 * @author valaphee
 */
public final class PrettyPrinter
{
	public static String print(final Object object)
			throws IllegalAccessException
	{
		return print(object, object.getClass(), Object.class);
	}

	public static String print(final Object object, final Class<?> begin, final Class<?> end)
			throws IllegalAccessException
	{
		return print(object, begin, end, 3);
	}

	public static String print(final Object object, final Class<?> begin, final Class<?> end, final int hierachyDepth)
			throws IllegalAccessException
	{
		if (object == null)
		{
			throw new IllegalArgumentException("Object cannot be null.");
		}

		final StringBuilder result = new StringBuilder();
		final Set<Object> previous = new HashSet<>();
		print(result, object, begin, end, previous, hierachyDepth);

		return result.toString();
	}

	private static void print(final StringBuilder result, final Object object, final Class<?> end, final Set<Object> previous, final int hierachyIndex)
			throws IllegalAccessException
	{
		print(result, object, object != null ? object.getClass() : null, end, previous, hierachyIndex);
	}

	@SuppressWarnings("unchecked")
	private static void print(final StringBuilder result, final Object object, final Class<?> type, final Class<?> end, final Set<Object> previous, final int hierachyIndex)
			throws IllegalAccessException
	{
		if (object == null)
		{
			result.append("null");
		}
		else if (type.isPrimitive())
		{
			result.append(object);
		}
		else if ((type == String.class) || (hierachyIndex <= 0))
		{
			result.append('"').append(object.toString()).append('"');
		}
		else if (type.isArray())
		{
			printArray(result, object, type, end, previous, hierachyIndex);
		}
		else if (Iterable.class.isAssignableFrom(type))
		{
			printIterable(result, (Iterable<Object>) object, type, end, previous, hierachyIndex);
		}
		else if (Map.class.isAssignableFrom(type))
		{
			printMap(result, (Map<Object, Object>) object, type, end, previous, hierachyIndex);
		}
		else if (Enum.class.isAssignableFrom(type) || previous.contains(object))
		{
			result.append('"').append(object).append('"');
		}
		else
		{
			printObject(result, object, object.getClass(), end, previous, hierachyIndex);
		}
	}

	private static void printArray(final StringBuilder result, final Object array, final Class<?> current, final Class<?> end, final Set<Object> previous, final int hierachyIndex)
			throws IllegalAccessException
	{
		final Class<?> component = current.getComponentType();
		if (!component.isArray())
		{
			result.append(component.getName());
		}

		result.append('[');
		final int length = Array.getLength(array);
		for (int i = 0; i < length; ++i)
		{
			print(result, Array.get(array, i), component, end, previous, hierachyIndex - 1);
			result.append(", ");
		}
		if (length > 0)
		{
			result.setLength(result.length() - 2);
		}
		result.append(']');
	}

	private static void printIterable(final StringBuilder result, final Iterable<Object> iterable, final Class<?> current, final Class<?> end, final Set<Object> previous, final int hierachyIndex)
			throws IllegalAccessException
	{
		result.append('(');
		for (final Object element : iterable)
		{
			print(result, element, end, previous, hierachyIndex - 1);
			result.append(", ");
		}
		if (iterable.iterator().hasNext())
		{
			result.setLength(result.length() - 2);
		}
		result.append(')');
	}

	private static void printMap(final StringBuilder result, final Map<Object, Object> map, final Class<?> current, final Class<?> end, final Set<Object> previous, final int hierachyIndex)
			throws IllegalAccessException
	{
		result.append('[');
		for (final Map.Entry<Object, Object> element : map.entrySet())
		{
			print(result, element.getKey(), end, previous, hierachyIndex - 1);
			result.append(": ");
			print(result, element.getValue(), end, previous, hierachyIndex - 1);
			result.append(", ");
		}
		if (map.size() > 0)
		{
			result.setLength(result.length() - 2);
		}
		result.append(']');
	}

	private static void printObject(final StringBuilder result, final Object object, final Class<?> current, final Class<?> end, final Set<Object> previous, final int hierachyIndex)
			throws IllegalAccessException
	{
		if ((current == null) || (current == Object.class) || ((end != null) && end.equals(current)))
		{
			return;
		}

		previous.add(object);

		if (hierachyIndex < 0)
		{
			result.append("...");
		}

		result.append('{');
		for (final Field field : current.getDeclaredFields())
		{
			final int modifier = field.getModifiers();
			if ((!Modifier.isTransient(modifier)) && (!Modifier.isStatic(modifier)))
			{
				final Class<?> type = field.getType();
				if (!field.isAccessible())
				{
					field.setAccessible(true);
				}
				final Object value = field.get(object);
				result.append(field.getName());
				result.append(" = ");
				print(result, value, type, end, previous, hierachyIndex - 1);
				result.append(", ");
			}
		}
		if (current.getDeclaredFields().length > 0)
		{
			result.setLength(result.length() - 2);
		}
		result.append('}');
	}

	private PrettyPrinter()
	{}
}
