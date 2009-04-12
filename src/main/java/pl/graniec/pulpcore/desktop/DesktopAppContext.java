/* Copyright (c) 20
 * 08, Peter Korzuszek <piotr.korzuszek@gmail.com>
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

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import pulpcore.Build;
import pulpcore.CoreSystem;
import pulpcore.Stage;
import pulpcore.image.CoreImage;
import pulpcore.platform.AppContext;
import pulpcore.platform.PolledInput;
import pulpcore.platform.Surface;
import pulpcore.scene.Scene;
import pulpcore.util.ByteArray;

/**
 * PulpCore AppContext for desktop applications.
 * 
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 * @author David Brackeen
 * 
 * 
 */
public class DesktopAppContext extends AppContext {

	/** Application input */
	private DesktopInput inputSystem;

	/** Draw surface */
	private Surface surface;
	/** AWT Component on which surface is finally drawn on */
	private Component component;

	/** The stage */
	private Stage stage;
	/** First scene */
	private Scene firstScene;

	/** Application properties */
	private Map<String, String> propertiesMap = new HashMap<String, String>();

	public DesktopAppContext( CoreWindow window ) {

		if ( window == null ) {
			throw new IllegalArgumentException(
					"window parameter cannot be null!" );
		}

		this.surface = window.getSurface();
		this.component = window.getComponent();

		inputSystem = new DesktopInput( component );

	}

	@Override
	public Scene createFirstScene() {

		if ( firstScene == null ) {

			// Create the first scene
			String sceneName = getAppProperty( CoreApplication.FIRST_SCENE_CLASS_PROPERTY );
			if ( sceneName == null || sceneName.length() == 0 ) {
				if ( Build.DEBUG )
					CoreSystem.print( "No defined scene." );
				return null;
			}

			try {
				Class<?> c = Class.forName( sceneName );
				firstScene = (Scene) c.newInstance();
			}

			catch ( Throwable t ) {
				if ( Build.DEBUG )
					CoreSystem.print( "Could not create Scene: " + sceneName, t );
				return null;
			}

//			firstScene = new SplashScene( firstScene );
		}

		return firstScene;
	}

	@Override
	public String getAppProperty( String name ) {
		return propertiesMap.get( name );
	}

	public void setAppProperty( String name, String property ) {
		propertiesMap.put( name, property );
	}

	@Override
	public URL getBaseURL() {
		return null;
	}

	@Override
	public int getCursor() {
		return Cursor.DEFAULT_CURSOR;
	}

	@Override
	public String getLocaleCountry() {
		return "";
	}

	@Override
	public String getLocaleLanguage() {
		return "";
	}

	@Override
	public PolledInput getPolledInput() {
		return inputSystem.getPolledInput();
	}

	@Override
	public Stage getStage() {
		if ( stage == null ) {
			stage = new Stage( getSurface(), this );
		}

		return stage;
	}

	@Override
	public Surface getSurface() {

		if ( surface.isReady() ) {
			return surface;
		} else {
			return null;
		}
	}

	@Override
	public byte[] getUserData( String key ) {

		System.out.println( key );

		// this could not be needed here
		return null;
	}

	@Override
	public CoreImage loadImage( ByteArray in ) {

		if ( in == null ) {
			return null;
		}

		Image image = Toolkit.getDefaultToolkit().createImage( in.getData() );

		MediaTracker tracker = new MediaTracker( component );
		tracker.addImage( image, 0 );
		try {
			tracker.waitForAll();
		} catch ( InterruptedException ex ) {
		}

		int width = image.getWidth( null );
		int height = image.getHeight( null );
		if ( width <= 0 || height <= 0 ) {
			return null;
		}

		int[] data = new int[width * height];
		PixelGrabber pixelGrabber = new PixelGrabber( image, 0, 0, width,
				height, data, 0, width );
		boolean success = false;
		try {
			success = pixelGrabber.grabPixels();
		} catch ( InterruptedException ex ) {
		}

		if ( success ) {
			boolean isOpaque = true;

			// Premultiply alpha
			for ( int i = 0; i < data.length; i++ ) {
				int argbColor = data[i];
				int a = argbColor >>> 24;

				if ( a < 255 ) {
					isOpaque = false;
					int r = (argbColor >> 16) & 0xff;
					int g = (argbColor >> 8) & 0xff;
					int b = argbColor & 0xff;

					r = (a * r + 127) / 255;
					g = (a * g + 127) / 255;
					b = (a * b + 127) / 255;

					data[i] = (a << 24) | (r << 16) | (g << 8) | b;
				}
			}
			return new CoreImage( width, height, isOpaque, data );
		} else {
			return null;
		}
	}

	@Override
	public void pollInput() {
		inputSystem.pollInput();
	}

	@Override
	public void putUserData( String key, byte[] data ) {
		// probably not needed
	}

	@Override
	public void removeUserData( String key ) {
		// probably not needed
	}

	@Override
	public void requestKeyboardFocus() {
		// probably not needed
	}

	@Override
	public void setCursor( int cursor ) {
		inputSystem.setCursor(cursor);
	}

	@Override
	public void showDocument( String url, String target ) {
		// probably not needed
	}

	@Override
	public void start() {
		getStage().start();

	}

	@Override
	public void stop() {
		getStage().stop();

	}

	/*
	 * @see pulpcore.platform.AppContext#getRefreshRate()
	 */
	@Override
	public int getRefreshRate() {
		return 60;
	}
}
