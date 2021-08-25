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
import java.sql.Timestamp;
import javax.persistence.*;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "cyclone__user_punishment")
public class UserPunishmentTable
		implements EntityBean
{
	private static final String _ebean_marker = UserPunishmentTable.class.getCanonicalName();
	public static final String[] _ebean_props = new String[]
	{
		"id", "user", "type", "active", "reason", "createdBy", "createdAt", "expire"
	};
	private transient final EntityBeanIntercept _ebean_intercept = new EntityBeanIntercept(this);
	@Id
	private long id;
	@JoinColumn(name = "user_id", nullable = false)
	@ManyToOne(targetEntity = UserTable.class)
	private UserTable user;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserPunishmentData.Type type;
	@Column(nullable = false)
	private boolean active;
	private String reason;
	@JoinColumn(name = "createdBy_id")
	@ManyToOne(targetEntity = UserTable.class)
	private UserTable createdBy;
	@Column(nullable = false, columnDefinition = "datetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp createdAt;
	@Column(columnDefinition = "datetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp expire;

	public UserPunishmentTable()
	{}

	public UserPunishmentTable(final UserTable user, final UserPunishmentData.Type type, final boolean active, final String reason, final UserTable createdBy, final Timestamp createdAt, final Timestamp expire)
	{
		this.user = user;
		this.type = type;
		this.active = active;
		this.reason = reason;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		this.expire = expire;
	}

	@Override
	public Object _ebean_newInstance()
	{
		return new UserPunishmentTable();
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

	private UserPunishmentData.Type _ebean_getni_type()
	{
		return type;
	}

	private void _ebean_setni_type(final UserPunishmentData.Type newValue)
	{
		type = newValue;
	}

	private boolean _ebean_getni_active()
	{
		return active;
	}

	private void _ebean_setni_active(final boolean newValue)
	{
		active = newValue;
	}

	private String _ebean_getni_reason()
	{
		return reason;
	}

	private void _ebean_setni_reason(final String newValue)
	{
		reason = newValue;
	}

	private UserTable _ebean_getni_createdBy()
	{
		return createdBy;
	}

	private void _ebean_setni_createdBy(final UserTable newValue)
	{
		createdBy = newValue;
	}

	private Timestamp _ebean_getni_createdAt()
	{
		return createdAt;
	}

	private void _ebean_setni_createdAt(final Timestamp newValue)
	{
		createdAt = newValue;
	}

	private Timestamp _ebean_getni_expire()
	{
		return expire;
	}

	private void _ebean_setni_expire(final Timestamp newValue)
	{
		expire = newValue;
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

	private UserPunishmentData.Type _ebean_get_type()
	{
		_ebean_intercept.preGetter(2);

		return type;
	}

	private void _ebean_set_type(final UserPunishmentData.Type newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 2, _ebean_get_type(), newValue);
		type = newValue;
		_ebean_intercept.postSetter(event);
	}

	private boolean _ebean_get_active()
	{
		_ebean_intercept.preGetter(3);

		return active;
	}

	private void _ebean_set_active(final boolean newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 3, _ebean_get_active(), newValue);
		active = newValue;
		_ebean_intercept.postSetter(event);
	}

	private String _ebean_get_reason()
	{
		_ebean_intercept.preGetter(4);

		return reason;
	}

	private void _ebean_set_reason(final String newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 4, _ebean_get_reason(), newValue);
		reason = newValue;
		_ebean_intercept.postSetter(event);
	}

	private UserTable _ebean_get_createdBy()
	{
		_ebean_intercept.preGetter(5);

		return createdBy;
	}

	private void _ebean_set_createdBy(final UserTable newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 5, _ebean_get_createdBy(), newValue);
		createdBy = newValue;
		_ebean_intercept.postSetter(event);
	}

	private Timestamp _ebean_get_createdAt()
	{
		_ebean_intercept.preGetter(6);

		return createdAt;
	}

	private void _ebean_set_createdAt(final Timestamp newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 6, _ebean_get_createdAt(), newValue);
		createdAt = newValue;
		_ebean_intercept.postSetter(event);
	}

	private Timestamp _ebean_get_expire()
	{
		_ebean_intercept.preGetter(7);

		return expire;
	}

	private void _ebean_set_expire(final Timestamp newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 7, _ebean_get_expire(), newValue);
		expire = newValue;
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
			return _ebean_getni_type();
		case 3:
			return _ebean_getni_active();
		case 4:
			return _ebean_getni_reason();
		case 5:
			return _ebean_getni_createdBy();
		case 6:
			return _ebean_getni_createdAt();
		case 7:
			return _ebean_getni_expire();
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
			return _ebean_get_type();
		case 3:
			return _ebean_get_active();
		case 4:
			return _ebean_get_reason();
		case 5:
			return _ebean_get_createdBy();
		case 6:
			return _ebean_get_createdAt();
		case 7:
			return _ebean_get_expire();
		}

		return null;
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
			_ebean_setni_type((UserPunishmentData.Type) value);
			break;
		case 3:
			_ebean_setni_active((Boolean) value);
			break;
		case 4:
			_ebean_setni_reason((String) value);
			break;
		case 5:
			_ebean_setni_createdBy((UserTable) value);
			break;
		case 6:
			_ebean_setni_createdAt((Timestamp) value);
			break;
		case 7:
			_ebean_setni_expire((Timestamp) value);
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
			_ebean_set_type((UserPunishmentData.Type) value);
			break;
		case 3:
			_ebean_set_active((Boolean) value);
			break;
		case 4:
			_ebean_set_reason((String) value);
			break;
		case 5:
			_ebean_set_createdBy((UserTable) value);
			break;
		case 6:
			_ebean_set_createdAt((Timestamp) value);
			break;
		case 7:
			_ebean_set_expire((Timestamp) value);
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

	public UserPunishmentData.Type getType()
	{
		return _ebean_get_type();
	}

	public void setType(final UserPunishmentData.Type type)
	{
		_ebean_set_type(type);
	}

	public boolean isActive()
	{
		return _ebean_get_active();
	}

	public void setActive(final boolean active)
	{
		_ebean_set_active(active);
	}

	public String getReason()
	{
		return _ebean_get_reason();
	}

	public void setReason(final String reason)
	{
		_ebean_set_reason(reason);
	}

	public UserTable getCreatedBy()
	{
		return _ebean_get_createdBy();
	}

	public void setCreatedBy(final UserTable createdBy)
	{
		_ebean_set_createdBy(createdBy);
	}

	public Timestamp getCreatedAt()
	{
		return _ebean_get_createdAt();
	}

	public void setCreatedAt(final Timestamp createdAt)
	{
		_ebean_set_createdAt(createdAt);
	}

	public Timestamp getExpire()
	{
		return _ebean_get_expire();
	}

	public void setExpire(final Timestamp expire)
	{
		_ebean_set_expire(expire);
	}
}
