/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.utils.project;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.BackingStoreException;

import edu.panimalar.ocr.core.OCRConstants;
import edu.panimalar.ocr.core.OCRNature;

public class ProjectUtils {

	/**
	 * Get language for the OCR project
	 * 
	 * @param project
	 *            OCR project
	 * @return language
	 */
	public static final String getLanguage(IProject project) {
		// Get language for project
		ProjectScope projectScope = new ProjectScope(project);
		IEclipsePreferences node = projectScope.getNode(OCRNature.NATURE_ID);
		String language = node.get(OCRConstants.OCR_PREFERNCES_LANGUAGE,
				OCRConstants.OCR_PREFERNCES_LANGUAGE_DEFAULT);
		return language;
	}

	/**
	 * Set language to the project
	 * 
	 * @param project
	 *            OCR Project
	 * @param language
	 *            language
	 */
	public static final void setLanguage(IProject project, String language) {

		// Set language in project scope
		IScopeContext context = new ProjectScope(project);
		IEclipsePreferences node = context.getNode(OCRNature.NATURE_ID);
		String languageKey = OCRConstants.OCR_PREFERNCES_LANGUAGE;
		node.put(languageKey, language);

		// save the changes
		if (node != null) {
			// save the project specific values
			try {
				node.flush();
			} catch (BackingStoreException e) {
			}
		}
	}
}
