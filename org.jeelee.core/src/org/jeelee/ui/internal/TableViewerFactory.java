package org.jeelee.ui.internal;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CellNavigationStrategy;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.FocusCellHighlighter;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.jface.viewers.TableViewerRow;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerRow;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jeelee.core.JeeleeActivator;
import org.jeelee.core.JeeleeMessages;
import org.jeelee.utils.PluginResources;

public class TableViewerFactory {
	public static TableViewerColumn createTableColumn(TableViewer tableViewer,
			String name) {
		return createTableColumn(tableViewer, name, name);
	}

	public static TableViewerColumn createTableColumn(TableViewer tableViewer,
			String name, String toolTips) {
		return createTableColumn(tableViewer, name, toolTips, 80, true, true);
	}

	public static TableViewerColumn createTableColumn(TableViewer tableViewer,
			String name, int defaultSize) {
		return createTableColumn(tableViewer, name, name, defaultSize, true,
				true);
	}

	public static TableViewerColumn createTableColumn(TableViewer tableViewer,
			String name, String toolTips, int defaultSize, boolean moveable,
			boolean resizable) {
		TableViewerColumn column = new TableViewerColumn(tableViewer, 0);
		column.getColumn().setText(name);
		column.getColumn().setToolTipText(toolTips == null ? name : toolTips);
		column.getColumn().setWidth(defaultSize);
		column.getColumn().setMoveable(moveable);
		column.getColumn().setResizable(resizable);

		return column;
	}

	public static TableViewerColumn createTableColumn(TableViewer tableViewer,
			String name, int defaultSize, int index) {
		TableViewerColumn column = createTableColumn(tableViewer, name,
				defaultSize);
		column.getColumn().addSelectionListener(
				getSelectionAdapter(column, index));
		return column;
	}

	private static SelectionListener getSelectionAdapter(
			final TableViewerColumn viewerColumn, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableViewer tableViewer =(TableViewer) viewerColumn.getViewer();
				ViewerColumnComparator comparator =(ViewerColumnComparator) viewerColumn.getViewer().getComparator();
				comparator.setColumn(index);
				int direction = comparator.getDirection();
				tableViewer.getTable().setSortDirection(direction);
				tableViewer.getTable().setSortColumn(viewerColumn.getColumn());
				tableViewer.refresh();
			}
		};
		return selectionAdapter;
	}

	public static CheckboxTableViewer createCheckboxTableViewer(
			Composite parent, boolean showRowNumber, boolean createNavigation,
			int style) {
		CheckboxTableViewer tableViewer = new CheckboxTableViewer(new Table(
				parent, style));
		initTableViewer(showRowNumber, createNavigation, tableViewer);
		return tableViewer;
	}

	public static TableViewer createTableViewer(Composite parent,
			boolean showRowNumber, boolean createNavigation, int style) {
		TableViewer tableViewer = new TableViewer(parent, style);
		initTableViewer(showRowNumber, createNavigation, tableViewer);
		return tableViewer;
	}

	private static void initTableViewer(boolean showRowNumber,
			boolean createNavigation, final TableViewer tableViewer) {
		final Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		if (createNavigation) {
			createNavigation(tableViewer, getDefaultEditorStrategy(tableViewer));
		}

		PluginResources r = JeeleeActivator.RESOURCE;

		MenuManager mgr = new MenuManager();
		Action configureColumns = new Action(r.getString(JeeleeMessages.TABLE_CONFIGURE)) {
			@Override
			public void run() {
				CConfigureColumns
						.forTable(table, new SameShellProvider(
								tableViewer.getControl()));
			}
		};
		mgr.add(configureColumns);
		tableViewer.getControl().setMenu(
				mgr.createContextMenu(tableViewer.getControl()));

		if (showRowNumber) {
			TableViewerColumn lineNumber = createTableColumn(tableViewer, " ",
					r.getString(JeeleeMessages.LINE_NUMBER), 40, true, true);
			lineNumber.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public void update(ViewerCell cell) {
					super.update(cell);
					TableViewerRow row = (TableViewerRow) cell.getViewerRow();
					cell.setText((tableViewer.getTable()
							.indexOf((TableItem) row.getItem()) + 1)+"");
				}

				@Override
				public Color getBackground(Object element) {
					return tableViewer.getTable().getShell().getBackground();
				}
			});
		}
	}

	private static ColumnViewerEditorActivationStrategy getDefaultEditorStrategy(
			TableViewer tableViewer) {
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(
				tableViewer) {
			@Override
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				if ((event.eventType != 5) && (event.eventType != 3)) {
					if (((event.eventType != 1) || (event.keyCode != 32))
							&& ((event.eventType != 1) || (event.keyCode != 16777227))
							&& (event.eventType != 4)) {
						return false;
					}
				}
				return true;
			}
		};
		return actSupport;
	}
	public static void createNavigation(final TableViewer tableViewer, ColumnViewerEditorActivationStrategy actSupport) {
//		tableViewer.getTable().setBackgroundMode(SWT.INHERIT_DEFAULT);
		CellNavigationStrategy naviStrat = new CellNavigationStrategy() {
			@Override
			public boolean isNavigationEvent(ColumnViewer viewer, Event event) {
				switch (event.keyCode) {
				case SWT.CR:
					return true;
				default:
					return super.isNavigationEvent(viewer, event);
				}
			}
			@Override
			public ViewerCell findSelectedCell(ColumnViewer viewer,
					ViewerCell currentSelectedCell, Event event) {
				ViewerCell cell =null ;
				Table table =tableViewer.getTable();
				int columnCount = table.getColumnCount();
				
				switch (event.keyCode) {
				case SWT.ARROW_UP:
					if (currentSelectedCell != null) {
						cell = currentSelectedCell.getNeighbor(ViewerCell.ABOVE, false);
					}
					break;
				case SWT.ARROW_DOWN:
					if (currentSelectedCell != null) {
						cell = currentSelectedCell.getNeighbor(ViewerCell.BELOW, false);
					}
					break;
				case SWT.ARROW_LEFT:
					if (currentSelectedCell != null) {
						if (currentSelectedCell.getColumnIndex() == 0 ) {
							cell = currentSelectedCell.getViewerRow().getCell(columnCount-1);
						}else {
							cell = currentSelectedCell.getNeighbor(ViewerCell.LEFT, false);
						}
					}
					break;
				case SWT.ARROW_RIGHT :
				case SWT.CR:
					if (currentSelectedCell != null) {
						if (currentSelectedCell.getColumnIndex() == columnCount-1 ) {
//							ViewerRow row = currentSelectedCell.getViewerRow();
//							if( event.keyCode==SWT.CR){ //FIXME here is a bug. it don't work.
//								row = row.getNeighbor(ViewerRow.BELOW, true);
//							}
							cell = currentSelectedCell.getViewerRow().getCell(0);
						}else {
							cell = currentSelectedCell.getNeighbor(ViewerCell.RIGHT, false);
						}
					}
					break;
				}
			
				if( cell != null ) {
					table.showColumn(table.getColumn(cell.getColumnIndex()));
				}
				return cell;
			}
			
		};
		
		int style = tableViewer.getControl().getStyle();
		FocusCellHighlighter highlighter = 
//				new EmptyHighLighter(tableViewer);
				(style & SWT.MULTI) !=0 ?
					new EmptyHighLighter(tableViewer):new FocusCellOwnerDrawHighlighter(tableViewer){
						@Override
						protected Color getSelectedCellBackgroundColorNoFocus(
								ViewerCell cell) {
							return null;
//							cell.getControl().getDisplay().getSystemColor(
//									SWT.COLOR_LIST_SELECTION);
						}
						@Override
						protected Color getSelectedCellForegroundColorNoFocus(
								ViewerCell cell) {
							return  null;
					//cell.getControl().getDisplay().getSystemColor(
//									SWT.COLOR_WIDGET_FOREGROUND);
						}
						
						@Override
						protected Color getSelectedCellBackgroundColor(ViewerCell cell) {
							return  null;
					//cell.getControl().getDisplay().getSystemColor(
//									SWT.COLOR_LIST_SELECTION);
						}
						
						@Override
						protected Color getSelectedCellForegroundColor(ViewerCell cell) {
							return  null;
					//cell.getControl().getDisplay().getSystemColor(
//									SWT.COLOR_LIST_SELECTION);
						}

					};
		
		TableViewerFocusCellManager focusCellManager = new TableViewerFocusCellManager(tableViewer,
				highlighter
				,naviStrat);
		
		TableViewerEditor.create(tableViewer, focusCellManager, actSupport, 
				ColumnViewerEditor.TABBING_HORIZONTAL
				| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
				| ColumnViewerEditor.TABBING_VERTICAL 
				| ColumnViewerEditor.KEYBOARD_ACTIVATION);
//		tableViewer.setColumnViewerEditor(new TableColumnViewer(tableViewer, actSupport, 
//				ColumnViewerEditor.TABBING_HORIZONTAL
//				| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
//				| ColumnViewerEditor.TABBING_VERTICAL 
//				| ColumnViewerEditor.KEYBOARD_ACTIVATION));
		tableViewer.getColumnViewerEditor().addEditorActivationListener(new ColumnViewerEditorActivationListener() {
			@Override
			public void afterEditorActivated(
					ColumnViewerEditorActivationEvent event) {
			}
			@Override
			public void afterEditorDeactivated(
					ColumnViewerEditorDeactivationEvent event) {}
			@Override
			public void beforeEditorActivated(
					ColumnViewerEditorActivationEvent event) {
				ViewerCell cell = (ViewerCell) event.getSource();
				tableViewer.getTable().showColumn(tableViewer.getTable().getColumn(cell.getColumnIndex()));
			}

			@Override
			public void beforeEditorDeactivated(
					ColumnViewerEditorDeactivationEvent event) {}
		});
		
	}
	
}

class EmptyHighLighter extends FocusCellHighlighter
{
  private ViewerCell oldCell;

  public EmptyHighLighter(ColumnViewer viewer)
  {
    super(viewer);
    hookListener(viewer);
  }

  private void markFocusedCell(Event event, ViewerCell cell)
  {
  }

  protected boolean onlyTextHighlighting(ViewerCell cell)
  {
    return false;
  }

  private void removeSelectionInformation(Event event, ViewerCell cell)
  {
  }

  private void hookListener(ColumnViewer viewer)
  {
    Listener listener = new Listener()
    {
      @Override
	public void handleEvent(Event event) {
        if ((event.detail & 0x2) > 0) {
          ViewerCell focusCell = EmptyHighLighter.this.getFocusCell();
          if (focusCell == null) {
            return;
          }

          ViewerRow row = focusCell.getViewerRow();
          Assert.isNotNull(row, 
            "Internal structure invalid. Item without associated row is not possible.");
          ViewerCell cell = row.getCell(event.index);
          if ((focusCell == null) || (!cell.equals(focusCell))) {
			EmptyHighLighter.this.removeSelectionInformation(event, cell);
		} else {
			EmptyHighLighter.this.markFocusedCell(event, cell);
		}
        }
      }
    };
    viewer.getControl().addListener(40, listener);
  }

  @Override
protected void focusCellChanged(ViewerCell cell)
  {
    if (cell != null) {
      Rectangle rect = cell.getBounds();
      int x = cell.getColumnIndex() == 0 ? 0 : rect.x;
      int width = cell.getColumnIndex() == 0 ? rect.x + rect.width : 
        rect.width;
      cell.getControl().redraw(x, rect.y, width, rect.height, true);
    }

    if (this.oldCell != null) {
      Rectangle rect = this.oldCell.getBounds();
      int x = this.oldCell.getColumnIndex() == 0 ? 0 : rect.x;
      int width = this.oldCell.getColumnIndex() == 0 ? rect.x + rect.width : 
        rect.width;
      this.oldCell.getControl().redraw(x, rect.y, width, rect.height, true);
    }

    this.oldCell = cell;
  }
}