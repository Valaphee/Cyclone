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

package com.valaphee.cyclone.language;

import java.util.Arrays;

/**
 * Default
 *
 * @author valaphee
 */
public final class I18n
{
	private static final I18n INSTANCE = new I18n(new Translation(I18n.class.getResourceAsStream("/en_US.properties")));
	final Translation defaultTranslation;
	Translation translation;

	public static void setTranslation(final Translation translation)
	{
		INSTANCE.translation = translation;
	}

	public static String tl(final String name, final Object... arguments)
	{
		String translated = INSTANCE.translation.format(name, arguments);
		if (translated == null)
		{
			translated = INSTANCE.defaultTranslation.format(name, arguments);
		}

		return translated != null ? translated : name + " " + Arrays.toString(arguments);
	}

	private I18n(final Translation defaultTranslation)
	{
		this.defaultTranslation = defaultTranslation;
		translation = defaultTranslation;
	}
}
