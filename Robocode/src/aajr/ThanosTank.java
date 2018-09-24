package aajr;

/**
 * Created by eahscs on 9/10/2018.
 */
/**
 * Copyright (c) 2001-2017 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 */

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;

/**
 * PaintingRobot - a sample robot that demonstrates the onPaint() and
 * getGraphics() methods.
 * Also demonstrate feature of debugging properties on RobotDialog
 * <p/>
 * Moves in a seesaw motion, and spins the gun around at each end.
 * When painting is enabled for this robot, a red circle will be painted
 * around this robot.
 *
 * @author Stefan Westen (original SGSample)
 * @author Pavel Savara (contributor)
 */

public class ThanosTank extends AdvancedRobot {
    int moveDirection = 10;
    int dir = 1;
    int i = 0;
    /**
     * PaintingRobot's run method - Seesaw
     */
    public void run() {
        while (true) {
            turnRadarRightRadians(Double.POSITIVE_INFINITY);
            setAdjustGunForRobotTurn(true);
            setAdjustRadarForGunTurn(true);
        }
    }

    public void onHitWall(HitWallEvent e) {
        dir=-dir;
    }

    public void onScannedRobot(ScannedRobotEvent e) {

        i += 7;
        setBodyColor( Color.getHSBColor( i / 360.0f, 1.0f, 1.0f ) );
        setGunColor( Color.getHSBColor( i / 360.0f, 1.0f, 1.0f ) );
        setRadarColor( Color.getHSBColor( i / 360.0f, 1.0f, 1.0f ) );
        setBulletColor( Color.getHSBColor( i / 360.0f, 1.0f, 1.0f ) );
        setScanColor( Color.getHSBColor( i / 360.0f, 1.0f, 1.0f ) );
        //setBodyColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
        //setGunColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
        //setRadarColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
        //setBulletColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
        //setScanColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
        setDebugProperty("lastScannedRobot", e.getName() + " at " + e.getBearing() + " degrees at time " + getTime());
        double absBearing=e.getBearingRadians()+getHeadingRadians();//enemies absolute bearing
        double latVel=e.getVelocity() * Math.sin(e.getHeadingRadians() -absBearing);//enemies later velocity
        double gunTurnAmt;//amount to turn our gun
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians());//lock on the radar
        if(Math.random()>.9){
            setMaxVelocity((12*Math.random())+12);//randomly change speed
        }
        if (e.getDistance() > 150) {//if distance is greater than 150
            gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+latVel/22);//amount to turn our gun, lead just a little bit
            setTurnGunRightRadians(gunTurnAmt); //turn our gun
            setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(absBearing-getHeadingRadians()+latVel/getVelocity()));//drive towards the enemies predicted future location
            setAhead((e.getDistance() - 140)*moveDirection*dir);//move forward
            setFire(3);//fire
        }
        else{//if we are close enough...
            gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+latVel/15);//amount to turn our gun, lead just a little bit
            setTurnGunRightRadians(gunTurnAmt);//turn our gun
            setTurnLeft(-90-e.getBearing()); //turn perpendicular to the enemy
            setAhead((e.getDistance() - 140)*moveDirection*dir);//move forward
            setFire(3);//fire
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        // demonstrate feature of debugging properties on RobotDialog
        setDebugProperty("lastHitBy", e.getName() + " with power of bullet " + e.getPower() + " at time " + getTime());

        // show how to remove debugging property
        setDebugProperty("lastScannedRobot", null);

        // gebugging by painting to battle view
        Graphics2D g = getGraphics();

        g.setColor(Color.orange);
        g.drawOval((int) (getX() - 55), (int) (getY() - 55), 110, 110);
        g.drawOval((int) (getX() - 56), (int) (getY() - 56), 112, 112);
        g.drawOval((int) (getX() - 59), (int) (getY() - 59), 118, 118);
        g.drawOval((int) (getX() - 60), (int) (getY() - 60), 120, 120);


    }

    /**
     * Paint a red circle around our PaintingRobot
     */
    public void onPaint(Graphics2D g) {
        g.setColor(Color.red);
        g.drawOval((int) (getX() - 50), (int) (getY() - 50), 100, 100);
        g.setColor(new Color(0, 0xFF, 0, 30));
        g.fillOval((int) (getX() - 60), (int) (getY() - 60), 120, 120);
    }
}

public class WaveBullet {
    private double startX, startY, startBearing, power;
    private long fireTime;
    private int direction;
    private int[] returnSegment;

    public WaveBullet(double x, double y, double bearing, double power,
                      int direction, long time, int[] segment) {
        startX = x;
        startY = y;
        startBearing = bearing;
        this.power = power;
        this.direction = direction;
        fireTime = time;
        returnSegment = segment;
    }

    public double getBulletSpeed()
    {
        return 20 - power * 3;
    }

    public double maxEscapeAngle()
    {
        return Math.asin(8 / getBulletSpeed());
    }


}

