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

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import uky.article.imageviewer.views.SWTImageCanvas;
import edu.panimalar.ocr.ui.console.OCRLogger;

public class ImageViewer extends EditorPart {

	private SWTImageCanvas imageCanvas;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite,
	 * org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public boolean isSaveOnCloseNeeded() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */

	@Override
	public void createPartControl(Composite parent) {

		// Create composite and sashForm to hold the image and text
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		SashForm sashForm = new SashForm(container, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Image Area
		Composite imageArea = new Composite(sashForm, SWT.BORDER);
		imageArea.setLayout(new FillLayout());
		imageCanvas = new SWTImageCanvas(imageArea);

		// Load Editor input
		try {
			loadEditorInput();
		} catch (Exception e) {
			OCRLogger.logError(e.getMessage());
		}
	}

	private void loadEditorInput() throws CoreException, IOException {
		IEditorInput input = getEditorInput();
		if (input instanceof IFileEditorInput) {

			// Get image file and show it
			IFile imageFile = ((IFileEditorInput) input).getFile();
			setPartName(imageFile.getName());
			if (imageFile != null && imageFile.exists()) {
				imageCanvas.loadImage(((IFile) imageFile).getLocationURI()
						.getPath());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		imageCanvas.setFocus();
	}

	public SWTImageCanvas getImageCanvas() {
		return imageCanvas;
	}

	@Override
	public void dispose() {
		imageCanvas.dispose();
		imageCanvas = null;
		super.dispose();
	}

}
