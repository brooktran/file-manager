package org.jeelee.filemanager.ui.actions;

import java.io.IOException;
import java.text.MessageFormat;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.FileInfo;
import org.jeelee.filemanager.ui.views.model.FileExplorer;
import org.jeelee.utils.AppLogging;

public abstract class FileSystemAction extends SelectionDispatchAction{

	protected FileSystemAction(FileExplorer fileExplorer) {
		super(fileExplorer);
	}
	
	@Override
	public void run(IStructuredSelection selection) {
		if(selection==null || selection.isEmpty()){
			return;
		}
		
		String command = getCommand();
		FileDelegate proxy=(FileDelegate) selection.getFirstElement();
		FileInfo info = proxy.getFileInfo();
		command= MessageFormat.format(command, (Object[])info.toArguements());
		
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			AppLogging.handleException(e);
		}
	}
	
	protected abstract String getCommand();

}
