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

package com.valaphee.cyclone.server.data;

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
@Table(name = "cyclone__server_category")
@UniqueConstraint(columnNames =
{
	"name"
})
public class ServerCategoryTable
		implements EntityBean
{
	private static final String _ebean_marker = ServerCategoryTable.class.getCanonicalName();
	public static final String[] _ebean_props = new String[]
	{
		"id", "name", "mask", "variables", "scoreCriterias", "parent"
	};
	private transient final EntityBeanIntercept _ebean_intercept = new EntityBeanIntercept(this);
	@Id
	private int id;
	@Column(nullable = false)
	private String name;
	@Lob
	private String mask;
	@OneToMany(targetEntity = ServerCategoryVariableTable.class, mappedBy = "serverCategory")
	private Set<ServerCategoryVariableTable> variables;
	@OneToMany(targetEntity = ServerCategoryScoreCriteriaTable.class, mappedBy = "serverCategory")
	private Set<ServerCategoryScoreCriteriaTable> scoreCriterias;
	@JoinColumn(name = "parent_id")
	@ManyToOne(targetEntity = ServerCategoryTable.class)
	private ServerCategoryTable parent;

	public ServerCategoryTable()
	{}

	public ServerCategoryTable(final String name)
	{
		this.name = name;
	}

	@Override
	public Object _ebean_newInstance()
	{
		return new ServerCategoryTable();
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

	private int _ebean_getni_id()
	{
		return id;
	}

	private void _ebean_setni_id(final int newValue)
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

	private String _ebean_getni_mask()
	{
		return mask;
	}

	private void _ebean_setni_mask(final String newValue)
	{
		mask = newValue;
	}

	private Set<ServerCategoryVariableTable> _ebean_getni_variables()
	{
		return variables;
	}

	private void _ebean_setni_variables(final Set<ServerCategoryVariableTable> newValue)
	{
		variables = newValue;
	}

	private Set<ServerCategoryScoreCriteriaTable> _ebean_getni_scoreCriterias()
	{
		return scoreCriterias;
	}

	private void _ebean_setni_scoreCriterias(final Set<ServerCategoryScoreCriteriaTable> newValue)
	{
		scoreCriterias = newValue;
	}

	private ServerCategoryTable _ebean_getni_parent()
	{
		return parent;
	}

	private void _ebean_setni_parent(final ServerCategoryTable newValue)
	{
		parent = newValue;
	}

	private int _ebean_get_id()
	{
//		_ebean_intercept.preGetter(0);

		return id;
	}

	private void _ebean_set_id(final int newValue)
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

	private String _ebean_get_mask()
	{
		_ebean_intercept.preGetter(2);

		return mask;
	}

	private void _ebean_set_mask(final String newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 2, _ebean_get_mask(), newValue);
		mask = newValue;
		_ebean_intercept.postSetter(event);
	}

	private Set<ServerCategoryVariableTable> _ebean_get_variables()
	{
		_ebean_intercept.preGetter(3);

		return variables;
	}

	private void _ebean_set_variables(final Set<ServerCategoryVariableTable> newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 3, _ebean_get_variables(), newValue);
		variables = newValue;
		_ebean_intercept.postSetter(event);
	}

	private Set<ServerCategoryScoreCriteriaTable> _ebean_get_scoreCriterias()
	{
		_ebean_intercept.preGetter(4);

		return scoreCriterias;
	}

	private void _ebean_set_scoreCriterias(final Set<ServerCategoryScoreCriteriaTable> newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 4, _ebean_get_scoreCriterias(), newValue);
		scoreCriterias = newValue;
		_ebean_intercept.postSetter(event);
	}

	private ServerCategoryTable _ebean_get_parent()
	{
		_ebean_intercept.preGetter(5);

		return parent;
	}

	private void _ebean_set_parent(final ServerCategoryTable newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 5, _ebean_get_parent(), newValue);
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
			return _ebean_getni_name();
		case 2:
			return _ebean_getni_mask();
		case 3:
			return _ebean_getni_variables();
		case 4:
			return _ebean_getni_scoreCriterias();
		case 5:
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
			return _ebean_get_name();
		case 2:
			return _ebean_get_mask();
		case 3:
			return _ebean_get_variables();
		case 4:
			return _ebean_get_scoreCriterias();
		case 5:
			return _ebean_get_parent();
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
			_ebean_setni_id((Integer) value);
			break;
		case 1:
			_ebean_setni_name((String) value);
			break;
		case 2:
			_ebean_setni_mask((String) value);
			break;
		case 3:
			_ebean_setni_variables((Set<ServerCategoryVariableTable>) value);
			break;
		case 4:
			_ebean_setni_scoreCriterias((Set<ServerCategoryScoreCriteriaTable>) value);
			break;
		case 5:
			_ebean_setni_parent((ServerCategoryTable) value);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void _ebean_setFieldIntercept(final int fieldIndex, final Object value)
	{
		switch (fieldIndex)
		{
		case 0:
			_ebean_set_id((Integer) value);
			break;
		case 1:
			_ebean_set_name((String) value);
			break;
		case 2:
			_ebean_set_mask((String) value);
			break;
		case 3:
			_ebean_set_variables((Set<ServerCategoryVariableTable>) value);
			break;
		case 4:
			_ebean_set_scoreCriterias((Set<ServerCategoryScoreCriteriaTable>) value);
			break;
		case 5:
			_ebean_set_parent((ServerCategoryTable) value);
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

	public int getId()
	{
		return _ebean_get_id();
	}

	public void setId(final int id)
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

	public String getMask()
	{
		return _ebean_get_mask();
	}

	public void setMask(final String mask)
	{
		_ebean_set_mask(mask);
	}

	public Set<ServerCategoryVariableTable> getVariables()
	{
		return _ebean_get_variables();
	}

	public void setVariables(final Set<ServerCategoryVariableTable> variables)
	{
		_ebean_set_variables(variables);
	}

	public Set<ServerCategoryScoreCriteriaTable> getScoreCriterias()
	{
		return _ebean_get_scoreCriterias();
	}

	public void setScoreCriterias(final Set<ServerCategoryScoreCriteriaTable> scoreCriterias)
	{
		_ebean_set_scoreCriterias(scoreCriterias);
	}

	public ServerCategoryTable getParent()
	{
		return _ebean_get_parent();
	}

	public void setParent(final ServerCategoryTable parent)
	{
		_ebean_set_parent(parent);
	}
}
