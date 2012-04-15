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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;

abstract public class OCRBuilder {

	protected String language;

	protected OCRBuilder() {
	}

	/**
	 * Get Image file extension
	 * 
	 * @param imageFileName
	 *            image file name
	 * @return file extension
	 */
	public String getFileExtension(String imageFileName) {
		int index = imageFileName.lastIndexOf(".");
		String imageExt = null;
		if (index != -1 && index + 1 < imageFileName.length()) {
			imageExt = imageFileName.substring(index + 1,
					imageFileName.length());
		}
		return imageExt;
	}

	/**
	 * Format output file name
	 * 
	 * @param imageFileName
	 *            image file name
	 * @param extension
	 *            output file extension
	 * @return output file name
	 */
	public String getOutputFileName(String imageFileName, String extension) {
		// Format output file name
		int index = imageFileName.lastIndexOf(".");
		String outputFileName = imageFileName;
		if (index != -1) {
			outputFileName = imageFileName.substring(0, index);
			String imageExt = imageFileName.substring(index + 1,
					imageFileName.length());
			outputFileName += "-" + imageExt;
		}

		if (extension != null && extension.trim().length() > 0) {
			outputFileName += "." + extension;
		}

		return outputFileName;
	}

	/**
	 * Read image file
	 * 
	 * @param imageFile
	 *            Image file to be read
	 * @param outputFolder
	 *            Output folder in which output file to be placed
	 * @throws IOException
	 * @throws OCRBuilderException
	 */
	public void build(File imageFile, File outputFolder,
			IProgressMonitor monitor) throws IOException, OCRBuilderException {
		if (imageFile == null) {
			throw new IllegalArgumentException();
		}

		if (!imageFile.exists()) {
			throw new FileNotFoundException("The file '" + imageFile.getName()
					+ "' does not exist.");
		}
		transform(imageFile, outputFolder, monitor);
	}

	/**
	 * Read list of image files
	 * 
	 * @param imageFiles
	 *            Image files to be read
	 * @param outputFolder
	 *            Output folder in which output file to be placed
	 * @throws IOException
	 * @throws OCRBuilderException
	 */
	public void build(File[] imageFiles, File outputFolder,
			IProgressMonitor monitor) throws IOException, OCRBuilderException {
		if (imageFiles == null) {
			throw new IllegalArgumentException();
		}

		for (int i = 0; i < imageFiles.length; i++) {
			if (monitor.isCanceled()) {
				return;
			}
			try {
				build(imageFiles[i], outputFolder, new SubProgressMonitor(
						monitor, 1));
			} catch (OCRBuilderReadException e) {
				logError(e.getMessage());
			}
			monitor.worked(1);
		}
	}

	/**
	 * Get language
	 * 
	 * @return language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Set language
	 * 
	 * @param language
	 *            language in which OCR reads
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Log error
	 * 
	 * @param error
	 */
	public void logError(String error) {
		System.err.println(error);
	}

	/**
	 * Log info messages
	 * 
	 * @param message
	 */
	public void logMessage(String message) {
		System.out.println(message);
	}

	abstract public void transform(File imageFile, File outputFolder,
			IProgressMonitor monitor) throws IOException, OCRBuilderException;

}
