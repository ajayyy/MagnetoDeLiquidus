package com.ajayinkingston.ld41;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Level {
	
	Main main;
	
	World world;
	
	ArrayList<Particle> particles = new ArrayList<>();
	
	public Level(Main main) {
		this.main = main;
		
		
		world = new World(new Vector2(0, -70), true);
		
		BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Particle.getRadius(), Particle.getRadius());
		
		for(int i = 0; i < 30; i++) {
	        bodyDef.position.set(20 + i * 15, Gdx.graphics.getHeight() / 2 + (i == 0 ? 30 : 0));
	        
	        Body body = world.createBody(bodyDef);

	        FixtureDef fixtureDef = new FixtureDef();
	        fixtureDef.shape = shape;
	        fixtureDef.density = 1f;
	        fixtureDef.friction = 1f;
	        fixtureDef.restitution = 0.5f;
	        
	        Fixture fixture = body.createFixture(fixtureDef);
	        
			particles.add(new Particle(body));

		}

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
        
        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.KinematicBody;
        
        PolygonShape ground = new PolygonShape();
        ground.setAsBox(10000, Particle.getRadius());
        
        groundDef.position.set(0, 0);
        
        Body body = world.createBody(groundDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = ground;
        fixtureDef.density = 0.00000001f;
        fixtureDef.friction = 1f;
        
        Fixture fixture = body.createFixture(fixtureDef);
        
        particles.get(0).body.applyForceToCenter(new Vector2(50000, 0), true);
	}
	
	public void render() {
		
		
		world.step(1/60f, 6, 2);

		main.shapeRenderer.begin(ShapeType.Filled);
		
		for(Particle particle: particles) {
			main.shapeRenderer.circle(particle.body.getPosition().x, particle.body.getPosition().y, particle.getRadius());
		}
		
		if(Gdx.input.isButtonPressed(Buttons.LEFT)) {
			BodyDef bodyDef = new BodyDef();
	        bodyDef.type = BodyDef.BodyType.DynamicBody;
	        
	        PolygonShape shape = new PolygonShape();
	        shape.setAsBox(Particle.getRadius(), Particle.getRadius());
			
			bodyDef.position.set(Gdx.input.getX(), Gdx.input.getY());
	        
	        Body body = world.createBody(bodyDef);

	        FixtureDef fixtureDef = new FixtureDef();
	        fixtureDef.shape = shape;
	        fixtureDef.density = 1f;
	        fixtureDef.friction = 1f;
	        fixtureDef.restitution = 0.5f;
	        
	        Fixture fixture = body.createFixture(fixtureDef);
	        
			particles.add(new Particle(body));
		}
		
		if(Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			for(int i = 30; i < 30; i++) {
				particles.get(i).body.applyForceToCenter(new Vector2(0, -100).scl(50000), true);
			}
		}
		
		main.shapeRenderer.end();
	}
	
	public void update() {
		
	}
}
