/* LightArrowSortRenderer.java 1.0 2012-5-25
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

import org.eclipse.nebula.widgets.grid.LightGridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.jeelee.core.JeeleeMessages;
import org.jeelee.utils.SharedResources;

/**
 * <B>LightArrowSortRenderer</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:c.zhiwu@gmail.com">c.zhiwu@gmail.com</a>
 * @version Ver 1.0.01 2012-5-25 created
 * @since org.jeelee.core Ver 1.0
 * 
 */
public class LightArrowSortRenderer extends AbstractRenderer{
	private final Cursor hoverCursor;
	private final Image arrowUp;
	private final Image arrowDown;

	private final LightGridColumn column;
	
	public LightArrowSortRenderer(LightGridColumn column) {
		super(column.getParent());
		this.column = column;
		hoverCursor = getDisplay().getSystemCursor(SWT.CURSOR_HAND);
		arrowUp = SharedResources.getResources().getImage(JeeleeMessages.ARROW_UP);
		arrowDown = SharedResources.getResources().getImage(JeeleeMessages.ARROW_DOWN);
		setBounds(arrowDown==null?new Rectangle(0, 0, 6, 6):arrowDown.getBounds());
	}
	
	@Override
	public Cursor getHoverCursor() {
		return hoverCursor;
	}
	
	@Override
	public Point computeSize(GC gc, int wHint, int hHint, Object value) {
		return new Point(16, 16);
	}

	@Override
	public void paint(GC gc, Object value) {
		 Rectangle bounds = getBounds();
		    switch (column.getSort()) {
		    case SWT.NONE:
//		      gc.drawImage(this.asterisk, bounds.x, bounds.y);
		      break;
		    case SWT.UP:
		      gc.drawImage(this.arrowUp, bounds.x, bounds.y);
		      break;
		    case SWT.DOWN:
		      gc.drawImage(this.arrowDown, bounds.x, bounds.y);
		    }
	}
}
