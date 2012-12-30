package org.jeelee.ui.viewers.cell.editors;

import java.text.MessageFormat;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;

public class NumberCellEditor extends CellEditor {

	private Spinner spinner;
	private int minValue;
	private int maxValue;
	private int initialValue;

	public NumberCellEditor(Composite parent) {
		this(parent, SWT.NONE);
	}
	public NumberCellEditor(Composite parent, int style) {
		this(parent, style, 0, Integer.MAX_VALUE, 0);
	}

	public NumberCellEditor(Composite parent, int style, int minValue,
			int maxValue, int initialValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.initialValue = initialValue;
		
		setStyle(style);
		create(parent);
	}
	

	@Override
	protected Control createControl(Composite parent) {
		spinner = new Spinner(parent, getStyle());
		spinner.setMaximum(maxValue);
		spinner.setMinimum(minValue);
		spinner.setIncrement(1);
		spinner.setSelection(initialValue);
		spinner.addListener(SWT.MouseWheel, new Listener() {
			@Override
			public void handleEvent(Event e) {
				spinner.setSelection(spinner.getSelection() + getIncrement(e));
				e.doit = false;
			}
		});

		spinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				handleDefaultSelection(e);
			}
		});
		spinner.addKeyListener(new KeyAdapter() {
			// hook key pressed - see PR 14201
			@Override
			public void keyPressed(KeyEvent e) {
				keyReleaseOccured(e);

				// as a result of processing the above call, clients may have
				// disposed this cell editor
				if ((getControl() == null) || getControl().isDisposed()) {
					return;
				}
				// checkSelection(); // see explanation below
				// checkDeleteable();
				// checkSelectable();
			}
		});
		spinner.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE
						|| e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
				}
			}
		});

		spinner.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				NumberCellEditor.this.focusLost();
			}
		});
		spinner.setFont(parent.getFont());
		spinner.setBackground(parent.getBackground());
		spinner.addModifyListener(getModifyListener());
		return spinner;
	}

	private ModifyListener modifyListener;

	private ModifyListener getModifyListener() {
		if (modifyListener == null) {
			modifyListener = new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					editOccured(e);
				}
			};
		}
		return modifyListener;
	}

	protected void editOccured(ModifyEvent e) {
		String value = spinner.getText();
		if (value == null) {
			value = "";//$NON-NLS-1$
		}
		Object typedValue = value;
		boolean oldValidState = isValueValid();
		boolean newValidState = isCorrect(typedValue);
		if (!newValidState) {
			// try to insert the current value into the error message.
			setErrorMessage(MessageFormat.format(getErrorMessage(),
					new Object[] { value }));
		}
		valueChanged(oldValidState, newValidState);
	}

	@Override
	protected void keyReleaseOccured(KeyEvent keyEvent) {
		if (keyEvent.character == '\r') { // Return key
			// Enter is handled in handleDefaultSelection.
			// Do not apply the editor value in response to an Enter key event
			// since this can be received from the IME when the intent is -not-
			// to apply the value.
			// See bug 39074 [CellEditors] [DBCS] canna input mode fires bogus
			// event from Text Control
			//
			// An exception is made for Ctrl+Enter for multi-line texts, since
			// a default selection event is not sent in this case.
			if (spinner != null && !spinner.isDisposed()
					&& (spinner.getStyle() & SWT.MULTI) != 0) {
				if ((keyEvent.stateMask & SWT.CTRL) != 0) {
					super.keyReleaseOccured(keyEvent);
				}
			}
			return;
		}
		super.keyReleaseOccured(keyEvent);
	}

	protected void handleDefaultSelection(SelectionEvent e) {
		fireApplyEditorValue();
		deactivate();
	}

	protected int getIncrement(Event e) {
		return e.count / Math.abs(e.count);
	}

	@Override
	protected Object doGetValue() {
		return spinner.getSelection();
	}

	@Override
	protected void doSetFocus() {
		if (spinner != null) {
			spinner.setFocus();
		}

	}

	@Override
	protected void doSetValue(Object value) {
		spinner.setSelection((int) value);
	}

}
