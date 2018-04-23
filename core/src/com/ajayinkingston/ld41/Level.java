package com.ajayinkingston.ld41;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class Level {
	
	Main main;
	
	ParticleBox particleBox;
	
	Clouds clouds;
	
	ProgressBar particlesLeft;
	
	public Level(Main main, LevelBase levelLoaded) {
		this.main = main;
		
		particleBox = new ParticleBox(main, levelLoaded);
		
		clouds = new Clouds(main);
		
		particlesLeft = new ProgressBar(30, Gdx.graphics.getHeight() - 100, 200, 50, new Color(45/255f, 155/255f, 56/255f, 1), main);
	}
	
	public void render() {
		
		particleBox.prerender();
		
		clouds.render();
		
		particleBox.render();
		
		particlesLeft.render(particleBox.particles.size() / (float)particleBox.loadedLevel.startAmount);
	}
	
	public void update() {
		particleBox.update();
		
		clouds.update();
	}
	
	public void resize(int width, int height) {
		particleBox.resize(width, height);
	}
}
