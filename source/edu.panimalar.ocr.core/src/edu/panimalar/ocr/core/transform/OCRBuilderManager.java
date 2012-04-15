/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.core.transform;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class OCRBuilderManager {

	public static final String OCR_BUILDER_SCHEMA_ID = "edu.panimalar.ocr.core.ocrbuilder";
	public static final String CLASS_ATTR = "Class";
	public static final String NAME_ATTR = "Name";
	public static final String IMAGE_TYPES_ATTR = "ImageTypes";
	public static final String LANGUAGE = "language";

	private static OCRBuilderManager instance = null;

	private OCRBuilderManager() {
	}

	public static OCRBuilderManager newInstance() {
		if (instance == null) {
			instance = new OCRBuilderManager();
		}
		return instance;
	}

	/**
	 * Get available language
	 * 
	 * @return array of language
	 */
	public String[] getLanguages() {
		Set<String> languages = new HashSet<String>();
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(OCR_BUILDER_SCHEMA_ID);
		for (IConfigurationElement e : config) {
			/*
			 * final Object ocrName = e.getAttribute(NAME_ATTR); final Object
			 * supportedImageTypes = e .getAttribute(IMAGE_TYPES_ATTR);
			 */
			IConfigurationElement[] children = e.getChildren(LANGUAGE);
			for (int i = 0; i < children.length; i++) {
				String lang = children[i].getAttribute(NAME_ATTR);
				languages.add(lang);
			}
		}
		
		String[] names = new String[languages.size()];
		return languages.toArray(names);
	}

	/**
	 * Get OCR builder for particular language
	 * 
	 * @param language
	 *            language for which builders are available
	 * @return array of OCR builder names
	 */
	public String[] getOCRBuilderForLanguage(String language) {
		Set<String> builders = new HashSet<String>();
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(OCR_BUILDER_SCHEMA_ID);
		for (IConfigurationElement e : config) {
			String ocrName = e.getAttribute(NAME_ATTR);
			IConfigurationElement[] children = e.getChildren(LANGUAGE);
			for (int i = 0; i < children.length; i++) {
				String lang = children[i].getAttribute(NAME_ATTR);
				if (lang.equalsIgnoreCase(language)) {
					builders.add(ocrName);
					break;
				}
			}
		}
		
		String[] names = new String[builders.size()];
		return builders.toArray(names);
	}

	/**
	 * Get OCR builder by name
	 * 
	 * @param name
	 *            name of the OCR builder
	 * @return OCR builder
	 */
	public OCRBuilder getOCRBuilder(String name) {

		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(OCR_BUILDER_SCHEMA_ID);

		for (IConfigurationElement e : config) {
			// Get OCR class 
			Object ocrContribution;
			try {
				ocrContribution = e.createExecutableExtension(CLASS_ATTR);
			} catch (CoreException e1) {
				continue;
			}
			
			// Get OCR name
			final Object ocrName = e.getAttribute(NAME_ATTR);

			if (name instanceof String
					&& ((String) ocrName).equalsIgnoreCase(name)) {
				if (ocrContribution instanceof OCRBuilder) {
					return (OCRBuilder) ocrContribution;
				}
			}

			// TODO get set of object
			/*
			 * if (ocrContribution instanceof OCRBuilder) { ISafeRunnable
			 * runnable = new ISafeRunnable() {
			 * 
			 * @Override public void handleException(Throwable exception) {
			 * System.out.println("Exception in client"); }
			 * 
			 * @Override public void run() throws Exception { //((OCRBuilder)
			 * o).greet(); } }; SafeRunner.run(runnable); }
			 */
		}
		return null;
	}

}
