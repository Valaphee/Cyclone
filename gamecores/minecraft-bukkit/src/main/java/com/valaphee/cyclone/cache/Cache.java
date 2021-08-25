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

import java.util.Map;

/**
 * Default
 *
 * @author valaphee
 */
public interface Cache<K, V>
{
	String getName();

	V get(K key);

	Map<K, V> getAll();

	void put(K key, V value);

	void putAll(Map<? extends K, ? extends V> map);

	void invalidate(Object key);

	void invalidateAll(Iterable<?> keys);

	void invalidateAll();

	long size();
}
