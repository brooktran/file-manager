package org.jeelee.ui.internal;

import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

public class TreeViewerFactory {

	public static TreeViewer create(Composite parent, int style) {
		TreeViewer treeViewer =	new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.VIRTUAL );
		createNavigation(treeViewer);
		return treeViewer;
	}

	private static void createNavigation(TreeViewer treeViewer) {
//		TreeViewerFocusCellManager focusCellManager = new TreeViewerFocusCellManager(
//		treeViewer, new FocusCellOwnerDrawHighlighter(treeViewer));
//
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(
				treeViewer) {
			@Override
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.F2)
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.SPACE)
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		};
		
		treeViewer.setColumnViewerEditor(new TreeColumnViewerEditor(treeViewer, actSupport, 
				ColumnViewerEditor.TABBING_HORIZONTAL
				| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
				| ColumnViewerEditor.TABBING_VERTICAL
				| ColumnViewerEditor.KEYBOARD_ACTIVATION));
		 
//		TreeViewerEditor.create(treeViewer, actSupport, 
//				ColumnViewerEditor.TABBING_HORIZONTAL
//				| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
//				| ColumnViewerEditor.TABBING_VERTICAL
//				| ColumnViewerEditor.KEYBOARD_ACTIVATION);
//		TreeViewerEditor.create(treeViewer, focusCellManager, actSupport,
//				ColumnViewerEditor.TABBING_HORIZONTAL
//						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
//						| ColumnViewerEditor.TABBING_VERTICAL
//						| ColumnViewerEditor.KEYBOARD_ACTIVATION);

	}
}
class TreeColumnViewerEditor extends ColumnViewerEditor implements Listener {
		private ViewerCell focusCell;
		private TreeEditor treeEditor;
		private TreeViewer viewer;
//		private ColumnViewerEditorActivationStrategy activationStrategy;
		private boolean arrayLeftKeyDownFound;
		protected TreeColumnViewerEditor(final TreeViewer viewer,
				ColumnViewerEditorActivationStrategy editorActivationStrategy,
				int feature) {
			super(viewer, editorActivationStrategy, feature);
			treeEditor = new TreeEditor(viewer.getTree());
//			this.activationStrategy = editorActivationStrategy;
			this.viewer =viewer ;
			viewer.setColumnViewerEditor(this);
			
			viewer.getControl().addListener(SWT.MouseDown, this);
			viewer.getControl().addListener(SWT.KeyDown, this);
			
			viewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					if(!arrayLeftKeyDownFound || focusCell==null){
						return;
					}
					IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
					if(!focusCell.getElement().equals(selection.getFirstElement())){
//						TreeItem parent =((TreeItem )focusCell.getItem()).getParentItem();
//						focusCell =new ViewerCell(null, 0, cell);
//						focusCell = null;
						while(focusCell!=null && !focusCell.getElement().equals(selection.getFirstElement())){
							focusCell = focusCell.getNeighbor(ViewerCell.ABOVE, false);
						}
					}
					arrayLeftKeyDownFound =false;
				}
			});
		}

		@Override
		public void handleEvent(Event event) {
			switch (event.type) {
			case SWT.MouseDown:
				focusCell = viewer.getCell(new Point(event.x,event.y));
				break;
			case SWT.KeyDown:
//				if(focusCell == null){
//					focusCell = viewer.getCell(new Point(0, 0));
//				}
				if(focusCell == null){
					return;
				}
				ViewerCell cell = null;
				switch (event.keyCode) {
				case SWT.ARROW_UP:
					cell= focusCell.getNeighbor(ViewerCell.ABOVE, false);
					break;
				case SWT.ARROW_DOWN:
					cell= focusCell.getNeighbor(ViewerCell.BELOW, false);
					break;
				case SWT.ARROW_LEFT:
					arrayLeftKeyDownFound=true;
					break;
				default:
					break;
				}
				if(cell!=null){
					focusCell = cell;
				}
			default:
				break;
			}
			
		}
		
		@Override
		protected void updateFocusCell(ViewerCell focusCell,
				ColumnViewerEditorActivationEvent event) {
			this.focusCell = focusCell;
		}
		
		
		@Override
		public ViewerCell getFocusCell() {
			return focusCell;
		}
		
		@Override
		protected void setLayoutData(LayoutData layoutData) {
			treeEditor.grabHorizontal = layoutData.grabHorizontal;
			treeEditor.horizontalAlignment = layoutData.horizontalAlignment;
			treeEditor.minimumWidth = layoutData.minimumWidth;
			treeEditor.verticalAlignment = layoutData.verticalAlignment;
			if( layoutData.minimumHeight != SWT.DEFAULT ) {
				treeEditor.minimumHeight = layoutData.minimumHeight;
			}
		}
		
		@Override
		protected void setEditor(Control w, Item item, int fColumnNumber) {
			treeEditor.setEditor(w, (TreeItem) item, fColumnNumber);
		}
	}