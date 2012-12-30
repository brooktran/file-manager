package org.jeelee.filemanager.ui.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.JeeleeFileSystem;
import org.jeelee.filemanager.core.filters.FileFilterDelegate;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.filemanager.ui.views.model.FilterViewer;
import org.jeelee.utils.PluginResources;

public class ListFileDialog extends TitleAreaDialog {
	private PluginResources r = FileManagerActivator.RESOURCES;

	private FileFilterDelegate fileFilter;

	private String targetString;

	private String prefixString;

	private String subFolderPrefixString;

	private boolean isCopyToClipboard;

	private boolean isDisplayInConsole;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param fileFilter
	 */
	public ListFileDialog(Shell parentShell, FileFilterDelegate fileFilter) {
		super(parentShell);
		setShellStyle(SWT.SHELL_TRIM | SWT.BORDER);
		this.fileFilter = fileFilter;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(r.getString(Messages.LIST_FOLDER_AS_TEXT));
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		FileDelegate source = fileFilter.getSource();
		setTitle(r
				.getFormatted(Messages.LIST_FOLDER,
						source.getChildren().size() == 1 ? source.getChild(0)
								: source.getChildren().size()));

		Composite area = (Composite) super.createDialogArea(parent);
//		Composite container = new Composite(area, SWT.NONE);
//		container.setLayout(new GridLayout(1, false));
//		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		final ScrolledComposite container = new ScrolledComposite(area,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		container.setExpandHorizontal(true);
		container.setExpandVertical(true);
		
		
		
		

		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayout(new GridLayout(3, false));
		
		
		final Button btnCopytocopyboard = new Button(composite_1, SWT.CHECK);
		btnCopytocopyboard.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnCopytocopyboard.setSelection(true);
		btnCopytocopyboard.setText(r.getString(Messages.COPY_TO_CLIPBOARD));
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		
		
		final Button btnDisplayInConsole = new Button(composite_1, SWT.CHECK);
		btnDisplayInConsole.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnDisplayInConsole.setSelection(true);
		btnDisplayInConsole.setText(r.getString(Messages.DISPLAY_IN_CONSOLE));
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);

		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel
				.setToolTipText(r.getString(Messages.LEFT_EMPTY_IF_NOT_NEED));
		lblNewLabel.setText(r.getString(Messages.SAVE_AS_FILE));

		// ControlDecoration controlDecoration = new
		// ControlDecoration(lblNewLabel, SWT.RIGHT | SWT.TOP);
		// controlDecoration.setImage(SWTResourceManager.getImage(ListFileDialog.class,
		// "/org/eclipse/jface/fieldassist/images/contassist_ovr.gif"));
		// controlDecoration.setShowOnlyOnFocus(true);
		// controlDecoration.setDescriptionText("Some description");

		final Text targetText = new Text(composite_1, SWT.BORDER);
		targetText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		targetText.setToolTipText(r.getString(Messages.LEFT_EMPTY_IF_NOT_NEED));

		Button btnBrowse = new Button(composite_1, SWT.NONE);
		btnBrowse.setText(r.getString(Messages.BROWSE));
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
				String filename = dialog.open();
				if (filename != null) {
					targetText.setText(filename);
				}
			}
		});

		Label lblDirectorypreffix = new Label(composite_1, SWT.NONE);
		lblDirectorypreffix.setText(r.getString(Messages.FOLDER_PREFIX));

		final Text prefixText = new Text(composite_1, SWT.BORDER);
		prefixText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		prefixText.setText("+");

		prefixString = prefixText.getText();
		prefixText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				prefixString = prefixText.getText();
			}
		});
		new Label(composite_1, SWT.NONE);

		Label lblFolderPreffix = new Label(composite_1, SWT.NONE);
		lblFolderPreffix.setText(r.getString(Messages.SUB_FOLDER_PREFIX));

		final Text subFolderPrefix = new Text(composite_1, SWT.BORDER);
		subFolderPrefix.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		subFolderPrefix.setText("\t");

		subFolderPrefixString = subFolderPrefix.getText();
		subFolderPrefix.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				subFolderPrefixString = subFolderPrefix.getText();
			}
		});
		new Label(composite_1, SWT.NONE);

		// Combo combo = new Combo(container, SWT.NONE);
		// combo.setItems(new String[] {"LineSeparator", "tab"});
		// combo.select(1);

		Group grpFilter = new Group(composite_1, SWT.NONE);
		grpFilter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		grpFilter.setLayout(new GridLayout(1, false));
		grpFilter.setText(r.getString(Messages.FILTER));
		
		FilterViewer filterViewer = new FilterViewer(grpFilter,JeeleeFileSystem.getGlobalFilter());
		Composite composite=filterViewer.getFilterView();
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		
		
		

		targetString = targetText.getText();
		targetText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				targetString = targetText.getText();
			}
		});
		btnDisplayInConsole.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isDisplayInConsole = btnDisplayInConsole.getSelection();
			}
		});
		isDisplayInConsole = btnDisplayInConsole.getSelection();
		btnCopytocopyboard.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isCopyToClipboard = btnCopytocopyboard.getSelection();
			}
		});
		isCopyToClipboard = btnCopytocopyboard.getSelection();
		container.setContent(composite_1);
		container.setMinSize(composite_1.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		// new Label(container, SWT.NONE);
		// new Label(container, SWT.NONE);
		//
		// Button btnDefaultSetting = new Button(container, SWT.NONE);
		// btnDefaultSetting.setText("default setting");

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(463, 571);
	}

	public String getFolderPrefix() {
		return prefixString;
	}

	public String getTarget() {
		return targetString;
	}

	public String getSubFolderPrefix() {
		return subFolderPrefixString;
	}

	public boolean isCopyToClipboard() {
		return isCopyToClipboard;
	}

	public boolean isDisplayInConsole() {
		return isDisplayInConsole;
	}
}
