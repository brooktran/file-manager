/* OpenInNewEditorAction.java 
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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.filemanager.ui.editors.FolderEditorHelper;
import org.jeelee.filemanager.ui.views.model.FileExplorer;
import org.jeelee.utils.PluginResources;

/**
 * <B>OpenInNewEditorAction</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-12-15 created
 */
public class OpenInNewEditorAction extends SelectionDispatchAction {

	protected OpenInNewEditorAction(FileExplorer fileExplorer) {
		super(fileExplorer);
		FileManagerActivator
		.RESOURCES.configAction(this, Messages.OPEN_IN_NEW_EDITOR);
	}
	
	
	@Override
	public void selectionChanged(IStructuredSelection selection) {
		@SuppressWarnings("rawtypes")
		Iterator it=selection.iterator();
		boolean enable = !selection.isEmpty();
		while(it.hasNext()){
			Object obj= it.next();
			if(!(obj instanceof FileDelegate)){
				enable = false;
				break;
			}
			FileDelegate file=(FileDelegate) obj;
			if(!file.isDirectory()){
				enable = false;
				break;
			}
		}
		setEnabled(enable);
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public void run(IStructuredSelection selection) {
		PluginResources r=FileManagerActivator.RESOURCES;
		Iterator it = selection.iterator();
		while(it.hasNext()){
			final FileDelegate file=(FileDelegate)it.next();
//			JobRunner.runShortUserJob(new Job(r.getString(Messages.OPEN)) {
//				@Override 
//				protected IStatus run(IProgressMonitor monitor) {
					FolderEditorHelper.openEditor(
							file, fileExplorer.getWorkbenchSite().getPage(),true);
//					return Status.OK_STATUS;
//				}
//			});
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}
