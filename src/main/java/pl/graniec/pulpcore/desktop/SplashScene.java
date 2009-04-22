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

import pulpcore.Stage;
import pulpcore.animation.Easing;
import pulpcore.animation.Timeline;
import pulpcore.scene.Scene;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.ImageSprite;

public class SplashScene extends Scene2D {

	/** Graniec logo path */
	private static final String LOGO_RESOURCE = "/pl/graniec/pulpcore/desktop/resources/graniec_logo.png";
	/** Sprite that keeps the logo */
	private ImageSprite logoSprite;

	/** Time that has passed from the beginning of scene */
	private int totalElapsedTime;
	/** How long the scene should last? */
	private final int SCENE_DURATION = 6000;
	/** Scene timeline */
	private Timeline timeline;
	/** Scene that plays next */
	private final Scene nextScene;
	
	
	public SplashScene( Scene nextScene ) {
		this.nextScene = nextScene;
	}

	@Override
	public void load() {
		
		System.out.println("loading splash");

		// loading logo
		logoSprite = new ImageSprite( LOGO_RESOURCE, 0, 0 );

		// adding background color and logo too
		add( new FilledSprite( 0, 0, 640, 480, Color.black.getRGB() ) );
		add( logoSprite );

		// creating animation
		logoSprite.alpha.set( 0 );

		int width = logoSprite.getImage().getWidth();
		int height = logoSprite.getImage().getHeight();

		logoSprite.setAnchor(ImageSprite.CENTER);
		logoSprite.setLocation( 320, 240 );

		timeline = new Timeline();
		timeline.animate( logoSprite.alpha, 0, 255, 2000 );
		timeline.animate( logoSprite.alpha, 255, 0, 2000, Easing.NONE, 4000 );
		timeline.scale( logoSprite, width / 2.0, height / 2.0, width / 1.5,
				height / 1.5, 6000 );
		timeline.interruptScene( nextScene, SCENE_DURATION );
		timeline.play();

	}

	@Override
	public void update( int elapsedTime ) {
		totalElapsedTime += elapsedTime;

		timeline.update( elapsedTime );

		if ( totalElapsedTime >= SCENE_DURATION ) {
			Stage.popScene();
		}

	}
}
