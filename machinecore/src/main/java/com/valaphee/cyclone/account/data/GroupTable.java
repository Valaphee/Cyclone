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
import java.util.Set;
import javax.persistence.*;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "cyclone__group", uniqueConstraints = @UniqueConstraint(columnNames =
{
	"name"
}))
public class GroupTable
		implements EntityBean
{
	private static final String _ebean_marker = GroupTable.class.getCanonicalName();
	public static final String[] _ebean_props = new String[]
	{
		"id", "name", "variables", "permissions", "inheritances"
	};
	private transient final EntityBeanIntercept _ebean_intercept = new EntityBeanIntercept(this);
	@Id
	private long id;
	@Column(nullable = false)
	private String name;
	@OneToMany(targetEntity = GroupVariableTable.class, mappedBy = "group")
	private Set<GroupVariableTable> variables;
	@OneToMany(targetEntity = GroupPermissionTable.class, mappedBy = "group")
	private Set<GroupPermissionTable> permissions;
	@OneToMany(targetEntity = GroupInheritanceTable.class, mappedBy = "child")
	private Set<GroupInheritanceTable> inheritances;

	public GroupTable()
	{}

	public GroupTable(final String name)
	{
		this.name = name;
	}

	@Override
	public Object _ebean_newInstance()
	{
		return new GroupTable();
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

	private String _ebean_getni_name()
	{
		return name;
	}

	private void _ebean_setni_name(final String newValue)
	{
		name = newValue;
	}

	private Set<GroupVariableTable> _ebean_getni_variables()
	{
		return variables;
	}

	private void _ebean_setni_variables(final Set<GroupVariableTable> newValue)
	{
		variables = newValue;
	}

	private Set<GroupPermissionTable> _ebean_getni_permissions()
	{
		return permissions;
	}

	private void _ebean_setni_permissions(final Set<GroupPermissionTable> newValue)
	{
		permissions = newValue;
	}

	private Set<GroupInheritanceTable> _ebean_getni_inheritances()
	{
		return inheritances;
	}

	private void _ebean_setni_inheritances(final Set<GroupInheritanceTable> newValue)
	{
		inheritances = newValue;
	}

	private long _ebean_get_id()
	{
		_ebean_intercept.preGetter(0);

		return id;
	}

	private void _ebean_set_id(final long newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 0, _ebean_get_id(), newValue);
		id = newValue;
		_ebean_intercept.postSetter(event);
	}

	private String _ebean_get_name()
	{
		_ebean_intercept.preGetter(1);

		return name;
	}

	private void _ebean_set_name(final String newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 1, _ebean_get_name(), newValue);
		name = newValue;
		_ebean_intercept.postSetter(event);
	}

	private Set<GroupVariableTable> _ebean_get_variables()
	{
		_ebean_intercept.preGetter(2);

		return variables;
	}

	private void _ebean_set_variables(final Set<GroupVariableTable> newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 2, _ebean_get_variables(), newValue);
		variables = newValue;
		_ebean_intercept.postSetter(event);
	}

	private Set<GroupPermissionTable> _ebean_get_permissions()
	{
		_ebean_intercept.preGetter(3);

		return permissions;
	}

	private void _ebean_set_permissions(final Set<GroupPermissionTable> newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 3, _ebean_get_permissions(), newValue);
		permissions = newValue;
		_ebean_intercept.postSetter(event);
	}

	private Set<GroupInheritanceTable> _ebean_get_inheritances()
	{
		_ebean_intercept.preGetter(4);

		return inheritances;
	}

	private void _ebean_set_inheritances(final Set<GroupInheritanceTable> newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 4, _ebean_get_inheritances(), newValue);
		inheritances = newValue;
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
			return _ebean_getni_name();
		case 2:
			return _ebean_getni_variables();
		case 3:
			return _ebean_getni_permissions();
		case 4:
			return _ebean_getni_inheritances();
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
			return _ebean_get_name();
		case 2:
			return _ebean_get_variables();
		case 3:
			return _ebean_get_permissions();
		case 4:
			return _ebean_get_inheritances();
		default:
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void _ebean_setField(final int fieldIndex, final Object value)
	{
		switch (fieldIndex)
		{
		case 0:
			_ebean_setni_id((Long) value);
			break;
		case 1:
			_ebean_setni_name((String) value);
			break;
		case 2:
			_ebean_setni_variables((Set<GroupVariableTable>) value);
			break;
		case 3:
			_ebean_setni_permissions((Set<GroupPermissionTable>) value);
			break;
		case 4:
			_ebean_setni_inheritances((Set<GroupInheritanceTable>) value);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void _ebean_setFieldIntercept(final int fieldIndex, final Object value)
	{
		switch (fieldIndex)
		{
		case 0:
			_ebean_set_id((Long) value);
			break;
		case 1:
			_ebean_set_name((String) value);
			break;
		case 2:
			_ebean_set_variables((Set<GroupVariableTable>) value);
			break;
		case 3:
			_ebean_set_permissions((Set<GroupPermissionTable>) value);
			break;
		case 4:
			_ebean_set_inheritances((Set<GroupInheritanceTable>) value);
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

	public String getName()
	{
		return _ebean_get_name();
	}

	public void setName(final String name)
	{
		_ebean_set_name(name);
	}

	public Set<GroupVariableTable> getVariables()
	{
		return _ebean_get_variables();
	}

	public void setVariables(final Set<GroupVariableTable> variables)
	{
		_ebean_set_variables(variables);
	}

	public Set<GroupPermissionTable> getPermissions()
	{
		return _ebean_get_permissions();
	}

	public void setPermissions(final Set<GroupPermissionTable> permissions)
	{
		_ebean_set_permissions(permissions);
	}

	public Set<GroupInheritanceTable> getInheritances()
	{
		return _ebean_get_inheritances();
	}

	public void setInheritances(final Set<GroupInheritanceTable> inheritances)
	{
		_ebean_set_inheritances(inheritances);
	}
}
