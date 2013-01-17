/* SharedImage.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.graphics.Image;
import org.jeelee.utils.DefaultPair;
import org.jeelee.utils.Pair;

/**
 * <B>SharedImage</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager Dec 13, 2012 created
 */
public class SimpleSharedImages {
	private static Map<String, Pair<Image,Integer >> images =new HashMap<String, Pair<Image,Integer >>();
	
	public static void put(String key, Image image) {
		Pair<Image, Integer> en = images.get(key);
		if(en!=null){
			throw new IllegalArgumentException(key);//XXX key already existed
		}
		images.put(key,new DefaultPair<Image, Integer>(image, 0));
	}

	
	public static Image get(String key) {
		Pair<Image, Integer> en = images.get(key);
		if(en==null  ){
			return null;
		}
		en.setValue(en.getValue()+1);
		return en.getKey();
	}

	public static void disposeAll(){
		Iterator<Entry<String,Pair<Image,Integer>>>  it=images.entrySet().iterator();
		while (it.hasNext()) {
			dispose(it.next().getKey());
		}
		
	}

	public static void pull(String key) {
		Pair<Image, Integer> en = images.get(key);
		if(en==null){
			return;
		}
		en.setValue(en.getValue());
		if( en.getValue()==0){
			dispose(key);
		}
	}

	private static void dispose(String key) {
		Pair<Image, Integer> en= images.remove(key);
		Image image = en.getKey();
		if(image!=null){
			image.dispose();
		}
	}


	public static boolean contains(String key) {
		return images.containsKey(key);
	}

	
}
