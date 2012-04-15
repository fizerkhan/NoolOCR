/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.ui.wizards;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import edu.panimalar.ocr.core.OCRConstants;
import edu.panimalar.ocr.core.OCRNature;
import edu.panimalar.ocr.ui.console.OCRLogger;
import edu.panimalar.ocr.ui.perspectives.OCRPerspectiveFactory;
import edu.panimalar.ocr.utils.file.FileUtil;
import edu.panimalar.ocr.utils.project.ProjectUtils;

public class NewOCRProjectWizard extends Wizard implements INewWizard {

	public static final String WIZARD_ID = "edu.panimalar.ocr.ui.newwizard.newocrproject";

	private NewOCRProjectWizardPage wizardPage;
	private NewOCRProjectImageWizardPage wizardImagePage;
	private IConfigurationElement config;
	private IWorkbench workbench;
	private IProject project;

	public NewOCRProjectWizard() {
		super();
	}

	public void addPages() {
		// Add project name page
		wizardPage = new NewOCRProjectWizardPage("NewOCRProject");
		wizardPage.setDescription("Create a new OCR Project.");
		wizardPage.setTitle("New OCR Project");
		addPage(wizardPage);

		// Add image page
		wizardImagePage = new NewOCRProjectImageWizardPage("NewOCRProject");
		wizardImagePage.setDescription("Add image files.");
		wizardImagePage.setTitle("New OCR Project");
		addPage(wizardImagePage);

	}

	@Override
	public boolean performFinish() {

		if (project != null) {
			return true;
		}

		final IProject projectHandle = wizardPage.getProjectHandle();
		final String[] imageFiles = wizardImagePage.getImageFiles();
		final String language = wizardPage.getLanguage();

		/*
		 * Just like the NewFileWizard, but this time with an operation object
		 * that modifies workspaces.
		 */
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			protected void execute(IProgressMonitor monitor)
					throws CoreException {
				createProject(projectHandle, imageFiles, language, monitor);
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

		BasicNewProjectResourceWizard.updatePerspective(config);

		// Open OCR perspective
		IPerspectiveRegistry reg = workbench.getPerspectiveRegistry();
		IWorkbenchPage activePage = workbench.getActiveWorkbenchWindow()
				.getActivePage();
		activePage.setPerspective(reg
				.findPerspectiveWithId(OCRPerspectiveFactory.PERSPECTIVE_ID));

		BasicNewProjectResourceWizard.selectAndReveal(project,
				workbench.getActiveWorkbenchWindow());
		return true;
	}

	/**
	 * This creates the project in the workspace.
	 * 
	 * @param imageFiles
	 * @param language
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
	void createProject(IProject project, String[] imageFiles, String language,
			IProgressMonitor monitor) throws CoreException,
			OperationCanceledException {
		try {

			monitor.beginTask(
					"Creating project '" + project.getName() + "'...", 3);

			// Create project
			project.create(new SubProgressMonitor(monitor, 1));
			// Refresh project
			project.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(
					monitor, 1));

			// Add project nature
			OCRNature.addOCRProjectNature(project);

			// Create images, output and document folder
			IFolder imageFolder = project.getFolder(new Path(
					OCRConstants.IMAGES_FOLDER));
			imageFolder.create(true, true, monitor);
			IFolder outputFolder = project.getFolder(new Path(
					OCRConstants.OUTPUT_FOLDER));
			outputFolder.create(true, true, monitor);
			IFolder docFolder = project.getFolder(new Path(
					OCRConstants.DOCUMENT_FOLDER));
			docFolder.create(true, true, monitor);

			// Copy image files to image folder
			for (int i = 0; i < imageFiles.length; i++) {
				File srcFile = new File(imageFiles[i]);
				String fileName = srcFile.getName();
				File destFile = imageFolder.getFile(fileName).getLocation()
						.toFile();
				try {
					FileUtil.copyFile(srcFile, destFile);
				} catch (IOException e) {
					OCRLogger.logError(e.getMessage());
				}
			}

			// Set language in project
			ProjectUtils.setLanguage(project, language);

			monitor.worked(1);

		} finally {
			monitor.done();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
	}

	/**
	 * Sets the initialization data for the wizard.
	 */
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		this.config = config;
	}

}
