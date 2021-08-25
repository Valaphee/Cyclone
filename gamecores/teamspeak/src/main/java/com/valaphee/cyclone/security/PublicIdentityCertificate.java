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
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Default
 *
 * @author valaphee
 */
public final class PublicIdentityCertificate
{
	private final BigInteger modulus;
	private final BigInteger exponent;
	private final BigInteger signature;

	public PublicIdentityCertificate(final BigInteger modulus, final BigInteger exponent, final BigInteger signature)
	{
		this.modulus = modulus;
		this.exponent = exponent;
		this.signature = signature;
	}

	public BigInteger getModulus()
	{
		return modulus;
	}

	public BigInteger getExponent()
	{
		return exponent;
	}

	public BigInteger getSignature()
	{
		return signature;
	}

	public byte[] encrypt(final byte[] data)
	{
		try
		{
			final Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, exponent)));

			return cipher.doFinal(data);
		}
		catch (final NoSuchAlgorithmException | NoSuchPaddingException ex)
		{
			throw new CertificateException("Insufficient support for RSA.", ex);
		}
		catch (final InvalidKeySpecException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex)
		{
			throw new CertificateException("An exception occured while encrypting data.", ex);
		}
	}

	public boolean verifySignedBy(final PublicIdentityCertificate signing)
	{
		try
		{
			final Signature signer = Signature.getInstance("SHA256withRSA");
			signer.initVerify(KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(signing.modulus, signing.exponent)));
			signer.update(modulus.toByteArray());
			signer.update(exponent.toByteArray());

			return signer.verify(signature.toByteArray());
		}
		catch (final NoSuchAlgorithmException ex)
		{
			throw new CertificateException("Insufficient support for SHA256 or RSA.", ex);
		}
		catch (final InvalidKeySpecException | InvalidKeyException | SignatureException ex)
		{
			return false;
		}
	}

	public boolean verifySelfSigned()
	{
		return verifySignedBy(this);
	}
}
