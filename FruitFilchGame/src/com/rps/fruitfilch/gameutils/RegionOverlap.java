package com.rps.fruitfilch.gameutils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class RegionOverlap {
		
	public static boolean pointInRectangle (final Rectangle r, final Vector2 touchPoint) {
		return r.x <= touchPoint.x && r.x + r.width >= touchPoint.x && r.y <= touchPoint.y && r.y + r.height >= touchPoint.y;
	}
}
