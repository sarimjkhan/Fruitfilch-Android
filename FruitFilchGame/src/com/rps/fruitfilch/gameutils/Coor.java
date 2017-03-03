package com.rps.fruitfilch.gameutils;

public class Coor {
	
	public int x, y, width, height;
	public float rotation;
	
	public Coor(int x, int y, int width, int height) {
		this.x = x; this.y = y; this.width = width; this.height = height;
	}
	
	public Coor(int x, int y, int width, int height, float rotation) {
		this.x = x; this.y = y; this.width = width; this.height = height;
		this.rotation = rotation;
	}

}
