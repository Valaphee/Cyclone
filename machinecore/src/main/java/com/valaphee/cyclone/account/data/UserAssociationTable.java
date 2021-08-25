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
@Table(name = "cyclone__user_association", uniqueConstraints = @UniqueConstraint(columnNames =
{
	"user_id", "linked_application", "linked"
}))
public class UserAssociationTable
		implements EntityBean
{
	private static final String _ebean_marker = UserAssociationTable.class.getCanonicalName();
	public static final String[] _ebean_props = new String[]
	{
		"id", "user", "linkedApplication", "linked"
	};
	private transient final EntityBeanIntercept _ebean_intercept = new EntityBeanIntercept(this);
	@Id
	private long id;
	@JoinColumn(name = "user_id", nullable = false)
	@ManyToOne(targetEntity = UserTable.class)
	private UserTable user;
	@Column(nullable = false, length = 3)
	private String linkedApplication;
	@Column(nullable = false)
	private String linked;

	public UserAssociationTable()
	{}

	public UserAssociationTable(final UserTable user, final String linkedApplication, final String linked)
	{
		this.user = user;
		this.linkedApplication = linkedApplication;
		this.linked = linked;
	}

	@Override
	public Object _ebean_newInstance()
	{
		return new UserAssociationTable();
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

	private UserTable _ebean_getni_user()
	{
		return user;
	}

	private void _ebean_setni_user(final UserTable newValue)
	{
		user = newValue;
	}

	private String _ebean_getni_linkedApplication()
	{
		return linkedApplication;
	}

	private void _ebean_setni_linkedApplication(final String newValue)
	{
		linkedApplication = newValue;
	}

	private String _ebean_getni_linked()
	{
		return linked;
	}

	private void _ebean_setni_linked(final String newValue)
	{
		linked = newValue;
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

	private UserTable _ebean_get_user()
	{
		_ebean_intercept.preGetter(1);

		return user;
	}

	private void _ebean_set_user(final UserTable newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 1, _ebean_get_user(), newValue);
		user = newValue;
		_ebean_intercept.postSetter(event);
	}

	private String _ebean_get_linkedApplication()
	{
		_ebean_intercept.preGetter(2);

		return linkedApplication;
	}

	private void _ebean_set_linkedApplication(final String newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 2, _ebean_get_linkedApplication(), newValue);
		linkedApplication = newValue;
		_ebean_intercept.postSetter(event);
	}

	private String _ebean_get_linked()
	{
		_ebean_intercept.preGetter(3);

		return linked;
	}

	private void _ebean_set_linked(final String newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 3, _ebean_get_linked(), newValue);
		linked = newValue;
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
			return _ebean_getni_user();
		case 2:
			return _ebean_getni_linkedApplication();
		case 3:
			return _ebean_getni_linked();
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
			return _ebean_get_user();
		case 2:
			return _ebean_get_linkedApplication();
		case 3:
			return _ebean_get_linked();
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
			_ebean_setni_user((UserTable) value);
			break;
		case 2:
			_ebean_setni_linkedApplication((String) value);
			break;
		case 3:
			_ebean_setni_linked((String) value);
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
			_ebean_set_user((UserTable) value);
			break;
		case 2:
			_ebean_set_linkedApplication((String) value);
			break;
		case 3:
			_ebean_set_linked((String) value);
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

	public UserTable getUser()
	{
		return _ebean_get_user();
	}

	public void setUser(final UserTable user)
	{
		_ebean_set_user(user);
	}

	public String getLinkedApplication()
	{
		return _ebean_get_linkedApplication();
	}

	public void setLinkedApplication(final String linkedApplication)
	{
		_ebean_set_linkedApplication(linkedApplication);
	}

	public String getLinked()
	{
		return _ebean_get_linked();
	}

	public void setLinked(final String linked)
	{
		_ebean_set_linked(linked);
	}
}
