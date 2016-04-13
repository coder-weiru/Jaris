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
package com.weiru.jaris.config;

import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * Jaris uses jakarta commons Configuration API. 
 * The <code>Config</code> provides the 
 * <code>org.apache.commons.configuration.Configuration</code>
 * object. 
 * 
 * @author Wei Ru
 */
public class Config {

	private static final String ERROR_MESSAGE_LOADING_CONFIGURATION = "Error loading configuration from %s. Exception message: s%";
	private static final String ERROR_MESSAGE_NO_CONFIGURATION_FOUND = "Not configuration is found for %s. Exception message: s%";

	private static final String CONFIGURATION_FILE = "jaris-preferences.xml";

	private static Logger LOG = Logger.getLogger(Config.class.getName());

	private XMLConfiguration configuration;

	private static final Config instance = new Config();

	private Config() {
		super();
		init();
	}

	public static Config instance() {
		return instance;
	}

	private void init() {
		try {
			configuration = new XMLConfiguration();
			configuration.load(CONFIGURATION_FILE);
			FileChangedReloadingStrategy strategy = new FileChangedReloadingStrategy();
			strategy.setRefreshDelay(configuration.getInt("config-refresh-delay", 5000));
			configuration.setReloadingStrategy(strategy);

		} catch (ConfigurationException e) {
			e.printStackTrace();
			LOG.log(Level.SEVERE,
					String.format(ERROR_MESSAGE_LOADING_CONFIGURATION,
					CONFIGURATION_FILE, e.getMessage()), e);
		}
	}

	/**
	 * Returns the configuration.
	 * @return Configuration the configuration object
	 */
	public Configuration getConfiguration() {
		return configuration;
	}	

	public String getIndexLocation() {
		return getStringProperty("index-location");
	}

	private String getStringProperty(String propertyName) {
		String prop = configuration.getString(propertyName);
		if (prop == null) {
			LOG.log(Level.SEVERE, String.format(ERROR_MESSAGE_NO_CONFIGURATION_FOUND, propertyName));
			throw new NoSuchElementException(String.format(ERROR_MESSAGE_NO_CONFIGURATION_FOUND, propertyName));
		}
		return prop;
	}
}
