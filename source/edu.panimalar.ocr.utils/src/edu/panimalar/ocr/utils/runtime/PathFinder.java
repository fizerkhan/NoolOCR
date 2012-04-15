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

import java.io.File;

import org.eclipse.core.runtime.Platform;

public class PathFinder {

	public static String getOCRInstallationPath() {
		String ocrInstallPath;
		try {
			ocrInstallPath = (new File(Platform.getInstallLocation().getURL()
					.getPath())).getCanonicalPath();
		} catch (Exception e) {
			ocrInstallPath = System.getProperty("user.dir");
		}
		return ocrInstallPath + File.separator;
	}
}
