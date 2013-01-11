/* OpenOperation.java 
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
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.program.Program;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.utils.RuntimeExecutor;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.filemanager.ui.preferences.IPreferenceConstants;
import org.jeelee.utils.AppLogging;
import org.jeelee.utils.ArrayUtils;



/**
 * <B>OpenOperation</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager Jan 4, 2013 created
 */
public class OpenOperation extends FileSystemOperation{
	private ExecuteOrder order;

	public OpenOperation(PathProvider pathProvider) {
		super(FileManagerActivator.RESOURCES.getString(Messages.OPEN)+" "+ArrayUtils.toString(pathProvider.getSource()));
		setPathProvider(pathProvider);
		setOrder(ExecuteOrder.CONCURRENT);
	}
	
	public void setOrder(ExecuteOrder order) {
		this.order = order;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		FileDelegate[] files = getSelectedFiles();
		String runCommand = FileManagerActivator.getDefault().getPreferenceStore().getString(IPreferenceConstants.COMMAND_RUN);
		try {
			for(FileDelegate f:files){
//				Program.launch(f.getAbsolutePath());
				switch (order) {
					case ONE_AFTER_ONE:
						Process process =RuntimeExecutor.execute(runCommand, f);
						process.waitFor();
						break;
					default:
						Program.launch(f.getAbsolutePath());
						break;
				}
			}
		} catch (Exception e) {
			AppLogging.handleException(e);
			return Status.CANCEL_STATUS;
		}
		
		return Status.OK_STATUS;
	}


	public enum ExecuteOrder{
		ONE_AFTER_ONE,
		CONCURRENT,
	}


}
