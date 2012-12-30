package org.jeelee.ui.internal;

import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

public class ViewerColumnComparator extends ViewerComparator{
	protected int propertyIndex;
	protected static final int DESCENDING = 1;
	protected int direction = 0;
	
	
	public ViewerColumnComparator(){
		this.propertyIndex = 0;
		direction = SWT.NONE;
	}
	
	
	public void setColumn(int column) {
		if (column == this.propertyIndex) {
			// Same column as last sort; toggle the direction
			direction = (direction+1)%3;
		} else {
			// New column; do an ascending sort
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}

	public int getDirection() { 
		return direction == 0 ? SWT.NONE :
				(direction==DESCENDING)?SWT.DOWN:SWT.UP;
	}

}
