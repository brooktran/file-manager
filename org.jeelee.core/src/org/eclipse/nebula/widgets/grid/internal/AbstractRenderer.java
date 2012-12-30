/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    chris.gross@us.ibm.com - initial API and implementation
 *******************************************************************************/
package org.eclipse.nebula.widgets.grid.internal;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * <p>
 * NOTE: THIS WIDGET AND ITS API ARE STILL UNDER DEVELOPMENT. THIS IS A
 * PRE-RELEASE ALPHA VERSION. USERS SHOULD EXPECT API CHANGES IN FUTURE
 * VERSIONS.
 * </p>
 * Base implementation of IRenderer. Provides management of a few values.
 * 
 * @author chris.gross@us.ibm.com
 */
public abstract class AbstractRenderer implements IRenderer {
	private final int row =-1;
	private final int column =-1;
	
	/** Hover state. */
	private boolean hover;

	/** Renderer has focus. */
	private boolean focus;

	/** Mouse down on the renderer area. */
	private boolean mouseDown;

	/** Selection state. */
	private boolean selected;

	/** Expansion state. */
	private boolean expanded;

	/** The bounds the renderer paints on. */
	private Rectangle bounds = new Rectangle(0, 0, 0, 0);

	/** Display used to create GC to perform painting. */
	private Display display;

	/** Dragging state. */
	private boolean dragging;

	protected Grid grid;

	public AbstractRenderer(Grid grid) {
		this.grid = grid;
		this.display = grid.getDisplay();
	}

	/**
	 * Returns the bounds.
	 * 
	 * @return Rectangle describing the bounds.
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBounds(int x, int y, int width, int height) {
		setBounds(new Rectangle(x, y, width, height));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	/**
	 * Returns the size.
	 * 
	 * @return size of the renderer.
	 */
	public Point getSize() {
		return new Point(bounds.width, bounds.height);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLocation(int x, int y) {
		setBounds(new Rectangle(x, y, bounds.width, bounds.height));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLocation(Point location) {
		setBounds(new Rectangle(location.x, location.y, bounds.width,
				bounds.height));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSize(int width, int height) {
		setBounds(new Rectangle(bounds.x, bounds.y, width, height));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSize(Point size) {
		setBounds(new Rectangle(bounds.x, bounds.y, size.x, size.y));
	}

	/**
	 * Returns a boolean value indicating if this renderer has focus.
	 * 
	 * @return True/false if has focus.
	 */
	public boolean isFocus() {
		return focus;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFocus(boolean focus) {
		this.focus = focus;
	}

	/**
	 * Returns the hover state.
	 * 
	 * @return Is the renderer in the hover state.
	 */
	public boolean isHover() {
		return hover;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHover(boolean hover) {
		this.hover = hover;
	}

	/**
	 * Returns the boolean value indicating if the renderer has the mouseDown
	 * state.
	 * 
	 * @return mouse down state.
	 */
	public boolean isMouseDown() {
		return mouseDown;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMouseDown(boolean mouseDown) {
		this.mouseDown = mouseDown;
	}

	/**
	 * Returns the boolean state indicating if the selected state is set.
	 * 
	 * @return selected state.
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Returns the expansion state.
	 * 
	 * @return Returns the expanded.
	 */
	public boolean isExpanded() {
		return expanded;
	}

	/**
	 * Sets the expansion state of this renderer.
	 * 
	 * @param expanded
	 *            The expanded to set.
	 */
	@Override
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	/**
	 * Sets the display for the renderer.
	 * 
	 * @return Returns the display.
	 */
	public Display getDisplay() {
		return display;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDisplay(Display display) {
		this.display = display;
	}

	public int getRow() {
		return row;
	}
	@Override
	public void setRow(int row) {
	}
	public int getColumn() {
		return column;
	}
	@Override
	public void setColumn(int column) {
	}

	public Cursor getHoverCursor() {
		return null;
	}


}
