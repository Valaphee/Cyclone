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
@Table(name = "cyclone__user_variable", uniqueConstraints = @UniqueConstraint(columnNames =
{
	"user_id", "name"
}))
public class UserVariableTable
		implements EntityBean
{
	private static final String _ebean_marker = UserVariableTable.class.getCanonicalName();
	public static final String[] _ebean_props = new String[]
	{
		"id", "user", "name", "content"
	};
	private transient final EntityBeanIntercept _ebean_intercept = new EntityBeanIntercept(this);
	@Id
	private long id;
	@JoinColumn(name = "user_id", nullable = false)
	@ManyToOne(targetEntity = UserTable.class)
	private UserTable user;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String content;

	public UserVariableTable()
	{}

	public UserVariableTable(final UserTable user, final String name, final String content)
	{
		this.user = user;
		this.name = name;
		this.content = content;
	}

	@Override
	public Object _ebean_newInstance()
	{
		return new UserVariableTable();
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

	private String _ebean_getni_name()
	{
		return name;
	}

	private void _ebean_setni_name(final String newValue)
	{
		name = newValue;
	}

	private String _ebean_getni_content()
	{
		return content;
	}

	private void _ebean_setni_content(final String newValue)
	{
		content = newValue;
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

	private String _ebean_get_name()
	{
		_ebean_intercept.preGetter(2);

		return name;
	}

	private void _ebean_set_name(final String newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 2, _ebean_get_name(), newValue);
		name = newValue;
		_ebean_intercept.postSetter(event);
	}

	private String _ebean_get_content()
	{
		_ebean_intercept.preGetter(3);

		return content;
	}

	private void _ebean_set_content(final String newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 3, _ebean_get_content(), newValue);
		content = newValue;
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
			return _ebean_getni_name();
		case 3:
			return _ebean_getni_content();
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
			return _ebean_get_name();
		case 3:
			return _ebean_get_content();
		default:
			return null;
		}
	}

	@Override
	public void _ebean_setField(final int fieldIndex, final Object content)
	{
		switch (fieldIndex)
		{
		case 0:
			_ebean_setni_id((Long) content);
			break;
		case 1:
			_ebean_setni_user((UserTable) content);
			break;
		case 2:
			_ebean_setni_name((String) content);
			break;
		case 3:
			_ebean_setni_content((String) content);
		}
	}

	@Override
	public void _ebean_setFieldIntercept(final int fieldIndex, final Object content)
	{
		switch (fieldIndex)
		{
		case 0:
			_ebean_set_id((Long) content);
			break;
		case 1:
			_ebean_set_user((UserTable) content);
			break;
		case 2:
			_ebean_set_name((String) content);
			break;
		case 3:
			_ebean_set_content((String) content);
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

	public String getName()
	{
		return _ebean_get_name();
	}

	public void setName(final String name)
	{
		_ebean_set_name(name);
	}

	public String getContent()
	{
		return _ebean_get_content();
	}

	public void setContent(final String content)
	{
		_ebean_set_content(content);
	}
}
