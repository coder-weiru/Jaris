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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

/**
 * The <code>JarResource</code> represents a specific jar or zip file.
 * All class and package related information are parsed and stored in 
 * <code>JarResource</code>.
 * 
 * @author Wei Ru
 */
public final class JarResource {

   public boolean debugOn=false;
   private Hashtable packageNames=new Hashtable();  
   private String jarFilePath;
   private Logger log = Logger.getLogger(JarResource.class);
   
   /**
    * Create a JarResource with a file type representing a jar file
    * @param jarFile the jar file
    */
   public JarResource(File jarFile) {
	   this.jarFilePath=jarFile.getPath();
   }
   
   /**
    * Create a JarResource with a file path string
    * @param jarFilePath the jar file path
    */
   public JarResource(String jarFilePath) {
	   this.jarFilePath=jarFilePath;
   }
   
   /**
    * Create a JarResource with a file directory path and a file name
    * @param jarFileDir the jar file directory path
    * @param jarFileName the jar file name
    */
   public JarResource(String jarFileDir, String jarFileName) {
      File file = new File(jarFileDir, jarFileName);
      this.jarFilePath = file.getPath();
   }

   /**
    * Parses all class information
    *
    */
   public void parseClasses() {
      try {
          ZipFile zf=new ZipFile(jarFilePath);
          Enumeration e=zf.entries();
          while (e.hasMoreElements()) {
              ZipEntry ze=(ZipEntry)e.nextElement();
              if (debugOn) {
                 log.debug(dumpZipEntry(ze));
              }
              String resourceName = ze.getName(); 
              if (isClassFile(resourceName)) {
	              String shortName = getClassShortName(resourceName);
	              String packageName = getPackageName(resourceName);
	              packageNames.put(shortName, packageName);
	          }
          }
          zf.close();
          if (debugOn) {
        	  log.debug("class-package-map:"+packageNames);
           }
       } catch (NullPointerException e) {
          log.debug("done.", e);
       } catch (FileNotFoundException e) {
    	  log.error("Exception: ", e); 
          e.printStackTrace();
       } catch (IOException e) {
    	  log.error("Exception: ", e);
          e.printStackTrace();
       } catch (Exception e) {
    	  log.error("Exception: ", e);
    	  e.printStackTrace();
       }
   }

   /**
    * Returns the jar file name
    * @return String the jar file name
    */
   public String getJarFileName() {
	   return new File(this.jarFilePath).getName();
   }

   /**
    * Returns the jar file path
    * @return String the jar file path
    */
   public String getJarFilePath() {
	   return jarFilePath;
   }

   /**
    * Sets the jar file path
    * @param jarFilePath the jar file path
    */
   public void setJarFilePath(String jarFilePath) {
	   this.jarFilePath = jarFilePath;
   }

   /**
    * Get an Enumeration of the class names
    * @return Enumeration the Enumeration
    */
   public Enumeration getClassNames() {
	   return packageNames.keys();
   }
   
   /**
    * Get a specific class package name by a class name
    * @param className the class name
    * @return String the package name
    */
   public String getClassPackage(String className) {
	   return (String)packageNames.get(className);
   }
   
   /**
    * Get a fully qualified class name by a class name 
    * @param className the class name
    * @return String the fully qualified class name 
    */
   public String getClassFQN(String className) {
	   String packagename = (String)packageNames.get(className);
	   String fqn = className;
	   if (packagename!=null&&!"".equals(packagename)) {
		   fqn = packagename += "." + className;
	   }
	   return fqn;
   }
   
   /**
    * Gets the fully qualified class name from resource name
    * @param resourceName the resource name
    * @return String the fully qualified class name 
    */
   private String getClassFullName(String resourceName) {
	   String name = resourceName.replace('/', '.');
	   name = name.substring(0, resourceName.indexOf(".class"));
	   return name;
   }
   
   /**
    * Gets the class short name from resource name
    * @param resourceName the resource name
    * @return String the class short name
    */
   private String getClassShortName(String resourceName) {
	   String name = resourceName.substring(resourceName.lastIndexOf("/")+1);
	   name = name.substring(0, name.indexOf(".class"));
	   return name;	   
   }
   
   /**
    * Gets the package name from resource name
    * @param resourceName the resource name
    * @return String the package name
    */
   private String getPackageName(String resourceName) {
	   String fullname = getClassFullName(resourceName);
	   String shortname = getClassShortName(resourceName);
       if (debugOn) {
    	   log.debug("f : " + fullname);
    	   log.debug("s : " + shortname);
       }
       String packagename = fullname.substring(0, fullname.indexOf(shortname));
       if (packagename.endsWith(".")) {
    	   packagename = packagename.substring(0, packagename.length()-".".length());
       }
	   return packagename;
   }
   
   /**
    * Check to see if this is a class file
    * @param resourceName the resource name
    * @return boolean if this file is of class type
    */
   private boolean isClassFile(String resourceName) {
	   int idx = resourceName.indexOf(".class"); 
	   return idx>0 && ".class".equals(resourceName.substring(idx));
   }
   
   /**
    * Dumps a zip entry into a string.
    * @param ze a ZipEntry
    */
   private String dumpZipEntry(ZipEntry ze) {
       StringBuffer sb=new StringBuffer();
       if (ze.isDirectory()) {
          sb.append("d "); 
       } else {
          sb.append("f "); 
       }
       if (ze.getMethod()==ZipEntry.STORED) {
          sb.append("stored   "); 
       } else {
          sb.append("defalted ");
       }
       sb.append(ze.getName());
       sb.append("\t");
       sb.append(""+ze.getSize());
       if (ze.getMethod()==ZipEntry.DEFLATED) {
          sb.append("/"+ze.getCompressedSize());
       }
       return (sb.toString());
   }

   /**
    * If the debug flag is set
    * @return boolean set or not
    */
	public boolean isDebugOn() {
		return debugOn;
	}
	
	/**
	 * Sets the debugging flag
	 * @param debugOn the debugging flag
	 */
	public void setDebugOn(boolean debugOn) {
		this.debugOn = debugOn;
	}
 
}
