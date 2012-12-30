/* AbstractPluginDialog.java 1.0 2012-5-22
 * 
 * Copyright (c) 2012 by Chen Zhiwu
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.ui.internal;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * <B>AbstractPluginDialog</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:c.zhiwu@gmail.com">c.zhiwu@gmail.com</a>
 * @version Ver 1.0.01 2012-5-22 created
 * @since org.jeelee.core Ver 1.0
 * 
 */
public class AbstractPluginDialog extends Dialog{
	private final AbstractUIPlugin plugin;
	private final String dialogID;

	protected AbstractPluginDialog(AbstractUIPlugin plugin,String ID,Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		this.plugin = plugin;
		this.dialogID = ID;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		ControlBoundSettingsSupport.configDialog(newShell,plugin,dialogID,getPreferenceSize());
	}
	
	
	protected Point getPreferenceSize(){
		return new Point(400,500);
	}

	
}
