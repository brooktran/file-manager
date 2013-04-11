/* UndoableAction.java 
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

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jeelee.filemanager.core.operation.OperationExecutor;
import org.jeelee.filemanager.core.operation.PathProvider;
import org.jeelee.filemanager.core.operation.SimplePathProvider;
import org.jeelee.filemanager.ui.views.model.FileExplorer;

/**
 * <B>UndoableAction</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-9-25 created
 */
public abstract class UndoableAction extends SelectionDispatchAction{
	protected UndoableAction(FileExplorer fileExplorer) {
		super(fileExplorer);
	}

	
	@Override
	public void run(final IStructuredSelection selection) {
		final IUndoableOperation op = getOperation(selection);
		if(op == null){
			return;
		}
		OperationExecutor.runOperationJob(op,fileExplorer);
		
	}
	protected PathProvider getPathProvider() {
		PathProvider pathProvider = fileExplorer.getPathProvider();
		return new SimplePathProvider(pathProvider.getSource(), pathProvider.getTarget());
	}
	
	protected abstract IUndoableOperation getOperation(IStructuredSelection selection) ;
	
}
