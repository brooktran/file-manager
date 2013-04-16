/* CreationAction.java 
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.file.InvalidPathException;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.ViewPart;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.FileHelper;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.filemanager.ui.views.model.FileExplorer;
import org.jeelee.utils.AppLogging;

/**
 * <B>CreationAction</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager Dec 20, 2012 created
 */
public abstract class CreationAction extends SelectionDispatchAction{

	protected CreationAction(FileExplorer fileExplorer) {
		super(fileExplorer);
	}
	
	protected FileDelegate getSelectionFolder(IStructuredSelection selection){
		FileDelegate file = null;
		if(selection.isEmpty()){
			file = fileExplorer.getDefaultSelection();
		}else {
			file = (FileDelegate) selection.getFirstElement();
		}
		if(file != null && !file.isDirectory()){
			file = file.getParent();
		}
		
		return file;
	}
	

	@Override
	public void run(IStructuredSelection selection) {
		FileDelegate parent = getSelectionFolder(selection);
		if(parent == null){
			return;
		}
		
		InputDialog dialog = new InputDialog(getShell(), 
				getDialogTitle(),
				getDialogMessage(parent),
				getDefaultValue(parent), 
				FileHelper.getFileNameInputValidator(parent));
		if(dialog.open() == InputDialog.OK){
			try {
				final FileDelegate file = execute(parent,dialog.getValue());
				if(file == null ){
					return;
				}
				PropertyChangeListener listener = new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if(evt.getPropertyName().equals(FileExplorer.REFRESHED_PROPERTY)){
							fileExplorer.getShell().getDisplay().asyncExec(new Runnable() {
								@Override
								public void run() {
									fileExplorer.setSelection(new StructuredSelection(file));
								}
							});
							fileExplorer.removePropertyChangeListener(this);
						}
					}
				};
				fileExplorer.addPropertyChangeListener(listener);
				fileExplorer.refresh();
			} catch (IOException|InvalidPathException e) {
				MessageDialog.openError(getShell(), FileManagerActivator.RESOURCES.getString(Messages.ERROR), e.getMessage());
				AppLogging.handleException(e);
			}
		}
		
	}
	
	protected abstract FileDelegate execute(FileDelegate parent, String value) throws IOException;
	protected abstract String getDefaultValue(FileDelegate parent) ;
	protected abstract String getDialogMessage(FileDelegate parent);
	protected abstract String getDialogTitle() ;

	@Override
	public void selectionChanged(IStructuredSelection selection) {
		if(fileExplorer instanceof ViewPart){
			setEnabled(selection.size()==1);
			return;
		}
		if(fileExplorer instanceof EditorPart){
			setEnabled(selection.size()<2);
		}
	}

	protected String getFileName(FileDelegate parent, final String fileName) {
		int p = fileName.lastIndexOf(".");
		String suffix = (p==-1&&fileName.length()>p?"":fileName.substring(p, fileName.length()));
		String name = p==-1?fileName :fileName.substring(0, p);
		String newFilename=  fileName;
		int i=1;
		while(FileHelper.exists(parent,newFilename)){
			newFilename = name+" ("+(i++)+")"+suffix;
		}
		return newFilename;
	}
	
}
