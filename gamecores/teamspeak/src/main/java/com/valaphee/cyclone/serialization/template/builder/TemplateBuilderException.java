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

import com.valaphee.cyclone.serialization.SerializationException;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("serial")
public class TemplateBuilderException
		extends SerializationException
{
	public TemplateBuilderException()
	{}

	public TemplateBuilderException(final String message)
	{
		super(message);
	}

	public TemplateBuilderException(final Throwable cause)
	{
		super(cause);
	}

	public TemplateBuilderException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
