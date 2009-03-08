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

import java.awt.Frame;
import java.awt.Graphics;

import pulpcore.platform.Surface;
import pulpcore.platform.applet.BufferedImageSurface;

/**
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 * 
 */
public class CoreWindow extends Frame {

	/** Class serial number */
	private static final long serialVersionUID = -8999835621498095740L;
	/** PulpCore draw surface */
	BufferedImageSurface surface;

	public CoreWindow() {
		// creating surface
		surface = new BufferedImageSurface( this );
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
	public void paint( Graphics g ) {
		surface.draw( g );
	}

	@Override
	public void update( Graphics g ) {
		paint( g );
	}

}
