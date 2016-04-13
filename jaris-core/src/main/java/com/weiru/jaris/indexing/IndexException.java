/*
 * Jaris - Jar Indexed Search
 * Copyright (C) 2006 Wei Ru
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
package com.weiru.jaris.indexing;

/**
 * Exception thrown by Indexer engine methods to indicate a problem
 * 
 * @author Wei Ru
 */
public class IndexException extends RuntimeException {
    
    private static final long serialVersionUID = -2512299322365826108L;

	/**
     * Creates a new instance of <code>IndexException</code> without message
     */
    public IndexException() {
    }
    
    /**
     * Constructs an instance of <code>IndexException</code> with the specified detail message.
     * @param msg the detail message.
     * @param t the associated Throwable.
     */
    public IndexException(String msg, Throwable t) {
        super(msg, t);
    }
}
