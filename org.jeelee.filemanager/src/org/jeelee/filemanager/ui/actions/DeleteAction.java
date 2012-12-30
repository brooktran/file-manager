/* DeleteAction.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.ui.actions;

import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.filemanager.ui.views.model.FileExplorer;
import org.jeelee.utils.JobRunner;
import org.jeelee.utils.PluginResources;

/**
 * <B>DeleteAction</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-12-19 created
 */
public class DeleteAction extends SelectionDispatchAction{
	public static final String ID=Messages.DELETE;
	private final PluginResources r=FileManagerActivator.RESOURCES;
	
	protected DeleteAction(FileExplorer fileExplorer) {
		super(fileExplorer);
		setId(ID);
		FileManagerActivator.RESOURCES.configAction(this,ID );
	}

	@Override
	public void run(final IStructuredSelection selection) {
		int size = selection.size();
		String confirmMessages= "";
		if(size==1){
			confirmMessages = r.getFormatted(
					Messages.SURE_TO_DELETE_FILE, 
					((FileDelegate) selection.getFirstElement()).getName());
		}else{
			confirmMessages = r.getFormatted(
					Messages.SURE_TO_DELETE_FILE, size);
		}
		if(!MessageDialog.openConfirm(fileExplorer.getShell(), r.getString(Messages.DELETE), confirmMessages)){
			return;
		}
		
		JobRunner.runShortUserJob(new Job(r.getString(ID)) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try{
					Iterator it=selection.iterator();
					while(it.hasNext()){
						FileDelegate file =(FileDelegate) it.next();
						file.delete();
					}
					fileExplorer.refresh();
					return Status.OK_STATUS;
				}catch (Exception e) {
					return Status.CANCEL_STATUS;
				}
				
			}
		});
		
	}
}
