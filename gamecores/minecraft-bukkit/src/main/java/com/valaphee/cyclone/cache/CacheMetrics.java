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

package com.valaphee.cyclone.cache;

/**
 * Default
 *
 * @author valaphee
 */
public interface CacheMetrics
{
	long getHits();

	long getMisses();

	default long getRequests()
	{
		return getHits() + getMisses();
	}

	default double getHitRate()
	{
		final long requests = getRequests();

		return (requests == 0) ? 1.0D : getHits() / (double) requests;
	}

	default double getMissRate()
	{
		final long requests = getRequests();

		return (requests == 0) ? 0.0D : getMisses() / (double) requests;
	}
}
