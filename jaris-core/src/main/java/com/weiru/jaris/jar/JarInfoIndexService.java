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
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.weiru.jaris.indexing.IndexException;
import com.weiru.jaris.indexing.IndexerFactory;
import com.weiru.jaris.indexing.Word;
import com.weiru.jaris.indexing.WordCollection;
import com.weiru.jaris.indexing.Indexer;

/**
 * <code>JarInfoIndexService</code> implements API methods 
 * defined in JarInfoIndex. It provides all neccessary functions
 * to indexer and search jar information. 
 */
public class JarInfoIndexService implements JarInfoIndex {

	private static JarInfoIndexService service;
	private Indexer indexer;
	private Logger log = Logger.getLogger(JarInfoIndexService.class);
	
	/**
	 * Private constructor
	 */
	private JarInfoIndexService() {
		try {
			indexer = IndexerFactory.getKeywordIndex(IndexServices.SERVICE_NAME_JAR_INFO);
		} catch (IndexException e) {
			log.error("constructor ", e);
		}
	}
	
	/**
	 * Get the singleton JarInfoIndexService
	 * @return JarInfoIndexService the singleton JarInfoIndexService
	 */
	public static synchronized JarInfoIndexService getService() {
		if (service==null) {
			service = new JarInfoIndexService();
		}
		return service;
	}

	/**
	 * Add the jar name to the Indexer
	 * @param jarName the jar name
	 * @throws IndexException
	 */
	public void add(String jarName) throws IndexException {
		WordCollection kwc = convertToKeywordContent(jarName);
		indexer.store(kwc);
	}

    /**
     * Get the count of total jar names indexed
     * @return int the count
     * @throws IndexException
     */
	public int getCount() throws IndexException {
		return indexer.getCount();
	}

    /**
     * Search for the jar name with an expression
     * @param term the string expression
     * @return String[] All matching jar names 
     * @throws IndexException
     */
	public String[] search(String term)
			throws IndexException {
		String field = ClassInfo.Field.JAR_NAME.getFieldName();
		WordCollection[] kwcs = indexer.searchByTerm(field, term);
		List kwList = new ArrayList();
		for (int i=0; i<kwcs.length; i++) {
			kwList.add(convertToJarName(kwcs[i]));
		}
		return (String[]) kwList.toArray( new String[kwList.size()]);
	}

	/**
     * Delete all jar names matching the string expression.
     * @param term the string expression
     * @return int the number of jar names deleted
     * @throws IndexException
     */
	public int delete(String term) throws IndexException {
		String field = ClassInfo.Field.JAR_NAME.getFieldName();
		return indexer.deleteByTerm(field, term);
	}

    /**
     * This method optimizes the underlying Indexer engine
     * @throws IndexException
     */
	public void optimizeIndex() throws IndexException {
		indexer.optimize();
	}

	/**
	 * Converts jar name to WordCollection
	 * @param jarName the jar name
	 * @return WordCollection the WordCollection object
	 */
	private WordCollection convertToKeywordContent(String jarName) {
		WordCollection kwc = new WordCollection();
		Word jarKW = new Word(ClassInfo.Field.JAR_NAME.getFieldName(), 
				jarName,
				true, 
				true, 
				true);
		kwc.addKeyword(jarKW);
		return kwc;
	}

	/**
	 * Converts WordCollection to jar name 
	 * @param kwc the WordCollection
	 * @return String the jar name
	 */
	private String convertToJarName(WordCollection kwc) {
		Iterator iter = kwc.iterateKeywords();
		Word kw = null;
		while (iter.hasNext()) {
			kw = (Word) iter.next();
			if (ClassInfo.Field.JAR_NAME.getFieldName().equals(kw.getName())) {
				return kw.getContent(); 
			}
		}
		return null;
	}	
}
