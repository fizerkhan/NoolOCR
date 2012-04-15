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
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.panimalar.ocr.core.OCRConstants;
import edu.panimalar.ocr.core.OCRNature;
import edu.panimalar.ocr.core.transform.OCRBuilder;
import edu.panimalar.ocr.core.transform.OCRBuilderManager;
import edu.panimalar.ocr.ui.console.OCRLogger;
import edu.panimalar.ocr.ui.dialogs.OCRSelectionDialog;
import edu.panimalar.ocr.utils.file.FileUtil;

public class ReadImagesHandler extends AbstractHandler {

	@Override
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

				// Clear console
				OCRLogger.clear();

				// Get language for project
				ProjectScope projectScope = new ProjectScope(project);
				IEclipsePreferences node = projectScope
						.getNode(OCRNature.NATURE_ID);
				String language = node.get(
						OCRConstants.OCR_PREFERNCES_LANGUAGE,
						OCRConstants.OCR_PREFERNCES_LANGUAGE_DEFAULT);

				// Open dialog to select OCRs based on the language
				OCRBuilderManager ocrBuilderManager = OCRBuilderManager
						.newInstance();
				String[] ocrNames = ocrBuilderManager
						.getOCRBuilderForLanguage(language);
				if (ocrNames == null || ocrNames.length == 0) {
					OCRLogger.logError("OCR does not exist for the language '"
							+ language + "'.");
					return null;
				}

				// Get images folder
				IFolder imagesFolder = project
						.getFolder(OCRConstants.IMAGES_FOLDER);

				// Get image files. If selection is image file, read only that
				// file, otherwise read all files
				File[] imageFiles;
				if (selResource instanceof IFile
						&& selResource.getParent().equals(imagesFolder)) {
					imageFiles = new File[] { selResource.getLocation()
							.toFile() };
				} else {
					imageFiles = FileUtil.getMemberFiles(imagesFolder
							.getLocation().toFile());
				}

				// throw error if there is no images to read
				if (imageFiles == null || imageFiles.length == 0) {
					OCRLogger.logError("There is no images to read.");
					return null;
				}

				// Get output folder and create it if it does not exist
				IFolder outputFolder = project
						.getFolder(OCRConstants.OUTPUT_FOLDER);
				if (!outputFolder.exists()) {
					try {
						outputFolder.create(true, false, null);
					} catch (CoreException e) {
						OCRLogger.logError(e.getMessage());
						return null;
					}
				}

				// Select OCR for particular language
				IWorkbenchWindow window = HandlerUtil
						.getActiveWorkbenchWindowChecked(event);
				OCRSelectionDialog dialog = new OCRSelectionDialog(
						window.getShell(), ocrNames);
				int result = dialog.open();
				if (result == Window.OK) {
					String ocrName = dialog.getOCR();

					// get OCR with particular name
					OCRBuilder ocrBuilder = ocrBuilderManager
							.getOCRBuilder(ocrName);
					if (ocrBuilder != null) {
						// Set project language
						ocrBuilder.setLanguage(language);

						// Run build job
						runBuildJob(ocrBuilder, imageFiles, outputFolder
								.getLocation().toFile(), project);
					} else {
						OCRLogger.logError("OCR Builder with name '" + ocrName
								+ "' does not exist.");
					}
				}
			}
		} else {
			IWorkbenchWindow window = HandlerUtil
					.getActiveWorkbenchWindowChecked(event);
			MessageDialog.openInformation(window.getShell(), "Read Images",
					"Please select project or image to read.");
		}

		return null;
	}

	private void runBuildJob(final OCRBuilder ocrBuilder,
			final File[] imageFiles, final File outputFolder,
			final IProject project) {
		Job job = new Job("Reading images '" + project.getName() + "'...") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// Read images
				try {
					OCRLogger.logMessage("Reading images of project '"
							+ project.getName() + "'...");
					monitor.beginTask("Reading images...", imageFiles.length);
					ocrBuilder.build(imageFiles, outputFolder, monitor);
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
