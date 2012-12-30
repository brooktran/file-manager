package org.jeelee.filemanager.ui.actions;

import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.preferences.IPreferenceConstants;
import org.jeelee.filemanager.ui.views.model.FileExplorer;

public class BrowseAction extends FileSystemAction {

	protected BrowseAction(FileExplorer fileExplorer) {
		super(fileExplorer);
		FileManagerActivator.RESOURCES.configAction(this, "browse");
	}
	
	@Override
	protected String getCommand() {
		return FileManagerActivator.getDefault().getPreferenceStore().getString(
				IPreferenceConstants.COMMAND_EXPLORE
				);
	}


	
}
