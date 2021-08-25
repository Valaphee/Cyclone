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

package com.valaphee.cyclone.communication;

import com.valaphee.cyclone.util.CharsetUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Default
 *
 * @author valaphee
 */
public final class PacketBuffer
		extends ByteBuf
{
	private final ByteBuf buffer;

	public PacketBuffer(final ByteBuf buffer)
	{
		this.buffer = buffer;
	}

	@Override
	public boolean hasMemoryAddress()
	{
		return buffer.hasMemoryAddress();
	}

	@Override
	public long memoryAddress()
	{
		return buffer.memoryAddress();
	}

	@Override
	public int capacity()
	{
		return buffer.capacity();
	}

	@Override
	public ByteBuf capacity(final int newCapacity)
	{
		buffer.capacity(newCapacity);

		return this;
	}

	@Override
	public int maxCapacity()
	{
		return buffer.maxCapacity();
	}

	@Override
	public ByteBufAllocator alloc()
	{
		return buffer.alloc();
	}

	@Override
	public ByteOrder order()
	{
		return buffer.order();
	}

	@Override
	public ByteBuf order(final ByteOrder endianness)
	{
		return buffer.order(endianness);
	}

	@Override
	public ByteBuf unwrap()
	{
		return buffer;
	}

	@Override
	public boolean isDirect()
	{
		return buffer.isDirect();
	}

	@Override
	public int readerIndex()
	{
		return buffer.readerIndex();
	}

	@Override
	public ByteBuf readerIndex(final int readerIndex)
	{
		buffer.readerIndex(readerIndex);

		return this;
	}

	@Override
	public int writerIndex()
	{
		return buffer.writerIndex();
	}

	@Override
	public ByteBuf writerIndex(final int writerIndex)
	{
		buffer.writerIndex(writerIndex);

		return this;
	}

	@Override
	public ByteBuf setIndex(final int readerIndex, final int writerIndex)
	{
		buffer.setIndex(readerIndex, writerIndex);

		return this;
	}

	@Override
	public int readableBytes()
	{
		return buffer.readableBytes();
	}

	@Override
	public int writableBytes()
	{
		return buffer.writableBytes();
	}

	@Override
	public int maxWritableBytes()
	{
		return buffer.maxWritableBytes();
	}

	@Override
	public boolean isReadable()
	{
		return buffer.isReadable();
	}

	@Override
	public boolean isWritable()
	{
		return buffer.isWritable();
	}

	@Override
	public ByteBuf clear()
	{
		buffer.clear();

		return this;
	}

	@Override
	public ByteBuf markReaderIndex()
	{
		buffer.markReaderIndex();

		return this;
	}

	@Override
	public ByteBuf resetReaderIndex()
	{
		buffer.resetReaderIndex();

		return this;
	}

	@Override
	public ByteBuf markWriterIndex()
	{
		buffer.markWriterIndex();

		return this;
	}

	@Override
	public ByteBuf resetWriterIndex()
	{
		buffer.resetWriterIndex();

		return this;
	}

	@Override
	public ByteBuf discardReadBytes()
	{
		buffer.discardReadBytes();

		return this;
	}

	@Override
	public ByteBuf discardSomeReadBytes()
	{
		buffer.discardSomeReadBytes();

		return this;
	}

	@Override
	public ByteBuf ensureWritable(final int minWritableBytes)
	{
		buffer.ensureWritable(minWritableBytes);

		return this;
	}

	@Override
	public int ensureWritable(final int minWritableBytes, final boolean force)
	{
		return buffer.ensureWritable(minWritableBytes, force);
	}

	@Override
	public boolean getBoolean(final int index)
	{
		return buffer.getBoolean(index);
	}

	@Override
	public byte getByte(final int index)
	{
		return buffer.getByte(index);
	}

	@Override
	public short getUnsignedByte(final int index)
	{
		return buffer.getUnsignedByte(index);
	}

	@Override
	public short getShort(final int index)
	{
		return buffer.getShort(index);
	}

	@Override
	public int getUnsignedShort(final int index)
	{
		return buffer.getUnsignedShort(index);
	}

	@Override
	public int getMedium(final int index)
	{
		return buffer.getMedium(index);
	}

	@Override
	public int getUnsignedMedium(final int index)
	{
		return buffer.getUnsignedMedium(index);
	}

	@Override
	public int getInt(final int index)
	{
		return buffer.getInt(index);
	}

	@Override
	public long getUnsignedInt(final int index)
	{
		return buffer.getUnsignedInt(index);
	}

	@Override
	public long getLong(final int index)
	{
		return buffer.getLong(index);
	}

	@Override
	public char getChar(final int index)
	{
		return buffer.getChar(index);
	}

	@Override
	public float getFloat(final int index)
	{
		return buffer.getFloat(index);
	}

	@Override
	public double getDouble(final int index)
	{
		return buffer.getDouble(index);
	}

	@Override
	public ByteBuf getBytes(final int index, final ByteBuf dst)
	{
		buffer.getBytes(index, dst);

		return this;
	}

	@Override
	public ByteBuf getBytes(final int index, final ByteBuf dst, final int length)
	{
		buffer.getBytes(index, dst, length);

		return this;
	}

	@Override
	public ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length)
	{
		buffer.getBytes(index, dst, dstIndex, length);

		return this;
	}

	@Override
	public ByteBuf getBytes(final int index, final byte[] dst)
	{
		buffer.getBytes(index, dst);

		return this;
	}

	@Override
	public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length)
	{
		buffer.getBytes(index, dst, dstIndex, length);

		return this;
	}

	@Override
	public ByteBuf getBytes(final int index, final ByteBuffer dst)
	{
		buffer.getBytes(index, dst);

		return this;
	}

	@Override
	public ByteBuf getBytes(final int index, final OutputStream out, final int length)
			throws IOException
	{
		buffer.getBytes(index, out, length);

		return this;
	}

	@Override
	public int getBytes(final int index, final GatheringByteChannel out, final int length)
			throws IOException
	{
		return buffer.getBytes(index, out, length);
	}

	@Override
	public ByteBuf setBoolean(final int index, final boolean value)
	{
		buffer.setBoolean(index, value);

		return this;
	}

	@Override
	public ByteBuf setByte(final int index, final int value)
	{
		buffer.setByte(index, value);

		return this;
	}

	@Override
	public ByteBuf setShort(final int index, final int value)
	{
		buffer.setShort(index, value);

		return this;
	}

	@Override
	public ByteBuf setMedium(final int index, final int value)
	{
		buffer.setMedium(index, value);

		return this;
	}

	@Override
	public ByteBuf setInt(final int index, final int value)
	{
		buffer.setInt(index, value);

		return this;
	}

	@Override
	public ByteBuf setLong(final int index, final long value)
	{
		buffer.setLong(index, value);

		return this;
	}

	@Override
	public ByteBuf setChar(final int index, final int value)
	{
		buffer.setChar(index, value);

		return this;
	}

	@Override
	public ByteBuf setFloat(final int index, final float value)
	{
		buffer.setFloat(index, value);

		return this;
	}

	@Override
	public ByteBuf setDouble(final int index, final double value)
	{
		buffer.setDouble(index, value);

		return this;
	}

	@Override
	public ByteBuf setBytes(final int index, final ByteBuf src)
	{
		buffer.setBytes(index, src);

		return this;
	}

	@Override
	public ByteBuf setBytes(final int index, final ByteBuf src, final int length)
	{
		buffer.setBytes(index, src, length);

		return this;
	}

	@Override
	public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length)
	{
		buffer.setBytes(index, src, srcIndex, length);

		return this;
	}

	@Override
	public ByteBuf setBytes(final int index, final byte[] src)
	{
		buffer.setBytes(index, src);

		return this;
	}

	@Override
	public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length)
	{
		buffer.setBytes(index, src, srcIndex, length);

		return this;
	}

	@Override
	public ByteBuf setBytes(final int index, final ByteBuffer src)
	{
		buffer.setBytes(index, src);

		return this;
	}

	@Override
	public int setBytes(final int index, final InputStream in, final int length)
			throws IOException
	{
		return buffer.setBytes(index, in, length);
	}

	@Override
	public int setBytes(final int index, final ScatteringByteChannel in, final int length)
			throws IOException
	{
		return buffer.setBytes(index, in, length);
	}

	@Override
	public ByteBuf setZero(final int index, final int length)
	{
		buffer.setZero(index, length);

		return this;
	}

	@Override
	public boolean readBoolean()
	{
		return buffer.readBoolean();
	}

	@Override
	public byte readByte()
	{
		return buffer.readByte();
	}

	@Override
	public short readUnsignedByte()
	{
		return buffer.readUnsignedByte();
	}

	@Override
	public short readShort()
	{
		return buffer.readShort();
	}

	@Override
	public int readUnsignedShort()
	{
		return buffer.readUnsignedShort();
	}

	@Override
	public int readMedium()
	{
		return buffer.readMedium();
	}

	@Override
	public int readUnsignedMedium()
	{
		return buffer.readUnsignedMedium();
	}

	@Override
	public int readInt()
	{
		return buffer.readInt();
	}

	@Override
	public long readUnsignedInt()
	{
		return buffer.readUnsignedInt();
	}

	@Override
	public long readLong()
	{
		return buffer.readLong();
	}

	@Override
	public char readChar()
	{
		return buffer.readChar();
	}

	@Override
	public float readFloat()
	{
		return buffer.readFloat();
	}

	@Override
	public double readDouble()
	{
		return buffer.readDouble();
	}

	@Override
	public ByteBuf readBytes(final int length)
	{
		return buffer.readBytes(length);
	}

	@Override
	public ByteBuf readSlice(final int length)
	{
		return buffer.readSlice(length);
	}

	@Override
	public ByteBuf readBytes(final ByteBuf dst)
	{
		buffer.readBytes(dst);

		return this;
	}

	@Override
	public ByteBuf readBytes(final ByteBuf dst, final int length)
	{
		buffer.readBytes(dst, length);

		return this;
	}

	@Override
	public ByteBuf readBytes(final ByteBuf dst, final int dstIndex, final int length)
	{
		buffer.readBytes(dst, dstIndex, length);

		return this;
	}

	@Override
	public ByteBuf readBytes(final byte[] dst)
	{
		buffer.readBytes(dst);

		return this;
	}

	@Override
	public ByteBuf readBytes(final byte[] dst, final int dstIndex, final int length)
	{
		buffer.readBytes(dst, dstIndex, length);

		return this;
	}

	@Override
	public ByteBuf readBytes(final ByteBuffer dst)
	{
		buffer.readBytes(dst);

		return this;
	}

	@Override
	public ByteBuf readBytes(final OutputStream out, final int length)
			throws IOException
	{
		buffer.readBytes(out, length);

		return this;
	}

	@Override
	public int readBytes(final GatheringByteChannel out, final int length)
			throws IOException
	{
		return buffer.readBytes(out, length);
	}

	@Override
	public ByteBuf skipBytes(final int length)
	{
		buffer.skipBytes(length);

		return this;
	}

	@Override
	public ByteBuf writeBoolean(final boolean value)
	{
		buffer.writeBoolean(value);

		return this;
	}

	@Override
	public ByteBuf writeByte(final int value)
	{
		buffer.writeByte(value);

		return this;
	}

	@Override
	public ByteBuf writeShort(final int value)
	{
		buffer.writeShort(value);

		return this;
	}

	@Override
	public ByteBuf writeMedium(final int value)
	{
		buffer.writeMedium(value);

		return this;
	}

	@Override
	public ByteBuf writeInt(final int value)
	{
		buffer.writeInt(value);

		return this;
	}

	@Override
	public ByteBuf writeLong(final long value)
	{
		buffer.writeLong(value);

		return this;
	}

	@Override
	public ByteBuf writeChar(final int value)
	{
		buffer.writeChar(value);

		return this;
	}

	@Override
	public ByteBuf writeFloat(final float value)
	{
		buffer.writeFloat(value);

		return this;
	}

	@Override
	public ByteBuf writeDouble(final double value)
	{
		buffer.writeDouble(value);

		return this;
	}

	@Override
	public ByteBuf writeBytes(final ByteBuf src)
	{
		buffer.writeBytes(src);

		return this;
	}

	@Override
	public ByteBuf writeBytes(final ByteBuf src, final int length)
	{
		buffer.writeBytes(src, length);

		return this;
	}

	@Override
	public ByteBuf writeBytes(final ByteBuf src, final int srcIndex, final int length)
	{
		buffer.writeBytes(src, srcIndex, length);

		return this;
	}

	@Override
	public ByteBuf writeBytes(final byte[] src)
	{
		buffer.writeBytes(src);

		return this;
	}

	@Override
	public ByteBuf writeBytes(final byte[] src, final int srcIndex, final int length)
	{
		buffer.writeBytes(src, srcIndex, length);

		return this;
	}

	@Override
	public ByteBuf writeBytes(final ByteBuffer src)
	{
		buffer.writeBytes(src);

		return this;
	}

	@Override
	public int writeBytes(final InputStream in, final int length)
			throws IOException
	{
		return buffer.writeBytes(in, length);
	}

	@Override
	public int writeBytes(final ScatteringByteChannel in, final int length)
			throws IOException
	{
		return buffer.writeBytes(in, length);
	}

	@Override
	public ByteBuf writeZero(final int length)
	{
		buffer.writeZero(length);

		return this;
	}

	@Override
	public int indexOf(final int fromIndex, final int toIndex, final byte value)
	{
		return buffer.indexOf(fromIndex, toIndex, value);
	}

	@Override
	public int bytesBefore(final byte value)
	{
		return buffer.bytesBefore(value);
	}

	@Override
	public int bytesBefore(final int length, final byte value)
	{
		return buffer.bytesBefore(length, value);
	}

	@Override
	public int bytesBefore(final int index, final int length, final byte value)
	{
		return buffer.bytesBefore(index, length, value);
	}

	@Override
	public int forEachByte(final ByteBufProcessor processor)
	{
		return buffer.forEachByte(processor);
	}

	@Override
	public int forEachByte(final int index, final int length, final ByteBufProcessor processor)
	{
		return buffer.forEachByte(index, length, processor);
	}

	@Override
	public int forEachByteDesc(final ByteBufProcessor processor)
	{
		return buffer.forEachByteDesc(processor);
	}

	@Override
	public int forEachByteDesc(final int index, final int length, final ByteBufProcessor processor)
	{
		return buffer.forEachByteDesc(index, length, processor);
	}

	@Override
	public ByteBuf copy()
	{
		return buffer.copy();
	}

	@Override
	public ByteBuf copy(final int index, final int length)
	{
		return buffer.copy(index, length);
	}

	@Override
	public ByteBuf slice()
	{
		return buffer.slice();
	}

	@Override
	public ByteBuf slice(final int index, final int length)
	{
		return buffer.slice(index, length);
	}

	@Override
	public ByteBuf duplicate()
	{
		return buffer.duplicate();
	}

	@Override
	public int nioBufferCount()
	{
		return buffer.nioBufferCount();
	}

	@Override
	public ByteBuffer nioBuffer()
	{
		return buffer.nioBuffer();
	}

	@Override
	public ByteBuffer nioBuffer(final int index, final int length)
	{
		return buffer.nioBuffer(index, length);
	}

	@Override
	public ByteBuffer[] nioBuffers()
	{
		return buffer.nioBuffers();
	}

	@Override
	public ByteBuffer[] nioBuffers(final int index, final int length)
	{
		return buffer.nioBuffers(index, length);
	}

	@Override
	public ByteBuffer internalNioBuffer(final int index, final int length)
	{
		return buffer.internalNioBuffer(index, length);
	}

	@Override
	public boolean hasArray()
	{
		return buffer.hasArray();
	}

	@Override
	public byte[] array()
	{
		return buffer.array();
	}

	@Override
	public int arrayOffset()
	{
		return buffer.arrayOffset();
	}

	@Override
	public String toString(final Charset charset)
	{
		return buffer.toString(charset);
	}

	@Override
	public String toString(final int index, final int length, final Charset charset)
	{
		return buffer.toString(index, length, charset);
	}

	@Override
	public int hashCode()
	{
		return buffer.hashCode();
	}

	@Override
	public boolean equals(final Object obj)
	{
		return buffer.equals(obj);
	}

	@Override
	public int compareTo(final ByteBuf buffer)
	{
		return buffer.compareTo(buffer);
	}

	@Override
	public String toString()
	{
		return StringUtil.simpleClassName(this) + '(' + buffer.toString() + ')';
	}

	@Override
	public ByteBuf retain(final int increment)
	{
		buffer.retain(increment);

		return this;
	}

	@Override
	public ByteBuf retain()
	{
		buffer.retain();
		return this;
	}

	@Override
	public boolean isReadable(final int size)
	{
		return buffer.isReadable(size);
	}

	@Override
	public boolean isWritable(final int size)
	{
		return buffer.isWritable(size);
	}

	@Override
	public int refCnt()
	{
		return buffer.refCnt();
	}

	@Override
	public boolean release()
	{
		return buffer.release();
	}

	@Override
	public boolean release(final int decrement)
	{
		return buffer.release(decrement);
	}

	public UUID readGUID()
	{
		return new UUID(readLong(), readLong());
	}

	public void writeGUID(final UUID value)
	{
		writeLong(value.getMostSignificantBits());
		writeLong(value.getLeastSignificantBits());
	}

	public String readString()
	{
		final byte[] bytes = new byte[readUnsignedShort()];
		readBytes(bytes);

		return new String(bytes, CharsetUtil.UTF_8);
	}

	public void writeString(final String value)
	{
		final byte[] bytes = value.getBytes(CharsetUtil.UTF_8);
		writeShort(bytes.length);
		writeBytes(bytes);
	}
}
