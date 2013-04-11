/* PluginTips.java 1.0 2012-5-21
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

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * <B>PluginTips</B>
 * 
 * @author Brook Tran . Email: <a href="mailto:c.brook.tran@gmail.com">c.brook.tran@gmail.com</a>
 * @version Ver 1.0.01 2012-5-21 created
 * @since org.jeelee.core Ver 1.0
 * 
 */
public class PluginTips {

	public static void showTips(String title, Control c) {
		showTips(title,null,c);
	}


	/**
	 * show a tips window in the center of the control
	 */
	public static void showTips(final String title,final String message, Control c) {
		Tooltip tips = new Tooltip(title,message);
		tips.show(c);
	}  
}
class Tooltip {
	private static DefaultInformationControl currentControl ;
	
	private static final DisposeListener disposeListener =new DisposeListener() {
		@Override
		public void widgetDisposed(DisposeEvent e) {
			hideCurrent();
		}
	};
	private String title="";
	private String message="";
	private static boolean autoHide =true;
	private Shell parentShell ;
	
	public Tooltip(String title, String message) {
		setTitle(title);
		setMessage(message);
	}
	
	public void show(final Control c) {
		if(currentControl!=null){
			currentControl.setVisible(false);
			if(parentShell!=null && !parentShell.isDisposed()){
				parentShell.removeDisposeListener(disposeListener);
			}
			currentControl = null;
		}
		
		
		parentShell = c.getShell();
		final DefaultInformationControl info =
				new DefaultInformationControl(parentShell,false);
		info.setSize(300, 200); //
		info.setSizeConstraints(300, 200);
		info.setInformation(getContent());
		//		info.setStatusText("098909");
		info.setVisible(true);
		info.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				Rectangle rect = info.getBounds();
				DefaultInformationControl newInfo =
						new DefaultInformationControl(parentShell,new ToolBarManager());
				newInfo.setSizeConstraints(300, 200);
				newInfo.setLocation(new Point(rect.x, rect.y));
				newInfo.setSize(rect.width, rect.height);
				newInfo.setInformation(
						"<b>"+
								title
								+"</b> <p>" 
								+ message);
				currentControl=newInfo;
				info.setVisible(false);
				newInfo.setVisible(true);
				newInfo.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent e) {
						autoHide =true;
						hideCurrent();
					}
				});
				autoHide=false;
			}
			@Override
			public void focusLost(FocusEvent e) {
				autoHide =true;
				hideCurrent();
			}
		});
		currentControl =info;
		parentShell.addDisposeListener(disposeListener);
		c.getDisplay().timerExec(5000, new Runnable() {
			@Override
			public void run() {
				hideCurrent();
			}
		});
	}

	private String getContent() {
		return 
				"<b>"+
				title
				+"</b> <p>" 
				+ message;
	}

	protected static void hideCurrent() {
		if(autoHide) {
			currentControl.setVisible(false);
		}
	}

	public String getTitle() {
		return title;
	}

	/**
	 * Note: it should be call before show();
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	/**
	 * Note: it should be call before show();
	 */
	public void setMessage(String message) {
		this.message = message.replace("\n", "<br>");
	}
}