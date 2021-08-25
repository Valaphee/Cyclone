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
import io.netty.handler.codec.MessageToByteEncoder;
import javax.crypto.Cipher;

/**
 * Default
 *
 * @author valaphee
 */
public final class CryptographicEncoder
		extends MessageToByteEncoder<ByteBuf>
{
	private final Cipher cipher;
	private byte[] inputBuffer = new byte[4096];
	private byte[] outputBuffer = new byte[4096];

	public CryptographicEncoder(final Cipher cipher)
	{
		this.cipher = cipher;
	}

	@Override
	protected synchronized void encode(final ChannelHandlerContext ctx, final ByteBuf msg, final ByteBuf out)
			throws Exception
	{
		final int inputSize = msg.readableBytes();
		if (inputBuffer.length < inputSize)
		{
			inputBuffer = new byte[inputSize];
		}
		msg.readBytes(inputBuffer, 0, inputSize);

		final int outputSize = cipher.getOutputSize(inputSize);
		if (outputBuffer.length < outputSize)
		{
			outputBuffer = new byte[outputSize];
		}
		out.writeBytes(outputBuffer, 0, cipher.update(inputBuffer, 0, inputSize, outputBuffer));
	}
}
