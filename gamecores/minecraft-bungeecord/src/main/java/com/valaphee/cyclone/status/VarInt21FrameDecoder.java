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

package com.valaphee.cyclone.status;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public final class VarInt21FrameDecoder
		extends ByteToMessageDecoder
{
	@Override
	protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out)
			throws Exception
	{
		while (in.readableBytes() > 0)
		{
			in.markReaderIndex();
			final byte[] lengthField = new byte[3];
			for (int i = 0; i < lengthField.length; ++i)
			{
				if (!in.isReadable())
				{
					in.resetReaderIndex();

					return;
				}

				lengthField[i] = in.readByte();
				if (lengthField[i] >= 0)
				{
					final int length = readVarInt(lengthField);
					if (length == 0)
					{
						throw new CorruptedFrameException("Packet is empty.");
					}
					if (in.readableBytes() < length)
					{
						in.resetReaderIndex();

						return;
					}

					if (in.hasMemoryAddress())
					{
						out.add(in.slice(in.readerIndex(), length).retain());
					}
					else
					{
						out.add(in.copy(in.readerIndex(), length));
					}
					in.skipBytes(length);

					break;
				}
			}
		}
	}

	private static int readVarInt(final byte[] data)
	{
		int value = 0, i = 0;
		do
		{
			value |= (data[i] & 0x7F) << (i * 7);
		}
		while ((data[(i++)] & 0x80) == 0x80);

		return value;
	}
}
