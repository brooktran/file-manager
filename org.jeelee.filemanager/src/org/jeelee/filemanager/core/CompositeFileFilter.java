/* CompositeFileFilter.java 
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

import org.jeelee.filemanager.core.filters.FileFilter;

/**
 * <B>CompositeFileFilter</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-12-9 created
 */
public interface CompositeFileFilter extends FileFilter{

	void addFilter(FileFilter filter);
	void removeFilter(FileFilter fileFilter);
	
	
}
