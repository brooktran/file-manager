/* AcceptableCounter.java 
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

import org.jeelee.utils.Acceptable;

/**
 * <B>AcceptableCounter</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-10-9 created
 */
public class AcceptableCounter<R,T extends Acceptable<R>> implements Acceptable<R> {
	private T t;
	private int count ;
	
	public AcceptableCounter(T t){
		this.t=t;
		count =0;
	}

	@Override
	public boolean select(R r) {
		if(t.select(r)){
			count ++;
			return true;
		}
		return false;
	}
	
	public T getFilter(){
		return t;
	}
	
	public int getCount() {
		return count;
	}
}