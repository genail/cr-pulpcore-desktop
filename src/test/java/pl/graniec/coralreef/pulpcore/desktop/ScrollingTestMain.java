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

import pulpcore.animation.Easing;
import pulpcore.animation.Int;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.Group;

/**
 * @author Piotr Korzuszek <piotr.korzuszek@gmail.com>
 *
 */
public class ScrollingTestMain extends Scene2D {

	final Int x = new Int();
	final Int y = new Int();
	private Group group;
	
	/* (non-Javadoc)
	 * @see pulpcore.scene.Scene#load()
	 */
	@Override
	public void load() {
		super.load();
		
		add(new FilledSprite(0xFF888888));
		
		final int space = 2;
		final int side = 64;
		final int w = 10;
		final int h = 7;
		
		group = new Group();
		
		for (int x = 0; x < w; ++x) {
			for (int y = 0; y < h; ++y) {
				group.add(new FilledSprite(x * (space + side), y * (space + side), side, side, 0xFFAA0000));
			}
		}
		
		add(group);
		
		x.animateTo(400, 10000, Easing.NONE);
		y.animateTo(400, 10000, Easing.NONE);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CoreApplication app = new CoreApplication(ScrollingTestMain.class);
		app.run();
	}
	
	/* (non-Javadoc)
	 * @see pulpcore.scene.Scene2D#update(int)
	 */
	@Override
	public void update(int elapsedTime) {
		super.update(elapsedTime);
		x.update(elapsedTime);
		y.update(elapsedTime);
		
		group.x.set(x.get());
		group.y.set(y.get());
	}

}
