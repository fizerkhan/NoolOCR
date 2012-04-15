/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.gocr.builder;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.console.MessageConsole;

import edu.panimalar.ocr.core.OCRConstants;
import edu.panimalar.ocr.core.transform.OCRBuilder;
import edu.panimalar.ocr.core.transform.OCRBuilderException;
import edu.panimalar.ocr.core.transform.OCRBuilderReadException;
import edu.panimalar.ocr.gocr.GOCRConstants;
import edu.panimalar.ocr.ui.OCRUIPlugin;
import edu.panimalar.ocr.ui.console.OCRLogger;
import edu.panimalar.ocr.utils.runtime.CommandLauncher;
import edu.panimalar.ocr.utils.runtime.PathFinder;

public class GOCRBuilder extends OCRBuilder {

	String[] supportedImageFormats = new String[] { "pnm", "pgm", "pbm", "ppm",
			"pcx", "jpeg", "jpg" };

	public GOCRBuilder() {
	}

	// TODO  Need to do localization

	@Override
	public void transform(File imageFile, File outputFolder,
			IProgressMonitor monitor) throws IOException, OCRBuilderException {

		boolean isJPGFile = false;
		String msg = "Reading image '" + imageFile.getName() + "'...";
		OCRLogger.logMessage(msg);
		monitor.setTaskName(msg);

		// Get GOCR executable path
		String ocrInstallPath = PathFinder.getOCRInstallationPath();
		String gocrPath = ocrInstallPath + GOCRConstants.GOCR_EXE_PATH;
		if (!(new File(gocrPath).exists())) {
			throw new OCRBuilderException("GOCR executable does not exist.");
		}

		// Get OCR Message Console
		MessageConsole messageConsole = OCRUIPlugin.getDefault()
				.getOcrConsole().getMessageConsole();

		// Get image extension
		String fileName = imageFile.getName();
		String ext = getFileExtension(fileName);

		// Get output file name
		String outputFileName = getOutputFileName(fileName, OCRConstants.TEXT_FILE_EXTENSION);

		// Check extension with supported images type
		int i;
		for (i = 0; i < supportedImageFormats.length; i++) {
			if (ext.equalsIgnoreCase(supportedImageFormats[i])) {
				break;
			}
		}
		if (i == supportedImageFormats.length) {
			throw new OCRBuilderReadException("The image with extension '"
					+ ext + "' is unsupported.");
		}

		// If image file is JPEG, then convert image into Portable AnyMap Format
		if (ext.equalsIgnoreCase(GOCRConstants.JPEG_EXTENSION)
				|| ext.equalsIgnoreCase(GOCRConstants.JPG_EXTENSION)) {
			isJPGFile = true;
			String djpegPath = ocrInstallPath + GOCRConstants.DJEPG_EXE_PATH;
			// Check DJpeg executable
			if (!(new File(djpegPath).exists())) {
				throw new OCRBuilderException(
						"DJpeg executable does not exist.");
			}

			// Convert jpeg into pnm
			final String[] djpegCmdArray = new String[5];
			djpegCmdArray[0] = djpegPath;
			djpegCmdArray[1] = GOCRConstants.DJPEG_PNM_OPTION;
			djpegCmdArray[2] = GOCRConstants.DJPEG_GRAY_OPTION;
			djpegCmdArray[3] = imageFile.getCanonicalPath();
			fileName = fileName.replace(".", "-") + "."
					+ GOCRConstants.PNM_EXTENSION;
			djpegCmdArray[4] = imageFile.getParentFile().getCanonicalPath()
					+ File.separator + fileName;
			try {
				CommandLauncher.launch(djpegCmdArray, messageConsole, null);
			} catch (Exception e) {
				throw new OCRBuilderReadException(e);
			}

			// After conversion, change image file to converted file
			imageFile = new File(djpegCmdArray[4]);
		}

		// GOCR command array
		final String[] gocrCommandArray = new String[7];
		gocrCommandArray[0] = gocrPath;
		gocrCommandArray[1] = GOCRConstants.OPERATION_MODE;
		gocrCommandArray[2] = String
				.valueOf(GOCRConstants.OPERATION_MODE_DEFAULT_VALUE);
		gocrCommandArray[3] = GOCRConstants.INPUT_FILE_OPTION;
		gocrCommandArray[4] = imageFile.getCanonicalPath();
		gocrCommandArray[5] = GOCRConstants.OUTPUT_FILE_OPTION;
		gocrCommandArray[6] = outputFolder.getCanonicalPath() + File.separator
				+ outputFileName;

		// Executing GOCR
		try {
			CommandLauncher.launch(gocrCommandArray, messageConsole, null);
		} catch (Exception e) {
			throw new OCRBuilderReadException(e);
		} finally {
			if (isJPGFile && imageFile.exists()) {
				imageFile.delete();
			}
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
