/* ObjectUtils.java 1.0 2010-2-2
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;



/**
 * <B>ObjectUtils</B>
 * 
 * @author Brook Tran . Email: <a href="mailto:c.brook.tran@gmail.com">c.brook.tran@gmail.com</a>
 * @version Ver 1.0.01 2012-1-12 created
 * @since Application Ver 1.0
 * 
 */
public class ObjectUtils {

	@SuppressWarnings("unchecked")
	public static <T> T isBasicEquals(T t, Object other) {
		if(other==null || t == null){
			return null;
		}
		if(!other.getClass().isInstance(t)){
			return null;
		}
		return (T) other;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T clone(T source) throws Exception{
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ObjectOutputStream oos =new ObjectOutputStream(bao);
		oos.writeObject(source);
		
		ByteArrayInputStream bis =new ByteArrayInputStream(bao.toByteArray());
		ObjectInputStream ois =new ObjectInputStream(bis);
		T retval =(T) ois.readObject();
		return retval;
	}

}
