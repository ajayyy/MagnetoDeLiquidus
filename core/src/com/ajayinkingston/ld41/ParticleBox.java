package com.ajayinkingston.ld41;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class ParticleBox {
	
	int width, height;
	
	Main main;
	
	World world;
	
	ArrayList<Particle> particles = new ArrayList<>();
	
	ShaderProgram blurShader;
	ShaderProgram flattenShader;

	Texture particleImage;
	
	FrameBuffer allParticles;
	FrameBuffer horizontallyBlurredParticles;
	FrameBuffer verticallyBlurredParticles;
	TextureRegion fboRegion;
	
	//to make the box centered there is an offset
	int offsetx, offsety;
	
	LevelBase loadedLevel;
	
	//all of the holes in the wall
	ArrayList<Hole> holes = new ArrayList<Hole>();
	ArrayList<Box> boxes = new ArrayList<Box>();
	
	public ParticleBox(Main main, LevelBase loadedLevel) {
		this.main = main;
		
		this.loadedLevel = loadedLevel;
		
		this.width = loadedLevel.width;
		this.height = loadedLevel.height;
		this.holes = loadedLevel.holes;
		this.boxes = loadedLevel.boxes;
		
		allParticles = new FrameBuffer(Format.RGBA8888, width, height, false);
		horizontallyBlurredParticles = new FrameBuffer(Format.RGBA8888, width, height, false);
		verticallyBlurredParticles = new FrameBuffer(Format.RGBA8888, width, height, false);
		
		fboRegion = new TextureRegion(allParticles.getColorBufferTexture());
		fboRegion.flip(false, true);
		
		particleImage = new Texture("white.png");
//		particleImage.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		//partcile shader
		blurShader = new ShaderProgram(Gdx.files.internal("shaders/blur.vsh"), Gdx.files.internal("shaders/blur.fsh"));
		flattenShader = new ShaderProgram(Gdx.files.internal("shaders/flatten.vsh"), Gdx.files.internal("shaders/flatten.fsh"));
		
		if (blurShader.getLog().length()!=0)
			System.out.println("blur error: \n\n" + blurShader.getLog());
		if (flattenShader.getLog().length()!=0)
			System.out.println("flatten error: \n\n" + flattenShader.getLog());
		
		ShaderProgram.pedantic = false;
		
		world = new World(new Vector2(0, -0), true);
		
		BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        
        CircleShape shape = new CircleShape();
        shape.setRadius(Particle.getRadius());
		
		for(int i = 0; i < loadedLevel.startAmount; i++) {
	        bodyDef.position.set(20 + i * 15, Gdx.graphics.getHeight() / 2);
	        
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
        
        generateWalls();
	}
	
	public void prerender() {
		allParticles.begin();
		//clear frame buffer
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//resize batch to this simulation size
		main.resizeBatch(width, height);
		
		main.batch.begin();
		
		main.batch.setShader(null);
		
		for(Particle particle: particles) {
			
			main.batch.draw(particleImage, particle.body.getPosition().x - Particle.getRadius(), particle.body.getPosition().y - Particle.getRadius(), Particle.getRadius() * 2, Particle.getRadius() * 2);
			
//			particleShapeRenderer.circle(particle.body.getPosition().x, particle.body.getPosition().y, Particle.getRadius());
		}
		
		main.batch.flush();
		allParticles.end();
		
		blur(allParticles);
		
		for(int i = 0; i < 30; i++) {
			blur(verticallyBlurredParticles);
		}
		
		//resize the batch back to normal
		main.resizeBatch(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0.768627451f, 0.768627451f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		main.batch.end();
		
	}
	
	public void render() {
		
		main.batch.begin();
		
		flattenShader.begin();

		//now do flatten
		main.batch.setShader(flattenShader);
		
		fboRegion.setTexture(verticallyBlurredParticles.getColorBufferTexture());
		
		main.batch.draw(fboRegion, offsetx, offsety);
		
		main.batch.end();
		flattenShader.end();
		
		//draw border
		
		main.shapeRenderer.begin(ShapeType.Filled);
		
		main.shapeRenderer.setColor(Color.BLACK);
		
		float thickness = 7;
		
		float leftLastDrawn = 0;
		float rightLastDrawn = 0;
		float topLastDrawn = 0;
		float bottomLastDrawn = 0;
		
		for(Hole hole : holes) {
			switch(hole.side) {
			case 0:
				main.shapeRenderer.rect(offsetx - thickness, offsety + leftLastDrawn, thickness, hole.start - leftLastDrawn);
				leftLastDrawn = hole.end;
				break;
			case 1:
				main.shapeRenderer.rect(offsetx + width, offsety + rightLastDrawn, thickness, hole.start - rightLastDrawn);
				rightLastDrawn = hole.end;
				break;
			case 2:
				main.shapeRenderer.rect(offsetx - thickness + topLastDrawn, offsety - thickness, hole.start - topLastDrawn, thickness);
				topLastDrawn = hole.end;
				break;
			case 3:
				main.shapeRenderer.rect(offsetx - thickness + bottomLastDrawn, offsety + height - thickness, hole.start - bottomLastDrawn, thickness);
				bottomLastDrawn = hole.end;
			}
		}
		
		if(leftLastDrawn < height) {
			main.shapeRenderer.rect(offsetx - thickness, offsety + leftLastDrawn, thickness, height - leftLastDrawn);
		}
		if(rightLastDrawn < height) {
			main.shapeRenderer.rect(offsetx + width, offsety + rightLastDrawn, thickness, height - rightLastDrawn);
		}
		
		if(topLastDrawn < width + thickness * 2) {
			main.shapeRenderer.rect(offsetx - thickness + topLastDrawn, offsety - thickness, width + thickness * 2 - topLastDrawn, thickness);
		}
		if(bottomLastDrawn < width + thickness * 2) {
			main.shapeRenderer.rect(offsetx - thickness + bottomLastDrawn, offsety + height - thickness, width + thickness * 2 - bottomLastDrawn, thickness);
		}
		
		//draw all boxes
		for(Box box : boxes) {
			main.shapeRenderer.rect(offsetx + box.x, offsety + box.y, box.width, box.height);
		}
		
		main.shapeRenderer.end();

	}
	
	public void resize(int width, int height) {
		offsetx = width/2 - fboRegion.getRegionWidth()/2;
		offsety = height/2 - fboRegion.getRegionHeight()/2;
	}
	
	public void blur(FrameBuffer start) {
		horizontallyBlurredParticles.begin();
		
		blurShader.begin();
		
		main.batch.setShader(blurShader);
		blurShader.setUniformf("dir", 1f, 0f);
		blurShader.setUniformf("resolution", width);
		
		fboRegion.setTexture(start.getColorBufferTexture());
		fboRegion.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		main.batch.draw(fboRegion, 0, 0);
		
		main.batch.flush();
		horizontallyBlurredParticles.end();
		
		verticallyBlurredParticles.begin();
		
		//now do a vertical blur
		main.batch.setShader(blurShader);
		blurShader.setUniformf("dir", 0f, 1f);
		blurShader.setUniformf("resolution", height);
		
		fboRegion.setTexture(horizontallyBlurredParticles.getColorBufferTexture());
		fboRegion.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		main.batch.draw(fboRegion, 0, 0);
		
		main.batch.flush();
		verticallyBlurredParticles.end();
		blurShader.end();
	}
	
	public void update() {
		world.step(1f, 6, 2);
		
		//check if any particles have left the area
		for(Particle particle : new ArrayList<Particle>(particles)) {
			if(particle.getPosition().x < 0 || particle.getPosition().x > width || particle.getPosition().y < 0 || particle.getPosition().y > height) {
				particles.remove(particle);
			}
		}
		
		if(Gdx.input.isButtonPressed(Buttons.LEFT)) {
			Vector3 mousePos = main.cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).sub(offsetx, offsety, 0);
			
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
				Vector3 mousePos = main.cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).sub(offsetx, offsety, 0);
				
				Vector2 dir = particles.get(i).getPosition().sub(mousePos.x, mousePos.y).nor();
				
				particles.get(i).body.applyForceToCenter(dir.scl(50000), true);
			}
		}
	}
	
	public void createWall(float x, float y, float width, float height) {
		BodyDef groundDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.KinematicBody;
        
        PolygonShape ground = new PolygonShape();
        ground.setAsBox(width/2, height/2);
        
        groundDef.position.set(x + width/2, y + height/2);
        
        Body body = world.createBody(groundDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = ground;
        fixtureDef.density = 0.00000001f;
        fixtureDef.friction = 1f;
        
        Fixture fixture = body.createFixture(fixtureDef);
        
        ground.dispose();
	}
	
	public void generateWalls() {
		float thickness = 7;
		
		float leftLastDrawn = 0;
		float rightLastDrawn = 0;
		float topLastDrawn = 0;
		float bottomLastDrawn = 0;
		
		for(Hole hole : holes) {
			switch(hole.side) {
			case 0:
				createWall(0, leftLastDrawn, thickness, hole.start - leftLastDrawn);
				leftLastDrawn = hole.end;
				break;
			case 1:
				createWall(width, rightLastDrawn, thickness, hole.start - rightLastDrawn);
				rightLastDrawn = hole.end;
				break;
			case 2:
				createWall(topLastDrawn - thickness, - thickness, hole.start - topLastDrawn, thickness);
				topLastDrawn = hole.end;
				break;
			case 3:
				createWall(bottomLastDrawn - thickness, height - thickness, hole.start - bottomLastDrawn, thickness);
				bottomLastDrawn = hole.end;
			}
		}
		
		if(leftLastDrawn < height) {
			createWall(0, leftLastDrawn, Particle.getRadius(), height - leftLastDrawn);
		}
		if(rightLastDrawn < height) {
			createWall(width, rightLastDrawn, Particle.getRadius(), height - rightLastDrawn);
		}
		
		if(topLastDrawn < width + thickness * 2) {
			createWall(- thickness + topLastDrawn, - thickness, width + thickness * 2 - bottomLastDrawn, thickness);
		}
		if(bottomLastDrawn < width + thickness * 2) {
			createWall(- thickness + topLastDrawn, height - thickness, width + thickness * 2 - bottomLastDrawn, thickness);
		}
		
		//create walls for the boxes
		
		for(Box box : boxes) {
			createWall(box.x, box.y, box.width, box.height);
		}
	}
}
