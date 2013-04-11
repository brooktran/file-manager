/* AppPreference.java 1.0 2010-2-2
 * 
 * Copyright (c) 2010 by Chen Zhiwu
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Properties;


/**
 * <B>AppPreference</B>
 * 
 * @author Brook Tran . Email: <a href="mailto:c.brook.tran@gmail.com">c.brook.tran@gmail.com</a>
 * @version Ver 1.0.01 2010-12-14 created
 * @since org.zhiwu.app.config Ver 1.0
 * 
 */
public class AppPreference{ 
	private final String path;//labels path
	private final String absolutePath;
	private Properties prop;
	private final String name;
	
	protected AppPreference(String name){
		absolutePath=".configurations"+File.separator;
		this.path =absolutePath+name+".prefs";
		this.name =name;
		init();
	}
	
	private void init() {
		File dir=new File(absolutePath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		File f=new File(path);
		loadFile(f);
	}

	private void loadFile(File f) {
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				AppLogging.handleException(e);
			}
		}
		
		try {
			prop=new Properties();
			prop.load(new FileInputStream(f));
		} catch (Exception e) {
			AppLogging.handleException(e);
		}
	}
	
	
	public AppPreference(File f){
		loadFile(f);
		absolutePath=f.getAbsolutePath();
		path=f.getPath();
		name=f.getName();
	}

	public String get(String key){
		try {
			return  prop.getProperty(key);
		} catch (Exception e) {
			AppLogging.handleException(e);
			return key;
		}
	}
	
	
	public synchronized void put(Object key, Object value){
//		firePropertyChange(key.toString(), get(key.toString()), value);
		prop.put(key, value.toString());
	}
	
	public synchronized void save() {
		try {
			FileOutputStream fos=new FileOutputStream(new File(path));
			prop.store(fos, Calendar.getInstance().toString());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			AppLogging.handleException(e);
		} catch (IOException e) {
			AppLogging.handleException(e);
		}
		
		
		
	}

	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(get(key));
	}

	/**
	 * @return -1 if not such key was found.
	 */
	public int getInteger(String key) {
		try {
			return Integer.parseInt(get(key));
		} catch (Exception e) {
			return -1;
		}
	}

	public double getDouble(String key) {
		try {
			return Double.valueOf(get(key));
		} catch (Exception e) {
			return -1;
		}
	}

	public String getName() {
		return name;
	}
	
	
	public Enumeration<Object> getKeys(){
		return prop.keys();
	}

}