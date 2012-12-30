package org.jeelee.filemanager.ui.views.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.JeeleeFileSystem;
import org.jeelee.filemanager.core.SimpleSharedImages;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.ui.internal.ImageCacheColumnLabelProvider;
import org.jeelee.utils.PluginResources;

public class FileDelegateLabelProvider extends ImageCacheColumnLabelProvider{
	private PluginResources r = FileManagerActivator.RESOURCES;
	List<String> imageKeys =new ArrayList<>();

	@Override
	public Image getImage(Object element) {
		FileDelegate file =((FileDelegate)element);
		Image image = file.getSystemIcon();
		imageKeys.add(JeeleeFileSystem.getImageKey(file.toFile()));
		return image;
	}
	
	
	
//	@Override
//	public Image createImage(Object element) {//FIXME cache image
//		return ((FileDelegate)element).getSystemIcon();
//	}

	@Override
	protected String getElementIdentify(Object element) {
		return ((FileDelegate)element).toUri().toString();
	}


	@Override
	public String getText(Object element) {
		FileDelegate file= ((FileDelegate)element);
		String name = file.getDisplayName();

		if(file.isDirectory()){
			if(file.isContentsInitialized()){
				name +=  " (" + file.getChildren().size() + ")"; 
			}else {
				name += " "+r.getString(Messages.FetchingContent); 
			}
		}
		return name;
	}

	@Override
	public Color getForeground(Object element) {
		FileDelegate file = (FileDelegate) element;
		if(file.isSystem()){
			return Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		}
		if(!file.isReadable()){
			return Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
		}
		return null;
	}


	@Override
	public void dispose() {
		for(String key:imageKeys){
			SimpleSharedImages.remove(key);
		}
		
		super.dispose();
	}



}
