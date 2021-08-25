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

package com.valaphee.cyclone.status.data;

import com.valaphee.cyclone.serialization.annotation.Index;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Default
 *
 * @author valaphee
 */
public final class StatusData
{
	@Index(0)
	public String name;
	@Index(1)
	public String version;
	@Index(2)
	public String shortDescription;
	@Index(3)
	public String description;
	@Index(4)
	public List<String> samples;
	@Index(5)
	public byte[] favicon;
	@Index(6)
	public int playerLimit;
	@Index(7)
	public int playerCount;
	@Index(8)
	public boolean maintenance;

	@Override
	public boolean equals(final Object object)
	{
		if (object == this)
		{
			return true;
		}
		if (object instanceof StatusData)
		{
			final StatusData other = (StatusData) object;

			return Objects.equals(name, other.name) && Objects.equals(version, other.version) && Objects.equals(shortDescription, other.shortDescription) && Objects.equals(description, other.description) && Objects.equals(samples, other.samples) && Arrays.equals(favicon, other.favicon) && (playerLimit == other.playerLimit) && (playerCount == other.playerCount) && (maintenance == other.maintenance);
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 31 * hash + Objects.hashCode(name);
		hash = 31 * hash + Objects.hashCode(version);
		hash = 31 * hash + Objects.hashCode(shortDescription);
		hash = 31 * hash + Objects.hashCode(description);
		hash = 31 * hash + Objects.hashCode(samples);
		hash = 31 * hash + Arrays.hashCode(favicon);
		hash = 31 * hash + playerLimit;
		hash = 31 * hash + playerCount;
		hash = 31 * hash + Boolean.hashCode(maintenance);

		return hash;
	}
}
