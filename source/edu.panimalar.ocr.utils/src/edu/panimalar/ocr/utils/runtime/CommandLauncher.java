/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.utils.runtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.progress.UIJob;

import edu.panimalar.ocr.utils.OCRUtilsPlugin;

// FIXME We have to replace this code
/**
 * A external command launcher implementation
 * 
 */
public class CommandLauncher {

	private static boolean firstTime = true;

	/**
	 * It will launch the command
	 * 
	 * @param commandArray
	 *            command to be executed
	 * @param messageConsole
	 *            message console
	 * @param monitor
	 *            progress monitor
	 * @throws Exception
	 *             if there is failure or process is canceled
	 */
	public static void launch(final String[] commandArray,
			final MessageConsole messageConsole, IProgressMonitor monitor)
			throws Exception {
		launch(commandArray, null, null, messageConsole, monitor);
	}

	/**
	 * It will launch the command with the given environmental variables and
	 * working directory
	 * 
	 * @param commandArray
	 *            command array
	 * @param env
	 *            array of environmental variable
	 * @param workingDir
	 *            working directory in which the command will be invoked
	 * @param messageConsole
	 *            message console
	 * @param monitor
	 *            progress monitor
	 * @throws Exception
	 */
	public static void launch(final String[] commandArray, String[] env,
			File workingDir, final MessageConsole messageConsole,
			IProgressMonitor monitor) throws Exception {

		OCRUtilsPlugin activator = OCRUtilsPlugin.getDefault();
		try {
			final Process process = Runtime.getRuntime().exec(commandArray,
					env, workingDir);
			if (firstTime) {
				firstTime = false;
			}

			UIJob uiJob = new UIJob("") {
				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					IWorkbenchWindow activeWorkbenchWindow = PlatformUI
							.getWorkbench().getActiveWorkbenchWindow();
					if (activeWorkbenchWindow != null) {
						IWorkbenchPage activePage = activeWorkbenchWindow
								.getActivePage();
						if (activePage != null) {
							IConsoleView view;
							try {
								view = (IConsoleView) activePage
										.showView(IConsoleConstants.ID_CONSOLE_VIEW);
								view.display(messageConsole);
								new Thread(new MessageConsoleWriter(
										messageConsole,
										process.getInputStream())).start();
								new Thread(new MessageConsoleWriter(
										messageConsole,
										process.getErrorStream(), Display
												.getCurrent().getSystemColor(
														SWT.COLOR_RED)))
										.start();
							} catch (PartInitException e) {
								return new Status(IStatus.ERROR, OCRUtilsPlugin
										.getDefault().getBundle()
										.getSymbolicName(), 0,
										"Execution is failed.", e);
							}
						}
					}
					return Status.OK_STATUS;
				}
			};
			if (monitor != null) {
				uiJob.setProgressGroup(monitor, -1);
			}
			uiJob.schedule();

			int status = 0;
			boolean cancelled = false;
			while (true) {
				if (monitor != null && monitor.isCanceled()) {
					cancelled = true;
					process.destroy();
					uiJob.cancel();
					break;
				}
				try {
					status = process.exitValue();
					break;
				} catch (Exception e) {
				}

			}

			if (cancelled) {
				throw new Exception("Process execution is cancelled.");
			} else if (status == 0) {
				// Status OK
			} else {
				activator.getLog().log(
						new Status(IStatus.ERROR, activator.getBundle()
								.getSymbolicName(), 0, "Process '"
								+ Arrays.asList(commandArray).toString()
								+ "' exited with status: " + status, null));
				throw new Exception("");
			}

		} catch (IOException ioe) {
			activator.getLog().log(
					new Status(IStatus.ERROR, activator.getBundle()
							.getSymbolicName(), 0,
							"Exception while executing '"
									+ Arrays.asList(commandArray).toString()
									+ "'", ioe));
			throw new Exception("");
		}
	}

	/**
	 * The Message console writer implementation in a thread
	 * 
	 */
	private static class MessageConsoleWriter implements Runnable {
		private final MessageConsoleStream msgStream;
		private final InputStream inputstream;

		private MessageConsoleWriter(MessageConsole messageConsole,
				InputStream from) {
			this(messageConsole, from, null);
		}

		private MessageConsoleWriter(MessageConsole messageConsole,
				InputStream from, Color color) {
			this.inputstream = from;
			this.msgStream = messageConsole.newMessageStream();
			if (color != null) {
				msgStream.setColor(color);
			}
		}

		public void run() {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputstream));
			String output = null;
			try {
				while ((output = reader.readLine()) != null) {
					msgStream.println(output);
				}
			} catch (IOException e) {
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
				}
				try {
					msgStream.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
