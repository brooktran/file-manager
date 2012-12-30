package org.jeelee.filemanager.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionContext;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.ui.IContextMenuConstants;
import org.jeelee.filemanager.ui.views.model.FileDragListener;
import org.jeelee.filemanager.ui.views.model.FileDropListener;
import org.jeelee.filemanager.ui.views.model.FileExplorer;
import org.jeelee.utils.DateUtils;
import org.jeelee.utils.StringFormatUtils;

public abstract class FileExplorerActionGroupHelper implements ISelectionProvider, IMenuListener {
	private FileViewerActionGroup actionSet;
	private FileExplorer fileExplorer;
	private ColumnViewer viewer;
	
	public FileExplorerActionGroupHelper(FileExplorer fileExplorer,ColumnViewer viewer) {
		this.viewer =viewer;
		this.fileExplorer = fileExplorer;
		actionSet = new FileViewerActionGroup(fileExplorer);
		
		prepareMenu();
		hookListeners();
		addDndSupport(viewer);
	}

	private void addDndSupport(ColumnViewer viewer) {
		int operations = DND.DROP_COPY | DND.DROP_MOVE; //TODO DND.DROP_LINK
		Transfer[] transferTypes  = { FileTransfer.getInstance() };
		viewer.addDragSupport(operations, transferTypes , new FileDragListener(fileExplorer,viewer));
		viewer.addDropSupport(operations, transferTypes, new FileDropListener(fileExplorer,viewer));

	}

	private void hookListeners() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent e) {
				handleDoubleClick(e);
			}
		});
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				String msg = selectionToString(selection);
				IStatusLineManager statusLineManager =getActionBars().getStatusLineManager();
				statusLineManager.setMessage(msg);
			}
		});
		
	}

	protected  abstract IActionBars getActionBars() ;

	protected String selectionToString(IStructuredSelection selection) {
		if(selection.isEmpty()){
			return fileToString(fileExplorer.getFilter().getSource());
		}
		if(selection.size() ==1){
			return fileToString((FileDelegate) selection.getFirstElement());
		}else {
			long size =0;
			@SuppressWarnings("rawtypes")
			Iterator it = selection.iterator();
			while(it.hasNext()){
				FileDelegate file =(FileDelegate) it.next();
				size += file.getFileSize();
			}
			
			return selection.size()+"\t "+StringFormatUtils.formatSize(size);
		}
	}

	private String fileToString(FileDelegate file) {
		StringBuilder sb = new StringBuilder();
		sb.append(file.getName()+"\t");
		sb.append(StringFormatUtils.formatSize(file.getFileSize())+"\t");
		sb.append( DateUtils.toLocalString(file.getLastModifiedTime())+"\t");
		return sb.toString();
	}

	
	
	protected void handleDoubleClick(DoubleClickEvent e) {
	}

	private void prepareMenu() {
		MenuManager menuMgr = new MenuManager(IContextMenuConstants.POPUP_MENU);//$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(this);
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		//		getSite().registerContextMenu(menuMgr, this);
	}
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		viewer.addSelectionChangedListener(listener);
	}

	@Override
	public ISelection getSelection() {
		return fileExplorer.getPathProvider().getSelection();
	}

	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		viewer.removeSelectionChangedListener(listener);
	}

	@Override
	public void setSelection(ISelection selection) {
		viewer.setSelection(selection);
	}

	@Override
	public void menuAboutToShow(IMenuManager menu) {
		menu.add(new GroupMarker(IContextMenuConstants.GROUP_GOTO));
		menu.add(new Separator(IContextMenuConstants.GROUP_OPEN));
		menu.add(new GroupMarker(IContextMenuConstants.GROUP_SHOW));
		menu.add(new Separator(IContextMenuConstants.GROUP_EDIT));
		menu.add(new Separator(IContextMenuConstants.GROUP_NEW));
		menu.add(new Separator(IContextMenuConstants.GROUP_REORGANIZE));
		menu.add(new Separator(IContextMenuConstants.GROUP_GENERATE));
		menu.add(new Separator(IContextMenuConstants.GROUP_SEARCH));
		menu.add(new Separator(IContextMenuConstants.GROUP_ADDITIONS));
		menu.add(new Separator(IContextMenuConstants.GROUP_VIEWER_SETUP));
		menu.add(new Separator(IContextMenuConstants.GROUP_PROPERTIES));

		actionSet.setContext(new ActionContext(getSelection()));
		actionSet.fillContextMenu(menu);
		actionSet.setContext(null);
	}

	//	private ExplorerViewActionGroup getActionSet() {
	//		if (actionSet == null) {
	//			actionSet = new ExplorerViewActionGroup(site,viewer);
	//		}
	//		return actionSet;
	//	}


	public IAction findAction(String id) {
		return (SelectionDispatchAction) actionSet.findAction(id);
	}
	
}
