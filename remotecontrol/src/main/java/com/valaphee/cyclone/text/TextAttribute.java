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

/**
 * Default
 *
 * @author valaphee
 */
public enum TextAttribute
{
	BLACK('0', true),
	BLUE('1', true),
	GREEN('2', true),
	CYAN('3', true),
	RED('4', true),
	MAGENTA('5', true),
	BROWN('6', true),
	LIGHT_GRAY('7', true),
	DARK_GRAY('8', true),
	LIGHT_BLUE('9', true),
	LIGHT_GREEN('a', true),
	LIGHT_CYAN('b', true),
	LIGHT_RED('c', true),
	LIGHT_MAGENTA('d', true),
	YELLOW('e', true),
	WHITE('f', true),
	BOLD('l', false),
	STRIKETHROUGH('m', false),
	UNDERLINE('n', false),
	ITALIC('o', false),
	BLINK('p', false);
	private static final TextAttribute[] ATTRIBUTES = new TextAttribute[0xFF];
	private final char character;
	private final boolean stackable;

	private TextAttribute(final char character, final boolean stackable)
	{
		this.character = character;
		this.stackable = stackable;
	}

	public char getCharacter()
	{
		return character;
	}

	public boolean isStackable()
	{
		return stackable;
	}

	public boolean isFlagable()
	{
		return !stackable;
	}

	public static TextAttribute getAttribute(final char abbreviation)
	{
		return ATTRIBUTES[abbreviation];
	}

	static
	{
		for (final TextAttribute attribute : TextAttribute.values())
		{
			ATTRIBUTES[attribute.getCharacter()] = attribute;
		}
	}
}
