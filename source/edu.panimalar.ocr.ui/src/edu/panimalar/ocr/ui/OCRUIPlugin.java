/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import edu.panimalar.ocr.ui.console.OCRConsole;

/**
 * The activator class controls the plug-in life cycle
 */
public class OCRUIPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "edu.panimalar.ocr.ui"; //$NON-NLS-1$

	// The shared instance
	private static OCRUIPlugin plugin;

	private OCRConsole ocrConsole;

	/**
	 * The constructor
	 */
	public OCRUIPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		// Initialize OCR console
		ocrConsole = new OCRConsole();
		ocrConsole.startConsole();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		
		// Stop console
		if (ocrConsole != null) {
			ocrConsole.stopConsole();
		}
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static OCRUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Get OCR Console	
	 * @return	OCR console
	 */
	public OCRConsole getOcrConsole() {
		return ocrConsole;
	}

}
