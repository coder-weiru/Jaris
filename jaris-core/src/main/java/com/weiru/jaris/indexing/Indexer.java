package com.weiru.jaris.indexing;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * <code>Indexer</code> uses underlying Lucene search engine library to build
 * indexes for WordCollections. It delegates index calls to the underlying
 * Lucene search engine library.
 */
public class Indexer {
	private boolean writerOpen = false;
	private boolean readerOpen = false;
	private IndexWriter writer = null;
	private IndexReader reader = null;
	private Analyzer analyzer = null;
	private String location = "idx";
	private static Logger LOGGER = Logger.getLogger(Indexer.class.getName());

	private static final String LOCATION_OPEN_ERROR_MESSAGE = "Unable to open the Indexer[%s].";

	/**
	 * Create a Indexer specifying the location
	 * 
	 * @param location
	 *            The path and name for the Indexer
	 * @throws IndexException
	 * @throws IOException
	 */
	public Indexer(String location) throws IndexException, IOException {
		this.location = location;
		analyzer = new KeywordAnalyzer();
	}

	/**
	 * Opens the IndexWriter to the underlying Indexer
	 * 
	 * @throws IOException
	 */
	private void openWriter() throws IOException {
		try {
			Directory directory = FSDirectory.open(Paths.get(location));
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			/*
			 * IndexWriterConfig.OpenMode.CREATE_OR_APPEND if used IndexWriter
			 * will create a new index if there is not already an index at the
			 * provided path and otherwise open the existing index.
			 */
			config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			writer = new IndexWriter(directory, config);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, String.format(LOCATION_OPEN_ERROR_MESSAGE, e.getMessage()), e);
		}
		writerOpen = true;
	}

	/**
	 * Opens the IndexReader to the underlying Indexer
	 * 
	 * @throws IOException
	 */
	private void openReader() throws IOException {
		reader = DirectoryReader.open(FSDirectory.open(Paths.get(location)));
		readerOpen = true;
	}

	/**
	 * Closed the IndexWriter to the underlying Indexer
	 * 
	 * @throws IOException
	 */
	private void closeWriter() throws IOException {
		if (!writerOpen)
			return;

		writer.close();
		writerOpen = false;
	}

	/**
	 * Closes the IndexReader to the underlying Indexer
	 * 
	 * @throws IOException
	 */
	private void closeReader() throws IOException {
		if (!readerOpen) {
			return;
		}
		reader.close();
		readerOpen = false;
	}

	/**
	 * Stores the WordCollection. Each Word specifies how they should be
	 * indexed.
	 * 
	 * @param words
	 *            the WordCollection
	 * @throws IndexException
	 */
	public synchronized void store(WordCollection words) throws IndexException {
		try {
			if (readerOpen) {
				closeReader();
			}
			if (!writerOpen) {
				openWriter();
			}
			writer.addDocument(words.document());
			closeWriter();
		} catch (IOException e) {
			throw new IndexException(String.format("error on store : %s", e.getMessage()), e);
		}

	}

	/**
	 * Get the count of total documents indexed
	 * 
	 * @return int the count
	 * @throws IndexException
	 */
	public synchronized int getCount() throws IndexException {
		try {
			if (!readerOpen) {
				openReader();
			} else {
				return -1;
			}
			return reader.numDocs();
		} catch (IOException e) {
			throw new IndexException(String.format("error on getCount : %s", e.getMessage()), e);
		}
	}

	/**
	 * Delete all WordCollection that match the string expression of one field.
	 * 
	 * @param key
	 *            the name of the field
	 * @param term
	 *            the string expression
	 * @return int the number of WordCollection deleted
	 * @throws IndexException
	 */
	public synchronized void deleteByTerm(String key, String term) throws IndexException {
		try {
			if (readerOpen) {
				closeReader();
			}
			if (!writerOpen) {
				openWriter();
			}
			writer.deleteDocuments(new Term(key, term));
			closeReader();
		} catch (IOException e) {
			throw new IndexException(String.format("error on deleteByTerm : %s", e.getMessage()), e);
		}
	}

	/**
	 * Returns the Analyzer that analyze text before indexing
	 * 
	 * @return Analyzer the Analyzer
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * Sets the Analyzer that analyze text before indexing
	 * 
	 * @param analyzer
	 *            the Analyzer, by default it is KeywordAnalyzer
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
}
