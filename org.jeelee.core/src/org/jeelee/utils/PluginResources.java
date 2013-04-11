/* PluginResources.java 1.0 2010-2-2
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

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.plugin.AbstractUIPlugin;



/**
 * <B>PluginResources</B>
 * 
 * @author Brook Tran . Email: <a href="mailto:c.brook.tran@gmail.com">c.brook.tran@gmail.com</a>
 * @version Ver 1.0.01 2011-12-27 created
 * @since Application Ver 1.0
 * 
 */
public class PluginResources {
	private final String pluginID;
	/** The wrapped resource bundle. */
	private final ResourceBundle resource;

	/**
	 * Instantiates a new resourcebundleutil which wraps the provided resource
	 * bundle.
	 * <br>
	 * Prevent new instance creation.
	 * 
	 * @param r the ResourceBundle
	 */
	private PluginResources(String pluginID,ResourceBundle r) {
		this.pluginID = pluginID;
		resource = r;
	}

	/**
	 * Gets a string from the ResourceBundles.
	 * <br> Convenience method to save casting.
	 * 
	 * @param key the key of the properties.
	 * 
	 * @return the value of the property. Return the key if the value is not found.
	 */
	public String getString(String key){
		try {
			return resource.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}
	
	public String getString(String ...keys) {
		StringBuilder sb = new StringBuilder();
		for(String key:keys){
			sb.append(getString(key));
			sb.append(" ");
		}
		return sb.toString();
	}

	
	public String getHelp(String key) {
		return getString(key+".help");
	}

	
	
	/**
	 * Gets the integer from the properties.
	 * 
	 * @param key the key of the property.
	 * 
	 * @return the value of the key. return -1 if the value is not found.
	 */
	public Integer getInteger(String key){
		try {
			return Integer.valueOf(resource.getString(key));
		} catch (MissingResourceException e) {
			return new Integer(-1);
		}
	}

	/**
	 * Gets the int.
	 * 
	 * @param key the key
	 * 
	 * @return the int
	 */
	public int getInt(String key){
		try {
			return Integer.parseInt(resource.getString(key));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	
	protected static PluginResources getResources(String pluginID,Class<?> c) {
		return getResources(pluginID,c.getPackage().getName()+".Labels", Locale.getDefault(),c);
	}
	
	protected static PluginResources getResources(String pluginID,Class<?> c,Locale locale) {
		return getResources(pluginID,c.getPackage().getName()+".Labels", locale,c);
	}
	
	protected static PluginResources getResources(String pluginID,String baseName,Locale locale,Class<?> c) {
		return new PluginResources(pluginID,ResourceBundle.getBundle(baseName, locale,c.getClassLoader()));
	}
	
	/**
	 * Gets the bundle.
	 * 
	 * @return the bundle
	 */
	public ResourceBundle getBundle(){
		return resource;
	}

	/**
	 * Gets the keys.
	 * 
	 * @return the keys
	 */
	public Enumeration<String> getKeys(){
		return resource.getKeys();
	}

	/**
	 * Gets a resource string formatted with MessageFormat.
	 * 
	 * @param key the key
	 * @param argument the argument
	 * 
	 * @return Formatted stirng.
	 */
	public String getFormatted(String key, String argument) {
		try {
			return MessageFormat.format(resource.getString(key), new Object[] {argument});
		} catch (Exception e) {
			return key;
		}
	}

	/**
	 * Gets a resource string formatted with MessageFormat.
	 * 
	 * @param key the key
	 * @param arguments the arguments
	 * 
	 * @return Formatted stirng.
	 */
	public String getFormatted(String key, Object... arguments) {
		try {
			return MessageFormat.format(resource.getString(key), arguments);
		} catch (Exception e) {
			return key;
		}
	}

	/**
	 * To string.
	 * 
	 * @return the string
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return super.toString()+"["+resource+"]";
	}

	
	private String getImageDir(){
		String imageDir = resource.getString("icon.dir");
		if(!(imageDir.endsWith("/"))){
			imageDir+="/";
		}
		return imageDir;
	}
	
	public ImageDescriptor getImageDescriptor(String key) {
		String src="";
		try {
			src=resource.getString(key+".icon");
			String imageDir=getImageDir();
			src = imageDir+src;
			ImageDescriptor id = AbstractUIPlugin.imageDescriptorFromPlugin(pluginID, src);
			return id;
		} catch (MissingResourceException e) {
		}
		return null;
	}
	
	public Image getImage(String key) {
		ImageDescriptor desc = getImageDescriptor(key);
		return desc==null?null:desc.createImage();
	}
		

	/**
	 * Gets the mnem.
	 * @return the mnem
	 */
	private char getMnem(String key) {
		String string=null;
		try {
			string=resource.getString(key+".mnem");;
		} catch (MissingResourceException e) {
		}
		return (string==null||string.length()==0)?'\0':string.charAt(0);
	}

	public void configTrayItrm(TrayItem trayItem) {
		trayItem.setImage(getImage("tray"));
		trayItem.setToolTipText(getTips("tray"));
	}	
	public void configAction(Action action,String id) {
		action.setText(getString(id));
		action.setToolTipText(getTips(id));
		ImageDescriptor image =getImageDescriptor(id);
		action.setImageDescriptor(image);
		action.setHoverImageDescriptor(image);
	}
	
	
	public String getTips(String key) {
		return getAdditionMessage(key,".tips");
	}
	public String getErrorTips(String key) {
		return getAdditionMessage(key,".error");
	}
	
	private String getAdditionMessage(String key,String addition){
		String tips = key+addition;
		String retval = getString(tips);
		if(retval.equals(tips)){
			retval= getString(key);
		}
		return retval;
	}

	public char getChar(String string) {
		try {
			return resource.getString(string).charAt(0);
		} catch (IndexOutOfBoundsException  e) {
			return Character.MIN_VALUE;
		}
		
	}

	public void configTabItem(TabItem item,String key) {
		item.setImage(getImage(key));
		item.setText(getString(key));		
	}

	public void configButton( Button button,String key) {
		button.setText(getString(key));	
		final Image image =  getImage(key);
		if(image!=null){
			button.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					image.dispose();
				}
			});
			button.setImage(image);
		}
	}

	

	



	

	
}
