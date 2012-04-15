/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.tamiocr.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import edu.panimalar.ocr.tamilocr.TamilOCRConstants;
import edu.panimalar.ocr.tamilocr.TamilOCRPlugin;

public class PreferencePage extends org.eclipse.jface.preference.PreferencePage
		implements IWorkbenchPreferencePage {

	private Label tamilOCRPathLabel;
	private Text tamilOCRPathText;
	private Button tamilOCRPathBrowse;

	public PreferencePage() {
	}

	public PreferencePage(String title) {
		super(title);
	}

	public PreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite group = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		group.setLayout(layout);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		group.setLayoutData(data);

		// tamil ocr path label
		tamilOCRPathLabel = new Label(group, SWT.NONE);
		tamilOCRPathLabel.setText("Tamil OCR exe path:");

		// tamil ocr path text
		tamilOCRPathText = new Text(group, SWT.SINGLE | SWT.BORDER);
		tamilOCRPathText.setFont(group.getFont());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		tamilOCRPathText.setLayoutData(gd);
		tamilOCRPathText.setEditable(false);

		// tamil ocr path browse
		tamilOCRPathBrowse = new Button(group, SWT.NULL);
		tamilOCRPathBrowse.setText("Browse");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 1;
		tamilOCRPathBrowse.setLayoutData(gd);
		tamilOCRPathBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(getShell(), SWT.OPEN
						| SWT.SINGLE);
				fd.setFilterExtensions(new String[] { "*.exe", "*.EXE" });
				String path = fd.open();
				if (path == null) { // cancel
					return;
				}
				tamilOCRPathText.setText(path);
			}
		});

		initControls();
		return group;
	}

	private void initControls() {
		// Initialize the text
		IPreferenceStore prefStore = TamilOCRPlugin.getDefault()
				.getPreferenceStore();
		tamilOCRPathText.setText(prefStore
				.getString(TamilOCRConstants.TAMILOCR_EXE_PATH_PREF));
	}

	@Override
	public boolean performOk() {
		// Save the tamil ocr path
		String path = tamilOCRPathBrowse.getText().trim();
		if (path.length() > 0) {
			IPreferenceStore prefStore = TamilOCRPlugin.getDefault()
					.getPreferenceStore();
			prefStore.setValue(TamilOCRConstants.TAMILOCR_EXE_PATH_PREF,
					tamilOCRPathText.getText());
		}
		return true;
	}
}
