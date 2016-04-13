package com.weiru.jaris.indexing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

/**
 * The <code>WordCollection</code> is an entity that contains a list of
 * Keywords, each Word can be separately indexed and stored. Multi-criteria
 * search for the document is based on each indexed Word.
 * 
 * @author Wei Ru
 */
public class WordCollection {

	private List<Word> wordList;
	private int internalId;

	/**
	 * Constructor
	 */
	public WordCollection() {
		super();
		wordList = new ArrayList<Word>();
	}

	/**
	 * Add a Word to the document
	 * 
	 * @param word
	 *            a Word
	 */
	public void addKeyword(Word word) {
		this.wordList.add(word);
	}

	/**
	 * Returns an iterator of the words
	 * 
	 * @return Iterator the iterator of the words
	 */
	public Iterator<Word> iterator() {
		return this.wordList.iterator();
	}

	/**
	 * Removes a Word
	 * 
	 * @param word
	 *            the Word
	 */
	public void remove(Word word) {
		this.wordList.remove(word);
	}

	/**
	 * Returns the internal id of the document
	 * 
	 * @return int the internal id
	 */
	public int getInternalId() {
		return internalId;
	}

	/**
	 * Sets the internal id of the document
	 * 
	 * @param internalId
	 *            the internal id of the document
	 */
	public void setInternalId(int internalId) {
		this.internalId = internalId;
	}

	/**
	 * Converts a WordCollection to a Lucene Search Engine Document using the
	 * instructions provided by words contained in the WordCollection.
	 * 
	 * @return Document the Lucene Search Engine Document
	 */
	public Document document() {

		Document doc = new Document();

		Word w = null;
		Iterator<Word> iter = wordList.iterator();
		while (iter.hasNext()) {
			w = (Word) iter.next();
			String field = w.getName();
			String value = w.getContent();
			boolean persist = w.isPersistent();
			boolean tokenizable = w.isTokenizable();
			if (tokenizable) {
				doc.add(new TextField(field, value, (persist ? Field.Store.YES : Field.Store.NO)));
			} else {
				doc.add(new StringField(field, value, (persist ? Field.Store.YES : Field.Store.NO)));
			}

		}
		return doc;
	}
}
