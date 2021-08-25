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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Default
 *
 * @author valaphee
 */
public class SerializationStreamWriter
		implements SerializationWriter
{
	private final BalanceStack stack = new BalanceStack();
	private final DataOutputStream stream;

	public SerializationStreamWriter(final OutputStream stream)
	{
		this.stream = new DataOutputStream(stream);
	}

	@Override
	public void writeNull()
			throws IOException
	{
		stream.writeByte(SerializationConstants.NULL);
		stack.decrease();
	}

	@Override
	public void writeBoolean(final boolean value)
			throws IOException
	{
		stream.writeByte(value ? SerializationConstants.TRUE : SerializationConstants.FALSE);
		stack.decrease();
	}

	@Override
	public void writeByte(final byte value)
			throws IOException
	{
		if (value < -(1 << 5))
		{
			stream.writeByte(SerializationConstants.BYTE);
			stream.writeByte(value);
		}
		else if (value < (1 << 7))
		{
			stream.writeByte(value);
		}
		else
		{
			stream.writeByte(SerializationConstants.UNSIGNED_BYTE);
			stream.writeByte(value);
		}
		stack.decrease();
	}

	@Override
	public void writeShort(final short value)
			throws IOException
	{
		if (value < -(1 << 5))
		{
			if (value < -(1 << 7))
			{
				stream.writeByte(SerializationConstants.SHORT);
				stream.writeShort(value);
			}
			else
			{
				stream.writeByte(SerializationConstants.BYTE);
				stream.writeByte(value);
			}
		}
		else if (value < (1 << 7))
		{
			stream.writeByte(value);
		}
		else
		{
			if (value < (1 << 8))
			{
				stream.writeByte(SerializationConstants.UNSIGNED_BYTE);
				stream.writeByte(value);
			}
			else
			{
				stream.writeByte(SerializationConstants.UNSIGNED_SHORT);
				stream.writeShort(value);
			}
		}
		stack.decrease();
	}

	@Override
	public void writeInt(final int value)
			throws IOException
	{
		if (value < -(1 << 5))
		{
			if (value < -(1 << 15))
			{
				stream.writeByte(SerializationConstants.INTEGER);
				stream.writeInt(value);
			}
			else if (value < -(1 << 7))
			{
				stream.writeByte(SerializationConstants.SHORT);
				stream.writeShort(value);
			}
			else
			{
				stream.writeByte(SerializationConstants.BYTE);
				stream.writeByte(value);
			}
		}
		else if (value < (1 << 7))
		{
			stream.writeByte(value);
		}
		else
		{
			if (value < (1 << 8))
			{
				stream.writeByte(SerializationConstants.UNSIGNED_BYTE);
				stream.writeByte(value);
			}
			else if (value < (1 << 16))
			{
				stream.writeByte(SerializationConstants.UNSIGNED_SHORT);
				stream.writeShort(value);
			}
			else
			{
				stream.writeByte(SerializationConstants.UNSIGNED_INTEGER);
				stream.writeInt(value);
			}
		}
		stack.decrease();
	}

	@Override
	public void writeLong(final long value)
			throws IOException
	{
		if (value < -(1 << 5))
		{
			if (value < -(1 << 15))
			{
				if (value < -(1 << 31))
				{
					stream.writeByte(SerializationConstants.LONG);
					stream.writeLong(value);
				}
				else
				{
					stream.writeByte(SerializationConstants.INTEGER);
					stream.writeInt((int) value);
				}
			}
			else
			{
				if (value < -(1 << 7))
				{
					stream.writeByte(SerializationConstants.SHORT);
					stream.writeShort((int) value);
				}
				else
				{
					stream.writeByte(SerializationConstants.BYTE);
					stream.writeByte((int) value);
				}
			}
		}
		else if (value < (1 << 7))
		{
			stream.writeByte((int) value);
		}
		else
		{
			if (value < (1 << 16))
			{
				if (value < (1 << 8))
				{
					stream.writeByte(SerializationConstants.UNSIGNED_BYTE);
					stream.writeByte((int) value);
				}
				else
				{
					stream.writeByte(SerializationConstants.UNSIGNED_SHORT);
					stream.writeShort((int) value);
				}
			}
			else
			{
				if (value < (1L << 32))
				{
					stream.writeByte(SerializationConstants.UNSIGNED_INTEGER);
					stream.writeInt((int) value);
				}
				else
				{
					stream.writeByte(SerializationConstants.UNSIGNED_LONG);
					stream.writeLong(value);
				}
			}
		}
		stack.decrease();
	}

	@Override
	public void writeFloat(final float value)
			throws IOException
	{
		stream.writeByte(SerializationConstants.FLOAT);
		stream.writeFloat(value);
		stack.decrease();
	}

	@Override
	public void writeDouble(final double value)
			throws IOException
	{
		if (((float) value) == value)
		{
			stream.writeByte(SerializationConstants.FLOAT);
			stream.writeFloat((float) value);
		}
		else
		{
			stream.writeByte(SerializationConstants.DOUBLE);
			stream.writeDouble(value);
		}
		stack.decrease();
	}

	@Override
	public void writeBytes(final byte[] value)
			throws IOException
	{
		if (value.length < (1 << 5))
		{
			stream.writeByte(SerializationConstants.BYTES | value.length);
		}
		else if (value.length < (1 << 8))
		{
			stream.writeByte(SerializationConstants.BYTES_BYTE);
			stream.writeByte(value.length);
		}
		else if (value.length < (1 << 16))
		{
			stream.writeByte(SerializationConstants.BYTES_SHORT);
			stream.writeShort(value.length);
		}
		else
		{
			stream.writeByte(SerializationConstants.BYTES_INTEGER);
			stream.writeInt(value.length);
		}
		stream.write(value, 0, value.length);
		stack.decrease();
	}

	@Override
	public void writeListBegin(final int size)
			throws IOException
	{
		if (size < (1 << 5))
		{
			stream.writeByte(SerializationConstants.LIST | size);
		}
		else if (size < (1 << 16))
		{
			stream.writeByte(SerializationConstants.LIST_SHORT);
			stream.writeShort(size);
		}
		else
		{
			stream.writeByte(SerializationConstants.LIST_INTEGER);
			stream.writeInt(size);
		}
		stack.pushList(size);
	}

	@Override
	public void writeListEnd()
			throws IOException
	{
		if (!stack.isPointingToList())
		{
			throw new IOException("There is no list more. Too many elements?");
		}

		final int remaining = stack.getRemaining();
		for (int i = remaining; i != 0; --i)
		{
			writeNull();
		}
		stack.pop();
		stack.decrease();
	}

	@Override
	public void writeMapBegin(final int size)
			throws IOException
	{
		if (size < (1 << 5))
		{
			stream.writeByte(SerializationConstants.MAP | size);
		}
		else if (size < (1 << 16))
		{
			stream.writeByte(SerializationConstants.MAP_SHORT);
			stream.writeShort(size);
		}
		else
		{
			stream.writeByte(SerializationConstants.MAP_INTEGER);
			stream.writeInt(size);
		}
		stack.pushMap(size);
	}

	@Override
	public void writeMapEnd()
			throws IOException
	{
		if (!stack.isPointingToMap())
		{
			throw new IOException("There is no map more. Too many elements?");
		}

		final int remaining = stack.getRemaining();
		for (int i = remaining; i != 0; --i)
		{
			writeNull();
		}
		stack.pop();
		stack.decrease();
	}

	@Override
	public void flush()
			throws IOException
	{
		stream.flush();
	}

	@Override
	public void close()
			throws IOException
	{
		stream.close();
	}
}
