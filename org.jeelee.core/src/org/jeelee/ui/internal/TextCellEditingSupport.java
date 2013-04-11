/* TextCellEditingSupport.java 1.0 2012-5-23
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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * <B>TextCellEditingSupport</B>
 * 
 * @author Brook Tran . Email: <a href="mailto:c.brook.tran@gmail.com">c.brook.tran@gmail.com</a>
 * @version Ver 1.0.01 2012-5-23 created
 * @since org.jeelee.core Ver 1.0
 * 
 */
public abstract class TextCellEditingSupport extends EditingSupport{
	protected final TextCellEditor cellEditor;

	public TextCellEditingSupport(ColumnViewer viewer) {
		super(viewer);
		cellEditor=new TextCellEditor((Composite) viewer.getControl());
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return cellEditor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}


}
