/* FileHelper.java 
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

import org.eclipse.jface.dialogs.IInputValidator;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.utils.PluginResources;


/**
 * <B>FileHelper</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager Dec 20, 2012 created
 */
public class FileHelper {
	private static PluginResources r = FileManagerActivator.RESOURCES;

	public static FileDelegate createFile(FileDelegate parent, String name) throws IOException {
		Path path = Files.createFile(parent.getSource().resolve(name));
		return new FileDelegate(path);
	}

	public static FileDelegate createDirectories(FileDelegate parent, String name) throws IOException {
		Path path = Files.createDirectories(parent.getSource().resolve(name));
		return new FileDelegate(path);
	}

	public static IInputValidator getFileNameInputValidator(final FileDelegate parent) {
 		String os=System.getProperty("os.name").toLowerCase();//$NON-NLS-1$
		if(os.startsWith("windows")){//$NON-NLS-1$
			return new IInputValidator() {
				@Override
				public String isValid(String newText) {
					if(newText.length()>255){
						return r.getString(Messages.NEW_FILE_NAME_LENGTH_TIPS); 
					}
					
					String[] invalid={"\\","/",":","*","<",">","|"};
					for(String c:invalid){
						if(newText.contains(c)){
							return r.getString(Messages.NEW_FILE_INVALID_TIPS);
						}
					}
					
					Path target = parent.getSource().resolve(newText);
					if(Files.exists(target)){
						return r.getFormatted(Messages.FILE_ALREADY_EXISTS,newText);
					}
					
					return null;
				}
			};
		}
		return null;
	}

	public static boolean exists(FileDelegate parent, String filename) {
		return Files.exists(parent.getSource().resolve(filename));
	}


}
