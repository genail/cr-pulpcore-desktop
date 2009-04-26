/* Copyright (c) ${year}, Peter Korzuszek <piotr.korzuszek@gmail.com>
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package pl.graniec.coralreef.pulpcore.desktop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import pulpcore.Build;
import pulpcore.CoreSystem;
import pulpcore.platform.Platform;
import pulpcore.scene.Scene;

public class CoreApplication implements Runnable {

	/** Properties that can be given thru arguments */
	public static final String FIRST_SCENE_CLASS_PROPERTY = "firstscene";
	public static final String WINDOW_WIDTH = "window-width";
	public static final String WINDOW_HEIGHT = "window-height";

	/** Default window dimensions */
	private static final int DEFAULT_WINDOW_WIDTH = 640;
	private static final int DEFAULT_WINDOW_HEIGHT = 480;

	/**
	 * @param args
	 */
	public static void main( String[] args ) {
		// reading properties from arguments
		String propertyName, propertyValue;
		int indexOfEquals;
		
		Map<String, String> properties = new HashMap<String, String>();
		

		for ( String arg : args ) {
			if ( arg.matches( ".+=.+" ) ) {

				indexOfEquals = arg.indexOf( '=' );

				propertyName = arg.substring( 0, indexOfEquals );
				propertyValue = arg.substring( indexOfEquals + 1 );

				properties.put( propertyName, propertyValue );
			}
		}
		
		new CoreApplication( properties ).run();

	}
	
	
	/** Application run properties */
	private Map<String, String> properties = new HashMap<String, String>();
	/** If window is created then this will be window default width and height */
	private int windowWidth, windowHeight;

	/** First scene that will be run */
	private Class<? extends Scene> sceneClass;
	/** Panel on which the scenes will be displayed */
	private CoreDisplayPanel displayPanel;
	/** Tells if window should be resizable */
	private boolean windowResizable;
	/** Is display mode is fullscreen? */
	private boolean fullScreen;
	/** Device on which window will be created */
	private GraphicsDevice defaultScreenDevice;
	
	/**
	 * Creates a new CoreApplication that will display a
	 * window with default dimensions. When run, the
	 * <code>firstSceneClass</code> will be launched.
	 * 
	 * @param firstSceneClass Scene to launch first.
	 */
	public CoreApplication(final Class<? extends Scene> firstSceneClass) {
		this(firstSceneClass, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
	}
	
	/**
	 * Creates a new CoreApplication that will display
	 * <code>firstSceneClass</code> and other scenes
	 * in selected <code>displayPanel</code>.
	 */
	public CoreApplication(final Class<? extends Scene> firstSceneClass, final CoreDisplayPanel displayPanel) {
		
		if (firstSceneClass == null || displayPanel == null) {
			throw new IllegalArgumentException("parameters cannot be null");
		}	
		
		this.sceneClass = firstSceneClass;
		this.displayPanel = displayPanel;
		
		initialize();
	}
	
	/**
	 * Creates a new CoreApplication that will display
	 * a window with dimensions of <code>width</code> and
	 * <code>height</code>. When run, the <code>firstSceneClass</code>
	 * will be launched.
	 * 
	 * @param firstSceneClass Scene to launch first.
	 * @param width Width of window to create.
	 * @param height Height of window to create.
	 */
	public CoreApplication(final Class<? extends Scene> firstSceneClass, final int width, final int height) {
		if (firstSceneClass == null) {
			throw new IllegalArgumentException("parameters cannot be null");
		}
		
		if (width < 1 || height < 1) {
			throw new IllegalArgumentException("Bad window dimensions");
		}
		
		this.sceneClass = firstSceneClass;
		this.windowWidth = width;
		this.windowHeight = height;
		
		initialize();
	}
	
	/**
	 * @deprecated use other constructors instead
	 */
	public CoreApplication(Map<String, String> properties) {
		
		try {
			if (properties.containsKey(WINDOW_WIDTH)) {
				windowWidth = Integer.parseInt(properties.get(WINDOW_WIDTH));
			}
		} catch ( NumberFormatException e ) {
			if ( Build.DEBUG ) {
				System.err.println( "Window width is not a integer" );
			}
		} finally {
			if (windowWidth <= 0) {
				windowWidth = DEFAULT_WINDOW_WIDTH;
			}
		}
		
		try {
			if (properties.containsKey(WINDOW_HEIGHT)) {
				windowHeight = Integer.parseInt(properties.get(WINDOW_HEIGHT));
			}
		} catch ( NumberFormatException e ) {
			if ( Build.DEBUG ) {
				System.err.println( "Window height is not a integer" );
			}
		} finally {
			if (windowHeight <= 0) {
				windowHeight = DEFAULT_WINDOW_HEIGHT;
			}
		}
		
		try {
			if (properties.containsKey(FIRST_SCENE_CLASS_PROPERTY)) {
				sceneClass = (Class<Scene>) Class.forName(properties.get(FIRST_SCENE_CLASS_PROPERTY));
			}
		} catch (ClassNotFoundException e) {
			if ( Build.DEBUG ) {
				System.err.println("Class not found: " + properties.get(FIRST_SCENE_CLASS_PROPERTY));
			}
		}
		
		
		this.properties = properties;
		
		initialize();
	}
	
	private void initialize() {
		final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		defaultScreenDevice = env.getDefaultScreenDevice();
	}

	/**
	 * Runs the application. All configuration should be done before
	 * this step!
	 */
	@Override
	public void run() {
		
		// if displayPanel is null then I should create
		// a new CoreWindow
		if (displayPanel == null) {
			
			// if fullscreen then check the display modes
			DisplayMode displayMode = null;
			
			if (fullScreen) {
				for (final DisplayMode mode : defaultScreenDevice.getDisplayModes()) {
					if (mode.getWidth() == windowWidth && mode.getHeight() == windowHeight) {
						displayMode = mode;
						break;
					}
				}
				
				if (Build.DEBUG && displayMode == null) {
					System.err.println("Error: Cannot switch to display mode " + windowWidth + " x " + windowHeight + ": not available");
				}
			}
			
			final CoreWindow window = new CoreWindow(defaultScreenDevice, (displayMode != null), displayMode);
			
			window.setSize(new Dimension(windowWidth, windowHeight));
			window.setBackground(Color.black);
			window.setVisible(true);
			
			window.setResizable(windowResizable);
			
			// closing window action
			window.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}

			});
			
			displayPanel = window.getDisplayPanel();
		}

		// creating desktop platform
		
		Platform platform = new DesktopPlatform(displayPanel, sceneClass);
		CoreSystem.init(platform);

		// setting properties
		DesktopAppContext appContext = (DesktopAppContext) platform.getThisAppContext();

		for (String key : properties.keySet()) {
			appContext.setAppProperty(key, properties.get(key));
		}

		// start running
		platform.getThisAppContext().start();
	}
	
	public void setAppProperty(final String name, final String value) {
		properties.put(name, value);
	}
	
	/**
	 * Sets the display mode to <i>fullscreen</i> or <i>windowed</i>.
	 * The default is <i>windowed</i>.
	 * <p>
	 * Switched to fullscreen can be only these resolutions that matches
	 * avaiable display modes. To check what display modes are available
	 * on the machine that the application is running, do the following:
	 * <p>
	 * <code>
	 * final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();<br>
	 * final DisplayMode[] displayModes = env.getDefaultScreenDevice().getDisplayModes();
	 * </code>
	 * 
	 * @param fullScreen the fullScreen to set
	 */
	public void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}

	/**
	 * Sets if created window can be resized. By default
	 * window cannot resize.
	 * 
	 * @param windowResizable Window resizable state.
	 */
	public void setWindowResizable(boolean windowResizable) {
		this.windowResizable = windowResizable;
	}
	
	public void setWindowSize(final int width, final int height) {
		windowWidth = width;
		windowHeight = height;
	}

}
