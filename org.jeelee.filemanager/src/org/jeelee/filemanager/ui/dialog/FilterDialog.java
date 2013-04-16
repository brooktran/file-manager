/*
 * @(#)FilterDialog.java  Aug 8, 2012
 *
 * Copyright (c) 2012 by the original authors of org.jeelee.filemanager
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and  
 * contributors of the org.jeelee.filemanager project ("the copyright holders").  
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * the copyright holders. For details see accompanying license terms. 
 */
package org.jeelee.filemanager.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jeelee.filemanager.core.filters.FileFilterDelegate;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.views.model.FilterViewer;
import org.jeelee.ui.internal.AbstractPluginDialog;

/**
 * <B>FilterDialog</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager  
 * 
 */
public class FilterDialog extends AbstractPluginDialog {

	private FileFilterDelegate fileFilter;
	private FilterViewer filterViewer;
	private ShellDecorator fShellDecorator;
	
	public FilterDialog(String id,Shell parentShell, FileFilterDelegate fileFilter) {
		super(FileManagerActivator.getDefault(),FilterDialog.class.getName()+id,parentShell);
		setShellStyle( SWT.RESIZE | SWT.TOOL);

		this.fileFilter = fileFilter;
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		filterViewer = new FilterViewer(container,fileFilter);
		Composite composite=filterViewer.getFilterView();
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		fShellDecorator = new ShellDecorator(getShell());
		fShellDecorator.hookListener(container,parent);
		
		return container;
	}
	

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);//IDialogConstants.CLOSE_ID
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(616, 457);
	}
	
	@Override
	protected void okPressed() {
		filterViewer.updateKeyword();
		super.okPressed();
	}


}
