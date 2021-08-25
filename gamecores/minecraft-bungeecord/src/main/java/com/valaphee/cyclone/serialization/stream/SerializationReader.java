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

import java.io.Closeable;
import java.io.IOException;

/**
 * Default
 *
 * @author valaphee
 */
public interface SerializationReader
		extends Closeable
{
	boolean readNull()
			throws IOException;

	boolean readBoolean()
			throws IOException;

	byte readByte()
			throws IOException;

	short readShort()
			throws IOException;

	int readInt()
			throws IOException;

	long readLong()
			throws IOException;

	float readFloat()
			throws IOException;

	double readDouble()
			throws IOException;

	byte[] readBytes()
			throws IOException;

	int readListBegin()
			throws IOException;

	void readListEnd()
			throws IOException;

	int readMapBegin()
			throws IOException;

	void readMapEnd()
			throws IOException;
}
