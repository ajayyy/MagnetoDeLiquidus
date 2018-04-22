package com.ajayinkingston.ld41;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Clouds {
	
	Main main;
	
	Texture[] clouds = new Texture[3];
	
	Vector2[] positions = new Vector2[3];
	
	float[] speeds = {5, 15, 25};
	
	Random random = new Random();

	public Clouds(Main main) {
		this.main = main;
		
		for(int i = 0; i < clouds.length; i++) {
			
			clouds[i] = new Texture("cloud" + i + ".png");
			
			positions[i] = new Vector2(i * 500, (float) (random.nextInt(Gdx.graphics.getHeight() - clouds[i].getHeight())));
		}
	}
	
	public void render() {
		main.batch.begin();
		
		main.batch.setShader(null);
		
		for(int i = 0; i < clouds.length; i++) {
			main.batch.draw(clouds[i], positions[i].x, positions[i].y);
		}
		
		main.batch.end();
	}
	
	public void update() {
		for(int i = 0; i < positions.length; i++) {
			positions[i].x += speeds[i] * Gdx.graphics.getDeltaTime();
			
			if(positions[i].x > Gdx.graphics.getWidth()) {
				positions[i].y = (float) (random.nextInt(Gdx.graphics.getHeight() - clouds[i].getHeight()));
				positions[i].x = -clouds[i].getWidth();
			}
		}
	}
}
