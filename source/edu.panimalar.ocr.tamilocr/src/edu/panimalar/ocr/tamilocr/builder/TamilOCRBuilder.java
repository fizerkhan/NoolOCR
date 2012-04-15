/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.tamilocr.builder;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.console.MessageConsole;

import edu.panimalar.ocr.core.OCRConstants;
import edu.panimalar.ocr.core.transform.OCRBuilder;
import edu.panimalar.ocr.core.transform.OCRBuilderException;
import edu.panimalar.ocr.core.transform.OCRBuilderReadException;
import edu.panimalar.ocr.tamilocr.TamilOCRConstants;
import edu.panimalar.ocr.ui.OCRUIPlugin;
import edu.panimalar.ocr.ui.console.OCRLogger;
import edu.panimalar.ocr.utils.file.FileUtil;
import edu.panimalar.ocr.utils.runtime.CommandLauncher;
import edu.panimalar.ocr.utils.runtime.PathFinder;

public class TamilOCRBuilder extends OCRBuilder {

	public TamilOCRBuilder() {
	}

	@Override
	public void transform(File imageFile, File outputFolder,
			IProgressMonitor monitor) throws IOException, OCRBuilderException {

		String msg = "Reading image '" + imageFile.getName() + "'...";
		OCRLogger.logMessage(msg);
		monitor.setTaskName(msg);

		// Get tamil ocr executable path
		String ocrInstallPath = PathFinder.getOCRInstallationPath();
		String tamilOCRPath = ocrInstallPath
				+ TamilOCRConstants.TAMILOCR_EXE_PATH;
		if (!(new File(tamilOCRPath).exists())) {
			throw new OCRBuilderException(
					"Tamil OCR executable does not exist.");
		}

		// Get temp directory
		String tamilOCRTempPath = ocrInstallPath
				+ TamilOCRConstants.TAMILOCR_TEMP_PATH;
		File tempDir = new File(tamilOCRTempPath);
		if (!tempDir.exists()) {
			tempDir.mkdir();
		}

		// Get OCR Message Console
		MessageConsole messageConsole = OCRUIPlugin.getDefault()
				.getOcrConsole().getMessageConsole();

		// Tamil OCR command array
		final String[] tamilOCRCommandArray = new String[2];
		tamilOCRCommandArray[0] = tamilOCRPath;
		tamilOCRCommandArray[1] = imageFile.getCanonicalPath();

		try {
			// Executing Tamil OCR
			CommandLauncher.launch(tamilOCRCommandArray, null, tempDir,
					messageConsole, null);

			// Move output file to the project output folder
			// Get generated output file name
			String generatedOutFile = tamilOCRTempPath
					+ TamilOCRConstants.TAMILOCR_OUTPUT_FILENAME;
			// Get output file path
			String outputFileName = getOutputFileName(imageFile.getName(),
					OCRConstants.TEXT_FILE_EXTENSION);
			String outputFilePath = outputFolder.getCanonicalPath()
					+ File.separator + outputFileName;
			FileUtil.copyFile(new File(generatedOutFile), new File(
					outputFilePath));

		} catch (Exception e) {
			if (e == null || e.getMessage() == null
					|| e.getMessage().trim().length() == 0) {
				throw new OCRBuilderReadException(
						"Reading of the image is failed.");
			} else {
				throw new OCRBuilderReadException(e);
			}
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
