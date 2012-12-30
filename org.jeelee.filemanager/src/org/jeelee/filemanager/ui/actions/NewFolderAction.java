/* NewFolderAction.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.ui.actions;

import java.io.IOException;

import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.FileHelper;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.filemanager.ui.views.model.FileExplorer;
import org.jeelee.utils.PluginResources;

/**
 * <B>NewFolderAction</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager Dec 19, 2012 created
 */
public class NewFolderAction extends CreationAction{
	private PluginResources r = FileManagerActivator.RESOURCES;
	
	protected NewFolderAction(FileExplorer fileExplorer) {
		super(fileExplorer);
		r.configAction(this, Messages.NEW_FOLDER);
	}
	 



	@Override
	protected FileDelegate execute(FileDelegate parent, String value) throws IOException {
		return FileHelper.createDirectories(parent,value);
	}


	@Override
	protected String getDefaultValue(FileDelegate parent) {
		return getFileName(parent,r.getString(Messages.NEW_FOLDER));
	}


	@Override
	protected String getDialogMessage(FileDelegate parent) {
		return r.getFormatted(Messages.NEW_FOLDER_INPUT_TIPS,parent.getName());
	}


	@Override
	protected String getDialogTitle() {
		return r.getString(Messages.NEW_FOLDER);
	}
	
	
}