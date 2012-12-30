/* BatchRenameDialog.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.ui.dialog;

import java.nio.file.Paths;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jeelee.filemanager.core.FileDelegate;

/**
 * <B>BatchRenameDialog</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-10-1 created
 */
public class BatchRenameDialog extends TitleAreaDialog {
	private Text txtBak;
	private Text txtDat;
	private Text txtbackup;
	private Text text;
	
	private FileDelegate path;
	private FileDelegate target;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public BatchRenameDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblNewLabel_3 = new Label(container, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("\u6587\u4EF6\u8DEF\u5F84");
		
		text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(getShell());
				String target = dialog.open();
				text.setText(target==null?"":target);
				path = target==null?null:new FileDelegate(Paths.get(target));
				
			}
		});
		btnNewButton.setText("\u6253\u5F00");
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setToolTipText("");
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("\u540E\u7F00\u540D:");
		
		txtBak = new Text(container, SWT.BORDER);
		txtBak.setText("bak");
		txtBak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("\u66F4\u6539\u4E3A:");
		
		txtDat = new Text(container, SWT.BORDER);
		txtDat.setText("dat");
		txtDat.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setText("\u5907\u4EFD\u81F3:");
		
		txtbackup = new Text(container, SWT.BORDER);
		txtbackup.setText("/backup");
		txtbackup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button button = new Button(container, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(getShell());
				String targetString = dialog.open();
				txtbackup.setText(targetString==null?"":targetString);
				target = targetString==null?null:new FileDelegate(Paths.get(targetString));
				
			}
		});
		button.setText("\u6253\u5F00");

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	public void setPath(FileDelegate path) {
		this.path = path;
	}
	
	
	@Override
	protected void okPressed() {
		handleRename();
		
		super.okPressed();
	}

	private void handleRename() {
		if (path == null) {
			MessageDialog.openError(getShell(), "错误", "请选择源文件路径");
			return;
		}
		if(target == null){
			
		}
	}

}
