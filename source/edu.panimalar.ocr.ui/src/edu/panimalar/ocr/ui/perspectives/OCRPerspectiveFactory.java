/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.ui.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import edu.panimalar.ocr.ui.OCRUIConstants;
import edu.panimalar.ocr.ui.OCRUIPlugin;
import edu.panimalar.ocr.ui.wizards.NewOCRProjectWizard;

public class OCRPerspectiveFactory implements IPerspectiveFactory {

	public static final String PERSPECTIVE_ID = OCRUIPlugin.PLUGIN_ID
			+ ".perspective.OCRPerspective";
	private static final String BOTTOM = "bottom";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		// Add Project Explorer in left pane
		layout.addView(OCRUIConstants.OCR_NAVIGATOR_VIEW, IPageLayout.LEFT,
				0.25f, layout.getEditorArea());

		// Add Console and Progress view in the bottom
		IFolderLayout bottomLayout = layout.createFolder(BOTTOM,
				IPageLayout.BOTTOM, 0.76f, layout.getEditorArea());
		bottomLayout.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		bottomLayout.addView(IPageLayout.ID_PROGRESS_VIEW);

		// Add Shortcut to ocr project wizard id
		layout.addNewWizardShortcut(NewOCRProjectWizard.WIZARD_ID);
		
		// Add Shortcut to OCR navigator view
		layout.addShowViewShortcut(OCRUIConstants.OCR_NAVIGATOR_VIEW);

	}

}
