package org.jeelee.filemanager.ui;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.jeelee.filemanager.ui.views.GlobalFileExplorerView;

public class Perspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		layout.addView(GlobalFileExplorerView.ID, IPageLayout.LEFT, 0.3f, layout.getEditorArea());
	}
}
