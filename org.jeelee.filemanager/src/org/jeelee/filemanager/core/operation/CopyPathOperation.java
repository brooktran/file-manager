/* CopyOperation.java 
 * Copyright (c) 2012 by Brook Tran
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
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.utils.ArrayUtils;
import org.jeelee.utils.SystemUtils;

/**
 * <B>CopyOperation</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-9-25 created
 */
public class CopyPathOperation extends FileSystemOperation{

	public CopyPathOperation(PathProvider pathProvider) {
		super(FileManagerActivator.RESOURCES.getString(Messages.COPY_FILE_PATH)+" "+ArrayUtils.toString(pathProvider.getSource()));
		this.pathProvider = pathProvider;
	}

	protected void transferTo(Transfer[] transfers,Object[] data) {
		Clipboard clipboard = new Clipboard(Display
				.getCurrent());
		clipboard.setContents(data, transfers);
		clipboard.dispose();
	}
	
	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		String paths =getSelection();
		if (paths == null) {
			return Status.CANCEL_STATUS;
		}
		
		TextTransfer textTransfer = TextTransfer.getInstance();
		Transfer[] transfers = { textTransfer };
		Object[] data = {paths};
		transferTo(transfers, data);
		return Status.OK_STATUS;
	}

	protected String getFilePath(FileDelegate proxy) {
		return proxy.getAbsolutePath()+SystemUtils.lineSeparator();
	}
	
	protected String getSelection() {
		StringBuilder sb = new StringBuilder();
		for(FileDelegate file:pathProvider.getSource()){
			sb.append(getFilePath(file));
		}
		return sb.toString();
	}
	
	

	

	
}
