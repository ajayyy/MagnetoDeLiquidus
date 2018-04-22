package com.ajayinkingston.ld41;

public class Level {
	
	Main main;
	
	ParticleBox particleBox;
	
	public Level(Main main) {
		this.main = main;
		
		particleBox = new ParticleBox(main, 400, 500);
	}
	
	public void render() {
		particleBox.render();
	}
	
	public void update() {
		
	}
	
	public void resize(int width, int height) {
		particleBox.resize(width, height);
	}
}
