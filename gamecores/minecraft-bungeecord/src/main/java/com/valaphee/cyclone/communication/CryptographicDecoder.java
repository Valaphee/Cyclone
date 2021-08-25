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

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import javax.crypto.Cipher;

/**
 * Default
 *
 * @author valaphee
 */
public final class CryptographicDecoder
		extends ByteToMessageDecoder
{
	private final Cipher cipher;
	private byte[] inputBuffer = new byte[4096];

	public CryptographicDecoder(final Cipher cipher)
	{
		this.cipher = cipher;
	}

	@Override
	protected synchronized void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out)
			throws Exception
	{
		final int inputSize = in.readableBytes();
		if (inputBuffer.length < inputSize)
		{
			inputBuffer = new byte[inputSize];
		}
		in.readBytes(inputBuffer, 0, inputSize);

		final ByteBuf outputBuffer = ctx.alloc().heapBuffer(cipher.getOutputSize(inputSize));
		outputBuffer.writerIndex(cipher.update(inputBuffer, 0, inputSize, outputBuffer.array(), outputBuffer.arrayOffset()));
		out.add(outputBuffer);
	}
}
