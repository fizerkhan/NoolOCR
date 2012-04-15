/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.tamilocr;

import java.io.File;

public class TamilOCRConstants {

	// Tamil OCR relative path
	public static final String TAMILOCR_RELATIVE_PATH = "utils"
			+ File.separator + "tamilocr" + File.separator;

	// Tamil OCR executable relative path
	public static final String TAMILOCR_EXE_PATH = TAMILOCR_RELATIVE_PATH
			+ "TamilOcr.exe";

	// Tamil OCR temp folder relative path
	public static final String TAMILOCR_TEMP_PATH = TAMILOCR_RELATIVE_PATH
			+ "temp" + File.separator;

	// Tamil OCR output file name
	public static final String TAMILOCR_OUTPUT_FILENAME = "recog.txt";

	// Tamil OCR Exe preference ID
	public static final String TAMILOCR_EXE_PATH_PREF = "tamilocr_exe_path";

}
