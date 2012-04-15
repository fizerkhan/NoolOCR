/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Mohamed Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.ui.wizards;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import edu.panimalar.ocr.core.OCRConstants;
import edu.panimalar.ocr.ui.console.OCRLogger;
import edu.panimalar.ocr.ui.perspectives.OCRPerspectiveFactory;
import edu.panimalar.ocr.utils.file.FileUtil;

public class ImportImagesWizard extends Wizard implements IImportWizard {

	private ImportImagesProjectWizardPage wizardPage;
	private NewOCRProjectImageWizardPage wizardImagePage;
	private IWorkbench workbench;
	private IProject project;

	public ImportImagesWizard() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		if (selection != null) {
			Object firstElement = selection.getFirstElement();
			if (firstElement instanceof IProject) {
				this.project = (IProject) firstElement;
			}
		}
	}

	@Override
	public boolean performFinish() {
		final IProject projectHandle = wizardPage.getProjectHandle();
		final String[] imageFiles = wizardImagePage.getImageFiles();

		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			protected void execute(IProgressMonitor monitor)
					throws CoreException {
				createProject(projectHandle, imageFiles, monitor);
			}
		};

		try {
			getContainer().run(true, true, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error",
					realException.getMessage());
			return false;
		}

		project = projectHandle;
		if (project == null) {
			return false;
		}

		// Open OCR perspective
		IPerspectiveRegistry reg = workbench.getPerspectiveRegistry();
		IWorkbenchPage activePage = workbench.getActiveWorkbenchWindow()
				.getActivePage();
		activePage.setPerspective(reg
				.findPerspectiveWithId(OCRPerspectiveFactory.PERSPECTIVE_ID));

		return true;
	}

	/**
	 * This creates the project in the workspace.
	 * 
	 * @param imageFiles
	 * 
	 * @param description
	 *            project description
	 * @param projectHandle
	 *            project handle
	 * @param monitor
	 *            progress monitor
	 * @throws CoreException
	 * @throws OperationCanceledException
	 */
	void createProject(IProject project, String[] imageFiles,
			IProgressMonitor monitor) throws CoreException,
			OperationCanceledException {
		try {

			monitor.beginTask(
					"Import images into project '" + project.getName() + "'...",
					imageFiles.length);

			// Create images, output and document folder
			IFolder imageFolder = project.getFolder(new Path(
					OCRConstants.IMAGES_FOLDER));
			if (!imageFolder.exists()) {
				imageFolder.create(true, true, monitor);
			}

			// Copy image files to image folder
			for (int i = 0; i < imageFiles.length; i++) {
				File srcFile = new File(imageFiles[i]);
				String fileName = srcFile.getName();
				monitor.subTask("Import image " + fileName + "...");
				File destFile = imageFolder.getFile(fileName).getLocation()
						.toFile();
				try {
					FileUtil.copyFile(srcFile, destFile);
				} catch (IOException e) {
					OCRLogger.logError(e.getMessage());
				}
				monitor.worked(1);
			}

		} finally {
			monitor.done();
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		}
	}

	public void addPages() {
		// Add project name page
		wizardPage = new ImportImagesProjectWizardPage("Import Images");
		wizardPage.setDescription("Import images into OCR Project.");
		wizardPage.setTitle("Import Images");
		addPage(wizardPage);

		// Add image page
		wizardImagePage = new NewOCRProjectImageWizardPage("Import images");
		wizardImagePage.setDescription("Add image files.");
		wizardImagePage.setTitle("Import images");
		addPage(wizardImagePage);

		// Set selected project name
		if (project != null) {
			wizardPage.setDefaultProjectName(project.getName());
		}
	}

}
