/* SharedResources.java 1.0 2010-2-2
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.jeelee.core.JeeleeActivator;



/**
 * <B>SharedResources</B> managers the UI resources.
 * @author  Zhi-Wu Chen. Email: <a href="mailto:c.zhiwu@gmail.com">c.zhiwu@gmail.com</a>
 * @version  Ver 1.0.01 2010-12-10 created
 * @since  org.zhiwu.app Ver 1.0
 */
public class SharedResources{
	private static String Labels = SharedResources.class.getPackage().getName()+".Labels";
	private static Map<String, PluginResources> resourceMap=new HashMap<>(8);
	public static final String ID ="org.jeelee.core";
	
	public static PluginResources getResources() {
		return getResources(ID, JeeleeActivator.class);
	}
	
	public static PluginResources getResources(String pluginID,Class<?> clazz) {
		return getResources(pluginID, clazz, clazz.getPackage().getName()+".Labels");
	}
	public static PluginResources getResources(String pluginID,
			Class<?> clazz, String baseName) {
		PluginResources r= resourceMap.get(pluginID);
		if(r==null){
			r=PluginResources.getResources(pluginID,baseName,locale,clazz);
			resourceMap.put(clazz.getName(), r);
		}
		return r;
	}
	
	
	
	
	
	private static final ISharedTextColors SHARED_TEXT_COLORS =new SharedTextColors();
	public static ISharedTextColors getShareColors() {
		return SHARED_TEXT_COLORS;
	}

	
//	public static SharedImages getSharedImages() {
//		return getSharedImages(JeeleeActivator.class);
//	}
//	
	private static Map<String, SharedImages> sharedImagesMap= new HashMap<>(8);
	public static SharedImages getSharedImages(Class<?> clazz){
		return getSharedImages(clazz.getName());
	}
	
	public static SharedImages getSharedImages(String key){
		SharedImages images= sharedImagesMap.get(key);
		if(images == null){
			images = new SharedImages();
			sharedImagesMap.put(key,images);
		}
		return images;
	}

	
	
	////
	private static Locale locale = Locale.getDefault();
	private static Map<Object, Object> AppDefault=new HashMap<Object, Object>();
	protected  static  PropertyChangeSupport propertySupport = new PropertyChangeSupport(SharedResources.class);
	
	
	
	
	
	

	public static void put(Object key,Object value){
		Object oldValue=AppDefault.put(key, value);
		firePropertyChange(key.toString(), oldValue, value);
	}
	
	public static Object get(Object key){
		return AppDefault.get(key);
	}

	public static void setLabels(String labels) {
		Labels = labels;
	}

	public static  String getLabels() {
		return Labels;
	}
	

	public static Locale getLocale() {
		locale = locale==null?Locale.getDefault():locale;
		return locale;
	}

	public static void addPropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.addPropertyChangeListener(listener);
	}

	public static void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertySupport.addPropertyChangeListener(propertyName, listener);
	}

	protected static void firePropertyChange(String propertyName,
			boolean oldValue, boolean newValue) {
		propertySupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	protected static void firePropertyChange(String propertyName, int oldValue,
			int newValue) {
		propertySupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	protected static void firePropertyChange(String propertyName,
			Object oldValue, Object newValue) {
		propertySupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	public static void removePropertyChangeListener(
			PropertyChangeListener listener) {
		propertySupport.removePropertyChangeListener(listener);
	}

	public static void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertySupport.removePropertyChangeListener(propertyName, listener);
	}

	
	

	
	
	
	
	
	
	public static Font bold(Font font) {
		return modifyFont(font,SWT.BOLD);
	}

	public static Font modifyFont(Font font, int style) {
		FontData[] fontData = font.getFontData();
		fontData[0].setStyle(fontData[0].getStyle() | style);
		return new Font(font.getDevice(), fontData[0]);
	}

	public static void disposeSharedImage(String imageKey) {
		SharedImages images = getSharedImages(imageKey);
		images.dispose();
		sharedImagesMap.remove(imageKey);
	}
	

	
	
	
	////////////////
}
