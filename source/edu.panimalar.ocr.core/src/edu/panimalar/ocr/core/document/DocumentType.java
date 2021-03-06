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

public enum DocumentType {

	PDF("pdf");

	private String type;

	private DocumentType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
