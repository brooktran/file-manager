/* FileUtils.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.ui.PlatformUI;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.utils.PluginResources;

/**
 * <B>FileUtils</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-8-13 created
 */
public class FileUtils {

	public static void moveTo(String[] paths, FileDelegate target) throws IOException {
		PluginResources r=FileManagerActivator.RESOURCES;

		boolean needPrompt=true;
		int handleType = IDialogConstants.CANCEL_ID;
		for (final String file : paths) {
			Path sourcePath = Paths.get(file);
			Path targetPath = target.getSource().resolve(
					Paths.get(file).getFileName());
			
			
			if (Files.exists(targetPath)) {
				if(targetPath.equals(sourcePath)){
					continue;
				}
				if (needPrompt) {
					MessageDialogWithToggle dialog = new MessageDialogWithToggle(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							 r.getFormatted(Messages.FILE_ALREADY_EXISTS), null, 
							 r.getFormatted(Messages.FILE_ALREADY_EXISTS, targetPath), MessageDialog.QUESTION,
							 new String[] {
										r.getString(Messages.REPLACE),
//										r.getString(Messages.REPLACE_ALL),
										r.getString(Messages.KEEP),
//										r.getString(Messages.KEEP_ALL),
										IDialogConstants.CANCEL_LABEL }, 1, r.getString(Messages.DO_THIS_FOR_THE_NEXT_CONFLICTS), false);
					dialog.open();
					needPrompt = !dialog.getToggleState();
					handleType = dialog.getReturnCode();
				}
				
				if(handleType == IDialogConstants.CANCEL_ID ){
					return;
				}
				if(handleType == IDialogConstants.INTERNAL_ID ){ // REPLACE
					delete(targetPath);
				} else if(handleType == IDialogConstants.INTERNAL_ID +1){ // KEEP
					Path newTarget = targetPath;
					
					for(int i=1;Files.exists(newTarget);i++){
						newTarget = target.getSource().resolve(createNewFilename(targetPath,i));
					}
					targetPath=newTarget;
				}
			}
			Files.move(sourcePath, targetPath);
		} 		
	}

	private static String createNewFilename(Path targetPath, int factor) {
		String name = targetPath.getFileName().toString();
		int p =name.lastIndexOf(".");
		if(p==-1){
			name = name+"("+factor+")";
		}else {
			name =name.substring(0, p)+"("+factor+")"+name.substring(p);
		}
		System.out.println(name);
		return name;
	}

	private static void delete(Path targetPath) throws IOException {
		Files.delete(targetPath);
	}

}
