/* GridViewerFactory.java 1.0 2012-5-25
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

import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;

/**
 * <B>GridViewerFactory</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:c.zhiwu@gmail.com">c.zhiwu@gmail.com</a>
 * @version Ver 1.0.01 2012-5-25 created
 * @since org.jeelee.core Ver 1.0
 * 
 */
public class GridViewerFactory {

	public static GridViewerColumn createGridColumn(
			GridTableViewer tableViewer, int style, String columnName) {
		GridViewerColumn col =new GridViewerColumn(tableViewer,style);
		col.getColumn().setText(columnName);
		col.getColumn().setWidth(140);
		col.getColumn().setMoveable(true);
		return col;
	}

}
