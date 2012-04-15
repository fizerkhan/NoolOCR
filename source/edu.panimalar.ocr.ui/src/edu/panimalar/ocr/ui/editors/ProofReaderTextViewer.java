/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.ui.editors;

import org.eclipse.jface.text.TextViewer;
import org.eclipse.ui.editors.text.TextEditor;

public class ProofReaderTextViewer extends TextEditor {

	protected ProofReaderTextViewer() {
		super();
		
		// make sure we inherit all the text editing commands (delete line etc).
		setKeyBindingScopes(new String[] { "org.eclipse.ui.textEditorScope" });  //$NON-NLS-1$
	
	}

	public TextViewer getSourceTextViewer() {
		return (TextViewer) getSourceViewer();
	}
}
