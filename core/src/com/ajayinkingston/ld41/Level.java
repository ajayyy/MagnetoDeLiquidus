package com.ajayinkingston.ld41;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Level {
	
	Main main;
	
	ParticleBox particleBox;
	
	Texture cloud;
	
	public Level(Main main) {
		this.main = main;
		
		particleBox = new ParticleBox(main, 400, 500);
		
		cloud = new Texture("cloud0.png");
	}
	
	public void render() {
		
		particleBox.prerender();
		
		main.batch.begin();
		
		main.batch.setShader(null);
		
		main.batch.draw(cloud, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		
		main.batch.end();
		
		particleBox.render();
		
	}
	
	public void update() {
		particleBox.update();
	}
	
	public void resize(int width, int height) {
		particleBox.resize(width, height);
	}
}
