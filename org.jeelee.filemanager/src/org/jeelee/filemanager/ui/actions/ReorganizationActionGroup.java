package org.jeelee.filemanager.ui.actions;

import org.eclipse.jface.action.IMenuManager;
import org.jeelee.filemanager.ui.IContextMenuConstants;
import org.jeelee.filemanager.ui.views.model.FileExplorer;

public class ReorganizationActionGroup extends EditorActionGroup {
	public ReorganizationActionGroup(FileExplorer fileExplorer) {
		super(fileExplorer);
		
		setActions(new SelectionDispatchAction[]{
				new RenameAction(fileExplorer)
				,new BatchRenameAction(fileExplorer)
				,new RefreshAction(fileExplorer)
		});
		initialize(actions);
	}
	
	@Override
	public void fillContextMenu(IMenuManager menu) {
		fillContextMenu(IContextMenuConstants.GROUP_REORGANIZE,menu);
	}
	
}
