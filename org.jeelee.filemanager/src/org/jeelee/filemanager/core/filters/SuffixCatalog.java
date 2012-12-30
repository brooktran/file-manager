/* Catalog.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.core.filters;

import org.jeelee.utils.ObjectUtils;



/**
 * <B>Catalog</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-12-8 created
 */
public class SuffixCatalog extends DefaultCatalog  {
	private String[] suffixes =new String[0];
	
	public SuffixCatalog(String id) {
		super(id);
	}

	public String[] getSuffixes() {
		return suffixes;
	}
	public void setSuffixes(String[] suffixes) {
		this.suffixes = suffixes;
	}
	@Override
	public boolean equals(Object obj) {
		SuffixCatalog other = ObjectUtils.isBasicEquals(this, obj);
		if(other == null){
			return false;
		}
		return id.equals(other.id);
	}
}
