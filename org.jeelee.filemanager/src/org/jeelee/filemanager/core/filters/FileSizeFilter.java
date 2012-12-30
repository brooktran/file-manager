/* FileSizeFilter.java 
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jeelee.filemanager.core.AbstractFileFilter;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.Scope;

/**
 * <B>FileSizeFilter</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-12-9 created
 */
public class FileSizeFilter extends AbstractFileFilter {
	private Set<ScopeCatalog> catalogs ;
	
	public FileSizeFilter() {
		catalogs = new HashSet<>();
	}

	public FileSizeFilter(ScopeCatalog catalog) {
		this();
		addScopeCatalog(catalog);
	}

	@Override
	public boolean select(FileDelegate t) {
		if(catalogs.size() ==0){
			return true;
		}
		
		long size = t.getFileSize();
		for(ScopeCatalog sc : catalogs){
			if(sc.getScope().include(size)){
				return true;
			}
		}
		return false;
	}

	public boolean contains(Scope scope) {
		for(ScopeCatalog sc : catalogs){
			if(sc.getScope().equals(scope)){
				return true;
			}
		}
		return false;
	}
//	public ScopeCatalog getScopeCatalog() {
//		return scopeCatalog;
//	}
//	public void setScopeCatalog(ScopeCatalog scopeCatalog) {
//		this.scopeCatalog = scopeCatalog;
//	}
	public void addScopeCatalog(ScopeCatalog scopeCatalog){
		catalogs.add(scopeCatalog);
		firePropertyChanged("file.size.filter",null,this);
	}
	public void removeScopeCatalog(ScopeCatalog scopeCatalog){
		catalogs.remove(scopeCatalog);
		firePropertyChanged("file.size.filter",null,this);
	}
	public Iterator<ScopeCatalog> iterator(){
		return catalogs.iterator();
	}
	
}
