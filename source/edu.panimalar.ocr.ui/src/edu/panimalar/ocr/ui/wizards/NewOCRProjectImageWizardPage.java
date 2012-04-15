/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.ui.wizards;

import java.io.File;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import edu.panimalar.ocr.ui.OCRFrameworkMessages;

public class NewOCRProjectImageWizardPage extends WizardPage {

	private List list;

	protected NewOCRProjectImageWizardPage(String pageName) {
		super(pageName);
		setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {

		final Shell shell = parent.getShell();

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Create list
		list = new List(composite, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER
				| SWT.MULTI);
		GridData data = new GridData(GridData.FILL_BOTH);
		list.setLayoutData(data);
		list.setFont(parent.getFont());

		// Add/Remove button specification group
		Composite buttonGroup = new Composite(composite, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		buttonGroup.setLayout(gridLayout);
		buttonGroup.setLayoutData(new GridData());

		// Create add button
		Button addButton = new Button(buttonGroup, SWT.NONE);
		addButton
				.setText(OCRFrameworkMessages.WizardNewProjectCreationPage_addButtonLabel);
		addButton
				.setToolTipText(OCRFrameworkMessages.WizardNewProjectCreationPage_addButtonToolTip);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
				fileDialog.open();

				// Get selected file names in the file dialog and add it to list
				StringBuffer buf = null;
				String[] fileNames = fileDialog.getFileNames();
				for (int i = 0; i < fileNames.length; i++) {
					buf = new StringBuffer();
					// Get file path
					buf.append(fileDialog.getFilterPath());
					if (buf.charAt(buf.length() - 1) != File.separatorChar) {
						buf.append(File.separatorChar);
					}
					buf.append(fileNames[i]);

					// Add file path to the list
					String filePath = buf.toString();
					if (filePath != null && filePath.trim().length() != 0) {
						if (list.indexOf(filePath) == -1) {
							list.add(filePath);
						}
					}
				}
				validatePage();
			}
		});

		// Create remove button
		Button removeButton = new Button(buttonGroup, SWT.NONE);
		removeButton
				.setText(OCRFrameworkMessages.WizardNewProjectCreationPage_removeButtonLabel);
		removeButton
				.setToolTipText(OCRFrameworkMessages.WizardNewProjectCreationPage_removeButtonToolTips);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Remove selected items in the list
				String[] selections = list.getSelection();
				for (int i = 0; i < selections.length; i++) {
					list.remove(selections[i]);
				}

				// Select last items
				if (list.getItemCount() > 0) {
					list.setSelection(list.getItemCount() - 1);
				}
				validatePage();
			}
		});

		setControl(composite);
		validatePage();

	}

	/**
	 * Get list of image files
	 * 
	 * @return images
	 */
	public String[] getImageFiles() {
		return list.getItems();
	}

	/**
	 * Returns whether this page's controls currently all contain valid values.
	 * 
	 * @return <code>true</code> if all controls are valid, and
	 *         <code>false</code> if at least one is invalid
	 */
	protected boolean validatePage() {
		int itemCount = list.getItemCount();
		if (itemCount == 0) {
			setErrorMessage(OCRFrameworkMessages.WizardNewProjectImageFilesPage_emptyListMessage);
			setPageComplete(false);
			return false;
		}

		setErrorMessage(null);
		setMessage(null);
		setPageComplete(true);
		return true;
	}

}
