package com.ajayinkingston.ld41;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Level {
	
	Main main;
	
	ParticleBox particleBox;
	
	Clouds clouds;
	
	ProgressBar particlesLeft;
	ProgressBar timeLeft;
	
	BitmapFont font;
	
	public Level(Main main, LevelBase levelLoaded) {
		this.main = main;
		
		font = new BitmapFont(Gdx.files.internal("arial.fnt"), false);
		
		particleBox = new ParticleBox(main, levelLoaded);
		
		clouds = new Clouds(main);
		
		particlesLeft = new ProgressBar(30, Gdx.graphics.getHeight() - 100, 200, 50, new Color(45/255f, 155/255f, 56/255f, 1), main);
		timeLeft = new ProgressBar(30, Gdx.graphics.getHeight() - 200, 200, 50, new Color(93/255f, 117/255f, 239/255f, 1), main);
	}
	
	public void render() {
		
		particleBox.prerender();
		
		clouds.render();
		
		particleBox.render();
		
		particlesLeft.render(particleBox.particles.size() / (float)particleBox.loadedLevel.startAmount);
		
		timeLeft.render(1 - particleBox.timeHolding / (float)particleBox.loadedLevel.timeToHold);
		
		main.batch.begin();
		main.batch.setShader(null);
		
		switch(particleBox.loadedLevel.level) {
			case 1:
				font.getData().setScale(0.13f);
				font.draw(main.batch, "Click to repel the liquid metal", 30, 75);
				font.getData().setScale(0.17f);
				font.draw(main.batch, "Push the liquid metal outside of the box", 30, 40);
				break;
			case 2:
				font.getData().setScale(0.18f);
				font.draw(main.batch, "Watch out! You can only repel for a certain amount of time", 30, 35);
				break;
			case 3:
				font.getData().setScale(0.18f);
				font.draw(main.batch, "Don't go to the red!!! You will immediatly lose.", 30, 35);
				break;
			case 5:
				font.getData().setScale(0.18f);
				font.draw(main.batch, "EXTREME!!", 30, 35);
				break;
			case 6:
				font.getData().setScale(0.3f);
				font.draw(main.batch, "Congrats! You won! Thanks for playing :)", Gdx.graphics.getWidth()/2 - 400, Gdx.graphics.getHeight()/2);
				break;
		}

		main.batch.end();
	}
	
	public void update() {
		particleBox.update();
		
		clouds.update();
	}
	
	public void resize(int width, int height) {
		particleBox.resize(width, height);
	}
}
