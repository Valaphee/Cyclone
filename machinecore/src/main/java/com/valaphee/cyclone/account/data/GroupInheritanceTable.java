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
@Table(name = "cyclone__group_inheritance", uniqueConstraints = @UniqueConstraint(columnNames =
{
	"child_id", "parent_id"
}))
public class GroupInheritanceTable
		implements EntityBean
{
	private static final String _ebean_marker = GroupInheritanceTable.class.getCanonicalName();
	public static final String[] _ebean_props = new String[]
	{
		"id", "child", "parent"
	};
	private transient final EntityBeanIntercept _ebean_intercept = new EntityBeanIntercept(this);
	@Id
	private long id;
	@JoinColumn(name = "child_id", nullable = false)
	@ManyToOne(targetEntity = GroupTable.class)
	private GroupTable child;
	@JoinColumn(name = "parent_id", nullable = false)
	@ManyToOne(targetEntity = GroupTable.class)
	private GroupTable parent;

	public GroupInheritanceTable()
	{}

	public GroupInheritanceTable(final GroupTable child, final GroupTable parent)
	{
		this.child = child;
		this.parent = parent;
	}

	@Override
	public Object _ebean_newInstance()
	{
		return new GroupInheritanceTable();
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

	private GroupTable _ebean_getni_child()
	{
		return child;
	}

	private void _ebean_setni_child(final GroupTable newValue)
	{
		child = newValue;
	}

	private GroupTable _ebean_getni_parent()
	{
		return parent;
	}

	private void _ebean_setni_parent(final GroupTable newValue)
	{
		parent = newValue;
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

	private GroupTable _ebean_get_child()
	{
		_ebean_intercept.preGetter(1);

		return child;
	}

	private void _ebean_set_child(final GroupTable newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 1, _ebean_get_child(), newValue);
		child = newValue;
		_ebean_intercept.postSetter(event);
	}

	private GroupTable _ebean_get_parent()
	{
		_ebean_intercept.preGetter(2);

		return parent;
	}

	private void _ebean_set_parent(final GroupTable newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 2, _ebean_get_parent(), newValue);
		parent = newValue;
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
			return _ebean_getni_child();
		case 2:
			return _ebean_getni_parent();
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
			return _ebean_get_child();
		case 2:
			return _ebean_get_parent();
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
			_ebean_setni_child((GroupTable) value);
			break;
		case 2:
			_ebean_setni_parent((GroupTable) value);
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
			_ebean_set_child((GroupTable) value);
			break;
		case 2:
			_ebean_set_parent((GroupTable) value);
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

	public GroupTable getChild()
	{
		return _ebean_get_child();
	}

	public void setChild(final GroupTable child)
	{
		_ebean_set_child(child);
	}

	public GroupTable getParent()
	{
		return _ebean_get_parent();
	}

	public void setParent(final GroupTable parent)
	{
		_ebean_set_parent(parent);
	}
}
