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

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Default
 *
 * @author valaphee
 */
public final class Secret
{
	private static final SecretKeyFactory KEY_FACTORY;
	private final byte[] key;

	public Secret(final byte[] key)
	{
		this.key = key;
	}

	public byte[] getKey()
	{
		return key;
	}

	public Cipher getEncryptCipher()
	{
		try
		{
			final Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(key));

			return cipher;
		}
		catch (final NoSuchAlgorithmException | NoSuchPaddingException ex)
		{
			throw new CertificateException("Insufficient support for AES.", ex);
		}
		catch (final InvalidKeyException | InvalidAlgorithmParameterException ex)
		{
			throw new CertificateException("An exception occured while creating cipher.", ex);
		}
	}

	public byte[] encrypt(final byte[] data)
	{
		try
		{
			return getEncryptCipher().doFinal(data);
		}
		catch (final IllegalBlockSizeException | BadPaddingException ex)
		{
			throw new CertificateException("An exception occured while encrypting data.", ex);
		}
	}

	public Cipher getDecryptCipher()
	{
		try
		{
			final Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(key));

			return cipher;
		}
		catch (final NoSuchAlgorithmException | NoSuchPaddingException ex)
		{
			throw new CertificateException("Insufficient support for AES.", ex);
		}
		catch (final InvalidKeyException | InvalidAlgorithmParameterException ex)
		{
			throw new CertificateException("An exception occured while creating cipher.", ex);
		}
	}

	public byte[] decrypt(final byte[] data)
	{
		try
		{
			return getDecryptCipher().doFinal(data);
		}
		catch (final IllegalBlockSizeException | BadPaddingException ex)
		{
			throw new CertificateException("Data is damaged.", ex);
		}
	}

	public static Secret generate(char[] key, byte[] salt)
	{
		try
		{
			return new Secret(KEY_FACTORY.generateSecret(new PBEKeySpec(key, salt, 65536, 128)).getEncoded());
		}
		catch (final InvalidKeySpecException ex)
		{
			throw new CertificateException("An exception occured while generating secret.", ex);
		}
	}

	static
	{
		try
		{
			KEY_FACTORY = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		}
		catch (final NoSuchAlgorithmException ex)
		{
			throw new CertificateException("Insufficient support for PBKDF2 with Hmac SHA256.", ex);
		}
	}
}
