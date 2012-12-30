package org.jeelee.core;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jeelee.utils.PluginResources;
import org.jeelee.utils.SharedResources;
import org.osgi.framework.BundleContext;

public class JeeleeActivator extends AbstractUIPlugin {
	public static final String ID = "org.jeelee.core"; //$NON-NLS-1$
	public static final PluginResources RESOURCE=SharedResources.getResources(ID,JeeleeActivator.class);
	
	private static JeeleeActivator plugin;
	
	
	public JeeleeActivator() {
	}
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	public static JeeleeActivator getDefault() {
		return plugin;
	}
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(ID, path);
	}
}
