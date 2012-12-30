package org.jeelee.filemanager.ui.actions;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.filemanager.ui.views.model.FileExplorer;

public class RefreshAction extends SelectionDispatchAction{
	public static final String ID = Messages.REFRESH;
	
	protected RefreshAction(FileExplorer fileExplorer) {
		super(fileExplorer);
		FileManagerActivator.RESOURCES.configAction(this, Messages.REFRESH);
	}
	

	@Override
	public void run(IStructuredSelection selection) {
		fileExplorer.refresh();
	}
	
	@Override
	public void selectionChanged(ISelection selection) {
	}
	
	@Override
	public String getId() {
		return Messages.REFRESH;
	}
}
