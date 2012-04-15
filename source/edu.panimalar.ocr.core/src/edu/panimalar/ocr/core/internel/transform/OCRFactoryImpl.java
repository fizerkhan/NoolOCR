/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.core.internel.transform;

import edu.panimalar.ocr.core.transform.OCRBuilder;
import edu.panimalar.ocr.core.transform.OCRFactory;

public class OCRFactoryImpl extends OCRFactory {

	@Override
	public OCRBuilder newOCRBuilder() {
		return new OCRBasicBuilderImpl();
	}

	@Override
	public OCRBuilder newOCRBuilder(OCRBuilderType type) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		if (type.equals(OCRBuilderType.ADVANCED)) {
			//TODO handle advanced builder 
			return null;
		}

		return new OCRBasicBuilderImpl();
	}
}
