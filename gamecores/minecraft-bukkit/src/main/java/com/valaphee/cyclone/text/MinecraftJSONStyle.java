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

import com.google.gson.*;
import java.lang.reflect.Type;
import org.bukkit.ChatColor;

/**
 * Default
 *
 * @author valaphee
 */
public class MinecraftJSONStyle
{
	private static final MinecraftJSONStyle ROOT = new MinecraftJSONStyle()
	{
		@Override
		public ChatColor getColor()
		{
			return null;
		}

		@Override
		public boolean isBold()
		{
			return false;
		}

		@Override
		public boolean isItalic()
		{
			return false;
		}

		@Override
		public boolean isStrikethrough()
		{
			return false;
		}

		@Override
		public boolean isUnderline()
		{
			return false;
		}

		@Override
		public boolean isObfuscated()
		{
			return false;
		}

		@Override
		public ClickEvent getClickEvent()
		{
			return null;
		}

		@Override
		public String getClickEventData()
		{
			return null;
		}

		@Override
		public HoverEvent getHoverEvent()
		{
			return null;
		}

		@Override
		public String getHoverEventData()
		{
			return null;
		}

		@Override
		public String getInsertion()
		{
			return null;
		}
	};

	private MinecraftJSONStyle parent = ROOT;
	protected ChatColor color;
	protected Boolean bold;
	protected Boolean italic;
	protected Boolean underline;
	protected Boolean strikethrough;
	protected Boolean obfuscated;
	protected ClickEvent clickEvent;
	protected String clickEventData;
	protected HoverEvent hoverEvent;
	protected String hoverEventData;
	protected String insertion;

	public MinecraftJSONStyle getParent()
	{
		return parent;
	}

	public MinecraftJSONStyle setParent(final MinecraftJSONStyle parent)
	{
		this.parent = parent;

		return this;
	}

	public ChatColor getColor()
	{
		return color == null ? parent.getColor() : color;
	}

	public MinecraftJSONStyle setColor(final ChatColor color)
	{
		this.color = color;

		return this;
	}

	public boolean isBold()
	{
		return bold == null ? parent.isBold() : bold;
	}

	public MinecraftJSONStyle setBold(final boolean bold)
	{
		this.bold = bold;

		return this;
	}

	public boolean isItalic()
	{
		return italic == null ? parent.isItalic() : italic;
	}

	public MinecraftJSONStyle setItalic(final boolean italic)
	{
		this.italic = italic;

		return this;
	}

	public boolean isStrikethrough()
	{
		return strikethrough == null ? parent.isStrikethrough() : strikethrough;
	}

	public MinecraftJSONStyle setStrikethrough(final boolean strikethrough)
	{
		this.strikethrough = strikethrough;

		return this;
	}

	public boolean isUnderline()
	{
		return underline == null ? parent.isUnderline() : underline;
	}

	public MinecraftJSONStyle setUnderline(final boolean underline)
	{
		this.underline = underline;

		return this;
	}

	public boolean isObfuscated()
	{
		return obfuscated == null ? parent.isObfuscated() : obfuscated;
	}

	public MinecraftJSONStyle setObfuscated(final boolean obfuscated)
	{
		this.obfuscated = obfuscated;

		return this;
	}

	public ClickEvent getClickEvent()
	{
		return clickEvent == null ? parent.getClickEvent() : clickEvent;
	}

	public String getClickEventData()
	{
		return clickEventData == null ? parent.getClickEventData() : clickEventData;
	}

	public MinecraftJSONStyle setClickEvent(final ClickEvent event, final String data)
	{
		this.clickEvent = event;
		this.clickEventData = data;

		return this;
	}

	public HoverEvent getHoverEvent()
	{
		return hoverEvent == null ? parent.getHoverEvent() : hoverEvent;
	}

	public String getHoverEventData()
	{
		return hoverEventData == null ? parent.getHoverEventData() : hoverEventData;
	}

	public MinecraftJSONStyle setHoverEvent(final HoverEvent event, final String data)
	{
		this.hoverEvent = event;
		this.hoverEventData = data;

		return this;
	}

	public String getInsertion()
	{
		return insertion == null ? parent.getInsertion() : insertion;
	}

	public MinecraftJSONStyle setInsertion(final String insertion)
	{
		this.insertion = insertion;

		return this;
	}

	public boolean isEmpty()
	{
		return (color == null) && (bold == null) && (italic == null) && (strikethrough == null) && (underline == null) && (obfuscated == null) && (clickEvent == null) && (hoverEvent == null);
	}

	public static enum ClickEvent
	{
		OPEN_URL("open_url"),
		RUN_COMMAND("run_command"),
		SUGGEST_COMMAND("suggest_command"),
		CHANGE_PAGE("change_page");
		private final String name;

		private ClickEvent(final String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}
	}

	public static enum HoverEvent
	{
		SHOW_TEXT("show_text"),
		SHOW_ACHIEVEMENT("show_achievement"),
		SHOW_ITEM("show_item"),
		SHOW_ENTITY("show_entity");
		private final String name;

		private HoverEvent(final String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}
	}

	public static final class Serializer
			implements JsonSerializer<MinecraftJSONStyle>, JsonDeserializer<MinecraftJSONStyle>
	{
		@Override
		public JsonElement serialize(final MinecraftJSONStyle element, final Type type, final JsonSerializationContext context)
		{
			if (element.isEmpty())
			{
				return null;
			}

			final JsonObject json = new JsonObject();
			if (element.color != null)
			{
				json.addProperty("color", element.color.getChar());
			}
			if (element.bold != null)
			{
				json.addProperty("bold", element.bold);
			}
			if (element.italic != null)
			{
				json.addProperty("italic", element.italic);
			}
			if (element.underline != null)
			{
				json.addProperty("underlined", element.underline);
			}
			if (element.strikethrough != null)
			{
				json.addProperty("strikethrough", element.strikethrough);
			}
			if (element.insertion != null)
			{
				json.addProperty("insertion", element.insertion);
			}
			if ((element.clickEvent != null) && (element.clickEventData != null))
			{
				final JsonObject jsonEvent = new JsonObject();
				jsonEvent.addProperty("action", element.clickEvent.getName());
				jsonEvent.addProperty("value", element.clickEventData);
				json.add("clickEvent", jsonEvent);
			}
			if ((element.hoverEvent != null) && (element.hoverEventData != null))
			{
				final JsonObject jsonEvent = new JsonObject();
				jsonEvent.addProperty("action", element.hoverEvent.getName());
				jsonEvent.addProperty("value", element.hoverEventData);
				json.add("hoverEvent", jsonEvent);
			}

			return json;
		}

		@Override
		public MinecraftJSONStyle deserialize(JsonElement rawJson, Type type, JsonDeserializationContext context)
		{
			if (!rawJson.isJsonObject())
			{
				return null;
			}

			final JsonObject json = rawJson.getAsJsonObject();
			final MinecraftJSONStyle element = new MinecraftJSONStyle();
			if (json.has("color"))
			{
				element.color = context.deserialize(json.get("color"), ChatColor.class);
			}
			if (json.has("bold"))
			{
				element.bold = json.get("bold").getAsBoolean();
			}
			if (json.has("italic"))
			{
				element.italic = json.get("italic").getAsBoolean();
			}
			if (json.has("underlined"))
			{
				element.underline = json.get("underlined").getAsBoolean();
			}
			if (json.has("strikethrough"))
			{
				element.strikethrough = json.get("strikethrough").getAsBoolean();
			}
			if (json.has("obfuscated"))
			{
				element.obfuscated = json.get("obfuscated").getAsBoolean();
			}
			if (json.has("insertion"))
			{
				element.insertion = json.get("insertion").getAsString();
			}
			if (json.has("clickEvent"))
			{
				final JsonObject jsonEvent = json.getAsJsonObject("clickEvent");
				if (jsonEvent != null)
				{
					element.clickEvent = ClickEvent.valueOf(jsonEvent.getAsJsonPrimitive("action").getAsString().toUpperCase());
					element.clickEventData = jsonEvent.getAsJsonPrimitive("value").getAsString();
				}
			}
			if (json.has("hoverEvent"))
			{
				final JsonObject jsonEvent = json.getAsJsonObject("hoverEvent");
				if (jsonEvent != null)
				{
					element.hoverEvent = HoverEvent.valueOf(jsonEvent.getAsJsonPrimitive("action").getAsString().toUpperCase());
					element.hoverEventData = jsonEvent.getAsJsonPrimitive("value").getAsString();
				}
			}

			return element;
		}
	}
}
