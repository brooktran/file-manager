/* Test.java 
 * Copyright (c) 2013 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.ui.breadcrumb.layout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * <B>Test</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager Jan 9, 2013 created
 */
public class Test {

	protected Shell	shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Test window = new Test();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(532, 299);
		shell.setText("SWT Application");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		composite.setLayout(new GridLayout(6, false));
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		BreadcrumbLayout rl_composite_1 = new BreadcrumbLayout(SWT.HORIZONTAL | SWT.RIGHT_TO_LEFT);
		rl_composite_1.spacing = 0;
		rl_composite_1.marginRight = 0;
		composite_1.setLayout(rl_composite_1);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
		toolBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		
		ToolItem tltmDropdownItem = new ToolItem(toolBar, SWT.DROP_DOWN);
		tltmDropdownItem.setText("DropDown Item");
		
		ToolItem tltmDropdownItem_1 = new ToolItem(toolBar, SWT.DROP_DOWN);
		tltmDropdownItem_1.setText("DropDown Item");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("0000000");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Composite composite_2 = new Composite(composite, SWT.BORDER);
		composite_2.setBackground(composite_2.getParent().getBackground());
		GridLayout gl_composite_2 = new GridLayout(2, false);
		gl_composite_2.verticalSpacing = 0;
		gl_composite_2.marginWidth = 0;
		gl_composite_2.marginHeight = 0;
		gl_composite_2.horizontalSpacing = 0;
		composite_2.setLayout(gl_composite_2);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel = new Label(composite_2, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		lblNewLabel.setText("New Label");
		
		Button btnNewButton = new Button(composite_2, SWT.ARROW | SWT.DOWN);
		btnNewButton.setText("New Button");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		
		for(int i=0;i<10;i++){
			Combo combo = new Combo(composite_1, SWT.NONE);
			combo.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
			combo.setText(i*1111111+"");
		}

	}
}
