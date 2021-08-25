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

import java.io.*;

/**
 * Default
 *
 * @author valaphee
 */
public final class FileUtil
{
	public static void recursiveCopy(final File source, final File destination)
			throws IOException
	{
		try
		{
			if (source.exists())
			{
				if (source.isDirectory())
				{
					if (!destination.exists())
					{
						destination.mkdir();
					}

					final String[] files = source.list();
					for (final String file : files)
					{
						recursiveCopy(new File(source, file), new File(destination, file));
					}
				}
				else
				{
					destination.createNewFile();
					try (final InputStream sourceStream = new FileInputStream(source); final OutputStream destinationStream = new FileOutputStream(destination))
					{
						final byte[] buffer = new byte[4096];
						int length;
						while ((length = sourceStream.read(buffer)) > 0)
						{
							destinationStream.write(buffer, 0, length);
						}
					}
				}
			}
		}
		catch (final IOException ex)
		{
			throw new IOException("File \"" + source.getPath() + "\" cannot be copied to \"" + destination.getPath() + "\".");
		}
	}

	public static boolean recursiveDelete(final File source)
			throws IOException
	{
		try
		{
			if (!source.exists())
			{
				return false;
			}
			if (source.isDirectory())
			{
				final String[] files = source.list();
				for (final String file : files)
				{
					if (!recursiveDelete(new File(source, file)))
					{
						return false;
					}
				}
			}
		}
		catch (final IOException ex)
		{
			throw new IOException("File \"" + source.getPath() + "\" cannot be deleted.");
		}

		return source.delete();
	}

	private FileUtil()
	{}
}
