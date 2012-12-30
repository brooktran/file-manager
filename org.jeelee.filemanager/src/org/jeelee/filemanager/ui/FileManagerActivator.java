package org.jeelee.filemanager.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jeelee.filemanager.ui.preferences.FileManagerPreferencePage;
import org.jeelee.utils.PluginResources;
import org.jeelee.utils.SharedResources;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class FileManagerActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jeelee.filemanager"; //$NON-NLS-1$

	// The shared instance
	private static FileManagerActivator plugin;
	
	public static final PluginResources RESOURCES= SharedResources.getResources(PLUGIN_ID, FileManagerActivator.class);
	
	public FileManagerActivator() {
	}

	@Override
	protected void initializeDefaultPluginPreferences() {
		new FileManagerPreferencePage();
	}
	
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		initializeDefaultPluginPreferences();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static FileManagerActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
