/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.ui.dialogs;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import edu.panimalar.ocr.ui.OCRFrameworkMessages;

public class OCRSelectionDialog extends TitleAreaDialog {

	private Combo ocrCombo;
	private String ocrNames[];
	private String selectedOCRName;

	public OCRSelectionDialog(Shell parentShell, String[] ocrNames) {
		super(parentShell);
		this.ocrNames = ocrNames;
	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		setTitle("Select OCR");
		setMessage("Select OCR by which the image will be read.",
				IMessageProvider.INFORMATION);
		return contents;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// select ocr label
		Label label = new Label(composite, SWT.NONE);
		label.setText(OCRFrameworkMessages.ReadImages_selectOCR);
		label.setFont(parent.getFont());

		// Create combo box
		ocrCombo = new Combo(composite, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		ocrCombo.setLayoutData(data);
		ocrCombo.setFont(parent.getFont());
		ocrCombo.setItems(ocrNames);
		ocrCombo.select(0);
		selectedOCRName = ocrNames[0];
		ocrCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedOCRName = ocrCombo.getItem(ocrCombo.getSelectionIndex());

			}
		});

		return composite;

	}

	/**
	 * Get selected OCR
	 * 
	 * @return ocr
	 */
	public String getOCR() {
		return selectedOCRName;
	}

}
