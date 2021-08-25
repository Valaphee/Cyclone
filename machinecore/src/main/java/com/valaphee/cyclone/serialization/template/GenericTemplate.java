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

package com.valaphee.cyclone.serialization.template;

import com.valaphee.cyclone.serialization.stream.SerializationReader;
import com.valaphee.cyclone.serialization.stream.SerializationWriter;
import java.io.IOException;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("rawtypes")
public interface GenericTemplate
{
	public Template build(Template<Object>[] parameter);

	@SuppressWarnings("unchecked")
	public static final class AnyTemplate<T>
			implements Template<T>
	{
		private final TemplateRegistry registry;

		public AnyTemplate(final TemplateRegistry registry)
		{
			this.registry = registry;
		}

		@Override
		public T read(final SerializationReader reader, T target)
				throws IOException
		{
			if (reader.readNull())
			{
				return null;
			}

			return (T) registry.lookup(target.getClass()).read(reader, target);
		}

		@Override
		public void write(final SerializationWriter writer, final T value)
				throws IOException
		{
			if (value == null)
			{
				writer.writeNull();

				return;
			}

			registry.lookup(value.getClass()).write(writer, value);
		}
	}
}
