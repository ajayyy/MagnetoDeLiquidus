package com.ajayinkingston.ld41;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Level {
	
	Main main;
	
	World world;
	
	ArrayList<Particle> particles = new ArrayList<>();
	
	ShaderProgram particleShader;
	
	Texture particleImage;
	
	FrameBuffer allParticles;
	TextureRegion fboRegion;
	
	public Level(Main main) {
		this.main = main;
		
		allParticles = new FrameBuffer(Format.RGBA8888, 1000, 600, false);
		
		fboRegion = new TextureRegion(allParticles.getColorBufferTexture());
		fboRegion.flip(false, true);
		
		particleImage = new Texture("white.png");
		
		//partcile shader
		particleShader = new ShaderProgram(Gdx.files.internal("shaders/particle.vsh"), Gdx.files.internal("shaders/particle.fsh"));
		
		if (particleShader.getLog().length()!=0)
			System.out.println(particleShader.getLog());
		
		ShaderProgram.pedantic = false;
		
		world = new World(new Vector2(0, -0), true);
		
		BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        
        CircleShape shape = new CircleShape();
        shape.setRadius(Particle.getRadius());
		
		for(int i = 0; i < 30; i++) {
	        bodyDef.position.set(20 + i * 15, Gdx.graphics.getHeight() / 2 + (i == 0 ? 30 : 0));
	        
	        Body body = world.createBody(bodyDef);

	        FixtureDef fixtureDef = new FixtureDef();
	        fixtureDef.shape = shape;
	        fixtureDef.density = 1f;
	        fixtureDef.friction = 0.01f;
//	        fixtureDef.restitution = 0.5f;
	        
	        Fixture fixture = body.createFixture(fixtureDef);
	        
			particles.add(new Particle(body));

		}

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
        
        {
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
	        
	        ground.dispose();
        }
        
        {
	        BodyDef groundDef = new BodyDef();
	        groundDef.type = BodyDef.BodyType.KinematicBody;
	        
	        PolygonShape ground = new PolygonShape();
	        ground.setAsBox(5, 10000);
	        
	        groundDef.position.set(0, 0);
	        
	        Body body = world.createBody(groundDef);
	
	        FixtureDef fixtureDef = new FixtureDef();
	        fixtureDef.shape = ground;
	        fixtureDef.density = 0.00000001f;
	        fixtureDef.friction = 1f;
	        
	        Fixture fixture = body.createFixture(fixtureDef);
	        
	        ground.dispose();
        }
        
        {
	        BodyDef groundDef = new BodyDef();
	        groundDef.type = BodyDef.BodyType.KinematicBody;
	        
	        PolygonShape ground = new PolygonShape();
	        ground.setAsBox(10000, Particle.getRadius());
	        
	        groundDef.position.set(0, Gdx.graphics.getHeight());
	        
	        Body body = world.createBody(groundDef);
	
	        FixtureDef fixtureDef = new FixtureDef();
	        fixtureDef.shape = ground;
	        fixtureDef.density = 0.00000001f;
	        fixtureDef.friction = 1f;
	        
	        Fixture fixture = body.createFixture(fixtureDef);
	        
	        ground.dispose();
        }
        
        {
	        BodyDef groundDef = new BodyDef();
	        groundDef.type = BodyDef.BodyType.KinematicBody;
	        
	        PolygonShape ground = new PolygonShape();
	        ground.setAsBox(5, 10000);
	        
	        groundDef.position.set(Gdx.graphics.getWidth(), 0);
	        
	        Body body = world.createBody(groundDef);
	
	        FixtureDef fixtureDef = new FixtureDef();
	        fixtureDef.shape = ground;
	        fixtureDef.density = 0.00000001f;
	        fixtureDef.friction = 1f;
	        
	        Fixture fixture = body.createFixture(fixtureDef);
	        
	        ground.dispose();
        }
        
        particles.get(0).body.applyForceToCenter(new Vector2(50000, 0), true);
	}
	
	public void render() {
		
		world.step(1/60f, 6, 2);
		
		allParticles.begin();
		//clear frame buffer
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		main.batch.begin();
		
		main.batch.setShader(null);
		
		for(Particle particle: particles) {
			
			main.batch.draw(particleImage, particle.body.getPosition().x - Particle.getRadius(), particle.body.getPosition().y - Particle.getRadius(), Particle.getRadius() * 2, Particle.getRadius() * 2);
			
//			particleShapeRenderer.circle(particle.body.getPosition().x, particle.body.getPosition().y, Particle.getRadius());
		}
		
		if(Gdx.input.isButtonPressed(Buttons.LEFT)) {
			Vector3 mousePos = main.cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
			
			BodyDef bodyDef = new BodyDef();
	        bodyDef.type = BodyDef.BodyType.DynamicBody;
	        
	        CircleShape shape = new CircleShape();
	        shape.setRadius(Particle.getRadius());
			
			bodyDef.position.set(mousePos.x, mousePos.y);
	        
	        Body body = world.createBody(bodyDef);

	        FixtureDef fixtureDef = new FixtureDef();
	        fixtureDef.shape = shape;
	        fixtureDef.density = 1f;
	        fixtureDef.friction = 1f;
//	        fixtureDef.restitution = 0.5f;
	        
	        Fixture fixture = body.createFixture(fixtureDef);
	        
			particles.add(new Particle(body));
		}
		
		if(Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			for(int i = 0; i < particles.size(); i++) {
				Vector3 mousePos = main.cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
				
				Vector2 dir = particles.get(i).getPosition().sub(mousePos.x, mousePos.y).nor();
				
				particles.get(i).body.applyForceToCenter(dir.scl(50000), true);
			}
		}
		
		main.batch.flush();
		allParticles.end();
		
		particleShader.begin();
		
		main.batch.setShader(particleShader);
		particleShader.setUniformf("dir", 1f, 0f);
		particleShader.setUniformf("resolution", 1000*600);
		particleShader.setUniformf("radius", 20f);
		
		fboRegion.setTexture(allParticles.getColorBufferTexture());
		
		main.batch.draw(fboRegion, 0, 0);
		
		main.batch.end();
		particleShader.end();

	}
	
	public void update() {
		
	}
}
