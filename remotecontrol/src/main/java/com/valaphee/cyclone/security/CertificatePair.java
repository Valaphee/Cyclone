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
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.UUID;

/**
 * Default
 *
 * @author valaphee
 */
public final class CertificatePair
{
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private static final KeyPairGenerator KEY_PAIR_GENERATOR;
	private static final KeyFactory KEY_FACTORY;
	private static final Signature SIGNER;
	private final PublicIdentityCertificate publicCertificate;
	private final PrivateIdentityCertificate privateCertificate;

	public CertificatePair(final PublicIdentityCertificate publicCertificate, final PrivateIdentityCertificate privateCertificate)
	{
		this.publicCertificate = publicCertificate;
		this.privateCertificate = privateCertificate;
	}

	public PublicIdentityCertificate getPublicCertificate()
	{
		return publicCertificate;
	}

	public PrivateIdentityCertificate getPrivateCertificate()
	{
		return privateCertificate;
	}

	public static CertificatePair generateSignedBy(final PrivateIdentityCertificate signing)
	{
		try
		{
			KEY_PAIR_GENERATOR.initialize(2048);
			final KeyPair pair = KEY_PAIR_GENERATOR.genKeyPair();
			final RSAPublicKeySpec publicSpec = KEY_FACTORY.getKeySpec(pair.getPublic(), RSAPublicKeySpec.class);
			final RSAPrivateKeySpec privateSpec = KEY_FACTORY.getKeySpec(pair.getPrivate(), RSAPrivateKeySpec.class);

			SIGNER.initSign(KEY_FACTORY.generatePrivate(new RSAPublicKeySpec(signing.getModulus(), signing.getExponent())), new SecureRandom());
			SIGNER.update(UUID.randomUUID().toString().getBytes(CHARSET));
			SIGNER.update(publicSpec.getModulus().toByteArray());
			SIGNER.update(publicSpec.getPublicExponent().toByteArray());
			final BigInteger signature = new BigInteger(SIGNER.sign());

			return new CertificatePair(new PublicIdentityCertificate(publicSpec.getModulus(), publicSpec.getPublicExponent(), signature), new PrivateIdentityCertificate(privateSpec.getModulus(), privateSpec.getPrivateExponent()));
		}
		catch (final InvalidKeySpecException | InvalidKeyException | SignatureException ex)
		{
			throw new CertificateException("An exception occured while generating certificate.", ex);
		}
	}

	public static CertificatePair generateSelfSigned()
	{
		try
		{
			KEY_PAIR_GENERATOR.initialize(2048);
			final KeyPair pair = KEY_PAIR_GENERATOR.genKeyPair();
			final RSAPublicKeySpec publicSpec = KEY_FACTORY.getKeySpec(pair.getPublic(), RSAPublicKeySpec.class);
			final RSAPrivateKeySpec privateSpec = KEY_FACTORY.getKeySpec(pair.getPrivate(), RSAPrivateKeySpec.class);

			SIGNER.initSign(pair.getPrivate(), new SecureRandom());
			SIGNER.update(UUID.randomUUID().toString().getBytes(CHARSET));
			SIGNER.update(publicSpec.getModulus().toByteArray());
			SIGNER.update(publicSpec.getPublicExponent().toByteArray());
			final BigInteger signature = new BigInteger(SIGNER.sign());

			return new CertificatePair(new PublicIdentityCertificate(publicSpec.getModulus(), publicSpec.getPublicExponent(), signature), new PrivateIdentityCertificate(privateSpec.getModulus(), privateSpec.getPrivateExponent()));
		}
		catch (final InvalidKeySpecException | InvalidKeyException | SignatureException ex)
		{
			throw new CertificateException("An exception occured while generating certificate.", ex);
		}
	}

	static
	{
		try
		{
			KEY_PAIR_GENERATOR = KeyPairGenerator.getInstance("RSA");
			KEY_FACTORY = KeyFactory.getInstance("RSA");
			SIGNER = Signature.getInstance("SHA256withRSA");
		}
		catch (final NoSuchAlgorithmException ex)
		{
			throw new CertificateException("Insufficient support for SHA256 or RSA.", ex);
		}
	}
}
