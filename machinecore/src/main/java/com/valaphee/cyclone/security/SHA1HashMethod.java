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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Default
 *
 * @author valaphee
 */
public final class SHA1HashMethod
		implements HashMethod
{
	@Override
	public byte[] hash(final byte[] data)
	{
		try
		{
			final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
			messageDigest.reset();
			messageDigest.update(data);

			return messageDigest.digest();
		}
		catch (final NoSuchAlgorithmException ex)
		{
			throw new CertificateException("Insufficient support for SHA1.", ex);
		}
	}

	@Override
	public String hashPassword(final String password, final String salt)
	{
		return "$SHA$" + salt + "$" + Base64.getEncoder().encodeToString(hash((Base64.getEncoder().encodeToString(hash(password.getBytes(CharsetUtil.UTF_8))) + salt).getBytes(CharsetUtil.UTF_8)));
	}

	@Override
	public boolean comparePasswords(final String password, final String hashedPassword)
	{
		return hashPassword(password, hashedPassword.split("\\$")[2]).equals(hashedPassword);
	}
}
