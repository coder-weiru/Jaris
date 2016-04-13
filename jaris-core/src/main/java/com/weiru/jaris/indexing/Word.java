package com.weiru.jaris.indexing;

import java.io.Serializable;

/**
 * Word is a basic unit that can be indexed. A Word has a name and its associated content.
 * Word can be persistent or non-persistent (i.e. stored or not stored in the search engine)
 * and searchable or non-searchable (i.e indexed or not indexed). Since Word is the basic unit 
 * it will not be further tokenized by search engine. 
 * 
 * @author Wei Ru
 */
public class Word implements Serializable {

	private static final long serialVersionUID = -4165424693219347343L;
	private boolean persistent;
	private boolean tokenizable;
	private boolean defaultField;
	private boolean searchable;
	private String name;
	private String content;

	/**
	 * Create a Word with a name and the content
	 * @param name
	 * @param content
	 */
	public Word(String name, String content) {
		super();
		this.name = name;
		this.content = content;
	}

	/**
	 * Create a Word with a name and the content specifying if it should be persistent and/or searchable  
	 * @param name
	 * @param content
	 * @param persistent
	 * @param searchable
	 */
	public Word(String name, String content, boolean persistent, boolean tokenizable) {
		super();
		this.persistent = persistent;
		this.tokenizable = tokenizable;
		this.name = name;
		this.content = content;
	}

	/**
	 * Create a Word with a name and the content specifying if it should be persistent, searchable and/or if it is the default field for the document 
	 * @param name
	 * @param content
	 * @param persistent
	 * @param searchable
	 * @param defaultField
	 */
	public Word(String name, String content, boolean persistent, boolean tokenizable, boolean defaultField) {
		super();
		this.persistent = persistent;
		this.tokenizable = tokenizable;
		this.defaultField = defaultField;
		this.name = name;
		this.content = content;
	}
	
	/**
	 * Return the content
	 * @return String The Word content
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * Set the content 
	 * @param content The Word content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Return if this Word is the default field in a document 
	 * @return boolean If it is the default field
	 */
	public boolean isDefaultField() {
		return defaultField;
	}
	
	/**
	 * Set if this Word is the default field in a document
	 * @param defaultField If it is the default field
	 */
	public void setDefaultField(boolean defaultField) {
		this.defaultField = defaultField;
	}

	/**
	 * Return the name
	 * @return String the Word name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name
	 * @param name The Word name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return if this Word needs to be persistent
	 * @return boolean If it is persistent
	 */
	public boolean isPersistent() {
		return persistent;
	}

	/**
	 * Set if this Word needs to be persistent
	 * @param persistent If it needs to be persistent
	 */
	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	/**
	 * Return if this Word needs to be tokenizable
	 * 
	 * @return boolean If it is tokenizable
	 */
	public boolean isTokenizable() {
		return tokenizable;
	}

	/**
	 * Set if this Word needs to be tokenizable
	 * 
	 * @param searchable
	 *            If it needs to be tokenizable
	 */
	public void setTokenizable(boolean tokenizable) {
		this.tokenizable = tokenizable;
	}

	/**
	 * Return if this Word needs to be searchable
	 * 
	 * @return boolean If it is searchable
	 */
	public boolean isSearchable() {
		return searchable;
	}

	/**
	 * Set if this Word needs to be searchable
	 * 
	 * @param searchable
	 *            If it needs to be searchable
	 */
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}
	
}
