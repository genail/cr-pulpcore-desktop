/**
 * Copyright (c) 2009, Coral Reef Project
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  * Neither the name of the Coral Reef Project nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
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

import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pulpcore.platform.applet.BufferedImageSurface;

/**
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 *
 */
public class CoreDisplayPanel extends JPanel {

	private static final long serialVersionUID = -1623045476967544733L;
	
	/** The second buffer */
	private BufferedImage buffer = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
	/** PulpCore draw surface */
	private final BufferedImageSurface surface;
	
	public CoreDisplayPanel() {
		
		surface = new BufferedImageSurface(this);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				BufferedImage newBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
				newBuffer.getGraphics().drawImage(buffer, 0, 0, null);
				buffer = newBuffer;
				
			}
		});
	}
	
	public  BufferedImageSurface getSurface() {
		return surface;
	}
	
	@Override
	public void paint(Graphics g) {
		surface.draw(buffer.getGraphics());
		g.drawImage(buffer, 0, 0, null);
	}
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}
	
	/*
	 * @see java.awt.Component#paintAll(java.awt.Graphics)
	 */
	@Override
	public void paintAll(Graphics g) {
		paint(g);
	}
	
}
