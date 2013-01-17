package org.jeelee.filemanager.ui.views.model;

import org.jeelee.filemanager.core.FileDelegate;

public class FileCounterLabelProvider extends FileDelegateLableProvider{
//	private PluginResources r = FileManagerActivator.RESOURCES;
	@Override
	public String getText(Object element) {
		FileDelegate file= ((FileDelegate)element);
		String name = file.getDisplayName();

		if(file.isDirectory()){
			if(file.isContentsInitialized()){
				name +=  " (" + file.getChildren().size() + ")"; 
			}
//			else {
//				name += " "+r.getString(Messages.FetchingContent); 
//			}
		}
		return name;
	}
}
