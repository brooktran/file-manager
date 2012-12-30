package org.jeelee.ui.preference;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.jeelee.core.JeeleeMessages;
import org.jeelee.utils.SharedResources;

public abstract class SetAsDefaultListEditor extends ListEditor{
	private final  String DefaultString = SharedResources.getResources().getString(JeeleeMessages.DEFAULT);
	
	private  Button defaultButton;
	private String defaultPreferenceName;
	
	protected SetAsDefaultListEditor(String name, String labelText, String defaultPreferenceName, Composite parent) {
        init(name, labelText);
        this.defaultPreferenceName = defaultPreferenceName;
        createControl(parent);
    }
	
	
	@Override
	public Composite getButtonBoxControl(Composite parent) {
		Composite buttonBox = super.getButtonBoxControl(parent);
		 
        defaultButton = new Button(buttonBox, SWT.PUSH);
        defaultButton.setText(
				SharedResources.getResources()
				.getString(JeeleeMessages.PREFERENCE_SET_AS_DEFAULT));
        defaultButton.setFont(parent.getFont());
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        int widthHint = convertHorizontalDLUsToPixels(defaultButton,
                IDialogConstants.BUTTON_WIDTH);
        data.widthHint = Math.max(widthHint, defaultButton.computeSize(SWT.DEFAULT,
                SWT.DEFAULT, true).x);
        defaultButton.setLayoutData(data);
        defaultButton.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(SelectionEvent event) {
                Widget widget = event.widget;
                if (widget == defaultButton) {
                    setDefaultPressed();
                } 
            }
			private void setDefaultPressed() {
				setDefaultValue();
			}
        });
		return buttonBox;
	}
	
	@Override
	protected void selectionChanged() {
		if(defaultButton == null){
			return;
		}
		super.selectionChanged();
		int index = getList().getSelectionIndex();
		defaultButton.setEnabled(index >= 0);
	}
	
	@Override
	protected void doStore() {
		String[] items = getList().getItems();
		for(int i=0;i<items.length;i++){
			removeDefaultString(items, i);
		}
		String s = createList(items);
		if (s != null) {
			getPreferenceStore().setValue(getPreferenceName(), s);
		}
	}
	protected void setDefaultValue(String value){
		if(value != null){
			getPreferenceStore().setValue(defaultPreferenceName,value);
		}
	}

	protected void setDefaultValue() {
		int index = getList().getSelectionIndex();
		if(index<0){
			return;
		}
		
		String[] items = getList().getItems();
		for(int i=0;i<items.length;i++){
			removeDefaultString(items, i);
		}
		
		setDefaultValue(items[index]);
		items[index] += DefaultString;
		getList().setItems(items);
		
	}


	private void removeDefaultString(String[] items, int i) {
		if(items[i].endsWith(DefaultString)){
			items[i] = items[i].substring(0, items[i].length()-DefaultString.length());
		}
	}

	
}
