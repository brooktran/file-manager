package org.jeelee.filemanager.ui.actions;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.jeelee.filemanager.ui.views.model.FileExplorer;
import org.jeelee.ui.internal.CompositeActionGroup;
import org.jeelee.ui.internal.RetrievableActionGroup;

public class FileViewerActionGroup extends CompositeActionGroup {
	public FileViewerActionGroup(FileExplorer explorer){
		super(new RetrievableActionGroup[]{
				new OpenEditorActionGroup(explorer), 
				new EditEditorActionGroup(explorer), 
				new NewActionGroup(explorer), 
				new ReorganizationActionGroup(explorer)
		});
	}
	

	@Override
	public void fillContextMenu(IMenuManager menu) {
		super.fillContextMenu(menu);
	}


	public void handleDoubleClick(DoubleClickEvent e) {
	}
}
