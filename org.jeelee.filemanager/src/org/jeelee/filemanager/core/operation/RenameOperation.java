/* RenameOperation.java 
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

import java.nio.file.FileAlreadyExistsException;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.jeelee.core.JeeleeActivator;
import org.jeelee.core.JeeleeMessages;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.utils.PluginResources;

/**
 * <B>RenameOperation</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-9-25 created
 */
public class RenameOperation extends FileSystemOperation{
	private String newName;
	private FileDelegate file;
	public RenameOperation(FileDelegate file, String newName) {
		super(JeeleeActivator.RESOURCE.getString(Messages.RENAME)+" "+file.getName());
		this.newName =newName;
		this.file = file;
	}

	
	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		String oldName = file.getName();
		renameTo(file, newName);
		newName  = oldName;
		System.out.println("RenameOperation.execute()");
		return Status.OK_STATUS; 
	}
	
	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return execute(monitor, info);
	}
	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return execute(monitor, info);
	}
	
	private void renameTo(FileDelegate file, String newName) {
		try {
			file.renameTo( newName);
		} catch (FileAlreadyExistsException e) {
			PluginResources r = JeeleeActivator.RESOURCE;
			MessageDialog.openConfirm(Display.getDefault().getActiveShell(), 
					r.getString(JeeleeMessages.ERROR),
					r.getString(Messages.FILE_ALREADY_EXISTS));
		}
	}
	
}
