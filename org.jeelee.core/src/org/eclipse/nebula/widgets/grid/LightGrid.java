/* LightGrid.java 1.0 2012-5-24
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


import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

/**
 * <B>LightGrid</B>
 * 
 * @author Zhi-Wu Chen. Email: <a
 *         href="mailto:c.zhiwu@gmail.com">c.zhiwu@gmail.com</a>
 * @version Ver 1.0.01 2012-5-24 created
 * @since org.jeelee.core Ver 1.0
 * 
 */
public class LightGrid extends Grid {
	public static final int MAX_TOOLTIP_LENGTH = 1000;
//	public static final int Event_ChangeSort = 1000;
	public static final int EventDoubleClickColumn=1001;
	
	private final int maxColumnDefWidth = 1000;
//	private Point gridSize;

	
	
	private IGridContentProvider contentProvider;
	private ILabelProvider contentLabelProvider;
	private ILabelProvider columnLabelProvider;
	private ILabelProvider rowLabelProvider;

	// private final Set<Point> selectedCells = new TreeSet<Point>(new
	// CellComparator());
	// private final Set<Point> selectedCellsBeforeRangeSelect = new
	// TreeSet<Point>(
	// new CellComparator());
	// private final Map<Integer, Boolean> selectedRows =new HashMap<>();

	
	public LightGrid(Composite parent, int style) {
		super(parent, style);
	}
//	@Override
//	boolean handleColumnHeaderPush(MouseEvent e) {
//		if (super.handleColumnHeaderPush(e) ) {
//			LightGridColumn col = (LightGridColumn) columnBeingPushed;
//			Event event = new Event();
//
//			event.x = e.x;
//			event.y = e.y;
//			event.data = col;
//			event.stateMask = e.stateMask;
//			notifyListeners(Event_ChangeSort, event);
//			return true;
//		}
//		return false;
//	}
	@Override
	boolean isOverSortArrow(GridColumn column, int x) {
		if(column instanceof LightGridColumn){
			return ((LightGridColumn)column).isOverSortArrow(x);
		}
		return super.isOverSortArrow(column, x);
	}
	
	@Override
	Cursor getColumnHoverCursor(GridColumn column) {
		if(column instanceof LightGridColumn){
			return ((LightGridColumn)column).getSortRenderer().getHoverCursor();
		}
		return super.getColumnHoverCursor(column);
	}
	
	
	
	
//	@Override
//	public int getItemCount() {
//		if(gridSize==null){
//			return super.getItemCount();
//		}
//		return gridSize.y;
//	}

	// -------------- selection
	// @Override
	// public void deselect(int index) {
	// checkWidget();
	//
	// if ((index < 0) || (index > getItemCount() - 1)) {
	// return;
	// }
	// deselectCells(getCells(index));
	// redraw();
	// }
	// private List<Point> getCells(int row) {
	// List<Point> cells = new ArrayList<Point>();
	// getCells(row, cells);
	// return cells;
	// }
	// private void getCells(int row, List<Point> cells) {
	// for (int i = 0; i < getColumns().length; i++)
	// cells.add(new Point(i, row));
	// }
	// public void deselectCells(Collection<Point> cells) {
	// checkWidget();
	//
	// if (cells == null) {
	// SWT.error(4);
	// return;
	// }
	//
	// for (Point cell : cells) {
	// if (cell == null) {
	// SWT.error(4);
	// }
	// }
	// for (Point cell : cells) {
	// selectedCells.remove(cell);
	// }
	//
	// updateSelectionCache();
	// redraw();
	// }
	// void updateSelectionCache() {
	// selectedColumns.clear();
	// selectedRows.clear();
	//
	// int[] colIndex = new int[selectedCells.size()];
	// boolean[] isColSelected = new boolean[selectedCells.size()];
	// // IntKeyMap columnIndices = new IntKeyMap();
	// int count =0;
	// for (Point cell : selectedCells) {
	// selectedRows.put(cell.x, Boolean.TRUE);
	//
	// colIndex[count]=cell.y;
	// isColSelected[count]=true;
	// count++;
	// }
	// for (Integer columnIndex : colIndex)
	// this.selectedColumns.add(getColumn(columnIndex));
	// }
	//
	// @Override
	// public void deselect(int start, int end) {
	// checkWidget();
	//
	// for (int i = start; i <= end; i++) {
	// if (i < 0) {
	// continue;
	// }
	// if (i > getItemCount() - 1) {
	// break;
	// }
	// deselectCells(getCells(i));
	// }
	// redraw();
	// }
	//
	// @Override
	// public void deselect(int[] indices) {
	// checkWidget();
	// if (indices == null) {
	// SWT.error(4);
	// return;
	// }
	//
	// for (int j : indices) {
	// if ((j >= 0) && (j < getItemCount())) {
	// deselectCells(getCells(j));
	// }
	// }
	// redraw();
	// }
	//
	// @Override
	// public void deselectAll() {
	// checkWidget();
	// deselectAllCells();
	// }
	//
	// -------------- end selection

	// -------------- viewer

//	public void refresh() {
//		removeAll();
//		if (contentProvider == null) {
//			return;
//		}
//		gridSize = contentProvider.getSize();
//		currentVisibleItems = gridSize.x;
//		topIndex = 0;
//		bottomIndex = -1;
//		startColumnIndex = -1;
//		endColumnIndex = -1;
//
//		int columnCount = contentProvider.getSize().x;
//		LightGridColumn column;
//		for (int i = 0; i < columnCount; i++) {
//			column = new LightGridColumn(this, SWT.NONE);
//			column.setText(columnLabelProvider.getText(i));
//			column.setImage(columnLabelProvider.getImage(i));
//			contentProvider.updateColumn(column);
//		}
//
//		if (getColumnCount() == 1) {
//			getColumn(0).setWidth(
//					getSize().x - getRowHeaderWidth()
//							- getHScrollSelectionInPixels()
//							- getVerticalBar().getSize().x);
//		} else {
//			GridColumn[] arrayOfGridColumn;
//			int localGridColumn1 = (arrayOfGridColumn = getColumns()).length;
//			for (int cc = 0; cc < localGridColumn1; cc++) {
//				GridColumn curColumn = arrayOfGridColumn[cc];
//				curColumn.pack();
//				if (curColumn.getWidth() > maxColumnDefWidth) {
//					curColumn.setWidth(maxColumnDefWidth);
//				}
//			}
//		}
//		updateScrollbars();
//	}

	public ILabelProvider getRowLabelProvider() {
		return rowLabelProvider;
	}

	public void setRowLabelProvider(ILabelProvider rowLabelProvider) {
		this.rowLabelProvider = rowLabelProvider;
	}

	public IGridContentProvider getContentProvider() {
		return contentProvider;
	}

	public void setContentProvider(IGridContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

	public ILabelProvider getContentLabelProvider() {
		return contentLabelProvider;
	}

	public void setContentLabelProvider(ILabelProvider contentLabelProvider) {
		this.contentLabelProvider = contentLabelProvider;
	}

	public ILabelProvider getColumnLabelProvider() {
		return columnLabelProvider;
	}

	public void setColumnLabelProvider(ILabelProvider columnLabelProvider) {
		this.columnLabelProvider = columnLabelProvider;
	}

	public String getCellText(int column, int row) {
		if (contentLabelProvider != null) {
			return contentLabelProvider.getText(new Point(column, row));
		}
		return null;
	}

	public String getCellToolTip(int column, int row) {
		if (contentLabelProvider != null) {
			String toolTip = getCellText(column, row);
			if (toolTip == null) {
				return null;
			}

			Point ttSize = sizingGC.textExtent(toolTip);
			GridColumn itemColumn = getColumn(column);
			if ((ttSize.x > itemColumn.getWidth())
					|| (ttSize.y > getItemHeight())) {
				int gridHeight = getBounds().height;
				if (ttSize.y > gridHeight) {
					StringBuilder newToolTip = new StringBuilder();
					StringTokenizer st = new StringTokenizer(toolTip, "'\n");
					int maxLineNumbers = gridHeight / getItemHeight();
					int lineNumber = 0;
					while (st.hasMoreTokens()) {
						newToolTip.append(st.nextToken()).append('\n');
						lineNumber++;
						if (lineNumber >= maxLineNumbers) {
							break;
						}
					}
					toolTip = newToolTip.toString();
				}
				return toolTip;
			}
			return null;
		}

		return null;
	}

	public Image getCellImage(int column, int row) {
		if (contentLabelProvider != null) {
			return contentLabelProvider.getImage(new Point(column, row));
		}
		return null;
	}

	public Color getCellBackground(int column, int row) {
		Color color = null;
		if ((contentLabelProvider instanceof IColorProvider)) {
			color = ((IColorProvider) contentLabelProvider)
					.getBackground(new Point(column, row));
		}
		return color != null ? color : getBackground();
	}

	public Color getCellForeground(int column, int row) {
		Color color = null;
		if ((contentLabelProvider instanceof IColorProvider)) {
			color = ((IColorProvider) contentLabelProvider)
					.getForeground(new Point(column, row));
		}
		return color != null ? color : getForeground();
	}

	public Rectangle getCellBounds(int columnIndex, int rowIndex) {
		if (!isShown(rowIndex)) {
			return new Rectangle(-1000, -1000, 0, 0);
		}
		GridColumn column = getColumn(columnIndex);
		Point origin = getOrigin(column, rowIndex);

		if ((origin.x < 0) && (isRowHeaderVisible())) {
			return new Rectangle(-1000, -1000, 0, 0);
		}
		return new Rectangle(origin.x, origin.y, column.getWidth(),
				getItemHeight());
	}

	boolean isShown(int row) {
		checkWidget();

		if (row == -1) {
			SWT.error(5);
		}
		int firstVisibleIndex = getTopIndex();
		int lastVisibleIndex = getBottomIndex();

		if ((row < firstVisibleIndex) || (row >= lastVisibleIndex)) {
			if ((row != lastVisibleIndex) || (!bottomIndexShownCompletely))
				return false;
		}
		return true;
	}

	Point getOrigin(GridColumn column, int item) {
		int x = 0;

		if (isRowHeaderVisible()) {
			x += getRowHeaderWidth();
		}

		x -= getHScrollSelectionInPixels();

		for (GridColumn colIter : getColumns()) {
			if (colIter == column) {
				break;
			}
			x += colIter.getWidth();
		}

		int y = 0;
		if (item >= 0) {
			if (isColumnHeadersVisible()) {
				y += getHeaderHeight();
			}

			int currIndex = getTopIndex();

			if (item == -1) {
				SWT.error(5);
			}

			while (currIndex != item) {
				if (currIndex < item) {
					y += getItemHeight() + 1;
					currIndex++;
				} else if (currIndex > item) {
					currIndex--;
					y -= getItemHeight() + 1;
				}
			}
		}

		return new Point(x, y);
	}
	public List<GridItem> getAllItems() { //XXX return super.getItems()
//		List<GridItem> retval = new ArrayList<>(getItemCount());
//		retval.addAll(items);
		return items;
	}
	public List<GridItem> getAllRootItems() { //XXX return super.getRootItems()
//		List<GridItem> retval = new ArrayList<>(rootItems.size());
//		retval.addAll(rootItems);
		return rootItems;
	}
	
	@Override
	public LightGridColumn getColumn(int index) {
		return (LightGridColumn) super.getColumn(index);
	}
	public void setRootItems(List<GridItem> newRootItems) {
		rootItems.clear();
		rootItems.addAll(newRootItems);
	}

	// -------------- end viewer

	// private static class CellComparator implements Comparator<Point>{
	// @Override
	// public int compare(Point pos1, Point pos2) {
	// int res = pos1.x - pos2.x;
	// return res != 0 ? res : pos1.y - pos2.y;
	// }
	// }
	
	
	
	
	//// -----------------   paint
	
	//// ------ end paint
}
