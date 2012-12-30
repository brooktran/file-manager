package org.jeelee.filemanager.ui.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.jeelee.filemanager.core.FileDelegate;

public class FileResourceInput implements IEditorInput {
	private final boolean openInNewEditor;
	private FileDelegate file ;
	public FileResourceInput(FileDelegate fileProxy,
			boolean openInNewEditor) {
		this.openInNewEditor=openInNewEditor;
		this.file = fileProxy;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return file.exists();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null; 
	}

	@Override
	public String getToolTipText() {
		return file.toUri().toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(! (obj instanceof FileResourceInput)){
			return false;
		}
		FileResourceInput other = (FileResourceInput)obj;
		return !other.openInNewEditor;
		
//		if(other.openInNewEditor){
//			return false;
//		}
//		return file.equals(other.getFileDelegate());
	}

	public boolean openInNewEditor() {
		return openInNewEditor;
	}
	public FileDelegate getFileDelegate() {
		return file;
	}


	
	
}
