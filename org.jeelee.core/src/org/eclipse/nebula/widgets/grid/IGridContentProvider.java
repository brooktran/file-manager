/* IGridContentProvider.java 1.0 2012-5-24
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
package org.eclipse.nebula.widgets.grid;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.swt.graphics.Point;

/**
 * <B>IGridContentProvider</B>
 * 
 * @author Zhi-Wu Chen. Email: <a
 *         href="mailto:c.zhiwu@gmail.com">c.zhiwu@gmail.com</a>
 * @version Ver 1.0.01 2012-5-24 created
 * @since org.jeelee.core Ver 1.0
 * 
 */
public interface IGridContentProvider extends IStructuredContentProvider {
	
	Point getSize();
	Object getElement(Point pos);
	void updateColumn(LightGridColumn column);
}
