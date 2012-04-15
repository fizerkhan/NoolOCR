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

public class OCRBuilderException extends Exception {

	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;

	public OCRBuilderException() {
	}

	public OCRBuilderException(String arg0) {
		super(arg0);
	}

	public OCRBuilderException(Throwable arg0) {
		super(arg0);
	}

	public OCRBuilderException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
