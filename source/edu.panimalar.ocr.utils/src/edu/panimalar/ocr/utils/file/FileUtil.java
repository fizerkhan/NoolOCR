/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.utils.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

//FIXME We have to remove unwanted functions
/**
 * Utility for file operations
 * 
 */
public final class FileUtil {

	/**
	 * Get file name filter with the given type
	 * 
	 * @param fileType
	 *            file type for the filter
	 * @return filter
	 */
	public static FilenameFilter defineFilter(final String fileType) {
		// define file name filter to get only defined string
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String fileName) {
				String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
				if ((ext != null) && (ext.equalsIgnoreCase(fileType))) {
					return true;
				}
				return false;
			}
		};
		return filter;
	}

	/**
	 * Get file name filter with the given types
	 * 
	 * @param fileTypes
	 *            array of file types for the filter
	 * @return filter
	 */
	public static FilenameFilter defineFilter(final String[] fileTypes) {
		// define file name filter to get only defined string
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String fileName) {
				String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
				if (ext != null) {
					for (String fileType : fileTypes) {
						if (ext.equalsIgnoreCase(fileType)) {
							return true;
						}
					}
				}
				return false;
			}
		};
		return filter;
	}

	/**
	 * Get Filename file which exclude .svn files
	 * 
	 * @return file filter
	 */
	public static FilenameFilter getSVNFilter() {
		// define file name filter to get only defined string
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String fileName) {
				if (fileName.indexOf(".svn") == -1) {
					return true;
				}
				return false;
			}
		};
		return filter;
	}

	/**
	 * Read content from file
	 * 
	 * @param file
	 *            file to be read
	 * @return file contents
	 * @throws IOException
	 */
	static public String readFile(File file) throws IOException {
		StringBuilder contents = new StringBuilder();

		BufferedReader input = new BufferedReader(new FileReader(file));
		try {
			String line = null;
			while ((line = input.readLine()) != null) {
				contents.append(line);
				contents.append(System.getProperty("line.separator"));
			}
		} finally {
			input.close();
		}

		return contents.toString();
	}

	/**
	 * Write file contents into specified file
	 * 
	 * @param file
	 *            file in which content to be written
	 * @param fileContent
	 *            content of the file
	 * @throws IOException
	 */
	public static void writeContentToFile(File file, String fileContent)
			throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write(fileContent);
		out.close();
	}

	/**
	 * Append file contents into specified file
	 * 
	 * @param file
	 *            file in which content to be written
	 * @param fileContent
	 *            content of the file
	 * @throws IOException
	 */
	public static void appendContentToFile(File file, String fileContent)
			throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
		out.write(fileContent);
		out.close();
	}

	/**
	 * Insert the file contents at beginging of specified file
	 * 
	 * @param file
	 *            file in which content to be written
	 * @param fileContent
	 *            content to be inserted into the file
	 * @throws IOException
	 */
	public static void prependContentToFile(File file, String fileContent)
			throws IOException {
		String oldFileContent = readFile(file);
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write(fileContent + oldFileContent);
		out.close();
	}

	/**
	 * Copy file from source to destination
	 * 
	 * @param source
	 *            source file
	 * @param dest
	 *            destination file
	 * @throws IOException
	 */
	public static void copyFile(File source, File dest) throws IOException {
		// copy source to target using file store
		IFileStore sourceFileStore = EFS.getLocalFileSystem().getStore(
				source.toURI());
		IFileStore targetFileStore = EFS.getLocalFileSystem().getStore(
				dest.toURI());
		try {
			sourceFileStore.copy(targetFileStore, EFS.OVERWRITE, null);
			if (source.canExecute()) {
				dest.setExecutable(true, false);
			}
		} catch (CoreException e) {
			throw new IOException(e.getMessage());
		}
	}

	/**
	 * Copy directory from one location to another
	 * 
	 * @param sourceLocation
	 *            source location
	 * @param targetLocation
	 *            target location
	 * @throws IOException
	 */
	public static void copyDirectory(File sourceLocation, File targetLocation,
			boolean overWrite) throws IOException {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(
						targetLocation, children[i]), overWrite);
			}
		} else {
			if (targetLocation.exists() && !overWrite) {
				return;
			}

			// Copy file from source to destination
			copyFile(sourceLocation, targetLocation);

		}
	}

	/**
	 * Get size of the file
	 * 
	 * @param file
	 *            the file for which size to be found
	 * @return size of the file
	 */
	public static long getFileSize(File dir) {
		long size = 0;
		if (dir.isFile()) {
			size = dir.length();
		} else {
			File[] subFiles = dir.listFiles();

			for (File file : subFiles) {
				if (file.isFile()) {
					size += file.length();
				} else {
					size += getFileSize(file);
				}
			}
		}

		return size;
	}

	/**
	 * Delete directory with non-empty contents
	 * 
	 * @param path
	 *            path of the directory
	 * @return true or false
	 */
	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	/**
	 * Get all the member files and/or folders of the given directory
	 * 
	 * @param parent
	 *            - parent directory
	 * @return Array of member files and/or folders if available,
	 *         <code>null</code> otherwise
	 */
	public static File[] getMembers(File parent) {
		if (parent.exists() && parent.isDirectory()) {
			return parent.listFiles();
		}
		return new File[0];
	}

	/**
	 * Get all the member files and/or folders in the ASCII sorted order
	 * 
	 * @param parent
	 *            - parent directory
	 * @return Array of member files and/or folders in ASCII sort order if
	 *         available, <code>null</code> otherwise
	 */
	public static File[] getASCIISortedMembers(File parent) {
		if (parent.exists() && parent.isDirectory()) {
			/**
			 * This is an internal Comparer object (created with an anonymous
			 * class) that compares two ASCII strings. It is used in the
			 * sortAscii
			 **/
			Comparator<File> asciiComparator = new Comparator<File>() {
				public int compare(File a, File b) {
					return (a.getAbsolutePath()).compareTo(b.getAbsolutePath());
				}
			};
			File[] filesArray = parent.listFiles();
			List<File> fileList = Arrays.asList(filesArray);
			Collections.sort(fileList, asciiComparator);
			return fileList.toArray(filesArray);
		}
		return new File[0];
	}

	/**
	 * Get all the member files of the given directory
	 * 
	 * @param parent
	 *            - parent directory
	 * @return Array of member files if available, <code>null</code> otherwise
	 */
	public static File[] getMemberFiles(File parent) {
		if (parent.exists() && parent.isDirectory()) {
			File[] files = parent.listFiles();
			ArrayList<File> filesList = new ArrayList<File>();
			for (File file : files) {
				if (file.isFile()) {
					filesList.add(file);
				}
			}
			return filesList.toArray(new File[filesList.size()]);
		}
		return new File[0];
	}

	/**
	 * Get all the member files of the given directory in the given fileTypes
	 * 
	 * @param parent
	 *            - parent directory
	 * @param fileTypes
	 *            - array of file types
	 * @return Array of member files if available, <code>null</code> otherwise
	 */
	public static File[] getMemberFiles(File parent, String[] fileTypes) {
		if (parent.exists() && parent.isDirectory()) {
			return parent.listFiles(defineFilter(fileTypes));
		}
		return new File[0];
	}

	/**
	 * Get all the member files of the given directory in the given fileType
	 * 
	 * @param parent
	 *            - parent directory
	 * @param fileType
	 *            - file type
	 * @return Array of member files if available, <code>null</code> otherwise
	 */
	public static File[] getMemberFiles(File parent, String fileType) {
		if (parent.exists() && parent.isDirectory()) {
			return parent.listFiles(defineFilter(fileType));
		}
		return new File[0];
	}

	/**
	 * Get all the member folders of the given parent
	 * 
	 * @param parent
	 *            - parent directory
	 * @return Array of member folders if available, <code>null</code> otherwise
	 */
	public static File[] getMemberFolders(File parent) {
		if (parent.exists() && parent.isDirectory()) {
			File[] folders = parent.listFiles();
			ArrayList<File> foldersList = new ArrayList<File>();
			for (File folder : folders) {
				if (folder.isDirectory()) {
					foldersList.add(folder);
				}
			}
			return foldersList.toArray(new File[foldersList.size()]);
		}
		return new File[0];
	}

	/**
	 * Get the member file of the given parent in the given name
	 * 
	 * @param parent
	 *            - parent directory
	 * @param fileName
	 *            of the required member file
	 * @return
	 */
	public static File getMemberFile(File parent, final String fileName) {
		File file = new File(parent.getAbsolutePath() + File.separator
				+ fileName);
		if (file.exists() && file.isFile()) {
			return file;
		}
		return null;
	}

	/**
	 * Get the member file of the given parent in the given name
	 * 
	 * @param parent
	 *            - parent directory
	 * @param folderName
	 *            of the required member file
	 * @return
	 */
	public static File getMemberFolder(File parent, final String folderName) {
		File folder = new File(parent.getAbsolutePath() + File.separator
				+ folderName);
		if (folder.exists() && folder.isDirectory()) {
			return folder;
		}
		return null;
	}

	/**
	 * Get project relative path of the given absolute file path
	 * 
	 * @param absolute
	 *            file path
	 * @param project
	 *            - project to which the related path is to be found
	 * @return Project relative path of the given absolute file path
	 */
	public static String getProjectRelativePath(String filePath,
			IProject project) {
		if (project == null) {
			return null;
		}
		IFile[] files = project.getWorkspace().getRoot().findFilesForLocation(
				new Path(filePath));
		for (IFile file : files) {
			if (file.getProject().equals(project)) {
				return file.getProjectRelativePath().toString();
			}
		}
		return null;
	}

	/**
	 * Get IFile reference for the given File instance in the given project
	 * 
	 * @param file
	 *            - file instance
	 * @param project
	 *            - project to which the IFile instance is needed
	 * @return IFile instance if available, <code>null</code> otherwise
	 */
	public static IFile getIFileForFile(File file, IProject project) {
		if (project == null) {
			return null;
		}
		IFile[] files = project.getWorkspace().getRoot().findFilesForLocation(
				new Path(file.getAbsolutePath()));
		for (IFile ifile : files) {
			if (ifile.getProject().equals(project)) {
				return ifile;
			}
		}
		return null;
	}

	/**
	 * Get IFolder reference for the given File instance in the given project
	 * 
	 * @param file
	 *            - file instance
	 * @param project
	 *            - project to which the IFile instance is needed
	 * @return IFolder instance if available, <code>null</code> otherwise
	 */
	public static IFolder getIFolderForFile(File file, IProject project) {
		if (project == null) {
			return null;
		}
		IContainer[] containers = project.getWorkspace().getRoot()
				.findContainersForLocation(new Path(file.getAbsolutePath()));
		for (IContainer container : containers) {
			if (container.getProject().equals(project)
					&& container instanceof IFolder) {
				return (IFolder) container;
			}
		}
		return null;
	}

	/**
	 * Get File reference for the given IFile instance
	 * 
	 * @param IFile
	 *            - IFile instance
	 * @return File instance if available, <code>null</code> otherwise
	 */
	public static File getFileForIFile(IFile file) {
		return file.getLocation().toFile();
	}

	/**
	 * Validates if the given text is a valid file name.
	 */
	public static boolean isValidFileName(String fileName) {
		if (fileName == null) {
			return false;
		}
		return fileName.trim().matches("^[^/:*\"<>|?]*$")
				&& !fileName.contains("\\");
	}
}
