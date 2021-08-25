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

package com.valaphee.cyclone.serialization.stream;

/**
 * Default
 *
 * @author valaphee
 */
public final class SerializationConstants
{
	public static final int EXTEND_BIT = 1 << 7,
			NULL = 0x80,
			BYTE = 0x81,
			UNSIGNED_BYTE = 0x82,
			SHORT = 0x83,
			UNSIGNED_SHORT = 0x84,
			INTEGER = 0x85,
			UNSIGNED_INTEGER = 0x86,
			LONG = 0x87,
			UNSIGNED_LONG = 0x88,
			FLOAT = 0x89,
			DOUBLE = 0x8A,
			FALSE = 0x8B,
			TRUE = 0x8C,
			BYTES_BYTE = 0x8D,
			BYTES_SHORT = 0x8E,
			BYTES_INTEGER = 0x8F,
			LIST_SHORT = 0x90,
			LIST_INTEGER = 0x91,
			MAP_SHORT = 0x92,
			MAP_INTEGER = 0x93,
			MULTI_MASK = EXTEND_BIT | 0x07 << 5,
			MULTI_DATA_MASK = 0x1F,
			BYTES = EXTEND_BIT | 0x05 << 5,
			LIST = EXTEND_BIT | 0x06 << 5,
			MAP = EXTEND_BIT | 0x07 << 5;

	private SerializationConstants()
	{}
}
