package com.ajayinkingston.ld41;

public class Level3 extends LevelBase {
	public Level3() {
		level = 3;
		
		width = 400;
		height = 500;
		
		startAmount = 27;
		timeToHold = 10f;
		
		//add all of the holes
		holes.add(new Hole(0, 100, 1, false));
		holes.add(new Hole(0, 150, 2, true));
		
		//add all the boxes
		boxes.add(new Box(350, 100, 50, 50));
		
	}
}
