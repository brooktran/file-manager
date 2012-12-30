/* DefaultCompositeFileFilter.java 
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.jeelee.filemanager.core.AbstractFileFilter;
import org.jeelee.filemanager.core.CompositeFileFilter;
import org.jeelee.filemanager.core.FileDelegate;

/**
 * <B>DefaultCompositeFileFilter</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-12-9 created
 */
public class DefaultCompositeFileFilter extends AbstractFileFilter implements CompositeFileFilter {
	private PropertyChangeListener listener=new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			firePropertyChanged(evt);
		}
	};
	private List<FileFilter> fileFilters ;
	
	public DefaultCompositeFileFilter() {
		fileFilters = new ArrayList<FileFilter>();
	}
	
	@Override
	public boolean select(FileDelegate t) {
		for(FileFilter f:fileFilters){
			if(!f.select(t) ){
				return false;
			}
		}
		return true;
	}

	@Override
	public void addFilter(FileFilter filter) {
		fileFilters.add(filter);
		filter.addPropertyChangeListener(listener);
		firePropertyChanged("filter", null, fileFilters);
	}

	

	@Override
	public void removeFilter(FileFilter fileFilter) {
		fileFilters.remove(fileFilter);
		fileFilter.removePropertyChangeListener(listener);
		firePropertyChanged("filter", null, fileFilters);
	}

}
