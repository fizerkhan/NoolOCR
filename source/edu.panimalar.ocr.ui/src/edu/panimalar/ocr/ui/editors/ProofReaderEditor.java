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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.TextViewerUndoManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import uky.article.imageviewer.views.SWTImageCanvas;
import edu.panimalar.ocr.core.OCRConstants;
import edu.panimalar.ocr.core.OCRNature;

public class ProofReaderEditor extends EditorPart {

	private TextViewer fTextViewer;
	private boolean fDirty = false;
	private boolean loading = false;
	private SWTImageCanvas imageCanvas;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		if (!isDirty()) {
			return;
		}
		String text = fTextViewer.getDocument().get();
		IEditorInput input = getEditorInput();
		if (input instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) input).getFile();
			try {
				file.setContents(new ByteArrayInputStream(text.getBytes()),
						true, false, monitor);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		resetDirty();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return fDirty;
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

		// call init first so that if an exception is thrown, we have created no
		// new widgets
		ProofReaderTextViewer editor;
		try {
			editor = createTextEditor(sashForm);
		} catch (PartInitException e1) {
			return;
		}

		// Create text viewer
		fTextViewer = editor.getSourceTextViewer();
		fTextViewer.addTextListener(new ITextListener() {
			@Override
			public void textChanged(TextEvent event) {
				if (!isLoading()) {
					setDirty();
				}
			}
		});

		// Load Editor input
		try {
			setLoading(true);
			loadEditorInput();
			setLoading(false);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ProofReaderTextViewer createTextEditor(Composite parent)
			throws PartInitException {
		ProofReaderTextViewer editor = new ProofReaderTextViewer();
		editor.init(getEditorSite(), getEditorInput());
		Composite parent2 = new Composite(parent, editor.getOrientation());
		parent2.setLayout(new FillLayout());
		editor.createPartControl(parent2);
		editor.addPropertyListener(new IPropertyListener() {
			public void propertyChanged(Object source, int propertyId) {
				firePropertyChange(propertyId);
			}
		});

		TextViewerUndoManager undoManager = new TextViewerUndoManager(10);
		undoManager.connect(editor.getSourceTextViewer());

		return editor;
	}

	public void setDirty() {
		fDirty = true;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	public void resetDirty() {
		fDirty = false;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	private void loadEditorInput() throws CoreException, IOException {
		IEditorInput input = getEditorInput();
		if (input instanceof IFileEditorInput) {

			// Read file and place the text document in text viewer
			IFile file = ((IFileEditorInput) input).getFile();
			FileReader fr = new FileReader(new File(file.getLocationURI()
					.getPath()));
			StringBuffer buff = new StringBuffer();
			int c;
			try {
				while ((c = fr.read()) != -1) {
					buff.append((char) c);
				}
			} finally {
				if (fr != null) {
					fr.close();
				}
			}
			Document document = new Document();
			document.set(buff.toString());
			fTextViewer.setDocument(document);
			setPartName(file.getName());

			// Check project nature
			IProject project = file.getProject();
			if (!project.hasNature(OCRNature.NATURE_ID)) {
				return;
			}

			// Check file parent folder, parent folder must be output
			if (!file.getParent().getName().equals(OCRConstants.OUTPUT_FOLDER)) {
				return;
			}

			// Check file format as text file
			String ext = file.getFileExtension();
			if (ext == null
					|| !ext.equalsIgnoreCase(OCRConstants.TEXT_FILE_EXTENSION)) {
				return;
			}

			// Format image file from text file
			String fileName = file.getName();
			int index = fileName.lastIndexOf("-");
			int extIndex = fileName.lastIndexOf(".");
			if (index == -1 || extIndex == -1 || index > extIndex) {
				return;
			}
			String imageFileName = fileName.substring(0, index);
			String imageExtension = fileName.substring(index + 1, extIndex);
			imageFileName += "." + imageExtension;

			// Get image file from image folder and place it in the label
			IFolder imagesFolder = project
					.getFolder(OCRConstants.IMAGES_FOLDER);
			IFile imageFile = imagesFolder.getFile(imageFileName);
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
		fTextViewer.getControl().setFocus();
	}

	private void setLoading(boolean loading) {
		this.loading = loading;
	}

	private boolean isLoading() {
		return loading;
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
