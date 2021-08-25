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

package com.valaphee.cyclone.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Default
 *
 * @author valaphee
 */
public final class ScoreCriteriaContainer
		implements Cloneable
{
	public static final Map<String, ScoreCriteria> REGISTRY = new HashMap<>();
	private final Map<ScoreCriteria, Weight> criterias = new HashMap<>();

	public ScoreCriteriaContainer()
	{}

	public ScoreCriteriaContainer(final Map<ScoreCriteria, Weight> criterias)
	{
		this.criterias.putAll(criterias);
	}

	public Map<ScoreCriteria, Weight> getCriterias()
	{
		return Collections.unmodifiableMap(criterias);
	}

	public Weight getCriteria(final ScoreCriteria criteria)
	{
		return criterias.get(criteria);
	}

	public void addCriteria(final ScoreCriteria criteria, final double prescaler, final double weight)
	{
		if (criterias.containsKey(criteria))
		{
			criterias.remove(criteria);
		}
		criterias.put(criteria, new Weight(prescaler, weight));
	}

	public boolean removeCriteria(final ScoreCriteria criteria)
	{
		if (criterias.containsKey(criteria))
		{
			criterias.remove(criteria);

			return true;
		}

		return false;
	}

	public int compute()
	{
		return criterias.entrySet().stream().map((pair) -> (int) Math.abs((pair.getKey().value() * pair.getValue().prescaler) + pair.getValue().remainder)).reduce(0, Integer::sum);
	}

	@Override
	public ScoreCriteriaContainer clone()
	{
		return new ScoreCriteriaContainer(criterias);
	}

	static
	{
		REGISTRY.put("processorUsage", ProcessorUsageScoreCriteria.getInstance());
		REGISTRY.put("memoryUsage", MemoryUsageScoreCriteria.getInstance());
	}

	public static final class Weight
	{
		final double prescaler;
		final double remainder;

		private Weight(final double prescaler, final double remainder)
		{
			this.prescaler = prescaler;
			this.remainder = remainder;
		}

		public double getPrescaler()
		{
			return prescaler;
		}

		public double getRemainder()
		{
			return remainder;
		}
	}
}
