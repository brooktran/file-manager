/* AutoAdjustRowLayout.java 
 * Copyright (c) 2013 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.ui.breadcrumb.layout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

/**
 * <B>AutoAdjustRowLayout</B>
 * extends from {@link org.eclipse.swt.layout.RowLayout} 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2013-1-9 created
 */
public class BreadcrumbLayout extends Layout{

	/**
	 * type specifies whether the layout places controls in rows or 
	 * columns.
	 * 
	 * The default value is HORIZONTAL.
	 * 
	 * Possible values are: <ul>
	 *    <li>HORIZONTAL: Position the controls horizontally from left to right</li>
	 *    <li>VERTICAL: Position the controls vertically from top to bottom</li>
	 * </ul>
	 * 
	 * @since 2.0
	 */
	public int type = SWT.HORIZONTAL | SWT.RIGHT_TO_LEFT;
	
	/**
	 * marginWidth specifies the number of pixels of horizontal margin
	 * that will be placed along the left and right edges of the layout.
	 *
	 * The default value is 0.
	 * 
	 * @since 3.0
	 */
 	public int marginWidth = 0;
 	
	/**
	 * marginHeight specifies the number of pixels of vertical margin
	 * that will be placed along the top and bottom edges of the layout.
	 *
	 * The default value is 0.
	 * 
	 * @since 3.0
	 */
 	public int marginHeight = 0;

	/**
	 * spacing specifies the number of pixels between the edge of one cell
	 * and the edge of its neighbouring cell.
	 *
	 * The default value is 3.
	 */
	public int spacing = 3;
	 		
	/**
	 * pack specifies whether all controls in the layout take
	 * their preferred size.  If pack is false, all controls will 
	 * have the same size which is the size required to accommodate the 
	 * largest preferred height and the largest preferred width of all 
	 * the controls in the layout.
	 *
	 * The default value is true.
	 */
	public boolean pack = true;
	
	/**
	 * fill specifies whether the controls in a row should be
	 * all the same height for horizontal layouts, or the same
	 * width for vertical layouts.
	 *
	 * The default value is false.
	 * 
	 * @since 3.0
	 */
	public boolean fill = false;


	/**
	 * marginLeft specifies the number of pixels of horizontal margin
	 * that will be placed along the left edge of the layout.
	 *
	 * The default value is 3.
	 */
	public int marginLeft = 3;

	/**
	 * marginTop specifies the number of pixels of vertical margin
	 * that will be placed along the top edge of the layout.
	 *
	 * The default value is 3.
	 */
	public int marginTop = 3;

	/**
	 * marginRight specifies the number of pixels of horizontal margin
	 * that will be placed along the right edge of the layout.
	 *
	 * The default value is 3.
	 */
	public int marginRight = 3;

	/**
	 * marginBottom specifies the number of pixels of vertical margin
	 * that will be placed along the bottom edge of the layout.
	 *
	 * The default value is 3.
	 */
	public int marginBottom = 3;

	
	public BreadcrumbLayout() {
	}
	
	public BreadcrumbLayout (int type) {
		this.type = type;
	}
	@Override
	protected void layout (Composite composite, boolean flushCache) {
		Rectangle clientArea = composite.getClientArea ();
		if ((type & SWT.HORIZONTAL) == SWT.HORIZONTAL) {
			layoutHorizontal (composite, true,  clientArea.width, flushCache);
		} else {
			layoutVertical (composite, true, clientArea.height, flushCache);
		}
	}
	@Override
	protected Point computeSize (Composite composite, int wHint, int hHint, boolean flushCache) {
		Point extent;
		if ((type & SWT.HORIZONTAL) == SWT.HORIZONTAL) {
			extent = layoutHorizontal (composite, false,  wHint, flushCache);
		} else {
			extent = layoutVertical (composite, false,  hHint, flushCache);
		}
		if (wHint != SWT.DEFAULT) {
			extent.x = wHint;
		}
		if (hHint != SWT.DEFAULT) {
			extent.y = hHint;
		}
		return extent;
	}
	
	Point layoutHorizontal (Composite composite, boolean move, int width, boolean flushCache) {
		Control [] children = composite.getChildren ();
		int count = 0;
		for (int i=0; i<children.length; i++) {
			Control control = children [i];
			RowData data = (RowData) control.getLayoutData ();
			if (data == null || !data.exclude) {
				children [count++] = children [i];
			} 
		}
		if (count == 0) {
			return new Point (marginLeft + marginWidth * 2 + marginRight, marginTop + marginHeight * 2 + marginBottom);
		}
		

		
		int startIndex = 0;
		if( ( type & SWT.RIGHT_TO_LEFT) == SWT.RIGHT_TO_LEFT){
			startIndex = count;
			int maxWidth = marginLeft + marginWidth + marginRight + marginWidth;
//			marginRight + marginLeft +marginWidth  ;
			while(startIndex>0 && maxWidth<width){
				Control child = children [startIndex-1];
				Point size = computeSize (child, flushCache);
				maxWidth += size.x +spacing  + marginWidth;
				startIndex--;
			}
			startIndex = adjust(move, children, count, startIndex);
		}
			
		int childWidth = 0, childHeight = 0, maxHeight = 0;
		// get max width & height
		if (!pack) {
			for (int i=startIndex; i<count; i++) {
				Control child = children [i];
				Point size = computeSize (child, flushCache);
				childWidth = Math.max (childWidth, size.x);
				childHeight = Math.max (childHeight, size.y);
			}
			maxHeight = childHeight;
		}
		
		int clientX = 0, clientY = 0;
		if (move) {
			Rectangle rect = composite.getClientArea ();
			clientX = rect.x;
			clientY = rect.y;
		}
		
		Rectangle [] bounds = null;
		if (move && (fill )) {
			bounds = new Rectangle [count];
		}
		int maxX = 0, x = marginLeft + marginWidth, y = marginTop + marginHeight;
		
		
		for (int i=startIndex; i<count; i++) {
			Control child = children [i];
			if (pack) {
				Point size = computeSize (child, flushCache);
				childWidth = size.x;
				childHeight = size.y;
			}
			if (pack || fill ) {
				maxHeight = Math.max (maxHeight, childHeight);
			}
			if (move) {
				int childX = x + clientX, childY = y + clientY;
				if ( fill ) {
					bounds [i] = new Rectangle (childX, childY, childWidth, childHeight);
				} else {
					child.setBounds (childX, childY, childWidth, childHeight);
				}
			}
			x += spacing + childWidth;
			maxX = Math.max (maxX, x);
		}
		maxX = Math.max (clientX + marginLeft + marginWidth, maxX - spacing);
			maxX += marginRight + marginWidth;
		if (move && (fill)) {
			for (int i=startIndex; i<count; i++) {
					if (fill) {
						bounds [i].height = maxHeight;
					} 
				children [i].setBounds (bounds [i]);
			}
		}
		return new Point (maxX, y + maxHeight + marginBottom + marginHeight);
	}

	Point layoutVertical (Composite composite, boolean move,int height, boolean flushCache) {
		Control [] children = composite.getChildren ();
		int count = 0;
		for (int i=0; i<children.length; i++) {
			Control control = children [i];
			RowData data = (RowData) control.getLayoutData ();
			if (data == null || !data.exclude) {
				children [count++] = children [i];
			} 
		}
		if (count == 0) {
			return new Point (marginLeft + marginWidth * 2 + marginRight, marginTop + marginHeight * 2 + marginBottom);
		}
		

		int startIndex = 0;
		if( ( type & SWT.RIGHT_TO_LEFT) == SWT.RIGHT_TO_LEFT){
			startIndex = count;
			int maxHeight = marginTop + marginHeight+ marginBottom +marginHeight  ;
			while(startIndex>0 && maxHeight<height){
				Control child = children [startIndex-1];
				Point size = computeSize (child, flushCache);
				maxHeight += size.y +spacing + marginTop +marginHeight;
				startIndex--;
			}
			startIndex = adjust(move, children, count, startIndex);
		}
		
		
		
		int childWidth = 0, childHeight = 0, maxWidth = 0;
		if (!pack) {
			for (int i=startIndex; i<count; i++) {
				Control child = children [i];
				Point size = computeSize (child, flushCache);
				childWidth = Math.max (childWidth, size.x);
				childHeight = Math.max (childHeight, size.y);
			}
			maxWidth = childWidth;
		}
		int clientX = 0, clientY = 0;
		if (move) {
			Rectangle rect = composite.getClientArea ();
			clientX = rect.x;
			clientY = rect.y;
		}
		Rectangle [] bounds = null;
		if (move && (fill)) {
			bounds = new Rectangle [count];
		}
		int maxY = 0, x = marginLeft + marginWidth, y = marginTop + marginHeight;
		for (int i=startIndex; i<count; i++) {
			Control child = children [i];
			if (pack) {
				Point size = computeSize (child, flushCache);
				childWidth = size.x;
				childHeight = size.y;
			}
			if (pack || fill ) {
				maxWidth = Math.max (maxWidth, childWidth);
			}
			if (move) {
				int childX = x + clientX, childY = y + clientY;
				if ( fill ) {
					bounds [i] = new Rectangle (childX, childY, childWidth, childHeight);
				} else {
					child.setBounds (childX, childY, childWidth, childHeight);
				}
			}
			y += spacing + childHeight;
			maxY = Math.max (maxY, y);
		}
		maxY = Math.max (clientY + marginTop + marginHeight, maxY - spacing);
		if (move && (fill )) {
			for (int i=startIndex; i<count; i++) {
				children [i].setBounds (bounds [i]);
			}
		}
		return new Point (x + maxWidth + marginRight + marginWidth, maxY);
	}

	private int adjust(boolean move, Control[] children, int count,
			int startIndex) {
		if (startIndex > 0) {
			startIndex++;
		}
		if(startIndex > count){
			startIndex--;
		}
		
		if(move){
			Rectangle hide = new Rectangle(0, 0, 0, 0);
			for(int i=0;i<startIndex;i++){
//				children[i].setVisible(false);
				children[i].setBounds(hide);
			}
		}
		return startIndex;
	}


	Point computeSize (Control control, boolean flushCache) {
		int wHint = SWT.DEFAULT, hHint = SWT.DEFAULT;
		RowData data = (RowData) control.getLayoutData ();
		if (data != null) {
			wHint = data.width;
			hHint = data.height;
		}
		return control.computeSize (wHint, hHint, flushCache);
	}
	
	
	
	@Override
	protected boolean flushCache (Control control) {
		return true;
	}

	String getName () {
		String string = getClass ().getName ();
		int index = string.lastIndexOf ('.');
		if (index == -1) {
			return string;
		}
		return string.substring (index + 1, string.length ());
	}
	@Override
	public String toString () {
	 	String string = getName ()+" {";
	 	string += "type="+((type != SWT.HORIZONTAL) ? "SWT.VERTICAL" : "SWT.HORIZONTAL")+" ";
	 	if (marginWidth != 0) {
			string += "marginWidth="+marginWidth+" ";
		}
	 	if (marginHeight != 0) {
			string += "marginHeight="+marginHeight+" ";
		}
	 	if (marginLeft != 0) {
			string += "marginLeft="+marginLeft+" ";
		}
	 	if (marginTop != 0) {
			string += "marginTop="+marginTop+" ";
		}
	 	if (marginRight != 0) {
			string += "marginRight="+marginRight+" ";
		}
	 	if (marginBottom != 0) {
			string += "marginBottom="+marginBottom+" ";
		}
	 	if (spacing != 0) {
			string += "spacing="+spacing+" ";
		}
		string += "pack="+pack+" ";
		string += "fill="+fill+" ";
		string = string.trim();
		string += "}";
	 	return string;
	}
	
}
