package com.ajayinkingston.ld41;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class ProgressBar {
	
	float x, y;
	
	float width, height;
	
	Color color;
	
	Main main;
	
	public ProgressBar(float x, float y, float width, float height, Color color, Main main) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		
		this.main = main;
	}
	
	public void render(float amount) {
		main.shapeRenderer.begin(ShapeType.Filled);
		
		main.shapeRenderer.setColor(Color.BLACK);

		main.shapeRenderer.rect(x - 5, y - 5, width + 10, height + 10);
		
		main.shapeRenderer.setColor(color);
		
		main.shapeRenderer.rect(x, y, width * amount, height);
		
		main.shapeRenderer.end();
	}
	
	public void update() {
		
	}
}
