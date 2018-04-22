package com.ajayinkingston.ld41;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

//each particle will be a box2d physics object that is a small circle, the choppyness will be cleared up in rendering
public class Particle {
	
	Body body;
	
	
	public Particle(Body body) {
		this.body = body;
	}
	
	public void update(Main main) {
		
	}
	
	public static float getRadius() {
		return 5;
	}
	
	public Vector2 getPosition() {
		return body.getPosition();
	}

}
