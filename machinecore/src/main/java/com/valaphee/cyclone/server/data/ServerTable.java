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
import java.util.UUID;
import javax.persistence.*;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "cyclone__server")
public class ServerTable
		implements EntityBean
{
	private static final String _ebean_marker = ServerTable.class.getCanonicalName();
	public static final String[] _ebean_props = new String[]
	{
		"id", "serverCategory", "node"
	};
	private transient final EntityBeanIntercept _ebean_intercept = new EntityBeanIntercept(this);
	@Id
	private UUID id;
	@JoinColumn(name = "server_category_id", nullable = false)
	@ManyToOne(targetEntity = ServerCategoryTable.class)
	private ServerCategoryTable serverCategory;
	@Column(nullable = false)
	private UUID node;

	public ServerTable()
	{}

	public ServerTable(final UUID id)
	{
		this.id = id;
	}

	@Override
	public Object _ebean_newInstance()
	{
		return new ServerTable();
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

	private UUID _ebean_getni_id()
	{
		return id;
	}

	private void _ebean_setni_id(final UUID newValue)
	{
		id = newValue;
	}

	private ServerCategoryTable _ebean_getni_serverCategory()
	{
		return serverCategory;
	}

	private void _ebean_setni_serverCategory(final ServerCategoryTable newValue)
	{
		serverCategory = newValue;
	}

	private UUID _ebean_getni_node()
	{
		return node;
	}

	private void _ebean_setni_node(final UUID newValue)
	{
		node = newValue;
	}

	private UUID _ebean_get_id()
	{
		_ebean_intercept.preGetter(0);

		return id;
	}

	private void _ebean_set_id(final UUID newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 0, _ebean_get_id(), newValue);
		id = newValue;
		_ebean_intercept.postSetter(event);
	}

	private ServerCategoryTable _ebean_get_serverCategory()
	{
		_ebean_intercept.preGetter(1);

		return serverCategory;
	}

	private void _ebean_set_serverCategory(final ServerCategoryTable newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 1, _ebean_get_serverCategory(), newValue);
		serverCategory = newValue;
		_ebean_intercept.postSetter(event);
	}

	private UUID _ebean_get_node()
	{
		_ebean_intercept.preGetter(2);

		return node;
	}

	private void _ebean_set_node(final UUID newValue)
	{
		final PropertyChangeEvent event = _ebean_intercept.preSetter(true, 2, _ebean_get_node(), newValue);
		node = newValue;
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
			return _ebean_getni_serverCategory();
		case 2:
			return _ebean_getni_node();
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
			return _ebean_get_serverCategory();
		case 2:
			return _ebean_get_node();
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
			_ebean_setni_id((UUID) value);
			break;
		case 1:
			_ebean_setni_serverCategory((ServerCategoryTable) value);
			break;
		case 2:
			_ebean_setni_node((UUID) value);
		}
	}

	@Override
	public void _ebean_setFieldIntercept(final int fieldIndex, final Object value)
	{
		switch (fieldIndex)
		{
		case 0:
			_ebean_set_id((UUID) value);
			break;
		case 1:
			_ebean_set_serverCategory((ServerCategoryTable) value);
			break;
		case 2:
			_ebean_set_node((UUID) value);
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

	public UUID getId()
	{
		return _ebean_get_id();
	}

	public void setId(final UUID id)
	{
		_ebean_set_id(id);
	}

	public ServerCategoryTable getServerCategory()
	{
		return _ebean_get_serverCategory();
	}

	public void setServerCategory(final ServerCategoryTable serverCategory)
	{
		_ebean_set_serverCategory(serverCategory);
	}

	public UUID getNode()
	{
		return _ebean_get_node();
	}

	public void setNode(final UUID node)
	{
		_ebean_set_node(node);
	}
}
