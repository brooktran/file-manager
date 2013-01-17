/* TreeContentProvider.java 
 * Copyright (c) 2013 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.ui.views.model;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.filters.FileFilterDelegate;

public class FileDelegateTreeContentProvider implements ITreeContentProvider  {
	private FileFilterDelegate	fileFilter;

	public FileDelegateTreeContentProvider(FileFilterDelegate fileFilter) {
		this.fileFilter = fileFilter;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput == null) {
			return;
		}
		if(newInput instanceof FileDelegate){
			((FileDelegate)newInput).resolveChildren();
		}
	}

	@Override
	public boolean hasChildren(Object element) {
		return ((FileDelegate) element).getChildren().size() != 0;
	}

	@Override
	public Object getParent(Object element) {
		return ((FileDelegate) element).getParent();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof FileDelegate){
			return ((FileDelegate) inputElement).getChildren().toArray();
		}
		return (Object[]) inputElement;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
//		ArrayList<FileDelegate> children = new ArrayList<>();
//		for(FileDelegate child:((FileDelegate) parentElement).getChildren()){
//			if(fileFilter.select(child)){
//				children.add(child);
//			}
//		}
//		return children.toArray();
		return ((FileDelegate) parentElement).getChildren().toArray();
	}

	@Override
	public void dispose() {
		
	}
}