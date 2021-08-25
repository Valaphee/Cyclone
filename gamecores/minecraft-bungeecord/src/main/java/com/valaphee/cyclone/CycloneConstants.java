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

package com.valaphee.cyclone;

import java.io.File;

/**
 * Default
 *
 * @author valaphee
 */
public final class CycloneConstants
{
	public static final String FILE_ENCODING = System.getProperty("file.encoding"),
			FILE_SEPARATOR = System.getProperty("file.separator");
	public static final String USER_NAME = System.getProperty("user.name"),
			USER_COUNTRY = System.getProperty("user.country", System.getProperty("user.region")),
			USER_TIMEZONE = System.getProperty("user.timezone"),
			USER_LANGUAGE = System.getProperty("user.language");
	public static final File USER_HOME_DIRECTORY = new File(System.getProperty("user.home", ".")),
			USER_WORKING_DIRECTORY = new File(System.getProperty("user.dir", "."));
	public static final String JAVA_VENDOR = System.getProperty("java.vendor"),
			JAVA_VENDOR_URL = System.getProperty("java.vendor.url"),
			JAVA_VERSION = System.getProperty("java.version"),
			JAVA_RUNTIME_NAME = System.getProperty("java.runtime.name"),
			JAVA_RUNTIME_VERSION = System.getProperty("java.runtime.version"),
			JAVA_SPECIFICATION_VENDOR = System.getProperty("java.specification.vendor"),
			JAVA_SPECIFICATION_NAME = System.getProperty("java.specification.name"),
			JAVA_SPECIFICATION_VERSION = System.getProperty("java.specification.version"),
			JAVA_VM_VENDOR = System.getProperty("java.vm.vendor"),
			JAVA_VM_NAME = System.getProperty("java.vm.name"),
			JAVA_VM_VERSION = System.getProperty("java.vm.version"),
			JAVA_VM_INFO = System.getProperty("java.vm.info"),
			JAVA_VM_SPECIFICATION_VENDOR = System.getProperty("java.vm.specification.vendor"),
			JAVA_VM_SPECIFICATION_NAME = System.getProperty("java.vm.specification.name"),
			JAVA_VM_SPECIFICATION_VERSION = System.getProperty("java.vm.specification.version"),
			JAVA_COMPILER = System.getProperty("java.compiler"),
			JAVA_CLASS_PATH = System.getProperty("java.class.path"),
			JAVA_CLASS_VERSION = System.getProperty("java.class.version");
	public static final File JAVA_HOME_DIRECTORY = new File(System.getProperty("java.home", ".")),
			JAVA_TMP_DIRECTORY = new File(System.getProperty("java.io.tmpdir", "."));
	public static final String JAVA_LIBRARY_DIRECTORIES = System.getProperty("java.library.path"),
			JAVA_ENDORSED_DIRECTORIES = System.getProperty("java.endorsed.dirs"),
			JAVA_EXT_DIRECTORIES = System.getProperty("java.ext.dirs");
	public static final String OS_NAME = System.getProperty("os.name"),
			OS_ARCHITECTURE = System.getProperty("os.arch"),
			OS_VERSION = System.getProperty("os.version");
	public static final String LINE_SEPARATOR = System.getProperty("line.separator"),
			PATH_SEPARATOR = System.getProperty("path.separator");
	public static final boolean IS_JAVA_1 = JAVA_SPECIFICATION_VERSION.startsWith("1.1"),
			IS_JAVA_2 = JAVA_SPECIFICATION_VERSION.startsWith("1.2"),
			IS_JAVA_3 = JAVA_SPECIFICATION_VERSION.startsWith("1.3"),
			IS_JAVA_4 = JAVA_SPECIFICATION_VERSION.startsWith("1.4"),
			IS_JAVA_5 = JAVA_SPECIFICATION_VERSION.startsWith("1.5"),
			IS_JAVA_6 = JAVA_SPECIFICATION_VERSION.startsWith("1.6"),
			IS_JAVA_7 = JAVA_SPECIFICATION_VERSION.startsWith("1.7"),
			IS_JAVA_8 = JAVA_SPECIFICATION_VERSION.startsWith("1.8");
	public static final boolean IS_WINDOWS = OS_NAME.startsWith("Windows"),
			IS_APPLE = OS_NAME.startsWith("Mac"),
			IS_LINUX = !(IS_WINDOWS && IS_APPLE);

	private CycloneConstants()
	{}
}
