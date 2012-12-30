/* Scope.java 
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

import org.jeelee.utils.ObjectUtils;


/**
 * <B>Scope</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-10-9 created
 */
public class Scope  {
	private long	minima;
	private long	maximum;

	public Scope(long minima, long maximum) {
		this.minima = minima;
		this.maximum = maximum;
	}


	public void setMinima(long minima) {
		this.minima = minima;
	}

	public long getMinima() {
		return minima;
	}

	public void setMaximum(long maximum) {
		this.maximum = maximum;
	}

	public long getMaximum() {
		return maximum;
	}

	public boolean include(long t){
		return minima < t && maximum >= t;
	}
	
	@Override
	public boolean equals(Object obj) {
		Scope other = ObjectUtils.isBasicEquals(this, obj);
		if(other == null){
			return false;
		}
		return other.minima == minima &&
				other.maximum == minima;
	}
	

	public static final long		KB		= 1024;
	public static final long		MB		= KB * 1024;
	public static final long		GB		= MB * 1024;
	public static final long		TB		= GB * 1024;

	public static final Scope	EMPTY	= new Scope(0, 0);
	public static final Scope	TINY	= new Scope(0, 100 * KB);
	public static final Scope	SMALL	= new Scope(100 * KB, MB);
	public static final Scope	MEDIUM	= new Scope(MB, 300 * MB);
	public static final Scope	LARGE	= new Scope(300 * MB, 3 * GB);
	public static final Scope	HUGE	= new Scope(3 * GB,  Long.MAX_VALUE);

	public static final Scope 	ANY_THING = new Scope(Long.MIN_VALUE, Long.MAX_VALUE);
	
}