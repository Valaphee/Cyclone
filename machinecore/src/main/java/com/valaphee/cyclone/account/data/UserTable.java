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
import com.valaphee.cyclone.security.HashMethod;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.*;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "cyclone__user", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class UserTable
		implements EntityBean
{
	private static final String _ebean_marker = UserTable.class.getCanonicalName();
	public static final String[] _ebean_props = new String[]
	{
		"id", "name", "email", "password", "created", "lastLogin", "playedTime", "variables", "associations", "ips", "punishments", "group"
	};
	private transient final EntityBeanIntercept _ebean_intercept = new EntityBeanIntercept(this);
	@Id
	private long id;
	@Column(nullable = false)
	private String name;
	private String email;
	@Column(length = 64)
	private String password;
	@Column(nullable = false, columnDefinition = "datetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp created;
	@Column(nullable = false, columnDefinition = "datetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp lastLogin;
	@Column(nullable = false)
	private long playedTime;
	@OneToMany(targetEntity = UserVariableTable.class, mappedBy = "user")
	private Set<UserVariableTable> variables;
	@OneToMany(targetEntity = UserAssociationTable.class, mappedBy = "user")
	private Set<UserAssociationTable> associations;
	@OneToMany(targetEntity = UserIpTable.class, mappedBy = "user")
	private Set<UserIpTable> ips;
	@OneToMany(targetEntity = UserPunishmentTable.class, mappedBy = "user")
	private Set<UserPunishmentTable> punishments;
	@JoinColumn(name = "group_id")
	@ManyToOne(targetEntity = GroupTable.class)
	private GroupTable group;

	public UserTable()
	{}

	public UserTable(final String name, final Timestamp created)
	{
		this.name = name;
		this.created = created;
		lastLogin = created;
	}

	@Override
	public Object _ebean_newInstance()
	{
		return new UserTable();
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

	private String _ebean_getni_email()
	{
		return email;
	}

	private void _ebean_setni_email(final String newValue)
	{
		email = newValue;
	}

	private String _ebean_getni_password()
	{
		return password;
	}

	private void _ebean_setni_password(final String newValue)
	{
		password = newValue;
	}

	private Timestamp _ebean_getni_created()
	{
		return created;
	}

	private void _ebean_setni_created(final Timestamp newValue)
	{
		created = newValue;
	}

	private Timestamp _ebean_getni_lastLogin()
	{
		return lastLogin;
	}

	private void _ebean_setni_lastLogin(final Timestamp newValue)
	{
		lastLogin = newValue;
	}

	private long _ebean_getni_playedTime()
	{
		return playedTime;
	}

	private void _ebean_setni_playedTime(final long newValue)
	{
		playedTime = newValue;
	}

	private Set<UserVariableTable> _ebean_getni_variables()
	{
		return variables;
	}

	private void _ebean_setni_variables(final Set<UserVariableTable> newValue)
	{
		variables = newValue;
	}

	private Set<UserAssociationTable> _ebean_getni_associations()
	{
		return associations;
	}

	private void _ebean_setni_associations(final Set<UserAssociationTable> newValue)
	{
		associations = newValue;
	}

	private Set<UserIpTable> _ebean_getni_ips()
	{
		return ips;
	}

	private void _ebean_setni_ips(final Set<UserIpTable> newValue)
	{
		ips = newValue;
	}

	private Set<UserPunishmentTable> _ebean_getni_punishments()
	{
		return punishments;
	}

	private void _ebean_setni_punishments(final Set<UserPunishmentTable> newValue)
	{
		punishments = newValue;
	}

	private GroupTable _ebean_getni_group()
	{
		return group;
	}

	private void _ebean_setni_group(final GroupTable newValue)
	{
		group = newValue;
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

	private String _ebean_get_email()
	{
		_ebean_intercept.preGetter(2);

		return email;
	}

	private void _ebean_set_email(final String newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 2, _ebean_get_email(), newValue);
		email = newValue;
		_ebean_intercept.postSetter(event);
	}

	private String _ebean_get_password()
	{
		_ebean_intercept.preGetter(3);

		return password;
	}

	private void _ebean_set_password(final String newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 3, _ebean_get_password(), newValue);
		password = newValue;
		_ebean_intercept.postSetter(event);
	}

	private Timestamp _ebean_get_created()
	{
		_ebean_intercept.preGetter(4);

		return created;
	}

	private void _ebean_set_created(final Timestamp newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 4, _ebean_get_created(), newValue);
		created = newValue;
		_ebean_intercept.postSetter(event);
	}

	private Timestamp _ebean_get_lastLogin()
	{
		_ebean_intercept.preGetter(5);

		return lastLogin;
	}

	private void _ebean_set_lastLogin(final Timestamp newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 5, _ebean_get_lastLogin(), newValue);
		lastLogin = newValue;
		_ebean_intercept.postSetter(event);
	}

	private long _ebean_get_playedTime()
	{
		_ebean_intercept.preGetter(6);

		return playedTime;
	}

	private void _ebean_set_playedTime(final long newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 6, _ebean_get_playedTime(), newValue);
		playedTime = newValue;
		_ebean_intercept.postSetter(event);
	}

	private Set<UserVariableTable> _ebean_get_variables()
	{
		_ebean_intercept.preGetter(7);

		return variables;
	}

	private void _ebean_set_variables(final Set<UserVariableTable> newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 7, _ebean_get_variables(), newValue);
		variables = newValue;
		_ebean_intercept.postSetter(event);
	}

	private Set<UserAssociationTable> _ebean_get_associations()
	{
		_ebean_intercept.preGetter(8);

		return associations;
	}

	private void _ebean_set_associations(final Set<UserAssociationTable> newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 8, _ebean_get_associations(), newValue);
		associations = newValue;
		_ebean_intercept.postSetter(event);
	}

	private Set<UserIpTable> _ebean_get_ips()
	{
		_ebean_intercept.preGetter(9);

		return ips;
	}

	private void _ebean_set_ips(final Set<UserIpTable> newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 9, _ebean_get_ips(), newValue);
		ips = newValue;
		_ebean_intercept.postSetter(event);
	}

	private Set<UserPunishmentTable> _ebean_get_punishments()
	{
		_ebean_intercept.preGetter(10);

		return punishments;
	}

	private void _ebean_set_punishments(final Set<UserPunishmentTable> newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 10, _ebean_get_punishments(), newValue);
		punishments = newValue;
		_ebean_intercept.postSetter(event);
	}

	private GroupTable _ebean_get_group()
	{
		_ebean_intercept.preGetter(11);

		return group;
	}

	private void _ebean_set_group(final GroupTable newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 11, _ebean_get_group(), newValue);
		group = newValue;
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
			return _ebean_getni_email();
		case 3:
			return _ebean_getni_password();
		case 4:
			return _ebean_getni_created();
		case 5:
			return _ebean_getni_lastLogin();
		case 6:
			return _ebean_getni_playedTime();
		case 7:
			return _ebean_getni_variables();
		case 8:
			return _ebean_getni_associations();
		case 9:
			return _ebean_getni_ips();
		case 10:
			return _ebean_getni_punishments();
		case 11:
			return _ebean_getni_group();
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
			return _ebean_get_email();
		case 3:
			return _ebean_get_password();
		case 4:
			return _ebean_get_created();
		case 5:
			return _ebean_get_lastLogin();
		case 6:
			return _ebean_get_playedTime();
		case 7:
			return _ebean_get_variables();
		case 8:
			return _ebean_get_associations();
		case 9:
			return _ebean_get_ips();
		case 10:
			return _ebean_get_punishments();
		case 11:
			return _ebean_get_group();
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
			_ebean_setni_email((String) value);
			break;
		case 3:
			_ebean_setni_password((String) value);
			break;
		case 4:
			_ebean_setni_created((Timestamp) value);
			break;
		case 5:
			_ebean_setni_lastLogin((Timestamp) value);
			break;
		case 6:
			_ebean_setni_playedTime((Long) value);
			break;
		case 7:
			_ebean_setni_variables((Set<UserVariableTable>) value);
			break;
		case 8:
			_ebean_setni_associations((Set<UserAssociationTable>) value);
			break;
		case 9:
			_ebean_setni_ips((Set<UserIpTable>) value);
			break;
		case 10:
			_ebean_setni_punishments((Set<UserPunishmentTable>) value);
			break;
		case 11:
			_ebean_setni_group((GroupTable) value);
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
			_ebean_set_email((String) value);
			break;
		case 3:
			_ebean_set_password((String) value);
			break;
		case 4:
			_ebean_set_created((Timestamp) value);
			break;
		case 5:
			_ebean_set_lastLogin((Timestamp) value);
			break;
		case 6:
			_ebean_set_playedTime((Long) value);
			break;
		case 7:
			_ebean_set_variables((Set<UserVariableTable>) value);
			break;
		case 8:
			_ebean_set_associations((Set<UserAssociationTable>) value);
			break;
		case 9:
			_ebean_set_ips((Set<UserIpTable>) value);
			break;
		case 10:
			_ebean_set_punishments((Set<UserPunishmentTable>) value);
			break;
		case 11:
			_ebean_set_group((GroupTable) value);
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

	public String getEmail()
	{
		return _ebean_get_email();
	}

	public void setEmail(final String email)
	{
		_ebean_set_email(email);
	}

	public boolean isPassword(final String password, final HashMethod hashMethod)
	{
		return hashMethod.comparePasswords(password, _ebean_get_password());
	}

	public void setPassword(final String password, final HashMethod hashMethod)
	{
		_ebean_set_password(hashMethod.hashPassword(password, null));
	}

	public Timestamp getCreated()
	{
		return _ebean_get_created();
	}

	public void setCreated(final Timestamp created)
	{
		_ebean_set_created(created);
	}

	public Timestamp getLastLogin()
	{
		return _ebean_get_lastLogin();
	}

	public void setLastLogin(final Timestamp lastLogin)
	{
		_ebean_set_lastLogin(lastLogin);
	}

	public long getPlayedTime()
	{
		return _ebean_get_playedTime();
	}

	public void setPlayedTime(final long playedTime)
	{
		_ebean_set_playedTime(playedTime);
	}

	public Set<UserVariableTable> getVariables()
	{
		return _ebean_get_variables();
	}

	public void setVariables(final Set<UserVariableTable> variables)
	{
		_ebean_set_variables(variables);
	}

	public Set<UserAssociationTable> getAssociations()
	{
		return _ebean_get_associations();
	}

	public void setAssociations(final Set<UserAssociationTable> associations)
	{
		_ebean_set_associations(associations);
	}

	public Set<UserIpTable> getIps()
	{
		return _ebean_get_ips();
	}

	public void setIps(final Set<UserIpTable> ips)
	{
		_ebean_set_ips(ips);
	}

	public Set<UserPunishmentTable> getPunishments()
	{
		return _ebean_get_punishments();
	}

	public void setPunishments(final Set<UserPunishmentTable> punishments)
	{
		_ebean_set_punishments(punishments);
	}

	public GroupTable getGroup()
	{
		return _ebean_get_group();
	}

	public void setGroup(final GroupTable group)
	{
		_ebean_set_group(group);
	}
}
