/* Copyright (c) 2008, Peter Korzuszek <piotr.korzuszek@gmail.com>
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

import pulpcore.CoreSystem;
import pulpcore.platform.AppContext;
import pulpcore.platform.Platform;
import pulpcore.platform.SoundEngine;
import pulpcore.platform.applet.JavaSound;
import pulpcore.scene.Scene;


/**
 * Desktop implementation of PulpCore Platform.
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 *
 */
public class DesktopPlatform implements Platform {

	/** Sound engine in use */
	private SoundEngine soundEngine;
	/** Created AppContext */
	private DesktopAppContext appContext;
	/** The clipboard */
	private String clipboardText = "";

	
	public DesktopPlatform(final CoreDisplayPanel displayPanel, final Class<? extends Scene> firstSceneClass) {
		
		// TODO: I'm not really sure if that should be done in this way
		appContext = new DesktopAppContext(displayPanel, firstSceneClass);
	}
	
	@Override
	public String getBrowserName() {
		return null;
	}

	@Override
	public String getBrowserVersion() {
		return null;
	}

	@Override
	public String getClipboardText() {
		return clipboardText;
	}

	@Override
	public SoundEngine getSoundEngine() {
		if (soundEngine == null) {
            soundEngine = JavaSound.create();
            CoreSystem.setTalkBackField("pulpcore.platform.sound", "javax.sound");
        }
        return soundEngine;
	}

	@Override
	public AppContext getThisAppContext() {
		return appContext;
	}

	@Override
	public long getTimeMicros() {
		return System.nanoTime() / 1000L;
	}

	@Override
	public long getTimeMillis() {
		return System.currentTimeMillis();
	}

	@Override
	public boolean isBrowserHosted() {
		return false;
	}

	@Override
	public boolean isNativeClipboard() {
		return false;
	}

	@Override
	public boolean isSoundEngineCreated() {
		return (soundEngine != null);
	}

	@Override
	public void setClipboardText( String text ) {
		clipboardText = text;
	}

	@Override
	public long sleepUntilTimeMicros( long timeMicros ) {
		try {
			long sleepTime = timeMicros - getTimeMicros();
			
			if (sleepTime > 1000) {
				Thread.sleep( sleepTime / 1000 );
			}
		} catch ( InterruptedException e ) {
			e.printStackTrace();
		}
		
		return getTimeMicros();
	}

	@Override
	public void updateSoundEngine( int timeUntilNextUpdate ) {
		if (soundEngine != null) {
            soundEngine.update(timeUntilNextUpdate);
        }
	}
	//@SuppressWarnings("unused")
	//private static Logger logger = Logger.getLogger( DesktopPlatform.class.getName() );
}
