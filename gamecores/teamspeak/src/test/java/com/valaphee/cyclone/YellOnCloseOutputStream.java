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

package com.valaphee.cyclone;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import junit.framework.AssertionFailedError;

/**
 * Default
 *
 * @author valaphee
 */
public class YellOnCloseOutputStream
		extends FilterOutputStream
{
	public YellOnCloseOutputStream(final OutputStream stream)
	{
		super(stream);
	}

	@Override
	public void close()
			throws IOException
	{
		throw new AssertionFailedError("close() was called on the output stream.");
	}
}
