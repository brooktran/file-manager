package org.jeelee.filemanager.ui.actions;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.filemanager.ui.operation.CopyFileNameOperation;
import org.jeelee.filemanager.ui.views.model.FileExplorer;

public class CopyFileNameAction extends UndoableAction {

	protected CopyFileNameAction(FileExplorer fileExplorer) {
		super(fileExplorer);
		FileManagerActivator.RESOURCES.configAction(this,Messages.COPY_FILE_NAME);
	}

	

	@Override
	protected IUndoableOperation getOperation(IStructuredSelection selection) {
		return new CopyFileNameOperation(fileExplorer.getPathProvider(),getText());
	}

}
