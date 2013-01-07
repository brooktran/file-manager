package org.jeelee.filemanager.ui.actions;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogLabelKeys;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jeelee.filemanager.core.operation.OpenOperation;
import org.jeelee.filemanager.core.operation.OpenOperation.ExecuteOrder;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.filemanager.ui.PreferenceConstants;
import org.jeelee.filemanager.ui.views.model.FileExplorer;
import org.jeelee.utils.PluginResources;

public class OpenAction extends UndoableAction  {
	public static final String ID=Messages.OPEN;
	private PluginResources r=FileManagerActivator.RESOURCES;

	public OpenAction(FileExplorer fileExplorer) {
		super(fileExplorer);
		setId(ID);
		FileManagerActivator.RESOURCES.configAction(this,ID );
	}

//	private String[] getButtonLabels() {
//		return new String[]{r.getString(Messages.ONE_AFTER_ONE),r.getString(Messages.CONCURRENT),JFaceResources.getString(IDialogLabelKeys.CANCEL_LABEL_KEY)};
//	}

	@Override
	protected IUndoableOperation getOperation(final IStructuredSelection selection) {
		OpenOperation op = new OpenOperation(getPathProvider());
		if (selection.size() > 1) {
			IPreferenceStore store = FileManagerActivator.getDefault()
					.getPreferenceStore();
			if(store.getString(PreferenceConstants.NEVER_PROMPT_ON_OPEN_MUTIL_FILES).equals(MessageDialogWithToggle.NEVER)){
				int order = store.getDefaultInt(PreferenceConstants.MUTIL_FILE_EXECUTE_ORDER);
				op.setOrder(order==0?ExecuteOrder.CONCURRENT:ExecuteOrder.ONE_AFTER_ONE);
			}else {

				MessageDialogWithToggle dialog = new MessageDialogWithToggle(getShell(), r.getString(Messages.OPEN), null, r.getString(Messages.OPEN_MUTIL_FILES_TIPS), MessageDialogWithToggle.QUESTION, getButtonLabels(), 0, r.getString(Messages.DONT_ASK_ME_AGAIN), false);
				dialog.setPrefStore(store);
				dialog.setPrefKey(PreferenceConstants.NEVER_PROMPT_ON_OPEN_MUTIL_FILES);
				int retVal =dialog.open();
//				 dialog.getReturnCode();
				if ( retVal== IDialogConstants.CANCEL_ID) {
					return null;
				}
				op.setOrder(retVal==IDialogConstants.INTERNAL_ID?ExecuteOrder.CONCURRENT:ExecuteOrder.ONE_AFTER_ONE);
			}

		}
		return op;
	}
	
	private String[] getButtonLabels() {
		return new String[]{r.getString(Messages.ONE_AFTER_ONE),r.getString(Messages.CONCURRENT),JFaceResources.getString(IDialogLabelKeys.CANCEL_LABEL_KEY)};
	}
}
