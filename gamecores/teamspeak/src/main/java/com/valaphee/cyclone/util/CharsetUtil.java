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

package com.valaphee.cyclone.util;

import java.nio.charset.Charset;

/**
 * Default
 *
 * @author valaphee
 */
public final class CharsetUtil
{
	public static final Charset US_ASCII = Charset.forName("US-ASCII"),
			UTF_8 = Charset.forName("UTF-8"),
			UTF_16 = Charset.forName("UTF-16"),
			UTF_16BE = Charset.forName("UTF-16BE"),
			UTF_16LE = Charset.forName("UTF-16LE");

	private CharsetUtil()
	{}
}
