/* LightCellRenderer.java 1.0 2012-5-24
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
package org.eclipse.nebula.widgets.grid.internal;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.nebula.widgets.grid.LightGrid;
import org.eclipse.swt.graphics.Color;

/**
 * <B>LightCellRenderer</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:c.zhiwu@gmail.com">c.zhiwu@gmail.com</a>
 * @version Ver 1.0.01 2012-5-24 created
 * @since org.jeelee.core Ver 1.0
 * 
 */
public class LightCellRenderer extends DefaultCellRenderer{
	public LightCellRenderer(Grid grid) {
		super(grid);
	}

	@Override
	protected Color getItemBackground(GridItem item) {
		return 	((LightGrid)grid).getCellBackground(getColumn(),getRow());
	}

}
