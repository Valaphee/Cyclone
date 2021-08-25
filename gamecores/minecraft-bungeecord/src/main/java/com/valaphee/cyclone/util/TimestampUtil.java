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

import static com.valaphee.cyclone.language.I18n.tl;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default
 *
 * @author valaphee
 */
public final class TimestampUtil
{
	private static final Pattern DIFF_PATTERN = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*n[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);
	private static final int[] TYPES = new int[]
	{
		Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND
	};
	private static final String[] NAMES = new String[]
	{
		tl("cyclone.year"), tl("cyclone.years"), tl("cyclone.month"), tl("cyclone.months"), tl("cyclone.day"), tl("cyclone.days"), tl("cyclone.hour"), tl("cyclone.hours"), tl("cyclone.minute"), tl("cyclone.minutes"), tl("cyclone.second"), tl("cyclone.seconds")
	};

	public static Timestamp getCurrentTimestamp()
	{
		return new Timestamp(System.currentTimeMillis());
	}

	public static Timestamp getTimestampForDiff(final Timestamp from, final String diff, final boolean future)
	{
		final Matcher matcher = DIFF_PATTERN.matcher(diff);
		int years = 0;
		int months = 0;
		int weeks = 0;
		int days = 0;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		while (matcher.find())
		{
			if ((matcher.group() != null) && (!matcher.group().isEmpty()))
			{
				for (int i = 0; i < matcher.groupCount(); ++i)
				{
					if ((matcher.group(i) != null) && (!matcher.group(i).isEmpty()))
					{
						break;
					}
				}
				if ((matcher.group(1) != null) && (!matcher.group(1).isEmpty()))
				{
					years = Integer.parseInt(matcher.group(1));
				}
				if ((matcher.group(2) != null) && (!matcher.group(2).isEmpty()))
				{
					months = Integer.parseInt(matcher.group(2));
				}
				if ((matcher.group(3) != null) && (!matcher.group(3).isEmpty()))
				{
					weeks = Integer.parseInt(matcher.group(3));
				}
				if ((matcher.group(4) != null) && (!matcher.group(4).isEmpty()))
				{
					days = Integer.parseInt(matcher.group(4));
				}
				if ((matcher.group(5) != null) && (!matcher.group(5).isEmpty()))
				{
					hours = Integer.parseInt(matcher.group(5));
				}
				if ((matcher.group(6) != null) && (!matcher.group(6).isEmpty()))
				{
					minutes = Integer.parseInt(matcher.group(6));
				}
				if ((matcher.group(7) != null) && (!matcher.group(7).isEmpty()))
				{
					seconds = Integer.parseInt(matcher.group(7));
				}
			}
		}

		final Calendar calendar = new GregorianCalendar();
		calendar.setTime(from);
		if (years > 0)
		{
			calendar.add(Calendar.YEAR, years * (future ? 1 : -1));
		}
		if (months > 0)
		{
			calendar.add(Calendar.MONTH, months * (future ? 1 : -1));
		}
		if (weeks > 0)
		{
			calendar.add(Calendar.WEEK_OF_YEAR, weeks * (future ? 1 : -1));
		}
		if (days > 0)
		{
			calendar.add(Calendar.DAY_OF_MONTH, days * (future ? 1 : -1));
		}
		if (hours > 0)
		{
			calendar.add(Calendar.HOUR_OF_DAY, hours * (future ? 1 : -1));
		}
		if (minutes > 0)
		{
			calendar.add(Calendar.MINUTE, minutes * (future ? 1 : -1));
		}
		if (seconds > 0)
		{
			calendar.add(Calendar.SECOND, seconds * (future ? 1 : -1));
		}

		return new Timestamp(calendar.getTimeInMillis());
	}

	public static String formatTimestamp(final Timestamp from, final Timestamp to)
	{
		if (to.equals(from))
		{
			return tl("cyclone.now");
		}

		boolean future = false;
		if (to.after(from))
		{
			future = true;
		}

		final Calendar calendar = new GregorianCalendar(), castedCalendar = new GregorianCalendar();
		calendar.setTime(from);
		castedCalendar.setTime(to);

		int accuracy = 0;
		final StringBuilder formatted = new StringBuilder();
		for (int i = 0; i < TYPES.length; ++i)
		{
			if (accuracy > 2)
			{
				break;
			}

			int diff = 0;
			long time = calendar.getTimeInMillis();
			while ((future && (!calendar.after(castedCalendar))) || ((!future) && (!calendar.before(castedCalendar))))
			{
				diff++;
				time = calendar.getTimeInMillis();
				calendar.add(TYPES[i], future ? 1 : -1);
			}
			diff--;
			calendar.setTimeInMillis(time);
			if (diff > 0)
			{
				accuracy++;
				formatted.append(' ').append(diff).append(' ').append(NAMES[(i * 2 + (diff > 1 ? 1 : 0))]);
			}
		}
		if (formatted.length() == 0)
		{
			return tl("cyclone.moment");
		}

		return formatted.toString().trim();
	}

	public static String formatTimestamp(final Timestamp timestamp)
	{
		return formatTimestamp(new Timestamp(0), timestamp);
	}

	private TimestampUtil()
	{}
}
