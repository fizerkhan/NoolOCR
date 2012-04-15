/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.ui.console;

import edu.panimalar.ocr.ui.OCRUIPlugin;

public class OCRLogger {

	/**
	 * Log message in OCR console
	 * @param msg	message to be print in the console
	 */
	public static void logMessage(String msg) {
		OCRUIPlugin.getDefault().getOcrConsole().logMessage(msg);
	}
	
	/**
	 * Log error in OCR console
	 * @param err	error to be print in the console
	 */
	public static void logError(String err) {
		OCRUIPlugin.getDefault().getOcrConsole().logError(err);
	}
	
	
	/**
	 * Clear OCR console
	 */
	public static void clear() {
		OCRUIPlugin.getDefault().getOcrConsole().clearConsole();
	}
	
	/**
	 * Print stack strace
	 * @param e exception
	 */
	public static void logException(Exception e) {
		e.printStackTrace();
	}
}
