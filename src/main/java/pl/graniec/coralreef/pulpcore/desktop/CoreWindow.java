/* Copyright (c) 2008-2009, Peter Korzuszek <piotr.korzuszek@gmail.com>
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

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;

import javax.swing.JFrame;

/**
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 * 
 */
public class CoreWindow extends JFrame {

	/** Class serial number */
	private static final long serialVersionUID = -8999835621498095740L;
	/** Inside panel */
	private final CoreDisplayPanel panel = new CoreDisplayPanel();
//	/** System display device */
//	private final GraphicsDevice device;
//	/** Is this window fullscreen? */
//	private final boolean fullScreen;
	
	/** Window frame */

	public CoreWindow(final GraphicsDevice device, final boolean fullScreen, final DisplayMode displayMode) {
		
//		this.device = device;
//		this.fullScreen = fullScreen;
		
		add(panel);
		
		setResizable(false);
		
		// switch to fullscreen if this mode is on
		if (fullScreen && displayMode != null) {
			setUndecorated(true);
			
			device.setFullScreenWindow(this);
			device.setDisplayMode(displayMode);
		}
	}
	
//	protected void toogleFullScreen() {
//		
//		if (!fullScreen) {
//		
//			final int width = panel.getWidth();
//			final int height = panel.getHeight();
//			
//			boolean found = false;
//			
//			for (final DisplayMode mode : device.getDisplayModes()) {
//				if (mode.getWidth() == width && mode.getHeight() == height) {
//					
//					device.setFullScreenWindow(this);
//					device.setDisplayMode(mode);
//					
//					
//					fullScreen = true;
//					found = true;
//					
//					break;
//				}
//			}
//			
//			if (!found && Build.DEBUG) {
//				CoreSystem.print("Warning: cannot switch to fullscreen using resolution " + width + " x " + height);
//			}
//		} else {
//			device.setFullScreenWindow(this);
//			device.setDisplayMode(defaultDisplayMode);
//			
//			fullScreen = false;
//		}
//	}

	/**
	 * @return
	 */
	CoreDisplayPanel getDisplayPanel() {
		return panel;
	}
	
	@Override
	public void setSize(Dimension d) {
		panel.setPreferredSize(d);
		pack();
	}

	@Override
	public void setSize(int width, int height) {
		setSize(new Dimension(width, height));
	}

}
