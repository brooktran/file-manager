package org.jeelee.filemanager.ui.actions;

import org.eclipse.jface.action.IMenuManager;
import org.jeelee.filemanager.ui.IContextMenuConstants;
import org.jeelee.filemanager.ui.views.model.FileExplorer;

public class EditEditorActionGroup extends EditorActionGroup {
	public EditEditorActionGroup(FileExplorer fileExplorer) {
		super(fileExplorer);
		setActions(new SelectionDispatchAction[]{
				new CopyFileAction(fileExplorer),
				new CopyFileNameAction(fileExplorer),
				new CopyFilePathAction(fileExplorer),
				new ListFilePathAction(fileExplorer),
				new DeleteAction(fileExplorer)
		}); 
		initialize(actions);
	}
	
	@Override
	public void fillContextMenu(IMenuManager menu) {
		fillContextMenu(IContextMenuConstants.GROUP_EDIT,menu);
	}
	
}
