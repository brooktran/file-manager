/* SuffixFilter.java 
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
import org.jeelee.utils.ArrayUtils;

/**
 * <B>SuffixFilter</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-12-8 created
 */
public class SuffixFilter extends AbstractFileFilter{
	public static final String PROPERTY_SUFFIX_FILTER_CHANGED="suffix.filter";//$NON-NLS-1$
	
	private Set<String> suffixes;
	private Set<SuffixCatalog> catalogs;
	
	private boolean allowFolder = true;

	public SuffixFilter() {
		suffixes = new HashSet<>();
		catalogs = new HashSet<>();
	}
	
	@Override
	public boolean select(FileDelegate f) {
		if(f.isDirectory() && isAllowFolder()){
			return true;
		}
		if(catalogs.size() ==0 && suffixes.size() == 0 ){
			return true;
		}
		
		String suffix=f.getSuffix().toLowerCase();
		return select(suffix);
	}
	public boolean select(String suffix) {
		for(String s:suffixes){
			if(s.equals(suffix)){
				return true;
			}
		}
		for(SuffixCatalog sc : catalogs){
			if(ArrayUtils.contains(sc.getSuffixes(), suffix)){
				return true;
			}
		}
		return false;
	}
	
	
	
	public void addSuffixes(String... suffixes) {
		for(String s:suffixes){
			this.suffixes.add(s.toLowerCase());
		}
		firePropertyChanged(PROPERTY_SUFFIX_FILTER_CHANGED, null, this);
	}



	public void removeSuffixes(String... suffixes) {
		for(String s:suffixes){
			this.suffixes.remove(s);
		}
		firePropertyChanged(PROPERTY_SUFFIX_FILTER_CHANGED, null, this);
	}



	public void addCatalog(SuffixCatalog catalog) {
		catalogs.add(catalog);
//		addSuffixes(catalog.getSuffixes());
		firePropertyChanged(PROPERTY_SUFFIX_FILTER_CHANGED, null, this);
	}



	public void removeCatalog(SuffixCatalog catalog) {
		catalogs.remove(catalog);
//		removeSuffixes(catalog.getSuffixes());
		firePropertyChanged(PROPERTY_SUFFIX_FILTER_CHANGED, null, this);
	}


	public boolean isAllowFolder() {
		return allowFolder;
	}
	public void setAllowFolder(boolean allowFolder) {
		this.allowFolder = allowFolder;
	}

	public boolean contains(Catalog catalog) {
		return catalogs.contains(catalog);
	}



	public void reset() {
		catalogs.clear();
		suffixes.clear();
		firePropertyChanged(PROPERTY_SUFFIX_FILTER_CHANGED, null, this);
	}

	public Iterator<String> iterator(){
		return suffixes.iterator();
	}


	

}
