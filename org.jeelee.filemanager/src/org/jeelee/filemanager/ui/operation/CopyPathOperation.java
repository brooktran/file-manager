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
package org.jeelee.filemanager.ui.operation;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.utils.SystemUtils;

/**
 * <B>CopyOperation</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-9-25 created
 */
public class CopyPathOperation extends FileSystemOperation{//extends CopyFileAsTextOperation
	protected PathProvider pathProvider;
	
	public CopyPathOperation(PathProvider pathProvider, String label) {
		super(label);
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
		IStructuredSelection selection = (IStructuredSelection) pathProvider
				.getSelection();
		if (!selection.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			Iterator it = selection.iterator();
			while (it.hasNext()) {
				FileDelegate proxy = (FileDelegate) it.next();
				sb.append(getFilePath(proxy));
			}
			return sb.toString();
		}
		return null;
	}
	
	@Override
	public void setPathProvider(PathProvider pathProvider) {
		this.pathProvider = pathProvider;
	}

	@Override
	public boolean canUndo() {
		return false;
	}
	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return null;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return null;
	}

	
}
