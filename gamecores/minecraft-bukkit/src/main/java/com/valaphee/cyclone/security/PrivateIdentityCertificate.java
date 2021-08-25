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

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Default
 *
 * @author valaphee
 */
public final class PrivateIdentityCertificate
{
	private final BigInteger modulus;
	private final BigInteger exponent;

	public PrivateIdentityCertificate(final BigInteger modulus, final BigInteger exponent)
	{
		this.modulus = modulus;
		this.exponent = exponent;
	}

	public BigInteger getModulus()
	{
		return modulus;
	}

	public BigInteger getExponent()
	{
		return exponent;
	}

	public byte[] decrypt(final byte[] data)
	{
		try
		{
			final Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateKeySpec(modulus, exponent)));

			return cipher.doFinal(data);
		}
		catch (final NoSuchAlgorithmException | NoSuchPaddingException ex)
		{
			throw new CertificateException("Insufficient support for RSA.", ex);
		}
		catch (final InvalidKeySpecException | InvalidKeyException ex)
		{
			throw new CertificateException("An exception occured while decrypting data.", ex);
		}
		catch (final IllegalBlockSizeException | BadPaddingException ex)
		{
			throw new CertificateException("Data is damaged.", ex);
		}
	}
}
