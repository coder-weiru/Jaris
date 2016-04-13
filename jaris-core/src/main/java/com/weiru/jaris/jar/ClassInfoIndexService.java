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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.weiru.jaris.indexing.IndexException;
import com.weiru.jaris.indexing.IndexerFactory;
import com.weiru.jaris.indexing.Word;
import com.weiru.jaris.indexing.WordCollection;
import com.weiru.jaris.indexing.Indexer;

/**
 * <code>ClassInfoIndexService</code> implements API methods 
 * defined in ClassInfoIndex. It provides all neccessary functions
 * to indexer and search class information. 
 */
public class ClassInfoIndexService implements ClassInfoIndex {

	private static ClassInfoIndexService service;
	private Indexer indexer;
	private Logger log = Logger.getLogger(ClassInfoIndexService.class);
	private HashMap persistConfig = new HashMap();
	private HashMap indexConfig = new HashMap();
	
	/**
	 * Private constructor
	 */
	private ClassInfoIndexService() {
		try {
			indexer = IndexerFactory.getKeywordIndex(IndexServices.SERVICE_NAME_CLASS_INFO);
		} catch (IndexException e) {
			log.error("constructor ", e);
		}
		indexConfig.put(ClassInfo.Field.CLASS_NAME, new Boolean(true));
		indexConfig.put(ClassInfo.Field.JAR_NAME, new Boolean(true));
		indexConfig.put(ClassInfo.Field.PACKAGE_NAME, new Boolean(true));
		persistConfig.put(ClassInfo.Field.CLASS_NAME, new Boolean(true));
		persistConfig.put(ClassInfo.Field.JAR_NAME, new Boolean(true));
		persistConfig.put(ClassInfo.Field.PACKAGE_NAME, new Boolean(true));
	}
	
	/**
	 * Get the singleton ClassInfoIndexService
	 * @return ClassInfoIndexService the singleton ClassInfoIndexService
	 */
	public static synchronized ClassInfoIndexService getService() {
		if (service==null) {
			service = new ClassInfoIndexService();
		}
		return service;
	}
	
	/**
	 * Add the ClassInfo object to the Indexer
	 * @param info the ClassInfo object
	 * @throws IndexException
	 */
	public void add(ClassInfo info) throws IndexException {
		WordCollection kwc = convertToKeywordContent(info);
		indexer.store(kwc);
	}

    /**
     * Get the count of total ClassInfo indexed
     * @return int the count
     * @throws IndexException
     */
	public int getCount() throws IndexException {
		return indexer.getCount();
	}

    /**
     * Search for matching ClassInfo with the criteria specified within a sample ClassInfo.
     * @param classInfo the sample ClassInfo
     * @return ClassInfo[] All matching ClassInfo[] 
     * @throws IndexException
     */
	public ClassInfo[] searchBySample(ClassInfo classInfo)
			throws IndexException {
		WordCollection sample = convertToKeywordContent(classInfo);
		WordCollection[] kwcs = indexer.searchBySample(sample);
		List kwList = new ArrayList();
		for (int i=0; i<kwcs.length; i++) {
			kwList.add(convertToClassInfo(kwcs[i]));
		}
		return (ClassInfo[]) kwList.toArray( new ClassInfo[kwList.size()]);
	}

    /**
     * Search for ClassInfo with a field matching the specified expression
     * @param field the field
     * @param term the specified expression
     * @return ClassInfo[] All matching ClassInfo[] 
     * @throws IndexException
     */
	public ClassInfo[] searchByTerm(ClassInfo.Field field, String term)
			throws IndexException {
		WordCollection[] kwcs = indexer.searchByTerm(field.getFieldName(), term);
		List kwList = new ArrayList();
		for (int i=0; i<kwcs.length; i++) {
			kwList.add(convertToClassInfo(kwcs[i]));
		}
		return (ClassInfo[]) kwList.toArray( new ClassInfo[kwList.size()]);
	}

    /**
	 * Delete all matching ClassInfo with the string expression of one field.
     * @param field the field
     * @param term the string expression
	 * @return int the number of ClassInfo deleted
	 * @throws IndexException
	 */
	public int deleteByTerm(ClassInfo.Field field, String term) throws IndexException {
		return indexer.deleteByTerm(field.getFieldName(), term);
	}

	/**
	 * This method optimizes the underlying Indexer engine
	 * @throws IndexException
	 */
	public void optimizeIndex() throws IndexException {
		indexer.optimize();
	}

	/**
	 * Provides convertion from ClassInfo to WordCollection
	 * @param info the ClassInfo object
	 * @return WordCollection the WordCollection object
	 */
	private WordCollection convertToKeywordContent(ClassInfo info) {
		WordCollection kwc = new WordCollection();
		Word jarName = new Word(ClassInfo.Field.JAR_NAME.getFieldName(), 
				info.getJarName(),
				((Boolean)indexConfig.get(ClassInfo.Field.JAR_NAME)).booleanValue(), 
				((Boolean)persistConfig.get(ClassInfo.Field.JAR_NAME)).booleanValue(), 
				false);
		Word packageName = new Word(ClassInfo.Field.PACKAGE_NAME.getFieldName(), 
				info.getPackageName(),
				((Boolean)indexConfig.get(ClassInfo.Field.PACKAGE_NAME)).booleanValue(), 
				((Boolean)persistConfig.get(ClassInfo.Field.PACKAGE_NAME)).booleanValue(), 
				false);
		Word className = new Word(ClassInfo.Field.CLASS_NAME.getFieldName(), 
				info.getClassShortName(),
				((Boolean)indexConfig.get(ClassInfo.Field.CLASS_NAME)).booleanValue(), 
				((Boolean)persistConfig.get(ClassInfo.Field.CLASS_NAME)).booleanValue(), 
				true);
		kwc.addKeyword(jarName);
		kwc.addKeyword(packageName);
		kwc.addKeyword(className);
		
		return kwc;
	}
	
	/**
	 * Provides convertion from WordCollection to ClassInfo
	 * @param kwc the WordCollection object
	 * @return ClassInfo the ClassInfo object
	 */
	private ClassInfo convertToClassInfo(WordCollection kwc) {
		ClassInfo info = new ClassInfo();
		Iterator iter = kwc.iterateKeywords();
		Word kw = null;
		while (iter.hasNext()) {
			kw = (Word) iter.next();
			if (ClassInfo.Field.JAR_NAME.getFieldName().equals(kw.getName())) {
				info.setJarName(kw.getContent());
			} else if (ClassInfo.Field.PACKAGE_NAME.getFieldName().equals(kw.getName())) {
				info.setPackageName(kw.getContent());
			} else if (ClassInfo.Field.CLASS_NAME.getFieldName().equals(kw.getName())) {
				info.setClassShortName(kw.getContent());
			}
		}
		info.validate();
		log.debug("converted to ClassInfo: " + info);
		return info;
	}	
}
