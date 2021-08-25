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
import java.io.Flushable;
import java.io.IOException;

/**
 * Default
 *
 * @author valaphee
 */
public interface SerializationWriter
		extends Flushable, Closeable
{
	void writeNull()
			throws IOException;

	void writeBoolean(boolean value)
			throws IOException;

	void writeByte(byte value)
			throws IOException;

	void writeShort(short value)
			throws IOException;

	void writeInt(int value)
			throws IOException;

	void writeLong(long value)
			throws IOException;

	void writeFloat(float value)
			throws IOException;

	void writeDouble(double value)
			throws IOException;

	void writeBytes(byte[] value)
			throws IOException;

	void writeListBegin(int size)
			throws IOException;

	void writeListEnd()
			throws IOException;

	void writeMapBegin(int size)
			throws IOException;

	void writeMapEnd()
			throws IOException;
}
