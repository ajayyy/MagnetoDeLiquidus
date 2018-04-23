package com.ajayinkingston.ld41;

import java.util.ArrayList;

public class LevelBase {
	int level;
	
	ArrayList<Hole> holes = new ArrayList<Hole>();
	ArrayList<Box> boxes = new ArrayList<Box>();
	
	int width;
	int height;
	
	int startAmount;
	float timeToHold;
}
