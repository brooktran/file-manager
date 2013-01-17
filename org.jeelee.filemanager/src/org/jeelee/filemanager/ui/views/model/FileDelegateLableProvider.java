/* FileDelegateLableProvider.java 
 * Copyright (c) 2013 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
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
import org.jeelee.ui.internal.ImageCacheColumnLabelProvider;

/**
 * <B>FileDelegateLableProvider</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager Jan 15, 2013 created
 */
public class FileDelegateLableProvider  extends ImageCacheColumnLabelProvider{
	private List<String> imageKeys =new ArrayList<>();

	@Override
	public Image getImage(Object element) {
		FileDelegate file =((FileDelegate)element);
		Image image = file.getSystemIcon();
		imageKeys.add(JeeleeFileSystem.getImageKey(file.toFile()));
		return image;
	}
	

	@Override
	protected String getElementIdentify(Object element) {
		return ((FileDelegate)element).toUri().toString();
	}


	@Override
	public String getText(Object element) {
		FileDelegate file= ((FileDelegate)element);
		String name = file.getDisplayName();
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
			SimpleSharedImages.pull(key);
		}
		super.dispose();
	}



}
