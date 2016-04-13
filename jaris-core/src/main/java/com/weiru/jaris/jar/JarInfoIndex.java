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

public interface JarInfoIndex {
    
	/**
	 * Add the jar name to the Indexer
	 * @param jarName the jar name
	 * @throws IndexException
	 */
    public void add(String jarName) throws IndexException;
    
    /**
     * Get the count of total jar names indexed
     * @return int the count
     * @throws IndexException
     */
    public int getCount() throws IndexException;
    
    /**
     * Search for the jar name with an expression
     * @param term the string expression
     * @return String[] All matching jar names 
     * @throws IndexException
     */
    public String[] search(String term) throws IndexException;
    
    /**
     * Delete all jar names matching the string expression.
     * @param term the string expression
     * @return int the number of jar names deleted
     * @throws IndexException
     */
    public int delete(String term) throws IndexException;
    
    /**
     * This method optimizes the underlying Indexer engine
     * @throws IndexException
     */
    public void optimizeIndex() throws IndexException;
}
