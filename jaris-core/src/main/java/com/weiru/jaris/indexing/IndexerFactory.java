package com.weiru.jaris.indexing;

import java.util.HashMap;
import java.util.Map;

import com.weiru.jaris.config.Config;

/**
 * The <code>IndexerFactory</code> provides factory methods to retrieve 
 * specific Indexes by their name 
 * 
 * @author Wei Ru
 */
public class IndexerFactory {
    
	private static IndexerFactory instance = new IndexerFactory();
	private HashMap<String, Indexer> indexers = null;
	
	/**
	 * Private constructor
	 */
    private IndexerFactory() {
		indexers = new HashMap<String, Indexer>();
    }
    
    /**
     * Get the singleton instance
     * @return IndexerFactory the IndexerFactory 
     */
    public static IndexerFactory getInstance() {
    	if (instance==null) {
    		instance = new IndexerFactory();
    	}
    	return instance;
    }
    
    /**
	 * Return the Map<String, Indexer> containing all Indexes
	 * 
	 * @return Map<String, Indexer> the Indexes
	 */
	private Map<String, Indexer> getIndexes() {
		return indexers;
	}

    /**
     * Return a specific Indexer by its name
     * @param name The name or the path to a specific Indexer
     * @return Indexer 
     * @throws IndexException
     */
	public static Indexer getWordIndexer(String name) throws IndexException {
		Indexer indexer = null;
    	try {
    		Map<String, Indexer> store = IndexerFactory.getInstance().getIndexes();
    		indexer = (Indexer) store.get(name);
    		if (indexer==null) {
    			indexer = new Indexer(Config.instance().getIndexLocation());
            	store.put(name, indexer);
    		}
        } catch(Exception e) {
			throw new IndexException(String.format("error on getWordIndexer : %s", e.getMessage()), e);
        }
        return indexer;
    }
}
