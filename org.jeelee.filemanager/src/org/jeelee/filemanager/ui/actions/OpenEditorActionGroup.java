package org.jeelee.filemanager.ui.actions;

import org.eclipse.jface.action.IMenuManager;
import org.jeelee.filemanager.ui.IContextMenuConstants;
import org.jeelee.filemanager.ui.views.model.FileExplorer;

public class OpenEditorActionGroup extends EditorActionGroup {
	public OpenEditorActionGroup(FileExplorer explorer) {
		super(explorer);
		setActions(new SelectionDispatchAction[]{
				new OpenAction(explorer)
//				,new OpenWithAction(fSite)
				, new BrowseAction(explorer)
				, new OpenInNewEditorAction(explorer)
				, new OpenShellAction(explorer)
		});
		
//		fOpen.setActionDefinitionId(IJavaEditorActionDefinitionIds.OPEN_EDITOR);
		initialize(actions);
//		if (specialSelectionProvider != null)
//			fOpen.setSpecialSelectionProvider(specialSelectionProvider);
	}
	
	
	@Override
	public void fillContextMenu(IMenuManager menu) {
		fillContextMenu(IContextMenuConstants.GROUP_OPEN,menu);
	}
}
