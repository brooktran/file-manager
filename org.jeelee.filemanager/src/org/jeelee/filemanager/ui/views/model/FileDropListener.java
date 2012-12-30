/*
 * @(#)FileDropListener.java  Aug 8, 2012
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

//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;

import java.io.IOException;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.FileUtils;
import org.jeelee.utils.AppLogging;

/**
 * <B>FileDropListener</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager Aug 8, 2012 
 * 
 */
public class FileDropListener extends ViewerDropAdapter {
	private FileExplorer	fileViewer;

	public FileDropListener(FileExplorer fileViewer,Viewer viewer) {
		super(viewer);
		this.fileViewer=fileViewer;
	}

	@Override
	public boolean performDrop(Object data) {
		return true;
	}

	@Override
	public boolean validateDrop(Object target, int operation,
			TransferData transferType) {
		return FileTransfer.getInstance().isSupportedType(transferType);
	}
	@Override
	public void drop(DropTargetEvent event) {
		FileDelegate target = (FileDelegate) determineTarget(event);
		if(target==null){
			return;
		}
		if(!target.isDirectory()){
			target = target.getParent();
		}
		
		try {
			FileUtils.moveTo((String[]) event.data,target);
		} catch (IOException e) {
			AppLogging.handleException(e);
		}
		fileViewer.refresh();
		super.drop(event);
	}
	
	@Override
	protected Object determineTarget(DropTargetEvent event) {
		Object target= super.determineTarget(event);
		if(target == null){
			target = fileViewer.getDefaultSelection();
		}
		return target;
	}
}
