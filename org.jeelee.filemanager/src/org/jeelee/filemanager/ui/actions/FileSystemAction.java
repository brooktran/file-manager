package org.jeelee.filemanager.ui.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.utils.RuntimeExecutor;
import org.jeelee.filemanager.ui.views.model.FileExplorer;

public abstract class FileSystemAction extends SelectionDispatchAction{

	protected FileSystemAction(FileExplorer fileExplorer) {
		super(fileExplorer);
	}
	
	@Override
	public void run(IStructuredSelection selection) {
		if(selection==null || selection.isEmpty()){
			return;
		}
		FileDelegate file=(FileDelegate) selection.getFirstElement();
		RuntimeExecutor.execute(getCommand(), file);
	}
	
	protected abstract String getCommand();

}
