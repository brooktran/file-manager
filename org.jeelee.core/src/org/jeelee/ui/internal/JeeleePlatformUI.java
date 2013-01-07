package org.jeelee.ui.internal;

import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.jeelee.utils.AppLogging;
import org.jeelee.utils.Sleak;

public class JeeleePlatformUI {
	private static Display display;
	
	public static Display createDisplay() {
		if(AppLogging.isDebugLeaking()){
			DeviceData data = new DeviceData();
		    data.tracking = true;
		    display = new Display(data);
		    Sleak sleak = new Sleak();
		    sleak.open();	
		}else {
			display =PlatformUI.createDisplay();
		}
		return display;
	}
	
	public static Display getDisplay() {
		return display;
	}

}
