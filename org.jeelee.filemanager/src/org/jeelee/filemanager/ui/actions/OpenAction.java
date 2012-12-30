package org.jeelee.filemanager.ui.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.program.Program;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.filemanager.ui.views.model.FileExplorer;

public class OpenAction extends SelectionDispatchAction  {
	public static final String ID=Messages.OPEN;

	public OpenAction(FileExplorer fileExplorer) {
		super(fileExplorer);
		setId(ID);
		FileManagerActivator.RESOURCES.configAction(this,ID );
	}

	@Override
	public void run(IStructuredSelection selection) {
		FileDelegate proxy=(FileDelegate) selection.getFirstElement();
		Program.launch(proxy.getAbsolutePath());
		//		super.run(selection);
	}

	//	@Override
	//	protected String getCommand() {
	//		return 	FileManagerActivator.getDefault().getPreferenceStore().getString(
	//				IPreferenceConstants.COMMAND_RUN);
	//	}
}
