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

package com.valaphee.cyclone.serialization.template.builder;

import com.valaphee.cyclone.serialization.stream.SerializationReader;
import com.valaphee.cyclone.serialization.stream.SerializationWriter;
import com.valaphee.cyclone.serialization.template.Template;
import com.valaphee.cyclone.serialization.template.TemplateRegistry;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import javassist.*;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings(
		{
			"unchecked", "rawtypes"
		})
public final class CompileTemplateBuilder
		extends AbstractTemplateBuilder
{
	private final ClassPool classPool = new ClassPool();
	private ClassLoader classLoader;
	private String name;
	private CtClass template;

	public CompileTemplateBuilder(final TemplateRegistry registry)
	{
		super(registry);

		classPool.insertClassPath(new ClassClassPath(getClass()));
		classLoader = classPool.getClassLoader();
		try
		{
			if (classLoader != null)
			{
				classPool.appendClassPath(new LoaderClassPath(classLoader));
			}
			else
			{
				classPool.appendSystemPath();
			}
		}
		catch (final SecurityException ex)
		{
			throw new TemplateBuilderException("Cannot add the path to the classpool", ex);
		}
	}

	@Override
	public void appendClassLoader(final ClassLoader classLoader)
	{
		this.classLoader = classLoader;
	}

	@Override
	public boolean match(final Type type)
	{
		return (!((Class<?>) type).isEnum()) && (!((Class<?>) type).isInterface());
	}

	@Override
	public <T> Template<T> build(final Class<?> clazz, final TemplateEntry[] entries)
	{
		try
		{
			name = clazz.getName();
			template = classPool.makeClass("_$$_" + name + "Template");
			buildClass();
			buildConstructor();
			buildReadMethod(entries);
			buildWriteMethod(entries);

			final Template<Object>[] templates = new Template[entries.length];
			for (int i = 0; i < templates.length; i++)
			{
				templates[i] = registry.lookup(entries[i].getGenericType());
			}

			return (Template<T>) template.toClass(classLoader, getClass().getProtectionDomain()).getConstructor(new Class[]
			{
				Class.class, Template[].class
			}).newInstance(new Object[]
			{
				clazz, templates
			});
		}
		catch (final CannotCompileException | NotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex)
		{
			throw new TemplateBuilderException("Compilation process of " + name + " has failed.", ex);
		}
	}

	private void buildClass()
			throws CannotCompileException, NotFoundException
	{
		template.setSuperclass(classPool.get(CompileTemplate.class.getName()));
		template.addInterface(classPool.get(Template.class.getName()));
	}

	private void buildConstructor()
			throws CannotCompileException, NotFoundException
	{
		template.addConstructor(CtNewConstructor.make(new CtClass[]
		{
			classPool.get(Class.class.getName()), classPool.get(Template.class.getName() + "[]")
		}, new CtClass[0], template));
	}

	private void buildReadMethod(final TemplateEntry[] entries)
			throws CannotCompileException, NotFoundException
	{
		final StringBuilder readMethod = new StringBuilder();
		readMethod.append("{if($1.readNull()){return null;}");
		readMethod.append(String.format("%s _$$_t;", name));
		readMethod.append("if($2==null){");
		readMethod.append(String.format("_$$_t=new %s();", name));
		readMethod.append("}else{");
		readMethod.append(String.format("_$$_t=(%s)$2;", name));
		readMethod.append("}$1.readListBegin();");
		for (int i = 0; i < entries.length; i++)
		{
			final TemplateEntry entry = entries[i];
			readMethod.append("if(!$1.readNull()){");
			if (Modifier.isPrivate(entry.getField().getModifiers()))
			{
				readMethod.append(String.format("%s.readPrivateField(_$$_t,%s.class,\"%s\",$1,templates[%d]);", CompileTemplateBuilder.class.getName(), entry.getField().getDeclaringClass().getName(), entry.getName(), i));
			}
			else
			{
				if (entry.getType().isPrimitive())
				{
					readMethod.append(String.format("_$$_t.%s=$1.%s();", entry.getName(), getPrimitiveReadMethod(entry.getType())));
				}
				else
				{
					readMethod.append(String.format("_$$_t.%s=(%s)templates[%d].read($1,_$$_t.%s);", entry.getName(), entry.getType().getCanonicalName(), i, entry.getName()));
				}
			}
			readMethod.append("}");
		}
		readMethod.append("$1.readListEnd();return _$$_t;}");
		template.addMethod(CtNewMethod.make(Modifier.PUBLIC, classPool.get(Object.class.getName()), "read", new CtClass[]
		{
			classPool.get(SerializationReader.class.getName()), classPool.get(Object.class.getName())
		}, new CtClass[]
		{
			classPool.get(IOException.class.getName())
		}, readMethod.toString(), template));
	}

	private void buildWriteMethod(final TemplateEntry[] entries)
			throws CannotCompileException, NotFoundException
	{
		final StringBuilder writeMethod = new StringBuilder();
		writeMethod.append("{if($2==null){$1.writeNull();return;}");
		writeMethod.append(String.format("%s _$$_t=(%s)$2;", name, name));
		writeMethod.append(String.format("$1.writeListBegin(%d);", entries.length));
		for (int i = 0; i < entries.length; i++)
		{
			final TemplateEntry entry = entries[i];
			if (Modifier.isPrivate(entry.getField().getModifiers()))
			{
				writeMethod.append(String.format("if(%s.getPrivateField(_$$_t,%s.class,\"%s\")==null){", CompileTemplateBuilder.class.getName(), entry.getField().getDeclaringClass().getName(), entry.getName()));
				writeMethod.append("$1.writeNull();}else{");
				writeMethod.append(String.format("%s.writePrivateField(_$$_t,%s.class,\"%s\",$1,templates[%d]);", CompileTemplateBuilder.class.getName(), entry.getField().getDeclaringClass().getName(), entry.getName(), i));
				writeMethod.append("}");
			}
			else
			{
				if (entry.getType().isPrimitive())
				{
					writeMethod.append(String.format("$1.%s(_$$_t.%s);", getPrimitiveWriteMethod(entry.getType()), entry.getName()));
				}
				else
				{
					writeMethod.append(String.format("if(_$$_t.%s==null){", entry.getName()));
					writeMethod.append("$1.writeNull();}else{");
					writeMethod.append(String.format("templates[%d].write($1, _$$_t.%s);", i, entry.getName()));
					writeMethod.append("}");
				}
			}
		}
		writeMethod.append("$1.writeListEnd();}");
		template.addMethod(CtNewMethod.make(Modifier.PUBLIC, CtClass.voidType, "write", new CtClass[]
		{
			classPool.get(SerializationWriter.class.getName()), classPool.get(Object.class.getName())
		}, new CtClass[]
		{
			classPool.get(IOException.class.getName())
		}, writeMethod.toString(), template));
	}

	public static Object getPrivateField(final Object object, final Class<Object> clazz, final String name)
	{
		Field field = null;
		try
		{
			field = clazz.getDeclaredField(name);
			field.setAccessible(true);

			return field.get(object);
		}
		catch (final NoSuchFieldException | IllegalArgumentException | IllegalAccessException | SecurityException ignore)
		{}
		finally
		{
			if (field != null)
			{
				field.setAccessible(false);
			}
		}

		return null;
	}

	public static void readPrivateField(final Object object, final Class<Object> clazz, final String name, final SerializationReader reader, final Template<Object> template)
			throws IOException
	{
		Field field = null;
		try
		{
			field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			final Object value = template.read(reader, field.get(object));
			if (value != field.get(object))
			{
				field.set(object, value);
			}
		}
		catch (final NoSuchFieldException | IllegalArgumentException | IllegalAccessException | SecurityException ignore)
		{}
		finally
		{
			if (field != null)
			{
				field.setAccessible(false);
			}
		}
	}

	public static void writePrivateField(final Object object, final Class<Object> clazz, final String name, final SerializationWriter writer, final Template<Object> template)
			throws IOException
	{
		Field field = null;
		try
		{
			field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			template.write(writer, field.get(object));
		}
		catch (final NoSuchFieldException | IllegalArgumentException | IllegalAccessException | SecurityException ignore)
		{}
		finally
		{
			if (field != null)
			{
				field.setAccessible(false);
			}
		}
	}

	private String getPrimitiveReadMethod(final Class<?> clazz)
	{
		if (clazz == boolean.class)
		{
			return "readBoolean";
		}
		else if (clazz == byte.class)
		{
			return "readByte";
		}
		else if (clazz == short.class)
		{
			return "readShort";
		}
		else if (clazz == char.class)
		{
			return "readShort";
		}
		else if (clazz == int.class)
		{
			return "readInt";
		}
		else if (clazz == long.class)
		{
			return "readLong";
		}
		else if (clazz == float.class)
		{
			return "readFloat";
		}
		else if (clazz == double.class)
		{
			return "readDouble";
		}

		throw new TemplateBuilderException("Type " + clazz.getName() + " is not a primitive.");
	}

	private String getPrimitiveWriteMethod(final Class<?> clazz)
	{
		if (clazz == boolean.class)
		{
			return "writeBoolean";
		}
		else if (clazz == byte.class)
		{
			return "writeByte";
		}
		else if (clazz == short.class)
		{
			return "writeShort";
		}
		else if (clazz == char.class)
		{
			return "writeShort";
		}
		else if (clazz == int.class)
		{
			return "writeInt";
		}
		else if (clazz == long.class)
		{
			return "writeLong";
		}
		else if (clazz == float.class)
		{
			return "writeFloat";
		}
		else if (clazz == double.class)
		{
			return "writeDouble";
		}

		throw new TemplateBuilderException("Type " + clazz.getName() + " is not a primitive.");
	}

	public static abstract class CompileTemplate<T>
			implements Template<T>
	{
		protected final Class<?> clazz;
		protected final Template<Object>[] templates;

		public CompileTemplate(final Class<?> clazz, final Template<Object>[] templates)
		{
			this.clazz = clazz;
			this.templates = templates;
		}
	}
}
