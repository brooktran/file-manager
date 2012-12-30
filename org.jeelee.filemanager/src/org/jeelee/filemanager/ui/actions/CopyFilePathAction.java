package org.jeelee.filemanager.ui.actions;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.operation.CopyPathOperation;
import org.jeelee.filemanager.ui.views.model.FileExplorer;

public class CopyFilePathAction extends UndoableAction {

	protected CopyFilePathAction(FileExplorer fileExplorer) {
		super(fileExplorer);
		FileManagerActivator.RESOURCES.configAction(this, "copy.file.path");
	}

	@Override
	protected IUndoableOperation getOperation(IStructuredSelection selection) {
		return new CopyPathOperation(fileExplorer.getPathProvider(), getText());
	}


}
