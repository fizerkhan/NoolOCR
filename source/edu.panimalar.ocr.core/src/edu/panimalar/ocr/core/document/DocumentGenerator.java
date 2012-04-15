/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.core.document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;

abstract public class DocumentGenerator {

	/**
	 * Generate document with the given file content
	 * 
	 * @param file
	 *            input file
	 * @param outputFileName
	 *            output file name
	 * @throws FileNotFoundException
	 */
	public void generateDocument(File file, File outputFileName,
			IProgressMonitor monitor) throws FileNotFoundException,
			DocumentGenerateException {
		if (file == null) {
			throw new IllegalArgumentException();
		}
		FileReader fileIS = null;
		FileOutputStream fileOS = null;
		try {
			fileOS = new FileOutputStream(outputFileName);
			fileIS = new FileReader(file);
			BufferedReader fileBuffReader = new BufferedReader(fileIS);
			generate(fileBuffReader, fileOS, monitor);
		} finally {
			if (fileIS != null) {
				try {
					fileIS.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (fileOS != null) {
				try {
					fileOS.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Generate document with given files
	 * 
	 * @param files
	 *            array list of files
	 * @param outputFileName
	 *            output file name
	 * @param monitor
	 * @throws FileNotFoundException
	 * @throws DocumentGenerateException
	 */
	public void generateDocument(File[] files, File outputFileName,
			IProgressMonitor monitor) throws FileNotFoundException,
			DocumentGenerateException {
		if (files == null) {
			throw new IllegalArgumentException();
		}

		for (int i = 0; i < files.length; i++) {
			generateDocument(files[i], outputFileName, monitor);
		}
	}

	public abstract void generate(BufferedReader buffReader,
			FileOutputStream fileOutputStream, IProgressMonitor monitor)
			throws DocumentGenerateException;
}
