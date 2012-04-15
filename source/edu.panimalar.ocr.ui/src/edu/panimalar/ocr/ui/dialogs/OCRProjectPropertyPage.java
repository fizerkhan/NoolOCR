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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import edu.panimalar.ocr.utils.project.ProjectUtils;

public class OCRProjectPropertyPage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public OCRProjectPropertyPage() {
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);

		// Label for language
		Label languageLabel = new Label(composite, SWT.NONE);
		languageLabel.setText("Language: ");
		GridData gd = new GridData();
		gd.verticalAlignment = SWT.TOP;
		languageLabel.setLayoutData(gd);

		// Language value text
		Text languageValueText = new Text(composite, SWT.WRAP | SWT.READ_ONLY);
		IResource resource = (IResource) getElement().getAdapter(
				IResource.class);
		if (resource.getType() == IResource.PROJECT) {
			String language = ProjectUtils.getLanguage(((IProject) resource));
			languageValueText.setText(language);
		}

		return composite;
	}
}
