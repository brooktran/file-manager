package org.jeelee.filemanager.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.jeelee.filemanager.ui.views.model.FileExplorer;
import org.jeelee.ui.internal.RetrievableActionGroup;

public class EditorActionGroup extends RetrievableActionGroup{
	protected SelectionDispatchAction[] actions;
	private FileExplorer fileExplorer;
	
	public EditorActionGroup(FileExplorer fileExplorer) {
		this.fileExplorer = fileExplorer;
	}

	public SelectionDispatchAction[] getActions() {
		return actions;
	}
	
	public void setActions(SelectionDispatchAction[] actions) {
		this.actions = actions;
	}
	
	
	protected void initialize(SelectionDispatchAction... actions ) {
		ISelection selection= fileExplorer.getPathProvider().getSelection();
		updateSelection(selection,actions);
		addSelectionChangedListeners(fileExplorer.getPathProvider(),actions);
	}
	
	protected void removeSelectioinChangedListeners(ISelectionChangedListener... listeners) {
		for(ISelectionChangedListener l:listeners){
			fileExplorer.getPathProvider().removeSelectionChangedListener(l);
		}
	}

	protected void appendToGroup(String groupName,IMenuManager menu, IAction... actions) {
		for(IAction a:actions){
			if (a.isEnabled()) {
				menu.appendToGroup(groupName, a);
			}
		}
	}

	protected void addSelectionChangedListeners(ISelectionProvider  selectionProvider, ISelectionChangedListener... listeners) {
		for(ISelectionChangedListener l:listeners){
			selectionProvider.addSelectionChangedListener(l);
		}
	}

	protected void updateSelection(ISelection selection, SelectionDispatchAction... actions) {
		for(SelectionDispatchAction ac:actions){
			ac.update(selection);
		}
	}
	
	
	protected void fillContextMenu(String groupName, IMenuManager menu) {
		super.fillContextMenu(menu);
		appendToGroup(groupName,menu, actions);
	}
	
	@Override
	public void dispose() {
		removeSelectioinChangedListeners(actions);
		super.dispose();
	}
	
	@Override
	public IAction getAction(String id) {
		for(SelectionDispatchAction action : actions){
			if(id.equals(action.getId())){
				return action;
			}
		}
		return null;
	}
}
