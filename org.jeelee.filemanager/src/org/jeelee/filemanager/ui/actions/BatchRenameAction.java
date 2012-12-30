package org.jeelee.filemanager.ui.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.dialog.BatchRenameDialog;
import org.jeelee.filemanager.ui.views.model.FileExplorer;

public class BatchRenameAction extends SelectionDispatchAction {
	public static final String ID="batch.rename";
	public BatchRenameAction(FileExplorer fileExplorer) {
		super(fileExplorer);
		setId(ID);
		FileManagerActivator.RESOURCES.configAction(this,ID);
	}
	
	
	@Override
	public void run(IStructuredSelection selection) {
		BatchRenameDialog dialog = new BatchRenameDialog(fileExplorer.getShell());

		if(!selection.isEmpty()){
			FileDelegate file =(FileDelegate) selection.getFirstElement();
			dialog.setPath(file);
			
		}
		
		dialog.open();
	}
	
}
