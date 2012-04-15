/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.ui;

import org.eclipse.osgi.util.NLS;

public class OCRFrameworkMessages extends NLS {
	// It should load message dynamically

	// New OCR project
	public static final String WizardNewProjectCreationPage_nameLabel = "Project Name:";
	public static final String WizardNewProjectCreationPage_languageLabel = "Language:";
	public static final String WizardNewProjectCreationPage_addButtonLabel = "Add";
	public static final String WizardNewProjectCreationPage_addButtonToolTip = "Browse and add image files to the list";
	public static final String WizardNewProjectCreationPage_removeButtonLabel = "Remove";
	public static final String WizardNewProjectCreationPage_removeButtonToolTips = "Remove image files from list";

	public static final String WizardNewProjectCreationPage_defaultProjectName = "OCR Project";
	public static final String WizardNewProjectCreationPage_projectNameEmpty = "Project name must be specified";
	public static final String WizardNewProjectCreationPage_projectExistsMessage = "A project with that name already exists in the workspace.";
	public static final String WizardNewProjectImageFilesPage_emptyListMessage = "There is no images. Please add images.";

	// Import images wizard
	public static final String WizardImportImagesProjectCreationPage_browse = "Browse...";
	public static final String WizardImportImagesProjectCreationPage_projectSelectionLabel = "Project Selection";
	public static final String WizardImportImagesProjectCreationPage_projectSelectionToolTip = "Select the project:";
	public static final String WizardImportImagesProjectCreationPage_noProjectMsg = "The project does not exists.";

	public static final String ReadImages_selectOCR = "Select OCR:";
	
}
