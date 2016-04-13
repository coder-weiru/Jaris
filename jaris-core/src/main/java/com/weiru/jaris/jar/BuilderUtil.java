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

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.Configuration;

import com.weiru.jaris.config.Config;

/**
 * The <code>BuilderUtil</code> provides some utility methods for building Indexes
 * 
 * @author Wei Ru
 */
public class BuilderUtil {

	/**
	 * Builds Indexes recursivly for all the jars under a directory
	 * @param directory the directory contains jar files
	 */
	public static void buildIndexUnderDir(String directory) {
		buildIndexUnderDir( new File(directory) );
	}
	
	/**
	 * Builds Indexes recursivly for all the jars under a directory
	 * @param directory the directory contains jar files
	 */
	public static void buildIndexUnderDir(File directory) {
		
		if (directory.isDirectory()) {
            String[] children = directory.list();
            for (int i=0; i<children.length; i++) {
            	buildIndexUnderDir(new File(directory, children[i]));
            }
        } else {
        	File file = directory;
        	String name = file.getName();
        	if (file.isFile() && rightFileTypeToProcess(name)) {
        			IndexBuilder builder = IndexBuilder.getInstance();
            		builder.buildIndex(file);
        	}
        	
        }
	}
	
	/**
	 * Check to see if the file type should be processed. 
	 * This is configured in an external configuration file.
	 * @param filename the name of the file
	 * @return boolean whether it should be processed
	 */
	private static boolean rightFileTypeToProcess(String filename) {
		Configuration config = Config.getConfiguration();
		List fileTypes = config.getList("file-type");
		Iterator iter = fileTypes.iterator();
		String type = null;
		while (iter.hasNext()) {
			type = (String) iter.next();
			if (filename!=null&&filename.indexOf(type)>1) {
				return true;
			}
		}
		return false;
	}
	
}
