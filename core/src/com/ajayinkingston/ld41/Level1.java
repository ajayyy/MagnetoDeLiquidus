package com.ajayinkingston.ld41;

public class Level1 extends LevelBase {
	public Level1() {
		width = 400;
		height = 500;
		
		//add all of the holes
		holes.add(new Hole(150, 300, 2));
		
		holes.add(new Hole(400, 450, 1));
		
		//add all the boxes
		boxes.add(new Box(100, 200, 100, 100));
	}
}
