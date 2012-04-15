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

import edu.panimalar.ocr.core.document.pdf.PDFGenerator;

public class DocumentFactory {

	public static DocumentFactory instance;

	private DocumentFactory() {
	}

	public static DocumentFactory newInstance() {
		if (instance == null) {
			instance = new DocumentFactory();
		}
		return instance;
	}

	/**
	 * Get Document Generator based on the document type
	 * 
	 * @param type
	 *            document type
	 * @return document generator
	 */
	public DocumentGenerator newDocumentGenerator(DocumentType type) {
		DocumentGenerator documentGenerator = null;
		if (type == DocumentType.PDF) {
			documentGenerator = new PDFGenerator();
		}
		return documentGenerator;
	}
	
}
