/* NewActionGroup.java 
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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.jeelee.filemanager.ui.IContextMenuConstants;
import org.jeelee.filemanager.ui.views.model.FileExplorer;


/**
 * <B>NewActionGroup</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager Dec 19, 2012 created
 */
public class NewActionGroup extends EditorActionGroup{

	public NewActionGroup(FileExplorer explorer) {
		super(explorer);
		
		setActions(new SelectionDispatchAction[]{
				new NewFileAction(explorer),
				new NewFolderAction(explorer)
		});
		
		initialize(actions);
		
	}
	@Override
	public void fillContextMenu(IMenuManager menu) {
		super.fillContextMenu(menu);
		
		MenuManager subMenu = new MenuManager("new","org.jeelee.filemanager.ui.menu.new");
		subMenu.setActionDefinitionId("org.jeelee.filemanager.ui.quickmenu");
		
//		subMenu.add(new Separator(IContextMenuConstants.GROUP_NEW));
		
		for(IAction a:actions){
			if (a.isEnabled()) {
				subMenu.add(a);
			}
		}
		menu.appendToGroup(IContextMenuConstants.GROUP_NEW, subMenu);
//		appendToGroup(IContextMenuConstants.GROUP_NEW, subMenu, actions);
		
	}
}
