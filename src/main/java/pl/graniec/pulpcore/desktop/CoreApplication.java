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

package pl.graniec.pulpcore.desktop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import pulpcore.CoreSystem;
import pulpcore.platform.Platform;

public class CoreApplication implements Runnable {

	private static Logger logger = Logger.getLogger( CoreApplication.class.getName() );

	/** Properties that can be given thru arguments */
	public static final String FIRST_SCENE_CLASS_PROPERTY = "firstscene";
	public static final String WINDOW_WIDTH = "window-width";
	public static final String WINDOW_HEIGHT = "window-height";

	/** Default window dimensions */
	private static final int DEFAULT_WINDOW_WIDTH = 640;
	private static final int DEFAULT_WINDOW_HEIGHT = 480;

	/** Application run properties */
	private Map<String, String> properties;

	public CoreApplication( Map<String, String> properites ) {
		this.properties = properites;
	}

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

	@Override
	public void run() {
		// creating a window
		CoreWindow window = new CoreWindow();

		// getting window dimensions
		int width = DEFAULT_WINDOW_WIDTH;
		int height = DEFAULT_WINDOW_HEIGHT;

		if ( properties.containsKey( WINDOW_WIDTH )
				&& properties.containsKey( WINDOW_HEIGHT ) ) {

			try {

				width = Integer.parseInt( properties.get( WINDOW_WIDTH ) );
				height = Integer.parseInt( properties.get( WINDOW_HEIGHT ) );

			} catch ( NumberFormatException e ) {
				logger.severe( "Invalid number format when defining window width and height: "
						+ e );
			} finally {
				if ( width <= 0 || height <= 0 ) {
					width = DEFAULT_WINDOW_WIDTH;
					height = DEFAULT_WINDOW_HEIGHT;
				}
			}
		}
		

		window.setSize( new Dimension( width, height ) );
		window.setBackground( Color.black );
		window.setVisible( true );

		// closing window action
		window.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing( WindowEvent e ) {
				System.exit( 0 );
			}

		} );

		// creating desktop platform
		Platform platform = new DesktopPlatform( window );
		CoreSystem.init( platform );

		// setting properties
		DesktopAppContext appContext = (DesktopAppContext) platform.getThisAppContext();

		for ( String key : properties.keySet() ) {
			appContext.setAppProperty( key, properties.get( key ) );
		}

		// start running
		platform.getThisAppContext().start();
	}

}
