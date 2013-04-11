/* FileView.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.ui.views.model;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchSite;
import org.jeelee.event.PropertySupport;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.filters.FileFilterDelegate;
import org.jeelee.filemanager.core.operation.PathProvider;

/**
 * <B>FileView</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-8-14 created
 */
public interface FileExplorer  extends PropertySupport ,ISelectionProvider{
	String REFRESHED_PROPERTY="refreshed";
	
	FileDelegate getDefaultSelection();
	PathProvider getPathProvider();
	Shell getShell();
	
	IWorkbenchSite getWorkbenchSite();
	
	
	void rename();
	void refresh();
	
	@Override
	void setSelection(ISelection selection);
	FileFilterDelegate getFilter();
	IUndoContext getUndoableContext();

}
