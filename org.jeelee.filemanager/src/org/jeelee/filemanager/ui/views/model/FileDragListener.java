/*
 * @(#)FileDragListener.java  Aug 8, 2012
 *
 * Copyright (c) 2012 by the original authors of org.jeelee.filemanager
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and  
 * contributors of the org.jeelee.filemanager project ("the copyright holders").  
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * the copyright holders. For details see accompanying license terms. 
 */
package org.jeelee.filemanager.ui.views.model;

import java.util.Iterator;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.jeelee.filemanager.core.FileDelegate;

/**
 * <B>FileDragListener</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 4:43:06 PM 
 * 
 */
public class FileDragListener implements DragSourceListener{
	private ColumnViewer viewer;
	private FileExplorer	fileViewer;

	public FileDragListener(FileExplorer fileViewer,ColumnViewer viewer){
		this.viewer = viewer;
		this.fileViewer=fileViewer;
	}


	@Override
	public void dragStart(DragSourceEvent event) {
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void dragSetData(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		if(selection.isEmpty()){
			return;
		}

		String[] paths = new String[selection.size()] ;
		Iterator it = selection.iterator();
		for(int i=0;i<paths.length && it.hasNext();i++){
			FileDelegate proxy = (FileDelegate) it.next();
			paths[i] = proxy.getAbsolutePath();
		}
		event.data = paths;
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		fileViewer.refresh();
	}

}
