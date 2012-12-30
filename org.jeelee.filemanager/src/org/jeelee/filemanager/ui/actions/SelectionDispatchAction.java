package org.jeelee.filemanager.ui.actions;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Shell;
import org.jeelee.filemanager.ui.views.model.FileExplorer;

public abstract class SelectionDispatchAction extends Action implements ISelectionChangedListener {

	protected FileExplorer fileExplorer;
	
	protected SelectionDispatchAction(FileExplorer fileExplorer) {
		Assert.isNotNull(fileExplorer);
		this.fileExplorer = fileExplorer;
	}

	public ISelection getSelection() {
		if (fileExplorer.getPathProvider() != null) {
			return fileExplorer.getPathProvider().getSelection();
		} else {
			return null;
		}
	}


	public void update(ISelection selection) {
		dispatchSelectionChanged(selection);
	}

	public void selectionChanged(IStructuredSelection selection) {
		selectionChanged((ISelection)selection);
	}
	public void selectionChanged(ISelection selection) {
		setEnabled(!selection.isEmpty());
	}
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		dispatchSelectionChanged(event.getSelection());
	}
	
	public void run(IStructuredSelection selection) {
		run((ISelection)selection);
	}
	
	public void run(ISelection selection) {
	}

	@Override
	public void run() {
		dispatchRun(getSelection());
	}

	private void dispatchSelectionChanged(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			selectionChanged((IStructuredSelection)selection);
		} else {
			selectionChanged(selection);
		}
	}

	private void dispatchRun(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			run((IStructuredSelection)selection);
//		} else if (selection instanceof ITextSelection) {
//			run((ITextSelection)selection);
		} else {
			run(selection);
		}
	}


	public Shell getShell(){
		return fileExplorer.getShell();
	}
}
