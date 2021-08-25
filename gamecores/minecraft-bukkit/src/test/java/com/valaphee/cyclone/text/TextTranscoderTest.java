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

package com.valaphee.cyclone.text;

import com.valaphee.cyclone.reflect.PrettyPrinter;
import junit.framework.TestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Default
 *
 * @author valaphee
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TextTranscoderTest
		extends TestCase
{
	private final String simpleText = "`4l`This is red and bold`l` and this just red.`4`";
	private final String complexText = "`4`This is red `6`and this yellow`l6` and this is bold and red.`4l`";
	private TextTranscoder[] transcoders;
	private String[] transcodedSimpleText;
	private String[] transcodedComplexText;

	@Override
	public void setUp()
	{
		transcoders = new TextTranscoder[]
		{
			new RawTextTranscoder(),
			new MinecraftLegacyTextTranscoder(),
			new MinecraftJSONTextTranscoder()
		};
		transcodedSimpleText = new String[]
		{
			"This is red and bold and this just red.",
			"\u00A74\u00A7lThis is red and bold\u00A74 and this just red.\u00A7r",
			null
		};
		transcodedComplexText = new String[]
		{
			"This is red and this yellow and this is bold and red.",
			"\u00A74This is red \u00A76and this yellow\u00A74§l and this is bold and red.\u00A7r",
			null
		};
	}

	@Override
	public void tearDown()
	{}

	@Test
	public void testStage1SimpleTextTranscode()
	{
		for (int i = 0; i < transcoders.length; ++i)
		{
			if (transcodedSimpleText[i] != null)
			{
				assertEquals(transcodedSimpleText[i], transcoders[i].transcode(simpleText));
			}
			else
			{
				try
				{
					System.out.println(PrettyPrinter.print(transcoders[i].transcode(simpleText)));
				}
				catch (final IllegalAccessException ignore)
				{}
			}
		}
	}

	@Test
	public void testStage2ComplexTextTranscode()
	{
		for (int i = 0; i < transcoders.length; ++i)
		{
			if (transcodedComplexText[i] != null)
			{
				assertEquals(transcodedComplexText[i], transcoders[i].transcode(complexText));
			}
			else
			{
				try
				{
					System.out.println(PrettyPrinter.print(transcoders[i].transcode(simpleText)));
				}
				catch (final IllegalAccessException ignore)
				{}
			}
		}
	}
}
