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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.jeelee.filemanager.ui.views.model.FileExplorer;

/**
 * <B>UndoableAction</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-9-25 created
 */
public abstract class UndoableAction extends SelectionDispatchAction{
	private IUndoContext undoContext;

	protected UndoableAction(FileExplorer fileExplorer) {
		super(fileExplorer);
		
		undoContext = new ObjectUndoContext(fileExplorer);
		int limit = 1000; // TODO read from preference
		getOperationHistory().setLimit(undoContext, limit);
	}
	private IOperationHistory getOperationHistory() {
		return PlatformUI.getWorkbench().getOperationSupport()
				.getOperationHistory();
	}
	
	@Override
	public void run(IStructuredSelection selection) {
		IUndoableOperation op = getOperation(selection);
		if(op == null){
			return;
		}
		try {
			op.addContext(undoContext);
			getOperationHistory().execute(op, null, null);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	protected abstract IUndoableOperation getOperation(IStructuredSelection selection) ;
	
	
	
	
	
	
}
