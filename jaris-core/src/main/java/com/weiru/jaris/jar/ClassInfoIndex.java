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

import com.weiru.jaris.indexing.IndexException;

/**
 * <code>ClassInfoIndex</code> interface defines API methods for ClassInfoIndex service.
 */
public interface ClassInfoIndex {
    
	/**
	 * Add the ClassInfo object to the Indexer
	 * @param info the ClassInfo object
	 * @throws IndexException
	 */
    public void add(ClassInfo info) throws IndexException;

    /**
     * Get the count of total ClassInfo indexed
     * @return int the count
     * @throws IndexException
     */
    public int getCount() throws IndexException;
    
    /**
     * Search for matching ClassInfo with the criteria specified within a sample ClassInfo.
     * @param classInfo the sample ClassInfo
     * @return ClassInfo[] All matching ClassInfo[] 
     * @throws IndexException
     */
    public ClassInfo[] searchBySample(ClassInfo classInfo) throws IndexException;

    /**
     * Search for ClassInfo with a field matching the specified expression
     * @param field the field
     * @param term the specified expression
     * @return ClassInfo[] All matching ClassInfo[] 
     * @throws IndexException
     */
    public ClassInfo[] searchByTerm(ClassInfo.Field field, String term) throws IndexException;
    
    /**
	 * Delete all matching ClassInfo with the string expression of one field.
     * @param field the field
     * @param term the string expression
	 * @return int the number of ClassInfo deleted
	 * @throws IndexException
	 */
	public int deleteByTerm(ClassInfo.Field field, String term) throws IndexException;
    
	/**
	 * This method optimizes the underlying Indexer engine
	 * @throws IndexException
	 */
    public void optimizeIndex() throws IndexException;
}
