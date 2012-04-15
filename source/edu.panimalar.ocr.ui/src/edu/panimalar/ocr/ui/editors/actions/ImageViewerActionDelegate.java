/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.ui.editors.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import uky.article.imageviewer.views.SWTImageCanvas;
import edu.panimalar.ocr.ui.editors.ImageViewer;
import edu.panimalar.ocr.ui.editors.ProofReaderEditor;

/**
 * Editor action delegate for all toolbar push-buttons.
 * <p>
 * 
 * @author Chengdong Li: cli4@uky.edu
 * @author Mohamed Fizer Khan
 */
public class ImageViewerActionDelegate implements IEditorActionDelegate {

	/** Action id of this delegate */
	public String id;
	private IEditorPart editor;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		String id = action.getId();
		
		// Get image canvas
		SWTImageCanvas imageCanvas;
		if (editor instanceof ProofReaderEditor) {
			imageCanvas = ((ProofReaderEditor) editor).getImageCanvas();
		} else if (editor instanceof ImageViewer) {
			imageCanvas = ((ImageViewer) editor).getImageCanvas();
		} else {
			return;
		}

		// Perform editor action based id
		if (imageCanvas.getSourceImage() == null)
			return;
		if (id.equals("toolbar.zoomin")) {
			imageCanvas.zoomIn();
			return;
		} else if (id.equals("toolbar.zoomout")) {
			imageCanvas.zoomOut();
			return;
		} else if (id.equals("toolbar.fit")) {
			imageCanvas.fitCanvas();
			return;
		} else if (id.equals("toolbar.rotate")) {
			/* rotate image anti-clockwise */
			ImageData src = imageCanvas.getImageData();
			if (src == null)
				return;
			PaletteData srcPal = src.palette;
			PaletteData destPal;
			ImageData dest;
			/* construct a new ImageData */
			if (srcPal.isDirect) {
				destPal = new PaletteData(srcPal.redMask, srcPal.greenMask,
						srcPal.blueMask);
			} else {
				destPal = new PaletteData(srcPal.getRGBs());
			}
			dest = new ImageData(src.height, src.width, src.depth, destPal);
			/* rotate by rearranging the pixels */
			for (int i = 0; i < src.width; i++) {
				for (int j = 0; j < src.height; j++) {
					int pixel = src.getPixel(i, j);
					dest.setPixel(j, src.width - 1 - i, pixel);
				}
			}
			imageCanvas.setImageData(dest);
			return;
		} else if (id.equals("toolbar.original")) {
			imageCanvas.showOriginal();
			return;
		}
	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.editor = targetEditor;
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
