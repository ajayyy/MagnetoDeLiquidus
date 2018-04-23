package com.ajayinkingston.ld41;

public class Level5 extends LevelBase {
	public Level5() {
		level = 5;
		
		width = 400;
		height = 500;
		
		startAmount = 27;
		timeToHold = 6f;
		
		//add all of the holes
		holes.add(new Hole(0, height - 50, 0, true));
		holes.add(new Hole(0, width+14, 2, true));
		holes.add(new Hole(0, 200, 3, true));
		holes.add(new Hole(200, width, 3, false));
		
		//add all the boxes
		boxes.add(new Box(300, width - 100, 100, 100));
		
	}
}
