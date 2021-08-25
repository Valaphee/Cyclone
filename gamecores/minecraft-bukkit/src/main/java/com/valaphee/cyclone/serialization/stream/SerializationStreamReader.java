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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Default
 *
 * @author valaphee
 */
public class SerializationStreamReader
		implements SerializationReader
{
	private static final int NONE = 0x9F;
	private final BalanceStack stack = new BalanceStack();
	private final DataInputStream stream;
	private int specifier;

	public SerializationStreamReader(final InputStream stream)
	{
		this.stream = new DataInputStream(stream);

		resetSpecifier();
	}

	private void resetSpecifier()
	{
		specifier = NONE;
	}

	private int getSpecifier()
			throws IOException
	{
		if (specifier == NONE)
		{
			specifier = stream.readByte() & 0xFF;
		}

		return specifier;
	}

	@Override
	public boolean readNull()
			throws IOException
	{
		stack.validate();

		final int specifier = getSpecifier();
		if (specifier == SerializationConstants.NULL)
		{
			stack.decrease();
			resetSpecifier();

			return true;
		}

		return false;
	}

	@Override
	public boolean readBoolean()
			throws IOException
	{
		stack.validate();

		final int specifier = getSpecifier();
		if (specifier == SerializationConstants.FALSE)
		{
			stack.decrease();
			resetSpecifier();

			return false;
		}
		else if (specifier == SerializationConstants.TRUE)
		{
			stack.decrease();
			resetSpecifier();

			return true;
		}

		return false;
	}

	@Override
	public byte readByte()
			throws IOException
	{
		stack.validate();

		final int specifier = getSpecifier();
		if ((specifier & SerializationConstants.EXTEND_BIT) == 0x00)
		{
			stack.decrease();
			resetSpecifier();

			return (byte) specifier;
		}
		else if (specifier == SerializationConstants.BYTE)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readByte();
		}
		else if (specifier == SerializationConstants.UNSIGNED_BYTE)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readByte();
		}

		return 0;
	}

	@Override
	public short readShort()
			throws IOException
	{
		stack.validate();

		final int specifier = getSpecifier();
		if ((specifier & SerializationConstants.EXTEND_BIT) == 0x00)
		{
			stack.decrease();
			resetSpecifier();

			return (short) specifier;
		}
		else if (specifier == SerializationConstants.BYTE)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readByte();
		}
		else if (specifier == SerializationConstants.UNSIGNED_BYTE)
		{
			stack.decrease();
			resetSpecifier();

			return (short) (stream.readByte() & 0xFF);
		}
		else if (specifier == SerializationConstants.SHORT)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readShort();
		}
		else if (specifier == SerializationConstants.UNSIGNED_SHORT)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readShort();
		}

		return 0;
	}

	@Override
	public int readInt()
			throws IOException
	{
		stack.validate();

		final int specifier = getSpecifier();
		if ((specifier & SerializationConstants.EXTEND_BIT) == 0x00)
		{
			stack.decrease();
			resetSpecifier();

			return specifier;
		}
		else if (specifier == SerializationConstants.BYTE)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readByte();
		}
		else if (specifier == SerializationConstants.UNSIGNED_BYTE)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readByte() & 0xFF;
		}
		else if (specifier == SerializationConstants.SHORT)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readShort();
		}
		else if (specifier == SerializationConstants.UNSIGNED_SHORT)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readShort() & 0xFFFF;
		}
		else if (specifier == SerializationConstants.INTEGER)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readInt();
		}
		else if (specifier == SerializationConstants.UNSIGNED_INTEGER)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readInt();
		}

		return 0;
	}

	@Override
	public long readLong()
			throws IOException
	{
		stack.validate();

		final int specifier = getSpecifier();
		if ((specifier & SerializationConstants.EXTEND_BIT) == 0x00)
		{
			stack.decrease();
			resetSpecifier();

			return specifier;
		}
		else if (specifier == SerializationConstants.BYTE)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readByte();
		}
		else if (specifier == SerializationConstants.UNSIGNED_BYTE)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readByte() & 0xFFL;
		}
		else if (specifier == SerializationConstants.SHORT)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readShort();
		}
		else if (specifier == SerializationConstants.UNSIGNED_SHORT)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readShort() & 0xFFFFL;
		}
		else if (specifier == SerializationConstants.INTEGER)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readInt();
		}
		else if (specifier == SerializationConstants.UNSIGNED_INTEGER)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readInt() & 0xFFFFFFFFL;
		}
		else if (specifier == SerializationConstants.LONG)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readLong();
		}
		else if (specifier == SerializationConstants.UNSIGNED_LONG)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readLong();
		}

		return 0;
	}

	@Override
	public float readFloat()
			throws IOException
	{
		stack.validate();

		final int specifier = getSpecifier();
		if (specifier == SerializationConstants.FLOAT)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readFloat();
		}

		return Float.NaN;
	}

	@Override
	public double readDouble()
			throws IOException
	{
		stack.validate();

		final int specifier = getSpecifier();
		if (specifier == SerializationConstants.FLOAT)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readFloat();
		}
		else if (specifier == SerializationConstants.DOUBLE)
		{
			stack.decrease();
			resetSpecifier();

			return stream.readDouble();
		}

		return Double.NaN;
	}

	@Override
	public byte[] readBytes()
			throws IOException
	{
		stack.validate();

		final int specifier = getSpecifier();
		if ((specifier & SerializationConstants.MULTI_MASK) == SerializationConstants.BYTES)
		{
			final int count = specifier & SerializationConstants.MULTI_DATA_MASK;
			if (count == 0)
			{
				return new byte[0];
			}

			final byte[] raw = new byte[count];
			stream.read(raw, 0, count);

			stack.decrease();
			resetSpecifier();

			return raw;
		}
		else if (specifier == SerializationConstants.BYTES_BYTE)
		{
			final int count = stream.readByte() & 0xFF;
			final byte[] raw = new byte[count];
			if (count != 0)
			{
				stream.read(raw, 0, count);
			}

			stack.decrease();
			resetSpecifier();

			return raw;
		}
		else if (specifier == SerializationConstants.BYTES_SHORT)
		{
			final int count = stream.readShort() & 0xFFFF;
			final byte[] raw = new byte[count];
			if (count != 0)
			{
				stream.read(raw, 0, count);
			}

			stack.decrease();
			resetSpecifier();

			return raw;
		}
		else if (specifier == SerializationConstants.BYTES_INTEGER)
		{
			final int count = stream.readInt();
			final byte[] raw = new byte[count];
			if (count != 0)
			{
				stream.read(raw, 0, count);
			}

			stack.decrease();
			resetSpecifier();

			return raw;
		}

		return null;
	}

	@Override
	public int readListBegin()
			throws IOException
	{
		final int specifier = getSpecifier();
		if ((specifier & SerializationConstants.MULTI_MASK) == SerializationConstants.LIST)
		{
			final int size = specifier & SerializationConstants.MULTI_DATA_MASK;

			stack.pushList(size);
			resetSpecifier();

			return size;
		}
		else if (specifier == SerializationConstants.LIST_SHORT)
		{
			final int size = stream.readShort() & 0xFFFF;

			stack.pushList(size);
			resetSpecifier();

			return size;
		}
		else if (specifier == SerializationConstants.LIST_INTEGER)
		{
			final int size = stream.readInt();

			stack.pushList(size);
			resetSpecifier();

			return size;
		}

		return 0;
	}

	@Override
	public void readListEnd()
			throws IOException
	{
		if (!stack.isPointingToList())
		{
			throw new IOException("There is no list more. Too many elements?");
		}

		final int remaining = stack.getRemaining();
		for (int i = remaining; i != 0; --i)
		{
			readNull();
		}
		stack.pop();
		stack.decrease();
	}

	@Override
	public int readMapBegin()
			throws IOException
	{
		final int specifier = getSpecifier();
		if ((specifier & SerializationConstants.MULTI_MASK) == SerializationConstants.MAP)
		{
			final int size = specifier & SerializationConstants.MULTI_DATA_MASK;

			stack.pushMap(size);
			resetSpecifier();

			return size;
		}
		else if (specifier == SerializationConstants.MAP_SHORT)
		{
			final int size = stream.readShort() & 0xFFFF;

			stack.pushMap(size);
			resetSpecifier();

			return size;
		}
		else if (specifier == SerializationConstants.MAP_INTEGER)
		{
			final int size = stream.readInt();

			stack.pushMap(size);
			resetSpecifier();

			return size;
		}

		return 0;
	}

	@Override
	public void readMapEnd()
			throws IOException
	{
		if (!stack.isPointingToMap())
		{
			throw new IOException("There is no map more. Too many elements?");
		}

		final int remaining = stack.getRemaining();
		for (int i = remaining; i != 0; --i)
		{
			readNull();
		}
		stack.pop();
		stack.decrease();
	}

	@Override
	public void close()
			throws IOException
	{
		stream.close();
	}
}
