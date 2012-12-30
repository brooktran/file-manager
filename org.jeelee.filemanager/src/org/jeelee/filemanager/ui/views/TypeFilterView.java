package org.jeelee.filemanager.ui.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.jeelee.filemanager.ui.views.model.FileExplorer;
import org.jeelee.filemanager.ui.views.model.FilterViewer;

public class TypeFilterView extends ViewPart {
	private IWorkbenchPart previous;
	private ISelectionListener listener = new ISelectionListener() {
		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if(!(part instanceof FileExplorer) || previous.equals(part)){
				// if(!(part instanceof FileExplorer) ) // clear all buttons
				return ;
			}
			// change the filter
		}
	};
	
	public TypeFilterView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		FilterViewer.createTypeFilterView(parent);
		
		
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(listener);
	}

	@Override
	public void setFocus() {
		
	}

}
