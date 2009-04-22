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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pulpcore.platform.Surface;
import pulpcore.platform.applet.BufferedImageSurface;

/**
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 * 
 */
public class CoreWindow extends JFrame {

	class CorePanel extends JPanel {

		private static final long serialVersionUID = -2625212614575475867L;
		
		BufferedImage buffer = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
		
		@Override
		public void paint(Graphics g) {
			
			surface.draw(buffer.getGraphics());
			g.drawImage(buffer, 0, 0, null);
		}
		
		@Override
		public void update(Graphics g) {
			paint(g);
		}
		
	}
	
	/** Class serial number */
	private static final long serialVersionUID = -8999835621498095740L;
	/** PulpCore draw surface */
	BufferedImageSurface surface;
	/** Inside panel */
	private final CorePanel panel = new CorePanel();
	
	/** Window frame */

	public CoreWindow() {
		
		add(panel);
		surface = new BufferedImageSurface(panel);
		
		setResizable(false);
	}

	/**
	 * Provides a PulpCore draw surface, which on engine can freely draw on.
	 * 
	 * @return Drawing surface.
	 */
	public Surface getSurface() {
		return surface;
	}
	
	@Override
	public void setSize(Dimension d) {
		panel.setPreferredSize(d);
		panel.buffer = new BufferedImage(d.width, d.height, BufferedImage.TYPE_4BYTE_ABGR);
		pack();
	}
	
	@Override
	public void setSize(int width, int height) {
		setSize(new Dimension(width, height));
	}

	/**
	 * @return
	 */
	Component getComponent() {
		return panel;
	}

}
