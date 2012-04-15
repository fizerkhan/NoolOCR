/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.gocr;

import java.io.File;

public class GOCRConstants {

	// GOCR executable relative path
	public static final String GOCR_EXE_PATH = "utils" + File.separator
			+ "gocr" + File.separator + "gocr.exe";
	public static final String DJEPG_EXE_PATH = "utils" + File.separator
			+ "gocr" + File.separator + "djpeg.exe";
	
	// GOCR options
	public static final String INPUT_FILE_OPTION = "-i";
	public static final String OUTPUT_FILE_OPTION = "-o";
	public static final String OPERATION_MODE = "-m";
	public static final String VALUE_OF_CERTAINITY = "-a";
	public static final String THRESHOLD_GRAY_LEVEL = "-l";
	public static final String DUST_SIZE = "-d";
	public static final String SPACE_WIDTH = "-s";
	
	// DJPEG Options
	public static final String DJPEG_PNM_OPTION = "-pnm";
	public static final String DJPEG_GRAY_OPTION = "-gray";
	
	// PNM, JPEG file extension
	public static final String PNM_EXTENSION = "pnm";
	public static final String JPEG_EXTENSION = "jpeg";
	public static final String JPG_EXTENSION = "jpg";
	
	// GOCR options default values
	public static final int OPERATION_MODE_DEFAULT_VALUE = 4; // -m number option
	public static final int VALUE_OF_CERTAINITY_DEFAULT_VALUE = 0; // -a number option
	public static final int THRESHOLD_GRAY_LEVEL_DEFAULT_VALUE = 0; // -l number option
	public static final int DUST_SIZE_DEFAULT_VALUE = -1; // -d number option
	public static final int SPACE_WIDTH_DEFAULT_VALUE = 0; // -s number option


}
