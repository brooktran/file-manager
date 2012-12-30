/* LightGridColumn.java 1.0 2012-5-24
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

import org.eclipse.nebula.widgets.grid.internal.AbstractRenderer;
import org.eclipse.nebula.widgets.grid.internal.DefaultColumnHeaderRenderer;
import org.eclipse.nebula.widgets.grid.internal.LightArrowSortRenderer;

/**
 * <B>LightGridColumn</B>
 * 
 * @author Zhi-Wu Chen. Email: <a
 *         href="mailto:c.zhiwu@gmail.com">c.zhiwu@gmail.com</a>
 * @version Ver 1.0.01 2012-5-24 created
 * @since org.jeelee.core Ver 1.0
 * 
 */
public class LightGridColumn extends GridColumn {
	private AbstractRenderer sortRenderer;
	private boolean isSortable=true;

	public LightGridColumn(Grid table, int style) {
		this(table, style, -1);
	}

	public LightGridColumn(Grid table, int style, int index) {
		super(table, style, index);
		init();
	}

	public LightGridColumn(GridColumnGroup parent, int style) {
		super(parent.getParent(), style, parent.getNewColumnIndex());
		init();
	}
	
	public int getIndex (){
		return getParent().indexOf(this);
	}

	private void init() {
		this.sortRenderer = new LightArrowSortRenderer(this);
		
		((DefaultColumnHeaderRenderer)getHeaderRenderer()).setArrowRenderer(sortRenderer);
	}

	public AbstractRenderer getSortRenderer() {
		return sortRenderer;
	}

	public void setSortRenderer(AbstractRenderer sortRenderer) {
		 checkWidget();
		this.sortRenderer = sortRenderer;
	}

	public int computeHeaderHeight() {
		int y = 3 + getParent().sizingGC.getFontMetrics().getHeight() + 3;

		if (getImage() != null) {
			y = Math.max(y, 3 + getImage().getBounds().height + 3);
		}

		if (getHeaderControl() != null) {
			y += getHeaderControl().computeSize(-1, -1).y;
		}

		return y;
	}

	public boolean isOverSortArrow(int x) {
		if (!isSortable()) {
			return false;
		}
		int arrowEnd = getBounds().width - 6;
		int arrowBegin = arrowEnd - this.sortRenderer.getBounds().width;
		return (x >= arrowBegin) && (x <= arrowEnd);
	}

	public int computeHeaderWidth() {
		int x = 6;
		if (getImage() != null) {
			x += getImage().getBounds().width + 3;
		}
		x += getParent().sizingGC.stringExtent(getText()).x + 6;
		if (isSortable()) {
			x += 6 + sortRenderer.getBounds().width;
		}

		return x;
	}

	public int computeFooterHeight() {
		int y = 3;

		y += getParent().sizingGC.getFontMetrics().getHeight();

		y += 3;

		if (getFooterImage() != null) {
			y = Math.max(y, 3 + getFooterImage().getBounds().height + 3);
		}

		return y;
	}

	public boolean isSortable() {
		return isSortable;
//		getSort() != SWT.NONE;
	}

	public void setSortable(boolean isSortable) {
		this.isSortable = isSortable;
	}

	
	@Override
	public LightGrid getParent() {
		 checkWidget();
		return (LightGrid) super.getParent();
	}

}
