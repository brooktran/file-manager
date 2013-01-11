/* RuntimeExecutor.java 
 * Copyright (c) 2013 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.core.utils;

import java.io.IOException;
import java.text.MessageFormat;

import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.preferences.IPreferenceConstants;
import org.jeelee.utils.AppLogging;

/**
 * <B>RuntimeExecutor</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager Jan 7, 2013 created
 */
public class RuntimeExecutor {
	
	
	
	public static Process executeFile(FileDelegate file){
		String command = 	FileManagerActivator.getDefault().getPreferenceStore().getString(
				IPreferenceConstants.COMMAND_RUN);
		return execute(command, file);
	}
	
	public static Process execute(String command,FileDelegate file){
		String cmd = MessageFormat.format(command, (Object[])getArguements(file));
		try {
			return Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			AppLogging.handleException(e);
			return null;
		}
	}
	

	private static String[] getArguements(FileDelegate file) {
		String drive = null;
		String full_path = null;
		String parent_path = null;
		String file_name = null;
		
		full_path = file.getAbsolutePath();

		if (file.isDirectory()) {
			parent_path = file.getAbsolutePath();
			file_name = "dir";
		} else {
			parent_path = file.getRealParent().getAbsolutePath();
			file_name = file.getName();
		}
		if (full_path != null) {
			if (full_path.indexOf(":") != -1) {
				drive = full_path.substring(0, full_path.indexOf(":"));
			}
		}
		return new String[]{drive,parent_path,full_path,
				file_name,"jeelee",System.getProperty("line.separator")};
	}
	
	
	

	
}
