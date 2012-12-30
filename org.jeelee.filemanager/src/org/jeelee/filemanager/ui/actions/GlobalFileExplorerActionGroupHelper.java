package org.jeelee.filemanager.ui.actions;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.ui.editors.FolderEditorHelper;
import org.jeelee.filemanager.ui.views.GlobalFileExplorerView;

public class GlobalFileExplorerActionGroupHelper extends FileExplorerActionGroupHelper{
	private GlobalFileExplorerView view;

	public GlobalFileExplorerActionGroupHelper(GlobalFileExplorerView view) {
		super(view,view.getTreeViewer());
		this.view = view;
	}

	@Override
	public void handleDoubleClick(DoubleClickEvent e){
		final IStructuredSelection selection = (IStructuredSelection) e
				.getSelection();
		final Object element = selection.getFirstElement();
		final TreeViewer treeViewer = view.getTreeViewer();

		if(!(element instanceof FileDelegate)){
			return;
		}
		final FileDelegate file = (FileDelegate)element;

		final IWorkbenchPage page =view.getViewSite().getPage();
		FolderEditorHelper.openEditor(file, page,false);

		// double click: expand/collapse the tree
		if (selection instanceof ITreeSelection) {
			treeViewer.getControl().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					TreePath[] paths = ((ITreeSelection) selection)
							.getPathsFor(element);
					if(treeViewer.getExpandedState(element)){
						treeViewer.collapseToLevel(element, paths.length);
					}else {
						treeViewer.expandToLevel(element, paths.length);
					}
				}
			});
		} 
		//			else {
		//				if(treeViewer.getExpandedState(element)){
		//					treeViewer.collapseToLevel(element, paths.length);
		//				}else {
		//					treeViewer.expandToLevel(element, paths.length);
		//				}
		//			}
	}

	@Override
	protected IActionBars getActionBars() {
		return view.getViewSite().getActionBars();
	}
}
