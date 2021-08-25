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

package com.valaphee.cyclone.security;

import com.valaphee.cyclone.util.CharsetUtil;
import java.util.Base64;

/**
 * Default
 *
 * @author valaphee
 */
public final class DOUBLEMD5HashMethod
		extends MD5HashMethod
{
	@Override
	public String hashPassword(final String password, final String salt)
	{
		return Base64.getEncoder().encodeToString(hash(hash(password.getBytes(CharsetUtil.UTF_8))));
	}
}
