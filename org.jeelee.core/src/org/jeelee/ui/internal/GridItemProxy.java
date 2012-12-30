package org.jeelee.ui.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

public class GridItemProxy {
	/**
	 * List of background colors for each column.
	 */
	private final ArrayList backgrounds = new ArrayList();

	/**
	 * Lists of check states for each column.
	 */
	private final ArrayList checks = new ArrayList();

	/**
	 * Lists of checkable states for each column.
	 */
	private final ArrayList checkable = new ArrayList();

	/**
	 * List of children.
	 */
	private final ArrayList children = new ArrayList();

	/**
	 * List of column spaning.
	 */
	private final ArrayList columnSpans = new ArrayList();

	/**
	 * List of row spaning.
	 */
	private final ArrayList rowSpans = new ArrayList();

	/**
	 * Default background color.
	 */
	private Color defaultBackground;

	/**
	 * Default font.
	 */
	private Font defaultFont;

	/**
	 * Default foreground color.
	 */
	private Color defaultForeground;

	/**
	 * The height of this <code>GridItem</code>.
	 */
	private int height = 1;

	/**
	 * Is expanded?
	 */
	private boolean expanded = false;

	/**
	 * Lists of fonts for each column.
	 */
	private final ArrayList fonts = new ArrayList();

	/**
	 * List of foreground colors for each column.
	 */
	private final ArrayList foregrounds = new ArrayList();

	/**
	 * Lists of grayed (3rd check state) for each column.
	 */
	private final ArrayList grayeds = new ArrayList();

	/**
	 * True if has children.
	 */
	private boolean hasChildren = false;

	/**
	 * List of images for each column.
	 */
	private final ArrayList images = new ArrayList();

	/**
	 * Level of item in a tree.
	 */
	private int level = 0;

	/**
	 * Parent grid instance.
	 */
	private final Grid parent;

	/**
	 * Parent item (if a child item).
	 */
	private GridItem parentItem;

	/**
	 * List of text for each column.
	 */
	private final ArrayList texts = new ArrayList();

	/**
	 * List of tooltips for each column.
	 */
	private final ArrayList tooltips = new ArrayList();

	/**
	 * Is visible?
	 */
	private boolean visible = true;

	/**
	 * Row header text.
	 */
	private String headerText = null;

	/**
	 * Row header image
	 */
	private Image headerImage = null;

	/**
	 * Background color of the header
	 */
	private Color headerBackground = null;

	/**
	 * Foreground color of the header
	 */
	public Color headerForeground = null;

	public GridItemProxy(Grid parent, int style) {
		this(parent, style, -1);
	}

	public GridItemProxy(Grid parent, int style, int index) {
		this.parent = parent;
		init();
	}

	public GridItemProxy(GridItem parent, int style, int index) {
		parentItem = parent;
		this.parent = parentItem.getParent();
		init();
		level = parentItem.getLevel() + 1;
	}

	public Color getBackground() {
		if (defaultBackground == null) {
			return parent.getBackground();
		}
		return defaultBackground;
	}

	public Color getBackground(int index) {
		Color c = (Color) backgrounds.get(index);
		return c;
	}


	public boolean getChecked() {
		return getChecked(0);
	}

	public boolean getChecked(int index) {
		Boolean b = (Boolean) checks.get(index);
		if (b == null) {
			return false;
		}
		return b.booleanValue();
	}

	public int getColumnSpan(int index) {
		
		Integer i = (Integer) columnSpans.get(index);
		if (i == null) {
			return 0;
		}
		return i.intValue();
	}

	public int getRowSpan(int index) {
		if (index >= 0 && index < rowSpans.size()) {
			Integer i = (Integer) rowSpans.get(index);
			if (i != null) {
				return i.intValue();
			}
		}

		return 0;
	}

	public Font getFont() {
		if (defaultFont == null) {
			return parent.getFont();
		}
		return defaultFont;
	}

	public Font getFont(int index) {
		Font f = (Font) fonts.get(index);
		if (f == null) {
			f = getFont();
		}
		return f;
	}

	public Color getForeground() {
		if (defaultForeground == null) {
			return parent.getForeground();
		}
		return defaultForeground;
	}

	public Color getForeground(int index) {
		

		

		Color c = (Color) foregrounds.get(index);
		if (c == null) {
			c = getForeground();
		}
		return c;
	}

	/**
	 * Returns <code>true</code> if the first column in the receiver is grayed,
	 * and false otherwise. When the GridColumn does not have the
	 * <code>CHECK</code> style, return false.
	 * 
	 * @return the grayed state of the checkbox
	 * @throws org.eclipse.swt.SWTException
	 *             <ul>
	 *             <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed
	 *             </li>
	 *             <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *             thread that created the receiver</li>
	 *             </ul>
	 */
	public boolean getGrayed() {
		return getGrayed(0);
	}

	/**
	 * Returns <code>true</code> if the column at the given index in the
	 * receiver is grayed, and false otherwise. When the GridColumn does not
	 * have the <code>CHECK</code> style, return false.
	 * 
	 * @param index
	 *            the column index
	 * @return the grayed state of the checkbox
	 * @throws org.eclipse.swt.SWTException
	 *             <ul>
	 *             <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed
	 *             </li>
	 *             <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *             thread that created the receiver</li>
	 *             </ul>
	 */
	public boolean getGrayed(int index) {
		Boolean b = (Boolean) grayeds.get(index);
		if (b == null) {
			return false;
		}
		return b.booleanValue();
	}

	public int getHeight() {
		return height;
	}

	public Image getImage() {
		return getImage(0);
	}

	public Image getImage(int index) {
		return (Image) images.get(index);
	}

	public GridItem getItem(int index) {
		
		return (GridItem) children.get(index);
	}

	public int getItemCount() {
		
		return children.size();
	}
	public int indexOf(GridItem item) {
		
		if (item == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (item.isDisposed())
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);

		return children.indexOf(item);
	}

	public GridItem[] getItems() {
		return (GridItem[]) children.toArray(new GridItem[children.size()]);
	}
	public int getLevel() {
		return level;
	}

	public Grid getParent() {
		return parent;
	}

	public GridItem getParentItem() {
		return parentItem;
	}

	public String getText() {
		return getText(0);
	}

	public String getText(int index) {
		String s = (String) texts.get(index);
		if (s == null) {
			return "";
		}
		return s;
	}

	public boolean hasChildren() {
		return hasChildren;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setBackground(Color background) {
		if (background != null && background.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		defaultBackground = background;
	}

	public void setBackground(int index, Color background) {
		if (background != null && background.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		backgrounds.set(index, background);
	}

	public void setChecked(boolean checked) {
		setChecked(0, checked);
	}

	public void setChecked(int index, boolean checked) {
		checks.set(index, new Boolean(checked));
	}

	public void setColumnSpan(int index, int span) {
		columnSpans.set(index, new Integer(span));
	}

	public void setRowSpan(int index, int span) {
		rowSpans.set(index, new Integer(span));
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public void setFont(Font f) {
		if (f != null && f.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		defaultFont = f;
	}

	public void setFont(int index, Font font) {
		if (font != null && font.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		fonts.set(index, font);
	}

	public void setForeground(Color foreground) {
		if (foreground != null && foreground.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		defaultForeground = foreground;
		
	}

	public void setForeground(int index, Color foreground) {
		if (foreground != null && foreground.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		foregrounds.set(index, foreground);
	}

	public void setGrayed(boolean grayed) {
		setGrayed(0, grayed);
	}

	public void setGrayed(int index, boolean grayed) {
		grayeds.set(index, new Boolean(grayed));
	}

	public void setHeight(int newHeight) {
		height = newHeight;
	}

	public void setImage(Image image) {
		setImage(0, image);
	}

	public void setImage(int index, Image image) {
		images.set(index, image);
	}

	public void setText(int index, String text) {
		texts.set(index, text);
	}

	public void setText(String string) {
		setText(0, string);
	}

	private void ensureSize(ArrayList al) {
		int count = Math.max(1, parent.getColumnCount());
		al.ensureCapacity(count);
		while (al.size() <= count) {
			al.add(null);
		}
	}

	boolean isVisible() {
		return visible;
	}

	void newItem(GridItem item, int index) {
		setHasChildren(true);

		if (index == -1) {
			children.add(item);
		} else {
			children.add(index, item);
		}
	}

	void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public String getHeaderText() {
		return headerText;
	}

	public Image getHeaderImage() {
		return headerImage;
	}

	public Color getHeaderBackground() {
		return headerBackground;
	}

	public Color getHeaderForeground() {
		return headerForeground;
	}

	public void setHeaderText(String text) {
		this.headerText = text;
	}

	public void setHeaderImage(Image image) {
		this.headerImage = image;
	}

	public void setHeaderBackground(Color headerBackground) {
		this.headerBackground = headerBackground;
	}

	public void setHeaderForeground(Color headerForeground) {
		this.headerForeground = headerForeground;
	}

	public boolean getCheckable(int index) {
		if (!parent.getColumn(index).getCheckable())
			return false;

		Boolean b = (Boolean) checkable.get(index);
		if (b == null) {
			return true;
		}
		return b.booleanValue();
	}

	public void setCheckable(int index, boolean checked) {
		checkable.set(index, new Boolean(checked));
	}

	public String getToolTipText(int index) {
		String s = (String) tooltips.get(index);
		return s;
	}

	@SuppressWarnings("unchecked")
	public void setToolTipText(int index, String tooltip) {
		tooltips.set(index, tooltip);
	}

	private void init() {
		ensureSize(backgrounds);
		ensureSize(checks);
		ensureSize(checkable);
		ensureSize(fonts);
		ensureSize(foregrounds);
		ensureSize(grayeds);
		ensureSize(images);
		ensureSize(texts);
		ensureSize(columnSpans);
		ensureSize(rowSpans);
		ensureSize(tooltips);
	}

	void columnRemoved(int index) {
		removeValue(index, backgrounds);
		removeValue(index, checks);
		removeValue(index, checkable);
		removeValue(index, fonts);
		removeValue(index, foregrounds);
		removeValue(index, grayeds);
		removeValue(index, images);
		removeValue(index, texts);
		removeValue(index, columnSpans);
		removeValue(index, rowSpans);
		removeValue(index, tooltips);
	}

	void columnAdded(int index) {
		insertValue(index, backgrounds);
		insertValue(index, checks);
		insertValue(index, checkable);
		insertValue(index, fonts);
		insertValue(index, foregrounds);
		insertValue(index, grayeds);
		insertValue(index, images);
		insertValue(index, texts);
		insertValue(index, columnSpans);
		insertValue(index, rowSpans);
		insertValue(index, tooltips);
	}

	private void insertValue(int index, List list) {
		if (index == -1) {
			list.add(null);
		} else {
			list.add(index, null);
		}
	}

	private void removeValue(int index, List list) {
		if (list.size() > index) {
			list.remove(index);
		}
	}
	
	private Map<String, Object> cacheData =new HashMap<String, Object>();
	public void setData(String key,Object value){
		cacheData.put(key, value);
	}
	public Object getData(String key){
		return cacheData.get(key);
	}
	
	private Object data;
	public void setData(Object data){
		this.data = data;
	}
	public Object getData(){
		return this.data;
	}
	
	public static GridItemProxy copy(GridItem item,int count,String... keys) {
		GridItemProxy proxy = new GridItemProxy(item.getParent(),item.getStyle());
		
		for(int i=0;i<count;i++){
			proxy.setBackground(i, item.getBackground(i));
			proxy.setCheckable(i, item.getCheckable(i));
			proxy.setChecked(i, item.getChecked(i));
			proxy.setFont(i, item.getFont(i));
			proxy.setForeground(i, item.getForeground(i));
			proxy.setGrayed(i, item.getGrayed(i));
			proxy.setImage(i, item.getImage(i));
			proxy.setText(i, item.getText(i));
			proxy.setToolTipText(i, item.getToolTipText(i));
		}
		
		proxy.setData(item.getData());
		for(String key:keys){
			proxy.setData(key,item.getData(key));
		}
		return proxy;
	}

	public void copyTo(GridItem item,int count,String... keys) {
		for(int i=0;i<count;i++){
			item.setBackground(i, getBackground(i));
			item.setCheckable(i, getCheckable(i));
			item.setChecked(i, getChecked(i));
			item.setFont(i, getFont(i));
			item.setForeground(i, getForeground(i));
			item.setGrayed(i, getGrayed(i));
			item.setImage(i, getImage(i));
			item.setText(i, getText(i));
			item.setToolTipText(i, getToolTipText(i));
		}
		item.setData(getData());
		for(String key:keys){
			item.setData(key,getData(key));
		}
	}
}
