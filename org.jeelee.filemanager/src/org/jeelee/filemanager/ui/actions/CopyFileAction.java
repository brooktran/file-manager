package org.jeelee.filemanager.ui.actions;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.operation.CopyFileOperation;
import org.jeelee.filemanager.ui.views.model.FileExplorer;

public class CopyFileAction extends UndoableAction{

	protected CopyFileAction(FileExplorer fileExplorer) {
		super(fileExplorer);
		FileManagerActivator.RESOURCES.configAction(this, "copy.file");
	}

	@Override
	protected IUndoableOperation getOperation(IStructuredSelection selection) {
		return new CopyFileOperation(fileExplorer.getPathProvider(),getText());
	}
}
