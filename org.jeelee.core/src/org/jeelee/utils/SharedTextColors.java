/* SharedTextColors.java 1.0 May 29, 2012
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
package org.jeelee.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * <B>SharedTextColors</B>
 * 
 * @author Brook Tran . Email: <a
 *         href="mailto:c.brook.tran@gmail.com">c.brook.tran@gmail.com</a>
 * @version Ver 1.0.01 May 29, 2012 created
 * @since org.jeelee.core Ver 1.0
 * 
 */
public class SharedTextColors implements ISharedTextColors {
	private Map<Display, Map<RGB, Color>> fDisplayTable;

	@Override
	public Color getColor(RGB rgb) {
		if (rgb == null) {
			return null;
		}
		if (fDisplayTable == null) {
			fDisplayTable = new HashMap<Display, Map<RGB, Color>>(2);
		}
		final Display display = Display.getCurrent();

		Map<RGB, Color> colorTable = fDisplayTable.get(display);
		if (colorTable == null) {
			colorTable = new HashMap<RGB, Color>(10);
			fDisplayTable.put(display, colorTable);
			display.disposeExec(new Runnable() {
				@Override
				public void run() {
					SharedTextColors.this.dispose(display);
				}
			});
		}
		Color color = colorTable.get(rgb);
		if (color == null) {
			color = new Color(display, rgb);
			colorTable.put(rgb, color);
		}

		return color;
	}

	@Override
	public void dispose() {
		if (fDisplayTable == null) {
			return;
		}
		for (Map<RGB, Color> rgbColorMap : fDisplayTable.values()) {
			dispose(rgbColorMap);
		}
		fDisplayTable = null;
	}

	private void dispose(Display display) {
		if (fDisplayTable != null) {
			dispose(fDisplayTable.remove(display));
		}
	}

	private void dispose(Map<RGB, Color> colorTable) {
		if (colorTable == null) {
			return;
		}
		for (Color color : colorTable.values()) {
			color.dispose();
		}

		colorTable.clear();
	}
}
