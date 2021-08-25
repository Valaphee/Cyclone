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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public class MinecraftJSONComponent
{
	protected MinecraftJSONStyle style;
	private final List<MinecraftJSONComponent> siblings = new ArrayList<>();

	public MinecraftJSONStyle getStyle()
	{
		if (style == null)
		{
			style = new MinecraftJSONStyle();
			siblings.forEach((sibling) ->
			{
				sibling.getStyle().setParent(style);
			});
		}

		return style;
	}

	public MinecraftJSONComponent setStyle(final MinecraftJSONStyle style)
	{
		this.style = style;
		siblings.forEach((sibling) ->
		{
			sibling.getStyle().setParent(getStyle());
		});

		return this;
	}

	public List<MinecraftJSONComponent> getSiblings()
	{
		return Collections.unmodifiableList(siblings);
	}

	public MinecraftJSONComponent appendSibling(final MinecraftJSONComponent sibling)
	{
		sibling.style.setParent(style);
		siblings.add(sibling);

		return this;
	}

	public static final class Serializer
			implements JsonSerializer<MinecraftJSONComponent>, JsonDeserializer<MinecraftJSONComponent>
	{
		private static final Gson GSON;

		@Override
		public JsonElement serialize(final MinecraftJSONComponent element, final Type type, final JsonSerializationContext context)
		{
			final JsonObject json = new JsonObject();

			if (!element.getStyle().isEmpty())
			{
				final JsonElement jsonFormat = context.serialize(element.getStyle());
				if (jsonFormat.isJsonObject())
				{
					final JsonObject jsonExFormat = (JsonObject) jsonFormat;
					jsonExFormat.entrySet().forEach((entry) ->
					{
						json.add(entry.getKey(), entry.getValue());
					});
				}
			}
			if (!element.getSiblings().isEmpty())
			{
				final JsonArray jsonExtra = new JsonArray();
				element.getSiblings().forEach((sibling) ->
				{
					jsonExtra.add(serialize(sibling, sibling.getClass(), context));
				});
				json.add("extra", jsonExtra);
			}

			if (element instanceof MinecraftJSONTextComponent)
			{
				json.addProperty("text", ((MinecraftJSONTextComponent) element).getText());
			}
			else if (element instanceof MinecraftJSONTranslationComponent)
			{
				final MinecraftJSONTranslationComponent component = (MinecraftJSONTranslationComponent) element;
				json.addProperty("translate", component.getName());
				if ((component.getArguments() != null) && (component.getArguments().length > 0))
				{
					final JsonArray jsonArguments = new JsonArray();
					for (final Object argument : component.getArguments())
					{
						if (argument instanceof MinecraftJSONComponent)
						{
							jsonArguments.add(serialize((MinecraftJSONComponent) argument, argument.getClass(), context));
						}
						else
						{
							jsonArguments.add(new JsonPrimitive(String.valueOf(argument)));
						}
					}
					json.add("with", jsonArguments);
				}
			}
			else if (element instanceof MinecraftJSONScoreComponent)
			{
				final MinecraftJSONScoreComponent component = (MinecraftJSONScoreComponent) element;
				json.addProperty("name", component.getName());
				json.addProperty("objective", component.getObjective());
			}
			else if (element instanceof MinecraftJSONSelectionComponent)
			{
				final MinecraftJSONSelectionComponent component = (MinecraftJSONSelectionComponent) element;
				json.addProperty("selector", component.getPattern());
			}
			else if (element instanceof MinecraftJSONKeybindComponent)
			{
				final MinecraftJSONKeybindComponent component = (MinecraftJSONKeybindComponent) element;
				json.addProperty("keybind", component.getKeybind());
			}

			return json;
		}

		@Override
		public MinecraftJSONComponent deserialize(JsonElement rawJson, Type type, JsonDeserializationContext context)
		{
			if (rawJson.isJsonPrimitive())
			{
				return new MinecraftJSONTextComponent(rawJson.getAsString());
			}

			if (rawJson.isJsonObject())
			{
				final JsonObject json = rawJson.getAsJsonObject();

				MinecraftJSONComponent element;
				if (json.has("text"))
				{
					element = new MinecraftJSONTextComponent(json.get("text").getAsString());
				}
				else if (json.has("translate"))
				{
					element = new MinecraftJSONTranslationComponent(json.get("translate").getAsString());
				}
				else if (json.has("objective"))
				{
					element = new MinecraftJSONScoreComponent(json.get("name").getAsString(), json.get("objective").getAsString());
				}
				else if (json.has("selector"))
				{
					element = new MinecraftJSONSelectionComponent(json.get("selector").getAsString());
				}
				else if (json.has("keybind"))
				{
					element = new MinecraftJSONSelectionComponent(json.get("keybind").getAsString());
				}
				else
				{
					throw new JsonParseException("Unknown component '" + json.toString() + "'");
				}

				if (json.has("extra"))
				{
					final JsonArray jsonExtra = json.getAsJsonArray("extra");
					if (jsonExtra.size() <= 0)
					{
						throw new JsonParseException("Unexpected empty array of components");
					}

					for (int i = 0; i < jsonExtra.size(); ++i)
					{
						element.appendSibling(deserialize(jsonExtra.get(i), type, context));
					}
				}

				return element;
			}

			return null;
		}

		public static String encode(final MinecraftJSONComponent component)
		{
			return GSON.toJson(component);
		}

		public static MinecraftJSONComponent decode(final String json)
		{
			return GSON.fromJson(json, MinecraftJSONComponent.class);
		}

		static
		{
			final GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeHierarchyAdapter(MinecraftJSONStyle.class, new MinecraftJSONStyle.Serializer());
			gsonBuilder.registerTypeHierarchyAdapter(MinecraftJSONComponent.class, new MinecraftJSONComponent.Serializer());
			GSON = gsonBuilder.create();
		}
	}
}
