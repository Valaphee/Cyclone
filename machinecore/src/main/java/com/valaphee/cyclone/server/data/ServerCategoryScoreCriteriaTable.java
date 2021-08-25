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
import javax.persistence.*;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "cyclone__server_category_score_criteria")
@UniqueConstraint(columnNames =
{
	"server_category_id", "name"
})
public class ServerCategoryScoreCriteriaTable
		implements EntityBean
{
	private static final String _ebean_marker = ServerCategoryScoreCriteriaTable.class.getCanonicalName();
	public static final String[] _ebean_props = new String[]
	{
		"id", "serverCategory", "name", "prescaler", "remainder"
	};
	private transient final EntityBeanIntercept _ebean_intercept = new EntityBeanIntercept(this);
	@Id
	private int id;
	@JoinColumn(name = "server_category_id")
	@ManyToOne(targetEntity = ServerCategoryTable.class, optional = false)
	private ServerCategoryTable serverCategory;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private double prescaler;
	@Column(nullable = false)
	private double remainder;

	public ServerCategoryScoreCriteriaTable()
	{}

	public ServerCategoryScoreCriteriaTable(final ServerCategoryTable serverCategory, final String name, final double prescaler, final double remainder)
	{
		this.serverCategory = serverCategory;
		this.name = name;
		this.prescaler = prescaler;
		this.remainder = remainder;
	}

	@Override
	public Object _ebean_newInstance()
	{
		return new ServerCategoryScoreCriteriaTable();
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

	private ServerCategoryTable _ebean_getni_server_category()
	{
		return serverCategory;
	}

	private void _ebean_setni_server_category(final ServerCategoryTable newValue)
	{
		serverCategory = newValue;
	}

	private String _ebean_getni_name()
	{
		return name;
	}

	private void _ebean_setni_name(final String newValue)
	{
		name = newValue;
	}

	private double _ebean_getni_prescaler()
	{
		return prescaler;
	}

	private void _ebean_setni_prescaler(final double newValue)
	{
		prescaler = newValue;
	}

	private double _ebean_getni_remainder()
	{
		return remainder;
	}

	private void _ebean_setni_remainder(final double newValue)
	{
		remainder = newValue;
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

	private ServerCategoryTable _ebean_get_server_category()
	{
		_ebean_intercept.preGetter(1);

		return serverCategory;
	}

	private void _ebean_set_server_category(final ServerCategoryTable newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 1, _ebean_get_server_category(), newValue);
		serverCategory = newValue;
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

	private double _ebean_get_prescaler()
	{
		_ebean_intercept.preGetter(3);

		return prescaler;
	}

	private void _ebean_set_prescaler(final double newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 3, _ebean_get_prescaler(), newValue);
		prescaler = newValue;
		_ebean_intercept.postSetter(event);
	}

	private double _ebean_get_remainder()
	{
		_ebean_intercept.preGetter(4);

		return remainder;
	}

	private void _ebean_set_remainder(final double newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 4, _ebean_get_remainder(), newValue);
		remainder = newValue;
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
			return _ebean_getni_server_category();
		case 2:
			return _ebean_getni_name();
		case 3:
			return _ebean_getni_prescaler();
		case 4:
			return _ebean_getni_remainder();
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
			return _ebean_get_server_category();
		case 2:
			return _ebean_get_name();
		case 3:
			return _ebean_get_prescaler();
		case 4:
			return _ebean_get_remainder();
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
			_ebean_setni_id((Integer) value);
			break;
		case 1:
			_ebean_setni_server_category((ServerCategoryTable) value);
			break;
		case 2:
			_ebean_setni_name((String) value);
			break;
		case 3:
			_ebean_setni_prescaler((Double) value);
			break;
		case 4:
			_ebean_setni_remainder((Double) value);
		}
	}

	@Override
	public void _ebean_setFieldIntercept(final int fieldIndex, final Object value)
	{
		switch (fieldIndex)
		{
		case 0:
			_ebean_set_id((Integer) value);
			break;
		case 1:
			_ebean_set_server_category((ServerCategoryTable) value);
			break;
		case 2:
			_ebean_set_name((String) value);
			break;
		case 3:
			_ebean_set_prescaler((Double) value);
			break;
		case 4:
			_ebean_set_remainder((Double) value);
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

	public ServerCategoryTable getServerCategory()
	{
		return _ebean_get_server_category();
	}

	public void setServerCategory(final ServerCategoryTable serverCategory)
	{
		_ebean_set_server_category(serverCategory);
	}

	public String getName()
	{
		return _ebean_get_name();
	}

	public void setName(final String name)
	{
		_ebean_set_name(name);
	}

	public double getPrescaler()
	{
		return _ebean_get_prescaler();
	}

	public void setPrescaler(final double prescaler)
	{
		_ebean_set_prescaler(prescaler);
	}

	public double getRemainder()
	{
		return _ebean_get_remainder();
	}

	public void setRemainder(final double remainder)
	{
		_ebean_set_remainder(remainder);
	}
}
