package com.rps.fruitfilch;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Trajectory {
	public final int NO_OF_DOTS = 60;
	
	private ArrayList<Image> trajectoryDots;
	private TextureRegion dotRegion;
	private final Vector2 gravity;
	private boolean trajectoryOnStage = false;
	private Vector2 dotPos;
	private Stage stage;
	
	public Trajectory(Stage stage, final TextureRegion dotRegion, final Vector2 worldGravity) {
		this.stage = stage;
		this.dotRegion = new TextureRegion(dotRegion);
		this.gravity = new Vector2(worldGravity);
		this.trajectoryDots = new ArrayList<Image>();
		for (int i = 0; i < NO_OF_DOTS; ++i) {
			trajectoryDots.add(new Image(this.dotRegion));
		}
	}
	
	/** Draws a complete trajectory (composed of dots) on the stage
	 * @param startPosition
	 * @param fruitStartVelocity */
	public void drawTrajectory(final Vector2 startPosition, final Vector2 fruitStartVelocity) {
		int i = 0;
		if(!trajectoryOnStage) {
			for(Image current: trajectoryDots) {
				dotPos = getTrajectoryPoint(startPosition, fruitStartVelocity, i++);
				current.x = dotPos.x - current.width / 2;
				current.y = dotPos.y - current.height / 2;
				stage.addActor(current);
			}
			trajectoryOnStage = true;
		}
		else {
			for(Image current: trajectoryDots) {
				dotPos = getTrajectoryPoint(startPosition, fruitStartVelocity, i++);
				current.x = dotPos.x - current.width / 2;
				current.y = dotPos.y - current.height / 2;
			}
		}
	}
	
	/** Removes the whole trajectory from the stage */
	public void removeTrajectory() {
		if(trajectoryOnStage) {
			for(Image current: trajectoryDots) {
				stage.removeActor(current);
			}
			trajectoryOnStage = false;
		}
	}
	
	/** This method returns a new dot for the trajectory. Note that the trajectory is composed of individual dots.
	 * @param startingPosition
	 * @param startingVelocity
	 * @param n (the nth dot)
	 * @return a single Vector2 trajectory dot */
	private Vector2 getTrajectoryPoint(final Vector2 startingPosition, final Vector2 startingVelocity, int n) {
		float t = 1 / 2.0f;
		Vector2 tmpStartVel = startingVelocity.cpy();
		tmpStartVel.mul(9.0500f);
		Vector2 tmpGravity = gravity.cpy();
		
		Vector2 stepVelocity = tmpStartVel.mul(t);
		Vector2 stepGravity = tmpGravity.mul(t * t);
		
		Vector2 returnVector = new Vector2(stepGravity);
		returnVector.mul(n*n+n);
		returnVector.mul(0.5f);
		returnVector.add(stepVelocity.mul(n));
		returnVector.add(startingPosition);
		return returnVector;
	}

}

//------------------------------------For Reference------------------------------------------

/*b2Vec2 getTrajectoryPoint( b2Vec2& startingPosition, b2Vec2& startingVelocity, float n )
{
    //velocity and gravity are given per second but we want time step values here
    float t = 1 / 60.0f; // seconds per time step (at 60fps)
    b2Vec2 stepVelocity = t * startingVelocity; // m/s
    b2Vec2 stepGravity = t * t * m_world->GetGravity(); // m/s/s

    return startingPosition + n * stepVelocity + 0.5f * (n*n+n) * stepGravity;
}*/

/*glColor3f(1,1,0);
glBegin(GL_LINES);
for (int i = 0; i < 180; i++) { // three seconds at 60fps
    b2Vec2 trajectoryPosition = getTrajectoryPoint( startingPosition, startingVelocity, i );
    glVertex2f(trajectoryPosition.x, trajectoryPosition.y );
}
glEnd();*/
