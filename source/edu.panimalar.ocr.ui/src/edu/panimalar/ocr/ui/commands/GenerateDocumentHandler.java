/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/
package edu.panimalar.ocr.ui.commands;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.panimalar.ocr.core.OCRConstants;
import edu.panimalar.ocr.core.OCRNature;
import edu.panimalar.ocr.core.document.DocumentFactory;
import edu.panimalar.ocr.core.document.DocumentGenerator;
import edu.panimalar.ocr.core.document.DocumentType;
import edu.panimalar.ocr.ui.console.OCRLogger;
import edu.panimalar.ocr.utils.file.FileUtil;

public class GenerateDocumentHandler extends AbstractHandler {

	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		// Get selection object
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage().getSelection();
		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			Object selElement = strucSelection.getFirstElement();

			// If selection object is IResource, then continue to proceed
			if (selElement instanceof IResource) {
				IResource selResource = ((IResource) selElement);
				IProject project = selResource.getProject();

				// Disable icon for other projects
				try {
					if (!project.hasNature(OCRNature.NATURE_ID)) {
						return null;
					}
				} catch (CoreException e1) {
					e1.printStackTrace();
				}

				// Get document folder and output folder
				IFolder docFolder = project
						.getFolder(OCRConstants.DOCUMENT_FOLDER);
				IFolder outputFolder = project
						.getFolder(OCRConstants.OUTPUT_FOLDER);
				if (!docFolder.exists()) {
					try {
						docFolder.create(true, true, null);
					} catch (CoreException e) {
						return null;
					}
				}

				// Get text files
				File[] textFiles = FileUtil.getMemberFiles(outputFolder
						.getLocation().toFile());
				if (textFiles.length == 0) {
					OCRLogger
							.logMessage("No output files are exist. Please read images!!!");
					return null;
				}

				// TODO Open dialog to select document type based on the
				// Document type and select output file name

				DocumentType type = DocumentType.PDF;
				IFile outputFile = docFolder.getFile(project.getName() + "."
						+ type);

				// Generate document based on the selection type
				DocumentGenerator documentGenerator = DocumentFactory
						.newInstance().newDocumentGenerator(type);
				if (documentGenerator != null) {
					runGenerateDocumentJob(documentGenerator, textFiles,
							outputFile.getLocation().toFile(), project);
				} else {
					OCRLogger
							.logMessage("No Document Generator are available for type "
									+ type);

				}
			}
		}
		return null;
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isHandled() {
		// TODO Auto-generated method stub
		return true;
	}

	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	private void runGenerateDocumentJob(
			final DocumentGenerator documentGenerator, final File[] textFiles,
			final File outputFile, final IProject project) {

		Job job = new Job("Generating document of '" + project.getName()
				+ "'...") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// Read images
				try {
					OCRLogger.logMessage("Generating document of '"
							+ project.getName() + "'...");
					monitor.beginTask("Reading text file...", textFiles.length);
					documentGenerator.generateDocument(textFiles, outputFile,
							monitor);
					OCRLogger.logMessage("The document '"
							+ outputFile.getName()
							+ "' is generated succussfully.");
				} catch (Exception e) {
					OCRLogger.logError(e.getMessage());
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
		job.setRule(ResourcesPlugin.getWorkspace().getRoot());
		job.schedule();

		job.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				super.done(event);
				try {
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
