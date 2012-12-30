package org.eclipse.nebula.widgets.grid.internal;

import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ScrollBar;

/**
 * Adapts a normal scrollbar to the IScrollBar proxy.
 * 
 * @author chris.gross@us.ibm.com
 * @since 2.0.0
 */
public class ScrollBarProxyAdapter implements IScrollBarProxy {
	/**
	 * Delegates to this scrollbar.
	 */
	private final ScrollBar scrollBar;

	/**
	 * Contructs this adapter by delegating to the given scroll bar.
	 * 
	 * @param scrollBar
	 *            delegate
	 */
	public ScrollBarProxyAdapter(ScrollBar scrollBar) {
		this.scrollBar = scrollBar;
	}

	@Override
	public int getIncrement() {
		return scrollBar.getIncrement();
	}

	@Override
	public int getMaximum() {
		return scrollBar.getMaximum();
	}

	@Override
	public int getMinimum() {
		return scrollBar.getMinimum();
	}

	@Override
	public int getPageIncrement() {
		return scrollBar.getPageIncrement();
	}

	@Override
	public int getSelection() {
		return scrollBar.getSelection();
	}

	@Override
	public int getThumb() {
		return scrollBar.getThumb();
	}

	@Override
	public boolean getVisible() {
		return scrollBar.getVisible();
	}

	@Override
	public void setIncrement(int value) {
		scrollBar.setIncrement(value);
	}

	@Override
	public void setMaximum(int value) {
		scrollBar.setMaximum(value);
	}

	@Override
	public void setMinimum(int value) {
		scrollBar.setMinimum(value);
	}

	@Override
	public void setPageIncrement(int value) {
		scrollBar.setPageIncrement(value);
	}

	@Override
	public void setSelection(int selection) {
		scrollBar.setSelection(selection);
	}

	@Override
	public void setThumb(int value) {
		scrollBar.setThumb(value);
	}
	@Override
	public void setValues(int selection, int minimum, int maximum, int thumb,
			int increment, int pageIncrement) {
		scrollBar.setValues(
				selection, minimum, maximum, thumb, increment,
				pageIncrement);
	}
	@Override
	public void setVisible(boolean visible) {
		scrollBar.setVisible(visible);
	}

	@Override
	public void handleMouseWheel(Event e) {
		// do nothing
	}

	@Override
	public void addSelectionListener(SelectionListener listener) {
		scrollBar.addSelectionListener(listener);
	}

	@Override
	public void removeSelectionListener(SelectionListener listener) {
		scrollBar.removeSelectionListener(listener);
	}
}
