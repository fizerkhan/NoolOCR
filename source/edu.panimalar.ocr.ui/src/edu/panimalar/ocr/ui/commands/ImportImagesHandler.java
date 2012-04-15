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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.panimalar.ocr.ui.wizards.ImportImagesWizard;

public class ImportImagesHandler extends AbstractHandler {

	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
	}

	public void dispose() {
		// TODO Auto-generated method stub
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		// Create import wizard
		ImportImagesWizard wizard = new ImportImagesWizard();
		IWorkbenchWindow activeWorkbenchWindow = HandlerUtil
				.getActiveWorkbenchWindow(event);
		IWorkbench workbench = activeWorkbenchWindow.getWorkbench();
		ISelection selection = activeWorkbenchWindow.getActivePage()
				.getSelection();
		IStructuredSelection structSel = null;
		if (selection instanceof IStructuredSelection) {
			structSel = (IStructuredSelection) selection;
		}
		wizard.init(workbench, structSel);

		// Instantiates the wizard container with the wizard and opens it
		WizardDialog dialog = new WizardDialog(
				activeWorkbenchWindow.getShell(), wizard);
		dialog.create();
		return dialog.open();
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

}
