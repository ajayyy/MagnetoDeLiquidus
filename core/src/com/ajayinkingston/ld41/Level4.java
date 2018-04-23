package com.ajayinkingston.ld41;

public class Level4 extends LevelBase {
	public Level4() {
		level = 4;
		
		width = 400;
		height = 500;
		
		startAmount = 27;
		timeToHold = 7f;
		
		//add all of the holes
		holes.add(new Hole(0, 100, 0, false));
		holes.add(new Hole(0, 150, 2, true));
		
		//add all the boxes
		boxes.add(new Box(0, 100, 50, 50));
		
	}
}
