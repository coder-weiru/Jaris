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
import java.util.Enumeration;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.weiru.jaris.config.Config;
import com.weiru.jaris.indexing.IndexException;

/**
 * The <code>IndexBuilder</code> can be used to build indexes for a specified jar.
 * 
 * @author Wei Ru
 */
public class IndexBuilder {
	
	public boolean debugOn=false;
	private static IndexBuilder instance;
	private Logger log = Logger.getLogger(IndexBuilder.class);
	private ClassInfoIndexService classIndex;
	private JarInfoIndexService jarIndex;
	
	private IndexBuilder() {
		classIndex = ClassInfoIndexService.getService();
		jarIndex = JarInfoIndexService.getService();
	}
	
	/**
	 * Returns the singleton IndexBuilder
	 * @return IndexBuilder
	 */
	public static synchronized IndexBuilder getInstance() {
		if (instance==null) {
			instance = new IndexBuilder();
		}
		return instance;
	}
	
	/**
	 * Builds the Indexer for a jar file
	 * @param jarFilePath the jar file path
	 */
	public void buildIndex(String jarFilePath) {
		JarResource jarRes = new JarResource(jarFilePath);
		jarRes.parseClasses();
		buildIndex(jarRes);
	}
	
	/**
	 * Builds the Indexer for a jar file
	 * @param jarFile the jar file
	 */
	public void buildIndex(File jarFile) {
		JarResource jarRes = new JarResource(jarFile);
		jarRes.parseClasses();
		buildIndex(jarRes);
	}
	
	/**
	 * Builds the Indexer for a jar file
	 * @param jarFileDirectory the jar file directory
	 * @param jarFile the jar file
	 */
	public void buildIndex(String jarFileDirectory, String jarFile) {
		JarResource jarRes = new JarResource(jarFileDirectory, jarFile);
		jarRes.parseClasses();
		buildIndex(jarRes);
	}
	
	/**
	 * Builds the Indexer for a JarResource object
	 * @param jarRes the JarResource object
	 */
	private void buildIndex(JarResource jarRes) {
		String jarName = jarRes.getJarFileName();
		
		Configuration config = Config.getConfiguration();
		boolean skipIfProcessed = config.getBoolean("skip-if-processed");
		boolean exists = this.existsJarIndex(jarName);
		
		if (skipIfProcessed&&exists) {	return; 	}
		
		Enumeration en = jarRes.getClassNames();
		String fqn = null;
		String key = null;
		ClassInfo ci = null;
		while (en.hasMoreElements()) {
			key = (String)en.nextElement();
			fqn = jarRes.getClassFQN(key);
			ci = new ClassInfo(fqn);
			ci.validate();
			try {
				if (debugOn) {
			       log.debug("indexing : " + ci);
			    }
				classIndex.add(ci);
			} catch (IndexException e) {
				log.error("buildIndex(JarResource jarRes)  : " + e, e);
			}
		}
		
		registerJarIndex(jarName);
	}
	
	/**
	 * Checks to see if the jar has been indexed already
	 * @param jarName the jar name
	 * @return boolean if it has been built
	 */
	private boolean existsJarIndex(String jarName) {
		String[] results = null;
		try {
			results = jarIndex.search(jarName);
		} catch (IndexException e) {
			log.error("existsJarIndex : " + e, e);
		} 
		return (results!=null&&results.length==1);
	}
	
	/**
	 * Registers the jar name as been indexed
	 * @param jarName the jar name
	 */
	private void registerJarIndex(String jarName) {
		try {
			jarIndex.add(jarName);
		} catch (IndexException e) {
			log.error("registerJarIndex : " + e, e);
		} 
	}
	
   /**
    * If the debug flag is set
    * @return boolean set or not
    */
	public boolean isDebugOn() {
		return debugOn;
	}

	/**
	 * Sets the debugging flag
	 * @param debugOn the debugging flag
	 */
	public void setDebugOn(boolean debugOn) {
		this.debugOn = debugOn;
	}

}
