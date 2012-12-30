package org.jeelee.filemanager.ui.actions;
//
//import org.eclipse.core.filesystem.IFileStore;
//import org.eclipse.core.resources.IResource;
//import org.eclipse.core.runtime.IAdaptable;
//import org.eclipse.jface.viewers.ISelectionChangedListener;
//import org.eclipse.jface.viewers.IStructuredSelection;
//import org.eclipse.jface.window.Window;
//import org.eclipse.ui.IEditorDescriptor;
//import org.eclipse.ui.IEditorRegistry;
//import org.eclipse.ui.IWorkbenchPage;
//import org.eclipse.ui.IWorkbenchSite;
//import org.eclipse.ui.PartInitException;
//import org.eclipse.ui.dialogs.EditorSelectionDialog;
//import org.eclipse.ui.ide.FileStoreEditorInput;
//import org.jeelee.filemanager.FileManagerActivator;
//
//public class OpenWithAction extends SelectionDispatchAction implements ISelectionChangedListener{
//
//	protected OpenWithAction(IWorkbenchSite site) {
//		super(site);
//		FileManagerActivator.RESOURCES.configAction(this, "open.with");
//	}
//
//
//	@Override
//	public void run(IStructuredSelection selection) {
//		if(selection.isEmpty()){
//			return;
//		}
//		
//		IStructuredSelection ss= selection;
//		if (ss.size() != 1)
//			return;
//		
//		Object o= ss.getFirstElement();
//		if (!(o instanceof IAdaptable))
//			return;
//		
//		
//		IAdaptable element= (IAdaptable)o;
//		Object resource= element.getAdapter(IResource.class);
//		
//		if (resource instanceof IFileStore){
//			IFileStore editorDescriptor = (IFileStore) resource;
//			EditorSelectionDialog dialog = new EditorSelectionDialog(
//					getSite().getShell());
//			dialog
//					.setMessage("ssdf");
//			if (dialog.open() == Window.OK) {
//				IEditorDescriptor editor = dialog.getSelectedEditor();
//				if (editor != null) {
//					try {
//						if (editor.isOpenExternal()) {
//							 getSite().getPage().openEditor(
//									 new FileStoreEditorInput(editorDescriptor), 
//									 editor.getId(),true,IWorkbenchPage.MATCH_NONE
//									 );
//						} else {
//							String editorId = editorDescriptor == null ? IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID
//									: editor.getId();
//
//							getSite().getPage().openEditor(new FileStoreEditorInput(editorDescriptor),
//									editorId, true,  IWorkbenchPage.MATCH_INPUT | IWorkbenchPage.MATCH_ID);
//							// only remember the default editor if the open
//							// succeeds
////							IDE.setDefaultEditor(file, editorId);
//						}
//					} catch (PartInitException e) {}
//				}
//			}
//		}
//		
//		
//	}
//	
//
//}
