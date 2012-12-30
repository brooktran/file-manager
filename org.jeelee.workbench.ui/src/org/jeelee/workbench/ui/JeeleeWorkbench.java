package org.jeelee.workbench.ui;

import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class JeeleeWorkbench {
	private static class SingletonHolder {
		private static final JeeleeWorkbench  singleton = new JeeleeWorkbench();
	}
	
	public static final JeeleeWorkbench getInstance(){
		return SingletonHolder.singleton;
	}
	private JeeleeWorkbench(){
	}
	
	
	
	private  ApplicationActionBarAdvisor actionBarAdvisor;
	
	public  ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return actionBarAdvisor=new ApplicationActionBarAdvisor(configurer);
	}

}
