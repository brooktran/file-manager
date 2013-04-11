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
package org.jeelee.filemanager.core.operation;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.utils.JobRunner;

/**
 * <B>CopyOperation</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-9-25 created
 */
public class CopyFileOperation extends CopyPathOperation{

	public CopyFileOperation(PathProvider pathProvider) {
		super(pathProvider);
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		FileDelegate[] files = pathProvider.getSource();
		
		final String[] paths = new String[files.length] ;
		for(int i=0;i<paths.length ;i++){
			paths[i] = files[i].getAbsolutePath();
		}
		
		JobRunner.runShortUserJob(new Job(FileManagerActivator.RESOURCES.getString(Messages.MOVE)) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				Transfer[] transferTypes  = { FileTransfer.getInstance() };
				Object[] data = {paths};
				transferTo(transferTypes , data);
				
				return Status.OK_STATUS;
			}
		});
		
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
