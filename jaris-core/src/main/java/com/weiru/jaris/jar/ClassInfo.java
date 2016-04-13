/*
 * Jaris - Jar Indexed Search
 * Copyright (C) 2006 Wei Ru (wei.ru@logicinspiration.com)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation (See 
 * license.txt for details).
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package com.weiru.jaris.jar;

import java.io.Serializable;

/**
 * The <code>ClassInfo</code> represents a class file.
 * 
 * @author Wei Ru
 */
public class ClassInfo implements Serializable {

	private static final long serialVersionUID = 5667694012142253654L;
	private String classFullName;
	private String jarName = "unknown";
	private String packageName;
	private String classShortName;
	private int internalId;
	
	/**
	 * Defines the fields that represents a class file
	 * 
	 * e.g.
	 * <code>Field.JAR_NAME</code>  
	 * <code>Field.PACKAGE_NAME</code>  
	 * <code>Field.CLASS_NAME</code>  
	 */
	public static final class Field implements Serializable {

		private static final long serialVersionUID = -2936172222286536942L;

		String fieldName;
		
	    private Field(String name) {
	      fieldName = name;
	    }

	    public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

	    public static final Field JAR_NAME = new Field("jn");

	    public static final Field PACKAGE_NAME = new Field("pn");

	    public static final Field CLASS_NAME = new Field("cn");

	}

	/**
	 * Creates an empty ClassInfo
	 */
	public ClassInfo() {
		super();
	}
	
	/**
	 * Creates a ClassInfo with a specified fully qualified class name
	 * @param classFullName the fully qualified class name
	 */
	public ClassInfo(String classFullName) {
		this(classFullName, "unknown");
	}
	
	/**
	 * Creates a ClassInfo with a specified fully qualified class name and containing jar
	 * 
	 * @param classFullName the fully qualified class name
	 * @param jarName the containing jar file name
	 */
	public ClassInfo(String classFullName, String jarName) {
		super();
		this.classFullName = classFullName;
		this.jarName = jarName;
		validate();
	}

	/**
	 * Gets the jar file name
	 * @return String the jar file name 
	 */
	public String getJarName() {
		return jarName;
	}

	/**
	 * Sets the jar file name
	 * @param jarName the jar file name
	 */
	public void setJarName(String jarName) {
		this.jarName = jarName;
	}

	/**
	 * Returns the fully qualified class name
	 * @return String the fully qualified class name
	 */
	public String getClassFullName() {
		return classFullName;
	}

	/**
	 * Sets the fully qualified class name
	 * @param classFullName the fully qualified class name
	 */
	public void setClassFullName(String classFullName) {
		this.classFullName = classFullName;
	}

	/**
	 * Sets the class short name
	 * @param classShortName the class short name
	 */
	public void setClassShortName(String classShortName) {
		this.classShortName = classShortName;
	}

	/**
	 * Sets the class package name
	 * @param packageName the package name
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	/**
	 * Returns the class short name
	 * @return String the class short name
	 */
	public String getClassShortName() {
		return classShortName;
	}

	/**
	 * Returns the class package name
	 * @return String the class package name
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * Gets the internal id
	 * @return int the internal id
	 */
	public int getInternalId() {
		return internalId;
	}

	/**
	 * Sets the internal id
	 * @param internalId the internal id
	 */
	public void setInternalId(int internalId) {
		this.internalId = internalId;
	}
	
	/**
	 * This method will synchronize all fields 
	 * @return the validated ClassInfo object
	 */
	public ClassInfo validate() {
		if (classFullName!=null) {
			this.packageName = validatePackageName();
			this.classShortName = validateClassShortName();
		} else {
			if (classShortName!=null&&packageName!=null) {
				this.classFullName = validateClassFullName();
			}
		}
		return this;
	}
	
	/**
	 * Synchronize the package name
	 * @return String the synchronized package name
	 */
	public String validatePackageName() {
		String pn = "";
		int idx = classFullName.lastIndexOf(".");
		if (idx>0) {
			pn = classFullName.substring(0, idx);
		}
		return pn;
	}

	/**
	 * Synchronize the class short name
	 * @return String the synchronized class short name
	 */
	public String validateClassShortName() {
		String sn = classFullName;
		int idx = classFullName.lastIndexOf(".");
		if (idx>=0) {
			sn = classFullName.substring(idx + 1);
		}
		return sn;
	}

	/**
	 * Synchronize the fully qualified class name
	 * @return String the fully qualified class name
	 */
	public String validateClassFullName() {
		String fn = classShortName;
		if (packageName!=null&&!"".equals(packageName)) {
			fn = packageName+"."+classShortName;
		}
		return fn;
	}
	
	/**
	 * Returns the String representation of this object
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(classShortName);
		sb.append("  ");
		sb.append(classFullName);
		sb.append("   ");
		sb.append(jarName);
		return sb.toString();
	}

}
