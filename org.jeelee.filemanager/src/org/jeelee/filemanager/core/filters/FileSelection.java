/* FileSelection.java 
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

import org.eclipse.jface.viewers.ISelection;

/**
 * <B>FileSelection</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-12-15 created
 */
public class FileSelection implements ISelection{
	private FileFilterDelegate	fileFilter;
	public FileSelection(FileFilterDelegate fileFilter) {
		this.fileFilter = fileFilter;
	}
	@Override
	public boolean isEmpty() {
		return false;
	}
	public FileFilterDelegate getFilter(){
		return fileFilter;
	}
}