/* CopyOperation.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.ui.operation;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.jeelee.filemanager.core.FileDelegate;

/**
 * <B>CopyOperation</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-9-25 created
 */
public class CopyFileOperation extends CopyPathOperation{

	public CopyFileOperation(PathProvider pathProvider, String label) {
		super(pathProvider,label);
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) pathProvider
				.getSelection();
		if (selection.isEmpty()) {
			return Status.CANCEL_STATUS;
		}
		
		String[] paths = new String[selection.size()] ;
		Iterator it = selection.iterator();
		for(int i=0;i<paths.length && it.hasNext();i++){
			FileDelegate proxy = (FileDelegate) it.next();
			paths[i] = proxy.getAbsolutePath();
		}
		Transfer[] transferTypes  = { FileTransfer.getInstance() };
		Object[] data = {paths};
		transferTo(transferTypes , data);
		
		return Status.OK_STATUS;
	}
	
	
	@Override
	public boolean canUndo() {
		return false;
	}
	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return null;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return null;
	}

}
