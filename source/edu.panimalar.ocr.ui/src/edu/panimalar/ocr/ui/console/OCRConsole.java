/*******************************************************************************
 * Copyright (c) 2000, 2011. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Fizer Khan, Yasmine - initial API and implementation
 *******************************************************************************/

package edu.panimalar.ocr.ui.console;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class OCRConsole implements Runnable {

	public static final String CONSOLE_NAME = "OCR Console";

	private MessageConsoleStream messageConsoleStream;
	private MessageConsoleStream errorConsoleStream;
	private MessageConsole messageConsole;
	private Thread consoleThread;
	private boolean consoleAlive;
	private LinkedBlockingQueue<ConsoleMessage> messageQueue;

	public OCRConsole() {
		createConsole();
	}

	/**
	 * Message Type represents message console type
	 * 
	 * @author fizer
	 * 
	 */
	enum MessageType {
		INFO(new Color(null, 0, 0, 0)), ERROR(new Color(null, 255, 0, 0)), WARN(
				new Color(null, 0, 0, 0));

		private Color color;

		private MessageType(Color color) {
			this.color = color;
		}

		public Color getColor() {
			return color;
		}
	}

	/**
	 * Represents Console messages
	 * 
	 * @author fizer
	 * 
	 */
	class ConsoleMessage {
		String message;
		MessageType type;

		public ConsoleMessage(String message, MessageType type) {
			this.message = message;
			this.type = type;
		}

		public String getMessage() {
			return message;
		}

		public MessageType getType() {
			return type;
		}
	}

	/**
	 * Start console by creating thread
	 */
	public void startConsole() {
		messageQueue = new LinkedBlockingQueue<ConsoleMessage>();
		consoleThread = new Thread(this);
		consoleThread.setName(CONSOLE_NAME);
		consoleAlive = true;
		consoleThread.start();
	}

	public void run() {
		while (consoleAlive) {
			try {
				if (messageQueue != null) {

					// Poll console message from queue
					ConsoleMessage consoleMsg = messageQueue.poll(5L,
							TimeUnit.SECONDS);
					if (consoleMsg == null) {
						continue;
					}

					// Get message string and its type
					String message = consoleMsg.getMessage();
					MessageType type = consoleMsg.getType();
					if (message == null || message.trim().length() == 0) {
						continue;
					}

					// Based on type, print message in the console stream
					if (type == MessageType.ERROR) {
						errorConsoleStream.println(message);
					} else if (type == MessageType.INFO) {
						messageConsoleStream.println(message);
					} else {
						messageConsoleStream.println(message);
					}
				}

				// if no event with handle queue then stop thread
				else {
					break;
				}
			} catch (InterruptedException e) {
			}
		}

	}

	/**
	 * Stops updating to console by terminating the thread
	 * 
	 */
	public void stopConsole() {
		consoleAlive = false;
		consoleThread.interrupt();
		consoleThread = null;
		messageQueue = null;
	}

	/**
	 * Create a console
	 * 
	 */
	private void createConsole() {
		messageConsole = new MessageConsole(CONSOLE_NAME, null);
		ConsolePlugin.getDefault().getConsoleManager()
				.addConsoles(new IConsole[] { messageConsole });
		messageConsoleStream = messageConsole.newMessageStream();
		messageConsoleStream.setColor(MessageType.INFO.getColor());
		errorConsoleStream = messageConsole.newMessageStream();
		errorConsoleStream.setColor(MessageType.ERROR.getColor());

	}

	/**
	 * Clear Console
	 */
	public void clearConsole() {
		messageConsoleStream = null;
		errorConsoleStream = null;
		ConsolePlugin.getDefault().getConsoleManager()
				.removeConsoles(new IConsole[] { messageConsole });
		createConsole();
	}

	/**
	 * Focus console view
	 */
	public void focusConsole() {
		IConsoleManager consoleMgr = ConsolePlugin.getDefault()
				.getConsoleManager();
		IConsole[] consoles = consoleMgr.getConsoles();
		for (int i = 0; i < consoles.length; i++) {
			if (consoles[i].getName().equalsIgnoreCase(CONSOLE_NAME)) {
				consoleMgr.showConsoleView(consoles[i]);
				break;
			}
		}
	}

	/**
	 * Log message to a console
	 * 
	 * @param msg
	 *            - message to be updated
	 */
	public void logMessage(String msg) {
		pushMessages(msg, MessageType.INFO);
	}

	/**
	 * Log error to a console
	 * 
	 * @param err
	 *            - error to be updated
	 */
	public void logError(String err) {
		pushMessages(err, MessageType.ERROR);
	}

	/**
	 * Get Message Console
	 * 
	 * @return message console
	 */
	public MessageConsole getMessageConsole() {
		return messageConsole;
	}

	private void pushMessages(String message, MessageType type) {
		ConsoleMessage consoleMsg = new ConsoleMessage(message, type);
		if (messageQueue != null) {
			messageQueue.add(consoleMsg);
		}
	}

}
