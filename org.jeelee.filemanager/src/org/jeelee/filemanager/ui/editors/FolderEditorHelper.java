/* DirectoryEditorHelper.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.ui.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.utils.AppLogging;
import org.jeelee.utils.JobRunner;

/**
 * <B>DirectoryEditorHelper</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-12-15 created
 */
public class FolderEditorHelper {
	
	public static void openEditor(final FileDelegate file,final IWorkbenchPage page,
			final boolean openInNewEditor ){
		if(!file.isVisitable()){
			return;
		}
		
		if(!file.isContentsInitialized()){
			file.resolveChildren();
		}
		
		JobRunner.runShortUserJob(new Job(FileManagerActivator.RESOURCES.getFormatted(Messages.PREPARE_DIRECTORY,file.getAbsolutePath())) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				final FileResourceInput input =new FileResourceInput(file,openInNewEditor);
				final FolderEditor editor = (FolderEditor) page.findEditor(input);
				if(openInNewEditor || editor == null){
					page.getWorkbenchWindow().getShell().getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							try {
								page.openEditor(input,FolderEditor.ID);
							} catch (PartInitException e) {
								AppLogging.handleException(e);
							}
						}
					});
				} else {
					page.getWorkbenchWindow().getShell().getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							editor.updateInput(input);
							page.bringToTop(editor);
						}
					});
				}
				return Status.OK_STATUS;
			}
		});
		
		
	}
}
