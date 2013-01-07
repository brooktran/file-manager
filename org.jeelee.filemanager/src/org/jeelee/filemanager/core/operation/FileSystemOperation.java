/* FileSystemOperation.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.core.operation;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.jeelee.filemanager.core.FileDelegate;



/**
 * <B>FileSystemOperation</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-9-25 created
 */
public abstract class FileSystemOperation  extends AbstractReproducibleOperation{
	protected PathProvider pathProvider;

	public FileSystemOperation(String label) {
		super(label);
	}

	@Override
	public void setPathProvider(PathProvider pathProvider) {
		this.pathProvider = pathProvider;
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

	protected FileDelegate getSelectedFile(){
//		IStructuredSelection selection = (IStructuredSelection) pathProvider
//				.getSelection();
//		return (FileDelegate) selection.getFirstElement();
		return pathProvider.getSource()[0];
	}
	protected FileDelegate[] getSelectedFiles(){
//		IStructuredSelection selection = (IStructuredSelection) pathProvider
//				.getSelection();
//		List<FileDelegate> result = new LinkedList<>();
//		if (!selection.isEmpty()) {
//			Iterator it = selection.iterator();
//			while (it.hasNext()) {
//				FileDelegate file = (FileDelegate) it.next();
//				result.add(file);
//			}
//		}
//		return result;
		return pathProvider.getSource();
	}
	
	
	
}
