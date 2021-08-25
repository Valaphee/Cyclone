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

package com.valaphee.cyclone.account.data;

import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.persistence.*;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "cyclone__group_permission", uniqueConstraints = @UniqueConstraint(columnNames =
{
	"group_id", "permission"
}))
public class GroupPermissionTable
		implements EntityBean
{
	private static final String _ebean_marker = GroupPermissionTable.class.getCanonicalName();
	public static final String[] _ebean_props = new String[]
	{
		"id", "group", "permission"
	};
	private transient final EntityBeanIntercept _ebean_intercept = new EntityBeanIntercept(this);
	@Id
	private long id;
	@JoinColumn(name = "group_id", nullable = false)
	@ManyToOne(targetEntity = GroupTable.class)
	private GroupTable group;
	@Column(nullable = false)
	private String permission;

	public GroupPermissionTable()
	{}

	public GroupPermissionTable(final GroupTable group, final String permission)
	{
		this.group = group;
		this.permission = permission;
	}

	@Override
	public Object _ebean_newInstance()
	{
		return new GroupPermissionTable();
	}

	@Override
	public EntityBeanIntercept _ebean_intercept()
	{
		return _ebean_intercept;
	}

	@Override
	public EntityBeanIntercept _ebean_getIntercept()
	{
		return _ebean_intercept;
	}

	@Override
	public void addPropertyChangeListener(final PropertyChangeListener listener)
	{
		_ebean_intercept.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(final PropertyChangeListener listener)
	{
		_ebean_intercept.removePropertyChangeListener(listener);
	}

	@Override
	public String _ebean_getMarker()
	{
		return _ebean_marker;
	}

	@Override
	public String[] _ebean_getPropertyNames()
	{
		return _ebean_props;
	}

	@Override
	public String _ebean_getPropertyName(final int pos)
	{
		return _ebean_props[pos];
	}

	private long _ebean_getni_id()
	{
		return id;
	}

	private void _ebean_setni_id(final long newValue)
	{
		id = newValue;
	}

	private GroupTable _ebean_getni_group()
	{
		return group;
	}

	private void _ebean_setni_group(final GroupTable newValue)
	{
		group = newValue;
	}

	private String _ebean_getni_permission()
	{
		return permission;
	}

	private void _ebean_setni_permission(final String newValue)
	{
		permission = newValue;
	}

	private long _ebean_get_id()
	{
//		_ebean_intercept.preGetter(0);

		return id;
	}

	private void _ebean_set_id(final long newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 0, _ebean_get_id(), newValue);
		id = newValue;
		_ebean_intercept.postSetter(event);
	}

	private GroupTable _ebean_get_group()
	{
		_ebean_intercept.preGetter(1);

		return group;
	}

	private void _ebean_set_group(final GroupTable newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 1, _ebean_get_group(), newValue);
		group = newValue;
		_ebean_intercept.postSetter(event);
	}

	private String _ebean_get_permission()
	{
		_ebean_intercept.preGetter(2);

		return permission;
	}

	private void _ebean_set_permission(final String newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 2, _ebean_get_permission(), newValue);
		permission = newValue;
		_ebean_intercept.postSetter(event);
	}

	@Override
	public Object _ebean_getField(final int fieldIndex)
	{
		switch (fieldIndex)
		{
		case 0:
			return _ebean_getni_id();
		case 1:
			return _ebean_getni_group();
		case 2:
			return _ebean_getni_permission();
		default:
			return null;
		}
	}

	@Override
	public Object _ebean_getFieldIntercept(final int fieldIndex)
	{
		switch (fieldIndex)
		{
		case 0:
			return _ebean_get_id();
		case 1:
			return _ebean_get_group();
		case 2:
			return _ebean_get_permission();
		default:
			return null;
		}
	}

	@Override
	public void _ebean_setField(final int fieldIndex, final Object value)
	{
		switch (fieldIndex)
		{
		case 0:
			_ebean_setni_id((Long) value);
			break;
		case 1:
			_ebean_setni_group((GroupTable) value);
			break;
		case 2:
			_ebean_setni_permission((String) value);
		}
	}

	@Override
	public void _ebean_setFieldIntercept(final int fieldIndex, final Object value)
	{
		switch (fieldIndex)
		{
		case 0:
			_ebean_set_id((Long) value);
			break;
		case 1:
			_ebean_set_group((GroupTable) value);
			break;
		case 2:
			_ebean_set_permission((String) value);
		}
	}

	@Override
	public boolean _ebean_isEmbeddedNewOrDirty()
	{
		return false;
	}

	@Override
	public void _ebean_setEmbeddedLoaded()
	{}

	public long getId()
	{
		return _ebean_get_id();
	}

	public void setId(final long id)
	{
		_ebean_set_id(id);
	}

	public GroupTable getGroup()
	{
		return _ebean_get_group();
	}

	public void setGroup(final GroupTable group)
	{
		_ebean_set_group(group);
	}

	public String getPermission()
	{
		return _ebean_get_permission();
	}

	public void setPermission(final String permission)
	{
		_ebean_set_permission(permission);
	}
}
