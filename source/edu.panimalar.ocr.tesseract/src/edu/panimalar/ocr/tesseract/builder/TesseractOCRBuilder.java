/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.tesseract.builder;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.console.MessageConsole;

import edu.panimalar.ocr.core.transform.OCRBuilder;
import edu.panimalar.ocr.core.transform.OCRBuilderException;
import edu.panimalar.ocr.core.transform.OCRBuilderReadException;
import edu.panimalar.ocr.tesseract.TesseractOCRConstants;
import edu.panimalar.ocr.tesseract.TesseractOCRPlugin;
import edu.panimalar.ocr.ui.OCRUIPlugin;
import edu.panimalar.ocr.ui.console.OCRLogger;
import edu.panimalar.ocr.utils.runtime.CommandLauncher;

public class TesseractOCRBuilder extends OCRBuilder {

	public TesseractOCRBuilder() {
	}

	@Override
	public void transform(File imageFile, File outputFolder,
			IProgressMonitor monitor) throws IOException, OCRBuilderException {

		String msg = "Reading image '" + imageFile.getName() + "'...";
		OCRLogger.logMessage(msg);
		monitor.setTaskName(msg);

		// Get Tesseract executable path
		IPreferenceStore prefStore = TesseractOCRPlugin.getDefault()
				.getPreferenceStore();
		String tesseractOCRPath = prefStore
				.getString(TesseractOCRConstants.TESSERACT_EXE_PATH_PREF);
		if (!(new File(tesseractOCRPath).exists())) {
			throw new OCRBuilderException(
					"Tesseract executable does not exist.");
		}

		// Get OCR Message Console
		MessageConsole messageConsole = OCRUIPlugin.getDefault()
				.getOcrConsole().getMessageConsole();

		// Get image extension
		String fileName = imageFile.getName();

		// Get output file name
		String outputFileName = getOutputFileName(fileName, null);

		// Tesseract command array
		final String[] tesseractCommandArray = new String[5];
		tesseractCommandArray[0] = tesseractOCRPath;
		tesseractCommandArray[1] = imageFile.getCanonicalPath();
		tesseractCommandArray[2] = outputFolder.getCanonicalPath()
				+ File.separator + outputFileName;
		tesseractCommandArray[3] = TesseractOCRConstants.LANGUAGE_OPTION;
		tesseractCommandArray[4] = language;

		// Executing Tesseract OCR
		try {
			CommandLauncher.launch(tesseractCommandArray, messageConsole, null);
		} catch (Exception e) {
			throw new OCRBuilderReadException(e);
		} finally {
			monitor.done();
		}
	}

	@Override
	public void logError(String error) {
		OCRLogger.logError(error);
	}

	@Override
	public void logMessage(String message) {
		OCRLogger.logMessage(message);
	}
}
