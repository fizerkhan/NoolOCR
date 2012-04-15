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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import edu.panimalar.ocr.core.transform.OCRBuilderManager;
import edu.panimalar.ocr.ui.OCRFrameworkMessages;

public class NewOCRProjectWizardPage extends WizardPage {

	private static final int SIZING_TEXT_FIELD_WIDTH = 250;
	private Text projectNameField;
	private Combo languageCombo;

	protected NewOCRProjectWizardPage(String pageName) {
		super(pageName);
		setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
		// project specification group
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// new project label
		Label projectLabel = new Label(composite, SWT.NONE);
		projectLabel
				.setText(OCRFrameworkMessages.WizardNewProjectCreationPage_nameLabel);
		projectLabel.setFont(parent.getFont());

		// new project name entry field
		projectNameField = new Text(composite, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		projectNameField.setLayoutData(data);
		projectNameField.setFont(parent.getFont());

		// Set the initial value first before listener
		// to avoid handling an event during the creation.
		projectNameField
				.setText(OCRFrameworkMessages.WizardNewProjectCreationPage_defaultProjectName);
		projectNameField.addListener(SWT.Modify, nameModifyListener);

		// new project label
		Label languageLabel = new Label(composite, SWT.NONE);
		languageLabel
				.setText(OCRFrameworkMessages.WizardNewProjectCreationPage_languageLabel);
		languageLabel.setFont(parent.getFont());

		// new project name entry field
		languageCombo = new Combo(composite, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		languageCombo.setLayoutData(data);
		languageCombo.setFont(parent.getFont());
		languageCombo.setItems(getLanguages());
		languageCombo.select(0);

		setControl(composite);
		validatePage();

	}

	private Listener nameModifyListener = new Listener() {
		public void handleEvent(Event e) {
			boolean valid = validatePage();
			setPageComplete(valid);

		}
	};

	/**
	 * Get available languages
	 * 
	 * @return languages
	 */
	private String[] getLanguages() {
		String[] languages = OCRBuilderManager.newInstance().getLanguages();
		if (languages == null || languages.length == 0) {
			languages = new String[] { "eng" };
		}
		return languages;
	}

	/**
	 * Returns whether this page's controls currently all contain valid values.
	 * 
	 * @return <code>true</code> if all controls are valid, and
	 *         <code>false</code> if at least one is invalid
	 */
	protected boolean validatePage() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		String projectFieldContents = getProjectName();
		if (projectFieldContents.equals("")) { //$NON-NLS-1$
			setErrorMessage(null);
			setMessage(OCRFrameworkMessages.WizardNewProjectCreationPage_projectNameEmpty);
			return false;
		}

		IStatus nameStatus = workspace.validateName(projectFieldContents,
				IResource.PROJECT);
		if (!nameStatus.isOK()) {
			setErrorMessage(nameStatus.getMessage());
			return false;
		}

		IProject handle = getProjectHandle();
		if (handle.exists()) {
			setErrorMessage(OCRFrameworkMessages.WizardNewProjectCreationPage_projectExistsMessage);
			return false;
		}

		setErrorMessage(null);
		setMessage(null);
		setPageComplete(true);
		return true;
	}

	/**
	 * Creates a project resource handle for the current project name field
	 * value. The project handle is created relative to the workspace root.
	 * <p>
	 * This method does not create the project resource; this is the
	 * responsibility of <code>IProject::create</code> invoked by the new
	 * project resource wizard.
	 * </p>
	 * 
	 * @return the new project resource handle
	 */
	public IProject getProjectHandle() {
		return ResourcesPlugin.getWorkspace().getRoot()
				.getProject(getProjectName());
	}

	/**
	 * Returns the value of the project name field with leading and trailing
	 * spaces removed.
	 * 
	 * @return the project name in the field
	 */
	public String getProjectName() {
		if (projectNameField == null) {
			return ""; //$NON-NLS-1$
		}

		return projectNameField.getText().trim();
	}

	/**
	 * Get selected language
	 * 
	 * @return language
	 */
	public String getLanguage() {
		if (languageCombo == null) {
			return "eng";
		}
		return languageCombo.getItem(languageCombo.getSelectionIndex());
	}

}
