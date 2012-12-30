package org.jeelee.ui.internal;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionGroup;

public class CompositeActionGroup extends ActionGroup{

	private RetrievableActionGroup[] fGroups;

	public CompositeActionGroup() {
	}

	public CompositeActionGroup(RetrievableActionGroup[] groups) {
		setGroups(groups);
	}

	protected void setGroups(RetrievableActionGroup[] groups) {
		Assert.isTrue(fGroups == null);
		Assert.isNotNull(groups);
		fGroups= groups;
	}

	public void addGroup(RetrievableActionGroup group) {
		if (fGroups == null) {
			fGroups= new RetrievableActionGroup[] { group };
		} else {
			RetrievableActionGroup[] newGroups= new RetrievableActionGroup[fGroups.length + 1];
			System.arraycopy(fGroups, 0, newGroups, 0, fGroups.length);
			newGroups[fGroups.length]= group;
			fGroups= newGroups;
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		if (fGroups == null) {
			return;
		}
		for (int i= 0; i < fGroups.length; i++) {
			fGroups[i].dispose();
		}
	}

	@Override
	public void fillActionBars(IActionBars actionBars) {
		super.fillActionBars(actionBars);
		if (fGroups == null) {
			return;
		}
		for (int i= 0; i < fGroups.length; i++) {
			fGroups[i].fillActionBars(actionBars);
		}
	}

	@Override
	public void fillContextMenu(IMenuManager menu) {
		super.fillContextMenu(menu);
		if (fGroups == null) {
			return;
		}
		for (int i= 0; i < fGroups.length; i++) {
			fGroups[i].fillContextMenu(menu);
		}
	}

	@Override
	public void setContext(ActionContext context) {
		super.setContext(context);
		if (fGroups == null) {
			return;
		}
		for (int i= 0; i < fGroups.length; i++) {
			fGroups[i].setContext(context);
		}
	}

	@Override
	public void updateActionBars() {
		super.updateActionBars();
		if (fGroups == null) {
			return;
		}
		for (int i= 0; i < fGroups.length; i++) {
			fGroups[i].updateActionBars();
		}
	}

//	public ActionGroup[] getfGroups() {
//		return fGroups;
//	}
	public IAction findAction(String id){
		IAction action=null;
		for(RetrievableActionGroup g:fGroups){
			action = g.getAction(id);
			if(action!=null){
				return action;
			}
		}
		return null;
	}
}
