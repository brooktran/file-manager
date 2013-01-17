package org.jeelee.filemanager.ui.views.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.progress.UIJob;
import org.jeelee.filemanager.core.AcceptableCounter;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.JeeleeFileSystem;
import org.jeelee.filemanager.core.filters.CatalogFactory;
import org.jeelee.filemanager.core.filters.FileFilterDelegate;
import org.jeelee.filemanager.core.filters.FileSizeFilter;
import org.jeelee.filemanager.core.filters.KeyWordFilter;
import org.jeelee.filemanager.core.filters.ScopeCatalog;
import org.jeelee.filemanager.core.filters.SuffixCatalog;
import org.jeelee.filemanager.core.filters.SuffixFilter;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.utils.DefaultPair;
import org.jeelee.utils.JobRunner;
import org.jeelee.utils.PluginResources;

import swing2swt.layout.FlowLayout;

public class FilterViewer {
	public static final int			KEY_WORD			= 1 << 1;
	public static final int			COMMON_ATTRIBUTES	= 1 << 2;
	public static final int			ADVANCED_ATTRIBUTES	= 1 << 3;
	public static final int			ALL					= KEY_WORD
																| COMMON_ATTRIBUTES
																| ADVANCED_ATTRIBUTES;

	private PropertyChangeListener	listener	= 
			new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName().equals(
							FileFilterDelegate.RESULT_CHANGED_PROPERTY)) {
						resetStatisticsArea();
					}
				}
			};

	private PluginResources			r					= FileManagerActivator.RESOURCES;
	private FileFilterDelegate		fileFilter;
	private Composite				parent;
	private Composite				viewerComposite;
	private Text					searchText;
	private Table					table;
	private Group					grpSuffixes;
	private Map<String, Button>		suffixButtons;
	private Map<String, Button>		catalogButtons;
	private Composite				container_1;

	private List<Button> sizeButtons;
	private Composite suffixComposite;
	private Composite emptySuffixComposite;
	
	
	/**
	 * @wbp.parser.constructor
	 */
	public FilterViewer(Composite parent) {
		this(parent, JeeleeFileSystem.getGlobalFilter(), SWT.NONE, ALL);
	}

	public FilterViewer(Composite parent, FileFilterDelegate fileFilter) {
		this(parent, fileFilter, SWT.NONE, ALL);
	}

	public FilterViewer(Composite parent, final FileFilterDelegate fileFilter,
			int compositeStyle, int filterStyle) {
		this.parent = parent;
		this.fileFilter = fileFilter;
		viewerComposite = createFilterView(compositeStyle,
				checkStyle(filterStyle));

		fileFilter.removePropertyChangeListener(listener);
		fileFilter.addPropertyChangeListener(listener);

		parent.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				fileFilter.removePropertyChangeListener(listener);
			}
		});
	}

	private int checkStyle(int filterStyle) {
		return filterStyle & (ALL);
	}

	public FileFilterDelegate getFileFilter() {
		return fileFilter;
	}

	public Composite createFilterView(int style, int filterStyle) {
		int columnNum = 3;
		// parent.setLayout(new FillLayout());
		final ScrolledComposite scrolledComposite = new ScrolledComposite(
				parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		container_1 = new Composite(scrolledComposite, SWT.NONE);
		container_1.setLayout(new GridLayout(1, false));

//		createSearchArea(columnNum, container_1);
//		createStatisticsArea(columnNum, container_1);
		createAdvancedFilterArea(columnNum, container_1);

		scrolledComposite.setContent(container_1);
		scrolledComposite.setMinWidth(0);
		// scrolledComposite.setSize(container.computeSize(SWT.DEFAULT,
				// SWT.DEFAULT));
		
		container_1.addControlListener(new ControlAdapter() {
			int	width	= -1;

			@Override
			public void controlResized(ControlEvent e) {
				int newWidth = container_1.getSize().x;
				if (newWidth != width) {
					scrolledComposite.setMinHeight(container_1.computeSize(
							newWidth, SWT.DEFAULT).y);
					width = newWidth;
				}
				// Rectangle r = container.getClientArea();
				// scrolledComposite.setMinSize(container.computeSize(r.width,
				// SWT.DEFAULT));
			};
		});
		scrolledComposite.setSize(new Point(329, 312));
		return scrolledComposite;
	}

	private void createAdvancedFilterArea(int columnNum,
			final Composite container) {
		// if( (filterStyle & ADVANCED_ATTRIBUTES) == ADVANCED_ATTRIBUTES){ //
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		GridData gd_tabFolder = new GridData(SWT.FILL, SWT.FILL, true, true,
				columnNum, 1);
		gd_tabFolder.widthHint = 716;
		tabFolder.setLayoutData(gd_tabFolder);

		createBasicFilterView(tabFolder);
		createSuffixFilterView(tabFolder);
		createSizeFilterView(tabFolder);
		creteDateFilterView(tabFolder);
		createAdvancedFilterView(tabFolder);
		createOtherFilterView(tabFolder);
		// } //
	}

	private void createOtherFilterView(TabFolder tabFolder) {
		TabItem tbtmOthers = new TabItem(tabFolder, SWT.NONE);
		tbtmOthers.setText("others");

		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmOthers.setControl(composite_1);
		composite_1.setLayout(new RowLayout(SWT.HORIZONTAL));

		ToolBar toolBar = new ToolBar(composite_1, SWT.FLAT | SWT.WRAP
				| SWT.RIGHT);

		ToolItem tltmImport = new ToolItem(toolBar, SWT.NONE);
		tltmImport.setText("import");

		ToolItem tltmExport = new ToolItem(toolBar, SWT.NONE);
		tltmExport.setText("export");

		ToolItem tltmSetAsDefault = new ToolItem(toolBar, SWT.NONE);
		tltmSetAsDefault.setText("set as default");
	}

	private void createAdvancedFilterView(TabFolder tabFolder) {
		TabItem tbtmArrtibutes = new TabItem(tabFolder, SWT.NONE);
		r.configTabItem(tbtmArrtibutes, Messages.ADVANCED_ATTRIBUTES);

		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmArrtibutes.setControl(composite_3);
		composite_3.setLayout(new FillLayout(SWT.HORIZONTAL));

		TableViewer tableViewer = new TableViewer(composite_3, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.MULTI);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnName = tableViewerColumn.getColumn();
		tblclmnName.setWidth(200);
		tblclmnName.setText("name");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnValue = tableViewerColumn_1.getColumn();
		tblclmnValue.setWidth(400);
		tblclmnValue.setText("value");
	}

	private void createSizeFilterView(TabFolder tabFolder) {
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		r.configTabItem(tbtmNewItem, Messages.SIZE);

		Composite composite_9 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(composite_9);
		composite_9.setLayout(new GridLayout(1, false));

		Composite buttonsComposite = new Composite(composite_9, SWT.NONE);
		buttonsComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		buttonsComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		createSizeCatalogButtons(buttonsComposite);
		

		Group grpCustom = new Group(composite_9, SWT.NONE);
		grpCustom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));
		grpCustom.setText("custom");
		grpCustom.setLayout(new GridLayout(4, false));

		Spinner spinner_1 = new Spinner(grpCustom, SWT.BORDER);
		spinner_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				3, 1));

		Combo combo = new Combo(grpCustom, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
				1));
		combo.setItems(new String[] { "B", "K", "M", "G", "T" });
		combo.select(2);

		Spinner spinner_2 = new Spinner(grpCustom, SWT.BORDER);
		spinner_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 3, 1));

		Combo combo_1 = new Combo(grpCustom, SWT.READ_ONLY);
		combo_1.setItems(new String[] { "B", "K", "M", "G", "T" });
		combo_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));
		combo_1.select(2);

		Composite composite = new Composite(composite_9, SWT.NONE);
		composite.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		Button btnAdd = new Button(composite, SWT.NONE);
		btnAdd.setText("add");

		Button button = new Button(composite, SWT.NONE);
		button.setText("clear");

		Button button_1 = new Button(composite, SWT.NONE);
		button_1.setText("import");

		Button button_2 = new Button(composite, SWT.NONE);
		button_2.setText("export");
	}

	private void createSizeCatalogButtons(Composite parent) {
		Iterator<AcceptableCounter<FileDelegate, FileSizeFilter>> it = fileFilter.getFilteredSizeIterator();
		
		sizeButtons= new ArrayList<>();
		while(it.hasNext()){
			AcceptableCounter<FileDelegate, FileSizeFilter> ac = it.next();
			
			Iterator<ScopeCatalog> scIterator = ac.getFilter().iterator();
			while(scIterator.hasNext()){
				final ScopeCatalog sc = scIterator.next();
				Button button = new Button(parent, SWT.CHECK);
				button.setText(r.getString(sc.getId())+"("+ac.getCount()+")");
				button.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						fileFilter.getFileSizeFilter().addScopeCatalog(sc);
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});
				button.setSelection(fileFilter.getFileSizeFilter().contains(sc.getScope()));
				sizeButtons.add(button);
			}
		}
	}

	private void creteDateFilterView(TabFolder tabFolder) {
		TabItem tbtmDate = new TabItem(tabFolder, SWT.NONE);
		r.configTabItem(tbtmDate, Messages.MODIFY_DATE);

		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmDate.setControl(composite_2);
		composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite_4 = new Composite(composite_2, SWT.NONE);
		composite_4.setLayout(new GridLayout(3, true));

		Composite composite_5 = new Composite(composite_4, SWT.NONE);
		composite_5.setLayout(new RowLayout(SWT.HORIZONTAL));
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 3, 1));

		Button btnAll_1 = new Button(composite_5, SWT.CHECK);
		btnAll_1.setText("all(1023)");

		Button btnToday = new Button(composite_5, SWT.CHECK);
		btnToday.setText("today(43)");

		Button btnYesterday = new Button(composite_5, SWT.CHECK);
		btnYesterday.setText("yesterday(434)");

		Button btnDonotDisplayAny = new Button(composite_5, SWT.CHECK);
		btnDonotDisplayAny.setText("donot display any 0 items");

		Group grpAdd = new Group(composite_4, SWT.NONE);
		grpAdd.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
		grpAdd.setText("add");
		grpAdd.setLayout(new GridLayout(3, false));

		Label lblFrom = new Label(grpAdd, SWT.NONE);
		lblFrom.setText("from:");

		DateTime dateTime_1 = new DateTime(grpAdd, SWT.DROP_DOWN | SWT.LONG);

		final DateTime dateTime = new DateTime(grpAdd, SWT.TIME | SWT.SHORT);
		dateTime.setEnabled(false);

		Label lblTo = new Label(grpAdd, SWT.NONE);
		lblTo.setSize(14, 15);
		lblTo.setText("to:");

		DateTime dateTime_2 = new DateTime(grpAdd, SWT.BORDER | SWT.DROP_DOWN
				| SWT.LONG);

		final DateTime dateTime_3 = new DateTime(grpAdd, SWT.BORDER | SWT.TIME
				| SWT.SHORT);
		dateTime_3.setEnabled(false);

		Button btnComfirm = new Button(grpAdd, SWT.NONE);
		btnComfirm.setText("comfirm");

		final Button btnEnableTime = new Button(grpAdd, SWT.CHECK);
		btnEnableTime.setEnabled(false);
		btnEnableTime.setSelection(true);
		btnEnableTime.setText("enable time");
		btnEnableTime.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dateTime.setEnabled(btnEnableTime.getSelection());
				dateTime_3.setEnabled(btnEnableTime.getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				dateTime.setEnabled(btnEnableTime.getSelection());
				dateTime_3.setEnabled(btnEnableTime.getSelection());
			}
		});
		new Label(grpAdd, SWT.NONE);
	}

	private void createSuffixFilterView(TabFolder tabFolder) {
		TabItem tbtmSuffixes = new TabItem(tabFolder, SWT.NONE);// type filter
		r.configTabItem(tbtmSuffixes, Messages.TYPE);

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmSuffixes.setControl(composite);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite_8 = new Composite(composite, SWT.NONE);
		composite_8.setLayout(new GridLayout(6, false));

		Group grpTypeFilters = new Group(composite_8, SWT.NONE);
		grpTypeFilters.setLayout(new RowLayout(SWT.HORIZONTAL));
		grpTypeFilters.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
				false, 6, 1));
		grpTypeFilters.setText("type filter");

		getSuffixCatalogButtons(grpTypeFilters);
		createSuffixButton(composite_8);
		createResetButton(composite_8);
		createInverseButton(composite_8);

		Button btnNewButton = new Button(composite_8, SWT.NONE);
		btnNewButton.setText("custom");

		Button btnExport = new Button(composite_8, SWT.NONE);
		btnExport.setSize(48, 25);
		btnExport.setText("export");

		Button btnNewButton_2 = new Button(composite_8, SWT.NONE);
		btnNewButton_2.setSize(48, 25);
		btnNewButton_2.setText("import");
		new Label(composite_8, SWT.NONE);
	}

	private void createBasicFilterView(TabFolder tabFolder) {
		TabItem basicItem = new TabItem(tabFolder, SWT.NONE); // basic filter
		basicItem.setText(r.getString(Messages.BASIC));

		Composite basicComposite = new Composite(tabFolder, SWT.NONE);
		basicItem.setControl(basicComposite);
		basicComposite.setLayout(new GridLayout(1, false));

		searchText = new Text(basicComposite, SWT.BORDER | SWT.SEARCH);
		searchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		searchText.setText(fileFilter.getKeyword());
		searchText.setMessage(r.getString(Messages.SEARCH));
		searchText.forceFocus();

		searchText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				String keyword = searchText.getText();

				if (keyword.isEmpty()) {
					fileFilter.setKeyword(keyword);
					return;
				}

				if (e.keyCode == SWT.CR) {
					fileFilter.setKeyword(keyword);
				}
			}
		});
		
		Composite composite_10_1 = new Composite(basicComposite, SWT.NONE);
		composite_10_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		composite_10_1.setLayout(new RowLayout(SWT.HORIZONTAL));

		final Button searchSubFolder = new Button(composite_10_1, SWT.CHECK);
		searchSubFolder.setText(r.getString(Messages.SEARCH_SUB_FOLDERS));
		searchSubFolder.setSelection(fileFilter.isIncludeSubFolders());

		final Button btnSearchContent = new Button(composite_10_1, SWT.CHECK);
		btnSearchContent.setText(r.getString(Messages.INCLUDE_FILE_CONTENT));
		btnSearchContent.setSelection(fileFilter.isSearchFileContent());

		final Button btnSearchArchive = new Button(composite_10_1, SWT.CHECK);
		btnSearchArchive.setText(r.getString(Messages.SEARCH_ARCHIVE));
		btnSearchArchive.setSelection(fileFilter.isSearchArchives());

		final Button btnCaseSensitive = new Button(composite_10_1, SWT.CHECK);
		btnCaseSensitive.setText(r.getString(Messages.CASE_SENSITIVE));
		btnCaseSensitive.setSelection(fileFilter.isCaseSensitive());
		final Button btnWholeWord = new Button(composite_10_1, SWT.CHECK);
		btnWholeWord.setText(r.getString(Messages.WHOLE_WORD));
		btnWholeWord.setSelection(fileFilter.isWholeWord());

		final Button btnUseRegex = new Button(composite_10_1, SWT.CHECK);
		btnUseRegex.setText(r.getString(Messages.USE_REGEX));
		btnUseRegex.setSelection(fileFilter.isUseRegex());

		final Button btnRegexGenerator = new Button(composite_10_1, SWT.NONE);
		btnRegexGenerator.setEnabled(false);
		btnRegexGenerator.setText(r.getString(Messages.REGEX_GENERATOR));

		Composite composite_11 = new Composite(basicComposite, SWT.NONE);
		RowLayout rl_composite_11 = new RowLayout(SWT.HORIZONTAL);
		rl_composite_11.fill = true;
		composite_11.setLayout(rl_composite_11);

		Group grpPosition = new Group(composite_11, SWT.NONE);
		grpPosition.setLayout(new FillLayout());
		grpPosition.setText(r.getString(Messages.POSITIOIN));

		final ComboViewer comPostion = new ComboViewer(grpPosition,
				SWT.READ_ONLY);
		comPostion.setContentProvider(ArrayContentProvider.getInstance());
		comPostion.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof SimpleValue) {
					SimpleValue position = (SimpleValue) element;
					return position.getDisplayName();
				}
				return super.getText(element);
			}
		});
		SimpleValue[] positions=new SimpleValue[]{
				new SimpleValue(KeyWordFilter.ANY_POSITION, r.getString(Messages.ANY_POSITION)),
				new SimpleValue(KeyWordFilter.IN_FRONT, r.getString(Messages.IN_FRONT)),
				new SimpleValue(KeyWordFilter.IN_MIDDLE, r.getString(Messages.IN_MIDDLE)),
				new SimpleValue(KeyWordFilter.IN_TAIL, r.getString(Messages.IN_TAIL))
		};
		comPostion.setInput(positions);
		comPostion.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				fileFilter.setPosition(((SimpleValue) selection
						.getFirstElement()).getValue());
			}
		});

		Group grpTimes = new Group(composite_11, SWT.NONE);
		// grpPosition.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
		// false,columnNum, 1));
		grpTimes.setText(r.getString(Messages.OCCUR_TIMES));
		grpTimes.setLayout(new RowLayout(SWT.HORIZONTAL));

		final ComboViewer comCompare = new ComboViewer(grpTimes, SWT.READ_ONLY);
		comCompare.setContentProvider(ArrayContentProvider.getInstance());
		comCompare.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof SimpleValue) {
					SimpleValue position = (SimpleValue) element;
					return position.getDisplayName();
				}
				return super.getText(element);
			}
		});
		SimpleValue[] compareValues=new SimpleValue[]{
				new SimpleValue(KeyWordFilter.ANY_TIMES, r.getString(Messages.ANY_TIMES)),
				new SimpleValue(KeyWordFilter.EQUAL, r.getString(Messages.EQUAL)),
				new SimpleValue(KeyWordFilter.GREATER_THAN, r.getString(Messages.GREATER_THAN)),
				new SimpleValue(KeyWordFilter.LESS_THAN, r.getString(Messages.LESS_THAN)),
				new SimpleValue(KeyWordFilter.NOT_EQUAL, r.getString(Messages.NOT_EQUAL))
		};
		comCompare.setInput(compareValues);
		comCompare.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				fileFilter.setOccurTimesOperator(((SimpleValue) selection
						.getFirstElement()).getValue());
			}
		});

		final Spinner spinner = new Spinner(grpTimes, SWT.BORDER);
		spinner.setTextLimit(30);
		spinner.setMaximum(10000);
		spinner.setMinimum(0);
		spinner.setSelection(fileFilter.getOccurTimes());
		// if( (filterStyle & COMMON_ATTRIBUTES) == COMMON_ATTRIBUTES){ //
		Group grpCommon = new Group(basicComposite, SWT.NONE);
		grpCommon.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		RowLayout rl_grpCommon = new RowLayout(SWT.HORIZONTAL);
		rl_grpCommon.fill = true;
		grpCommon.setLayout(rl_grpCommon);
		grpCommon.setText(r.getString(Messages.COMMON_ATTRIBUTES));

		final Button btnDisplayFolder = new Button(grpCommon, SWT.CHECK);
		btnDisplayFolder.setText(r.getString(Messages.DISPLAY_FOLDER));
		btnDisplayFolder.setSelection(fileFilter.isDisplayFolder());
		btnDisplayFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileFilter.setDisplayFolder(btnDisplayFolder.getSelection());
			}
		});

		final Button btnFiles = new Button(grpCommon, SWT.CHECK);
		btnFiles.setText(r.getString(Messages.DISPLAY_FILE));
		btnFiles.setSelection(fileFilter.isDisplayFile());
		btnFiles.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileFilter.setDisplayFile(btnFiles.getSelection());
			}
		});

		final Button btnCheckButton_2 = new Button(grpCommon, SWT.CHECK);
		btnCheckButton_2.setText(r.getString(Messages.DISPLAY_READ_ONLY_FILES));
		btnCheckButton_2.setSelection(fileFilter.isIncludeReadOnly());
		btnCheckButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileFilter.setIncludeReadOnly(btnCheckButton_2.getSelection());
			}
		});

		final Button btnHiden = new Button(grpCommon, SWT.CHECK);
		btnHiden.setText(r.getString(Messages.DISPLAY_HIDEN_FILES));
		btnHiden.setSelection(fileFilter.isIncludeHiden());
		btnHiden.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileFilter.setIncludeHiden(btnHiden.getSelection());
			}
		});

		final Button btnSystem = new Button(grpCommon, SWT.CHECK);
		btnSystem.setText(r.getString(Messages.DISPLAY_SYSTEM_FILES));
		btnSystem.setSelection(fileFilter.isIncludeSystemFile());
		btnSystem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileFilter.setIncludeSystemFile(btnSystem.getSelection());
			}
		});
		spinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileFilter.setOccurTimes(spinner.getSelection());
			}
		});
		btnUseRegex.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileFilter.setUseRegex(btnUseRegex.getSelection());
				btnRegexGenerator.setEnabled(btnUseRegex.getSelection());
			}
		});
		btnWholeWord.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileFilter.setWholeWord(btnWholeWord.getSelection());
			}
		});
		btnCaseSensitive.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileFilter.setCaseSensitive(btnCaseSensitive.getSelection());
			}
		});
		btnSearchArchive.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileFilter.setSearchArchives(btnSearchArchive.getSelection());
			}
		});
		btnSearchContent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileFilter.setSearchFileContent(btnSearchContent.getSelection());
			}
		});
		searchSubFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileFilter.setIncludeSubFolders(searchSubFolder.getSelection());
			}
		});
	}

	private void createInverseButton(Composite parent) {
		Button btnInverse = new Button(parent, SWT.NONE);
		btnInverse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileFilter.getSuffixFilter().reset();

				Iterator<Entry<String, Button>> it = suffixButtons.entrySet()
						.iterator();
				while (it.hasNext()) {
					Entry<String, Button> en = it.next();
					Button btn = en.getValue();
					btn.setSelection(!btn.getSelection());
					updateSuffixButton(en.getKey(), btn);
				}

			}
		});
		btnInverse.setText(r.getString(Messages.INVERSE));
	}

	private void createResetButton(Composite composite_8) {
		Button resetButton = new Button(composite_8, SWT.NONE);
		resetButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileFilter.getSuffixFilter().reset();
				clearSelection(catalogButtons.entrySet().iterator());
				clearSelection(suffixButtons.entrySet().iterator());
			}

			private void clearSelection(Iterator<Entry<String, Button>> iterator) {
				while (iterator.hasNext()) {
					Entry<String, Button> en = iterator.next();
					en.getValue().setSelection(false);
					updateSuffixButtonBackground(en.getKey(), en.getValue());
				}
			}

			private void updateSuffixButtonBackground(String key, Button button) {
				boolean selection = button.getSelection();
				button.setBackground(selection ? parent.getDisplay()
						.getSystemColor(SWT.COLOR_LIST_SELECTION) : parent
						.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
			}
		});
		resetButton.setSize(48, 25);
		resetButton.setText(r.getString(Messages.RESET));
	}

	private void createSuffixButton(Composite parent) {
		grpSuffixes = new Group(parent, SWT.NONE);
		grpSuffixes.setLayout(new GridLayout(1, false));
		grpSuffixes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				6, 1));
		grpSuffixes.setText(r.getString(Messages.SUFFIX));
		
		suffixComposite = new Composite(grpSuffixes, SWT.NONE);
		suffixComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		RowLayout rl_suffixComposite = new RowLayout(SWT.HORIZONTAL);
		suffixComposite.setLayout(rl_suffixComposite);
		
		Label label = new Label(grpSuffixes, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		emptySuffixComposite = new Composite(grpSuffixes, SWT.NONE);
		emptySuffixComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		suffixButtons = new HashMap<String, Button>();

		createSuffixButtons();
	}
	
	
	public static void createTypeFilterView(Composite parent2) {//XXX for test
		Button button = new Button(parent2, SWT.NONE);
		button.setText("sdf");
	}
	private void getSuffixCatalogButtons(Group grpTypeFilters) {
		SuffixCatalog[] catalogs = CatalogFactory.getDefaultTypeCatalog();
		catalogButtons = new HashMap<>();
		SuffixFilter filter = fileFilter.getSuffixFilter();

		for (final SuffixCatalog catalog : catalogs) {
			final Button catalogButton = new Button(grpTypeFilters, SWT.CHECK);
			r.configButton(catalogButton, catalog.getId());
			catalogButtons.put(catalog.getId(), catalogButton);

			catalogButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					boolean selection = catalogButton.getSelection();
					for (String suffix : catalog.getSuffixes()) {
						if (suffixButtons.containsKey(suffix)) {
							suffixButtons.get(suffix).setSelection(selection);
							updateSuffixButton(suffix,
									suffixButtons.get(suffix));
						}
					}
					if (selection) {
						fileFilter.getSuffixFilter().addCatalog(catalog);
					} else {
						fileFilter.getSuffixFilter().removeCatalog(catalog);
					}

					catalogButton.setBackground(selection ? parent.getDisplay()
							.getSystemColor(SWT.COLOR_LIST_SELECTION) : parent
							.getDisplay().getSystemColor(
									SWT.COLOR_LIST_BACKGROUND));

				}
			});
			catalogButton.setSelection(filter.contains(catalog));
		}
	}
	
	private void createSuffixButtons() {
		Iterator<Entry<String, Integer>> it = fileFilter.getFilteredSuffixesIterator();
		ArrayList<Entry<String, Integer>> entries = new ArrayList<>();
		while (it.hasNext()) {
			entries.add(it.next());
		}
		// add selected items
		Iterator<String> selectedSuffixes=fileFilter.getSuffixFilter().iterator();
		while(selectedSuffixes.hasNext()){
			String suffix = selectedSuffixes.next();
			Entry<String, Integer> entry  = null;
			
			for(Entry<String, Integer> en:entries){
				if(suffix.equals(en.getKey())){
					entry = en;
					break;
				}
			}
			if(entry == null){
				entries.add(new DefaultPair<String,Integer>(suffix, 0));
			}
		}
		// sort
		Collections.sort(entries, new Comparator<Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				int i = o1.getValue();
				int j = o2.getValue();
				return i < j ? 1 : (i == j ? 0 : -1);
			}
		});

		for (final Entry<String, Integer> en : entries) {
			final Button button = new Button(suffixComposite, SWT.CHECK);
			suffixButtons.put(en.getKey(), button);
			button.setText(en.getKey() + "(" + en.getValue() + ")");
			button.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					updateSuffixButton(en.getKey(), button);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					updateSuffixButton(en.getKey(), button);
				}
			});
			Program program = Program.findProgram(en.getKey());//TODO get image from ImageRegistry
			if (program != null) {
				ImageData imageData = program.getImageData();
				if (imageData != null) {
					final Image image = new Image(parent.getDisplay(),
							imageData);
					button.addDisposeListener(new DisposeListener() {
						@Override
						public void widgetDisposed(DisposeEvent e) {
							if (image != null) {
								image.dispose();
							}
						}
					});
					button.setImage(image);
				}
			}
			button.setSelection(fileFilter.getSuffixFilter()
					.select(en.getKey()));
		}
		// grpSuffixes.pack();
		grpSuffixes.layout(true);
		grpSuffixes.getParent().layout(true);
	}

	/**
	 * when the selection state of the suffixes changed this method should be
	 * call to change the background of the buttons and update the state of
	 * FileFilterDelegate.
	 */
	protected void updateSuffixButton(String suffix, Button button) {
		boolean selection = button.getSelection();
		button.setBackground(selection ? parent.getDisplay().getSystemColor(
				SWT.COLOR_LIST_SELECTION) : parent.getDisplay().getSystemColor(
				SWT.COLOR_LIST_BACKGROUND));

		if (selection) {
			fileFilter.getSuffixFilter().addSuffixes(suffix);
		} else {
			fileFilter.getSuffixFilter().removeSuffixes(suffix);
		}
	}

	protected void resetStatisticsArea() {
		JobRunner.runUIJob(new UIJob(r.getString(Messages.FetchingContent)) {
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				for (Control c : suffixComposite.getChildren()) {
					c.dispose();
				}
				suffixButtons.clear();
				createSuffixButtons();		
				return Status.OK_STATUS;
			}
		});
		
	}

	

	public Composite getFilterView() {
		return viewerComposite;
	}

	public void updateKeyword() {
		String keyword = searchText.getText();
		fileFilter.setKeyword(keyword);
	}
}

class SimpleValue {
	private int		value;
	private String	displayName;

	public SimpleValue(int value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}