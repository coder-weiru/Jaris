package com.weiru.jaris.indexing;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

public class Searcher {

	private IndexSearcher indexSearcher;
	private Analyzer analyzer = null;
	private static Logger LOG = Logger.getLogger(Searcher.class.getName());

	/**
	 * <code>Searcher</code> implements all query related logic
	 * @param location The path and name for the Indexer
	 * @throws IOException
	 */
	public Searcher(String location) throws IOException {
		super();
		initializeIndexSearcher(location);
	}

	public Searcher(String location, Analyzer analyzer) throws IOException {
		super();
		this.analyzer = analyzer;
		initializeIndexSearcher(location);
	}

	private void initializeIndexSearcher(String location) throws IOException {
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(location)));
		indexSearcher = new IndexSearcher(reader);
	}
	/**
	 * Search for WordCollections that match the criteria specified within a
	 * sample WordCollection.
	 * 
	 * @param words
	 *            The sample WordCollection
	 * @return WordCollection[] All matching WordCollection
	 * @throws IOException
	 * @throws ParseException
	 */
	public WordCollection[] searchBySample(WordCollection words) throws IOException, ParseException {
		QueryBuilder builder = new QueryBuilder(analyzer);
		Word word = null;
		Iterator<Word> iter = words.iterator();
	    while(iter.hasNext()) {
			word = (Word) iter.next();
			boolean searchable = word.isSearchable();
	    	if (!searchable) {
	    		continue;
	    	}
			Query q = builder.createBooleanQuery(word.getName(), word.getContent());
			parser.setLowercaseExpandedTerms(false);
			qs[1] = parser.parse(Version.LUCENE_CURRENT, kw.getContent());
	    	if (qs[0]!=null) {
	    		qs[0] = qs[0].combine( qs );
	    	} else {
	    		qs[0] = qs[1];
	    	}
	    	
	    }
	    log.debug("Searching for: " + qs[0].toString());
	    return searchByQuery(qs[0]);
	}

	/**
     * Search for KeywordDocuments that match the string expression of one field.
     * 
     * @param key the name of the field
     * @param term the string expression
     * @return WordCollection[] All matching KeywordDocuments
	 * @throws IOException
	 * @throws ParseException
	 */
	public WordCollection[] searchByTerm(String key, String term) throws IOException, ParseException {

		QueryParser parser = new QueryParser(key, analyzer);
		parser.setLowercaseExpandedTerms(false);
		Query query = parser.parse(term);
	    log.debug("Searching for: " + query.toString());

        return searchByQuery(query);
	}

	/**
     * Search for KeywordDocuments using lucene Query objec.
     * This private method is invoked by all other search methods.
     * 
	 * @param query The Query object
	 * @return WordCollection[] All matching KeywordDocuments
	 * @throws IOException
	 */
	private WordCollection[] searchByQuery(Query query) throws IOException {
		Hits hits = idxSearcher.search(query);
	    log.debug(hits.length() + " total matching documents");
	    
	    WordCollection[] results = new WordCollection[hits.length()];

	    for(int i=0; i<hits.length(); i++)
	    {
	          Document doc = hits.doc(i);
	          WordCollection kwc = new WordCollection();
	          
	          log.debug("id:" + hits.id(i));
	          kwc.setInternalId(hits.id(i));
	          
	          Word kw = null;
	          Enumeration en = doc.fields();
	          while(en.hasMoreElements())
	          {
	              Field f = (Field)en.nextElement();
	              kw = new Word(f.name(), f.stringValue());
	              
	              if (f.isIndexed()) {
	            	  kw.setSearchable(true);
	              }
	              if (f.isStored()) {
	            	  kw.setPersistent(true);
	              }
	              kwc.addKeyword(kw);
	          }
	          results[i]=kwc;
	      }      
	      idxSearcher.close();
	      return results;
	}

    /**
     * Returns the Analyzer that analyze text before searching
     * @return Analyzer the Analyzer
     */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * Sets the Analyzer that analyze text before searching
	 * @param analyzer the Analyzer, by default it is KeywordAnalyzer 
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	
	
}
