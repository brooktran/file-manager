/* ControlBoundSettingsSupport.java 1.0 2012-5-22
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
package org.jeelee.ui.internal;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * <B>ControlBoundSettingsSupport</B>
 * 
 * @author Brook Tran . Email: <a href="mailto:c.brook.tran@gmail.com">c.brook.tran@gmail.com</a>
 * @version Ver 1.0.01 2012-5-22 created
 * @since org.jeelee.core Ver 1.0
 * 
 */
public class ControlBoundSettingsSupport {

	public static void configDialog(final Shell newShell, AbstractUIPlugin plugin,
			final String dialogID,Point defaultSize) {
		try {
			IDialogSettings settings = plugin.getDialogSettings();
			IDialogSettings dialogSettings =settings.getSection(dialogID);
			if(dialogSettings == null){
				dialogSettings = settings.addNewSection(dialogID);
				
				newShell.setSize(defaultSize);
				Rectangle parentBounds = newShell.getParent().getBounds();
				newShell.setLocation(parentBounds.x+ (parentBounds.width - 400)/2,
									parentBounds.y+ (parentBounds.height -500)/2);
			}else {
				newShell.setSize(dialogSettings.getInt(dialogID+".bounds.width"),dialogSettings.getInt(dialogID+".bounds.height"));
				newShell.setLocation(dialogSettings.getInt(dialogID+".bounds.x"),dialogSettings.getInt(dialogID+".bounds.y") );
				if(dialogSettings.getBoolean(dialogID+".max")){
					newShell.setMaximized(true);
				}
				if(dialogSettings.getBoolean(dialogID+".full")){
					newShell.setFullScreen(true);
				}
			}
			final IDialogSettings finalDialogSettings=dialogSettings;
			newShell.addControlListener(new ControlListener() {
				@Override
				public void controlResized(ControlEvent e) {
					finalDialogSettings.put(dialogID+".full", newShell.getFullScreen());
					finalDialogSettings.put(dialogID+".max", newShell.getMaximized());
					
					if(newShell.getFullScreen() || newShell.getMaximized() || newShell.getMinimized()){
						return;
					}
					finalDialogSettings.put(dialogID+".bounds.x", newShell.getLocation().x);
					finalDialogSettings.put(dialogID+".bounds.y", newShell.getLocation().y);
					finalDialogSettings.put(dialogID+".bounds.width", newShell.getSize().x);
					finalDialogSettings.put(dialogID+".bounds.height", newShell.getSize().y);
				}
				
				@Override
				public void controlMoved(ControlEvent e) {
					finalDialogSettings.put(dialogID+".bounds.x", newShell.getLocation().x);
					finalDialogSettings.put(dialogID+".bounds.y", newShell.getLocation().y);
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	
	}
	

}
