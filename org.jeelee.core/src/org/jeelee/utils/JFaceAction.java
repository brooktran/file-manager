package org.jeelee.utils;

import org.eclipse.jface.action.Action;

public class JFaceAction extends Action{
	public JFaceAction(String id,PluginResources resources){
		this(id, resources, true);
	}

	public JFaceAction(String id, PluginResources resources, boolean enable) {
		resources.configAction(this, id);
		setEnabled(enable);
	}
}
