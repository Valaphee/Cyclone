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

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("serial")
public class CertificateException
		extends RuntimeException
{
	public CertificateException()
	{}

	public CertificateException(final String message)
	{
		super(message);
	}

	public CertificateException(final Throwable cause)
	{
		super(cause);
	}

	public CertificateException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
