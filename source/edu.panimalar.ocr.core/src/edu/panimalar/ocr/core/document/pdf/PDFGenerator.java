/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.core.document.pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import org.eclipse.core.runtime.IProgressMonitor;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import edu.panimalar.ocr.core.document.DocumentGenerateException;
import edu.panimalar.ocr.core.document.DocumentGenerator;
import edu.panimalar.ocr.core.document.DocumentType;

/**
 * PDF Generator generates pdf file for the given text file
 * 
 * @author fizer
 * 
 */
public class PDFGenerator extends DocumentGenerator {

	private static final DocumentType type = DocumentType.PDF;
	private Rectangle pageSize = PageSize.A4;
	private float marginLeft = 36;
	private float marginRight = 72;
	private float marginTop = 108;
	private float marginBottom = 180;
	private Document document;

	public static DocumentType getType() {
		return type;
	}

	@Override
	public void generate(BufferedReader buffReader,
			FileOutputStream fileOutputStream, IProgressMonitor monitor)
			throws DocumentGenerateException {
		Document document = null;
		String text = null;

		try {
			document = new Document(pageSize, marginLeft, marginRight,
					marginTop, marginBottom);
			PdfWriter.getInstance(document, fileOutputStream);
			document.open();
			while ((text = buffReader.readLine()) != null) {
				document.add(new Paragraph(text));
			}
		} catch (Exception e) {
			throw new DocumentGenerateException(e);
		} finally {
			if (document != null)
				document.close();
		}
	}

	@Override
	public void generateDocument(File[] files, File outputFileName,
			IProgressMonitor monitor) throws DocumentGenerateException {
		if (files == null) {
			throw new IllegalArgumentException();
		}

		// Create file output stream
		FileOutputStream fileOutputStream = null;
		try {
			outputFileName.createNewFile();
			fileOutputStream = new FileOutputStream(outputFileName);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Create pdf document
		document = new Document(pageSize, marginLeft, marginRight, marginTop,
				marginBottom);
		try {
			PdfWriter.getInstance(document, fileOutputStream);
			document.open();

			// Write all files into PDF document
			for (int i = 0; i < files.length; i++) {
				FileReader fileIS = null;
				try {
					fileIS = new FileReader(files[i]);
					BufferedReader fileBuffReader = new BufferedReader(fileIS);
					String text;
					while ((text = fileBuffReader.readLine()) != null) {
						document.add(new Paragraph(text));
					}

					// document.newPage();
				} finally {
					if (fileIS != null) {
						fileIS.close();
					}
				}
			}
		} catch (Exception e) {
			throw new DocumentGenerateException(e.getMessage());
		} finally {
			if (document != null) {
				document.close();
			}
		}
	}

	public Rectangle getPageSize() {
		return pageSize;
	}

	public void setPageSize(Rectangle pageSize) {
		this.pageSize = pageSize;
	}

	public float getMarginLeft() {
		return marginLeft;
	}

	public void setMarginLeft(float marginLeft) {
		this.marginLeft = marginLeft;
	}

	public float getMarginRight() {
		return marginRight;
	}

	public void setMarginRight(float marginRight) {
		this.marginRight = marginRight;
	}

	public float getMarginTop() {
		return marginTop;
	}

	public void setMarginTop(float marginTop) {
		this.marginTop = marginTop;
	}

	public float getMarginBottom() {
		return marginBottom;
	}

	public void setMarginBottom(float marginBottom) {
		this.marginBottom = marginBottom;
	}

}
