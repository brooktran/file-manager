package org.jeelee.filemanager.ui.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.filemanager.ui.views.model.FileExplorer;

public class RenameAction extends SelectionDispatchAction{
	protected RenameAction(FileExplorer fileExplorer) {
		super(fileExplorer);
		FileManagerActivator.RESOURCES.configAction(this, Messages.RENAME);
	}


	@Override
	public void run(IStructuredSelection selection) {
		if(selection.isEmpty()){
			return;
		}
		fileExplorer.rename();
	}
}
