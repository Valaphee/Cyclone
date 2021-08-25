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

package com.valaphee.cyclone.account;

import com.valaphee.cyclone.Cyclone;
import com.valaphee.cyclone.account.event.GroupUpdateEvent;
import com.valaphee.cyclone.reflect.Accessors;
import com.valaphee.cyclone.reflect.FieldAccessor;
import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

/**
 * Default
 *
 * @author valaphee
 */
@Deprecated
public final class PermissionListener
		implements Listener
{
	private static final FieldAccessor PERMISSIONS_ATTACHMENT_PERMISSIONS = Accessors.getFieldAccessorOrNull(PermissionAttachment.class, "permissions");
	private final Cyclone cyclone;
	private final AccountManager accountManager;
	private final Map<String, Permission> bukkitPermissions = new LinkedHashMap<>();
	private final Map<UUID, PermissionAttachment> bukkitAttachments = new WeakHashMap<>();

	public PermissionListener(final Cyclone cyclone)
	{
		this.cyclone = cyclone;
		this.accountManager = cyclone.getAccountManager();
	}

	@SuppressWarnings("unchecked")
	@EventHandler(priority = EventPriority.LOW)
	public void on(final PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		final UUID id = player.getUniqueId();
		if (bukkitAttachments.containsKey(id))
		{
			bukkitAttachments.get(id).remove();
			bukkitAttachments.remove(id);
		}
		updatePermissions(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void on(final PlayerQuitEvent event)
	{
		final Player player = event.getPlayer();
		final UUID id = player.getUniqueId();
		if (bukkitAttachments.containsKey(id))
		{
			bukkitAttachments.get(id).remove();
			bukkitAttachments.remove(id);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void on(final PluginEnableEvent event)
	{
		collectPermissions();
		updateAllPermissions();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void on(final PluginDisableEvent event)
	{
		collectPermissions();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void on(final GroupUpdateEvent event)
	{
		updateAllPermissions();
	}

	private void collectPermissions()
	{
		bukkitPermissions.clear();
		cyclone.getServer().getPluginManager().getPermissions().forEach((permission) ->
		{
			bukkitPermissions.put(permission.getName().toLowerCase(), permission);
		});
	}

	private void updateAllPermissions()
	{
		Bukkit.getServer().getOnlinePlayers().forEach((player) ->
		{
			updatePermissions(player);
		});
	}

	@SuppressWarnings("unchecked")
	private void updatePermissions(final Player player)
	{
		PermissionAttachment attachment = bukkitAttachments.get(player.getUniqueId());
		if (attachment == null)
		{
			bukkitAttachments.put(player.getUniqueId(), attachment = player.addAttachment(cyclone));
		}

		List<String> rawPermsissions = new ArrayList<>(getAllPermissions(accountManager.getUser(accountManager.findUserId(player.getUniqueId())), false));
		final HashMap<String, Boolean> permissions = new LinkedHashMap<>();

		rawPermsissions = sort(rawPermsissions);

		for (final String permission : rawPermsissions)
		{
			final boolean value = !permission.startsWith("-");
			permissions.put(value ? permission : permission.substring(1), value);
		}

		synchronized (attachment.getPermissible())
		{
			final Map<String, Boolean> originalPermissions = (Map<String, Boolean>) PERMISSIONS_ATTACHMENT_PERMISSIONS.get(attachment);
			originalPermissions.clear();
			originalPermissions.putAll(permissions);
			attachment.getPermissible().recalculatePermissions();
		}
	}

	private List<String> sort(final List<String> permissions)
	{
		final List<String> sortedPermissions = new ArrayList<>();
		for (final String permission : permissions)
		{
			if (!permission.isEmpty())
			{
				final String a = permission.charAt(0) == '-' ? permission.substring(1) : permission;
				final Map<String, Boolean> childrens = getAllChildren(a, new HashSet<>());
				if (childrens != null)
				{
					final ListIterator<String> sortedPermissionIterator = sortedPermissions.listIterator();
					while (sortedPermissionIterator.hasNext())
					{
						final String sortedPermission = sortedPermissionIterator.next();
						final String b = sortedPermission.charAt(0) == '-' ? sortedPermission.substring(1) : sortedPermission;
						if (childrens.containsKey(b))
						{
							sortedPermissionIterator.set(permission);
							sortedPermissionIterator.add(sortedPermission);
							break;
						}
					}
				}
				if (!sortedPermissions.contains(permission))
				{
					sortedPermissions.add(permission);
				}
			}
		}

		return sortedPermissions;
	}

	private Set<String> getAllPermissions(final User user, final boolean includeChildren)
	{
		final Set<String> permissionSet = new LinkedHashSet<>(populatePerms(user.getPermissions(), includeChildren));
		final Set<String> overrides = new LinkedHashSet<>();
		final Stack<Group> stacked = new Stack<>();
		stacked.push(user.getGroup());
		while (!stacked.isEmpty())
		{
			final Group current = stacked.pop();
			populatePerms(current.getPermissions(), includeChildren).forEach((permission) ->
			{
				if (permission.startsWith("+"))
				{
					overrides.add(permission.substring(1));
				}
				else
				{
					final boolean negated = (permission.startsWith("-"));
					if (((!negated) && (!permissionSet.contains(permission)) && (!wildcardNegation(permissionSet, permission))) || (negated && (!permissionSet.contains(permission.substring(1))) && (!wildcardNegation(permissionSet, permission.substring(1)))))
					{
						permissionSet.add(permission);
					}
				}
			});
		}

		overrides.forEach((override) ->
		{
			if (permissionSet.contains("-" + override))
			{
				permissionSet.remove("-" + override);
			}
			permissionSet.add(override);
		});

		return permissionSet;
	}

	private boolean wildcardNegation(final Set<String> permissionSet, final String node)
	{
		if (permissionSet.contains("-" + node))
		{
			return true;
		}

		final String[] parts = node.split("\\.");
		final StringBuilder builder = new StringBuilder(node.length());
		for (final String part : parts)
		{
			builder.append('*');
			if (permissionSet.contains("-" + builder.toString()))
			{
				return true;
			}

			builder.deleteCharAt(builder.length() - 1);
			builder.append(part).append('.');
		}

		return false;

	}

	public List<String> getAllRegisteredPermissions(final boolean includeChildren)
	{
		final List<String> permissions = new ArrayList<>();
		bukkitPermissions.keySet().forEach((key) ->
		{
			if (!permissions.contains(key))
			{
				permissions.add(key);

				if (includeChildren)
				{
					Map<String, Boolean> children = getAllChildren(key, new HashSet<>());
					if (children != null)
					{
						children.keySet().stream().filter((node) -> (!permissions.contains(node))).forEachOrdered((node) ->
						{
							permissions.add(node);
						});
					}
				}
			}
		});

		return permissions;
	}

	public Map<String, Boolean> getAllChildren(final String node, final Set<String> playerPermArray)
	{
		final LinkedList<String> stack = new LinkedList<>();
		final Map<String, Boolean> visited = new HashMap<>();
		stack.push(node);
		visited.put(node, true);
		while (!stack.isEmpty())
		{
			final String current = stack.pop();
			final Map<String, Boolean> childrens = getChildrens(current);
			if ((childrens != null) && (!playerPermArray.contains("-" + current)))
			{
				childrens.keySet().stream().filter((childName) -> (!visited.containsKey(childName))).forEachOrdered((childName) ->
				{
					stack.push(childName);
					visited.put(childName, childrens.get(childName));
				});
			}
		}
		visited.remove(node);
		if (!visited.isEmpty())
		{
			return visited;
		}

		return null;
	}

	public Map<String, Boolean> getChildrens(final String node)
	{
		final Permission permission = bukkitPermissions.get(node.toLowerCase());
		if (permission == null)
		{
			return null;
		}

		return permission.getChildren();
	}

	private Set<String> populatePerms(final Collection<String> immutablePermissions, final boolean includeChildren)
	{
		final List<String> permissions = new ArrayList<>(immutablePermissions);
		final Set<String> permissionSet = new LinkedHashSet<>();

		boolean wildcardPermission = false;
		if (permissions.contains("*"))
		{
			permissionSet.addAll(getAllRegisteredPermissions(includeChildren));
			wildcardPermission = true;
			permissions.remove("*");
		}

		for (final String permission : permissions)
		{
			final boolean negated = permission.startsWith("-");
			if (!permissionSet.contains(permission))
			{
				permissionSet.add(permission);
				if (negated && permissionSet.contains(permission.substring(1)))
				{
					permissionSet.remove(permission.substring(1));
				}

				if (includeChildren || (negated && wildcardPermission))
				{
					final Map<String, Boolean> children = getAllChildren((negated ? permission.substring(1) : permission), new LinkedHashSet<>());
					if ((children != null) && negated)
					{
						if (wildcardPermission)
						{
							children.keySet().stream().filter((child) -> (children.get(child))).filter((child) -> (permissionSet.contains(child))).forEach((child) ->
							{
								permissionSet.remove(child);
							});
						}
						else
						{
							children.keySet().stream().filter((child) -> children.get(child)).filter((child) -> !(permissionSet.contains(child) || permissionSet.contains("-" + child))).forEach((child) ->
							{
								permissionSet.add(child);
							});
						}
					}
				}
			}
		}

		return permissionSet;
	}
}
