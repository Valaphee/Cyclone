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

package com.valaphee.cyclone.engine.subsystem;

import com.valaphee.cyclone.config.Config;
import com.valaphee.cyclone.context.Context;
import com.valaphee.cyclone.engine.EngineSubsystem;
import com.valaphee.cyclone.language.I18n;
import com.valaphee.cyclone.language.Translation;

/**
 * Default
 *
 * @author valaphee
 */
public class I18nSubsystem
		implements EngineSubsystem
{
	private I18n i18n;

	@Override
	public String getName()
	{
		return "I18n";
	}

	@Override
	public void preStartup(final Context context)
	{
		I18n.setTranslation(new Translation(getClass().getResourceAsStream("/" + context.get(Config.class).getMain().getLocale() + ".properties")));
	}
}
