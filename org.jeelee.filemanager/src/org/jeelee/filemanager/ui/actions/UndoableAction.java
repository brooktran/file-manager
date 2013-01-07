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
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jeelee.filemanager.core.operation.PathProvider;
import org.jeelee.filemanager.core.operation.SimplePathProvider;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.views.model.FileExplorer;
import org.jeelee.utils.AppLogging;
import org.jeelee.utils.JobRunner;

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
		FileManagerActivator.getOperationHistory().setLimit(undoContext, limit);
	}

	
	@Override
	public void run(final IStructuredSelection selection) {
		final IUndoableOperation op = getOperation(selection);
		if(op == null){
			return;
		}
		JobRunner.runShortUserJob(new Job(getText()) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					monitor.beginTask(getText(), selection.size());
					op.addContext(undoContext);
					FileManagerActivator.getOperationHistory().execute(op,
							monitor, null);
				} catch (ExecutionException e) {
					AppLogging.handleException(e);
					return Status.CANCEL_STATUS;
				}
				return Status.OK_STATUS;
			}
		});
		
	}
	protected PathProvider getPathProvider() {
		PathProvider pathProvider = fileExplorer.getPathProvider();
		return new SimplePathProvider(pathProvider.getSource(), pathProvider.getTarget());
	}
	
	protected abstract IUndoableOperation getOperation(IStructuredSelection selection) ;
	
}
