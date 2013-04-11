/* FileDelegateCellModifier.java 1.0 2010-2-2
 * 
 * Copyright (c) 2010 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.ui.views.model;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.Item;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.operation.OperationExecutor;
import org.jeelee.filemanager.core.operation.RenameOperation;
import org.jeelee.filemanager.ui.Messages;

/**
 * <B>FileDelegateCellModifier</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @version Ver 1.0.01 Aug 7, 2012 created
 * @since org.jeelee.filemanager Ver 1.0
 * 
 */
public final class FileDelegateCellModifier implements ICellModifier {

	private ColumnViewer viewer;
	private FileExplorer fileExplorer;
	public FileDelegateCellModifier(ColumnViewer viewer,FileExplorer fileExplorer){
		this.viewer = viewer;  
		this.fileExplorer = fileExplorer;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		if(property.equals(Messages.NAME)){
			FileDelegate file = (FileDelegate) ( ((Item) element).getData());

			if(file.getName().equals(value)){
				return;
			}
			RenameOperation op = new RenameOperation(file, value.toString());
			OperationExecutor.runOperationJob(op, fileExplorer);
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			viewer.update(file,null);
		}
	}

	

	@Override
	public Object getValue(Object element, String property) {
		return ((FileDelegate)element).getName();
	}

	@Override
	public boolean canModify(Object element, String property) {
		return ((FileDelegate)element).isReadable();
	}
}
