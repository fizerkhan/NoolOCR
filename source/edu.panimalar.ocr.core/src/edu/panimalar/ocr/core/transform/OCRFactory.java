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

import edu.panimalar.ocr.core.internel.transform.OCRBuilderType;
import edu.panimalar.ocr.core.internel.transform.OCRFactoryImpl;

abstract public class OCRFactory {

	private static OCRFactory instance;

	protected OCRFactory() {
	}

	public static OCRFactory newInstance() {

		// FIXME we should use some FactoryFinder to find the implementation class
		// instead of using directly
		if (instance == null) {
			instance = new OCRFactoryImpl();
		}
		return instance;
	}

	abstract public OCRBuilder newOCRBuilder();
	abstract public OCRBuilder newOCRBuilder(OCRBuilderType type);
}
