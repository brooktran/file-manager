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

import java.nio.file.FileAlreadyExistsException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.Item;
import org.jeelee.core.JeeleeActivator;
import org.jeelee.core.JeeleeMessages;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.ui.FileManagerActivator;
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
	public FileDelegateCellModifier(ColumnViewer viewer){
		this.viewer = viewer;  
	}

	@Override
	public void modify(Object element, String property, Object value) {
		if(property.equals(Messages.NAME)){
			FileDelegate file = (FileDelegate) ( ((Item) element).getData());

			if(file.getName().equals(value)){
				return;
			}
			renameTo(file, value.toString());

			viewer.update(file, null);
			//			treeViewer.refresh(file);
			//			
			//			List<FileDelegate> path=new LinkedList<FileDelegate>();
			//			path.add(file);
			//			for(FileDelegate parent = file.getParent();parent!=null;parent=parent.getParent()){
			//				path.add(0,parent);
			//			}
			//			treeViewer.setSelection(new TreeSelection(new TreePath(path.toArray())));
		}
	}

	private void renameTo(FileDelegate file, String newName) {
		try {
			file.renameTo( newName);
		} catch (FileAlreadyExistsException e) {
			MessageDialog.openConfirm(viewer.getControl().getShell(), 
					JeeleeActivator.RESOURCE.getString(JeeleeMessages.ERROR),
					FileManagerActivator.RESOURCES.getString(Messages.FILE_ALREADY_EXISTS));
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
