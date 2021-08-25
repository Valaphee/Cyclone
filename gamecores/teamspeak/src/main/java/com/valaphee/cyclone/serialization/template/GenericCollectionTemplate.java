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

import com.valaphee.cyclone.reflect.Accessors;
import com.valaphee.cyclone.reflect.ConstructorAccessor;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("rawtypes")
public final class GenericCollectionTemplate
		implements GenericTemplate
{
	private final ConstructorAccessor accessor;

	public GenericCollectionTemplate(final TemplateRegistry registry, final Class<? extends Template> template)
	{
		accessor = Accessors.getConstructorAccessor(template, Template.class);
		accessor.invoke(new AnyTemplate(registry));
	}

	@Override
	public Template build(final Template<Object>[] parameter)
	{
		return (Template) accessor.invoke((Object[]) parameter);
	}
}
