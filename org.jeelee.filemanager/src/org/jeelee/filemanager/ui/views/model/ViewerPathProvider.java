/* ViewerFileExplorer.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.ui.views.model;

import java.util.Iterator;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.operation.PathProvider;

/**
 * <B>ViewerFileExplorer</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-9-26 created
 */
public class ViewerPathProvider implements PathProvider{

	private ISelectionProvider	selectionProvider;
	
	public ViewerPathProvider(Viewer	viewer){
		selectionProvider= viewer;
	}
	
	@Override
	public FileDelegate[] getSource() {
		IStructuredSelection selection =(IStructuredSelection) selectionProvider.getSelection(); 
		FileDelegate[] files = new FileDelegate[selection.size()];
		Iterator<?> it = selection.iterator();
		for(int i=0;i<files.length && it.hasNext();i++){
			files[i] = (FileDelegate) it.next();
		}
		return files;
	}
	@Override
	public FileDelegate[] getTarget() {
		return null;
	}


}
