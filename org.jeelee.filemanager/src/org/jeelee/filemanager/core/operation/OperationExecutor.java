/* OperationExecutor.java 
 * Copyright (c) 2013 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.core.operation;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.jeelee.filemanager.ui.views.model.FileExplorer;
import org.jeelee.utils.AppLogging;

/**
 * <B>OperationExecutor</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager Jan 7, 2013 created
 */
public class OperationExecutor {

	public static void runOperationJob(final IUndoableOperation op,
			final FileExplorer fileExplorer) {
//		JobRunner.runShortUserJob(new Job(op.getLabel()) {
//			@Override
//			protected IStatus run(IProgressMonitor monitor) {
				try {
//					monitor.setTaskName(op.getLabel());
//					monitor.beginTask(op.getLabel(), selection.size());
					op.addContext(fileExplorer.getUndoableContext()); 
					OperationHistoryFactory
								.getOperationHistory().execute(op,
											null, null);
				} catch (ExecutionException e) {
					AppLogging.handleException(e);
//					return Status.CANCEL_STATUS;
				}
//				return Status.OK_STATUS;
//			}
//		});
		
	}
}
