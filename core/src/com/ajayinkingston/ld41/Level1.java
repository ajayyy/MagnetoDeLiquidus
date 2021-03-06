package com.ajayinkingston.ld41;

public class Level1 extends LevelBase {
	public Level1() {
		level = 1;
		
		width = 400;
		height = 500;
		
		startAmount = 25;
		timeToHold = 5;
		
		//add all of the holes
		holes.add(new Hole(150, 300, 2, false));
		
		holes.add(new Hole(400, 450, 1, false));
		
		//add all the boxes
		boxes.add(new Box(100, 200, 100, 100));
		
	}
}
