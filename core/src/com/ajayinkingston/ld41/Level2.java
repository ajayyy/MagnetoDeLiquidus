package com.ajayinkingston.ld41;

public class Level2 extends LevelBase {
	public Level2() {
		level = 2;
		
		width = 400;
		height = 500;
		
		startAmount = 27;
		timeToHold = 2.5f;
		
		//add all of the holes
		holes.add(new Hole(0, 50, 1, false));
		
		//add all the boxes
		boxes.add(new Box(300, 50, 100, 100));
		
	}
}
