package org.jeelee.filemanager.ui.actions;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionBars;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.ui.editors.FolderEditor;
import org.jeelee.filemanager.ui.editors.FileResourceInput;

public class FileEditorActionGroupHelper extends FileExplorerActionGroupHelper{
	private FolderEditor editor;
	
	public FileEditorActionGroupHelper(FolderEditor editor) {
		super(editor,editor.getTableViewer());
		this.editor = editor;
		
	}
	@Override
	public void handleDoubleClick(DoubleClickEvent e){
		IStructuredSelection selection = (IStructuredSelection) e
				.getSelection();
		final Object element = selection.getFirstElement();
		if(!(element instanceof FileDelegate)){
			return;
		}
		FileDelegate file= (FileDelegate)element;
		
		
		if(file.isVisitable()){
			FileResourceInput input = new FileResourceInput(file, false);
			editor.updateInput(input);
		} else {
//			findAction(OpenAction.ID).run(selection);
		}
	}
	@Override
	protected IActionBars getActionBars() {
		return editor.getEditorSite().getActionBars();
	}

}
