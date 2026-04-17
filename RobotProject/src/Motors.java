package src;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class Motors {
    
    EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.C);
    EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.D);

    public void forward(){
        leftMotor.setSpeed(180);   // Set to 360 for real deal
        rightMotor.setSpeed(180);   // Set to 360 for real deal
        leftMotor.forward();
        rightMotor.forward();
    }
    public void stop(){
        leftMotor.stop();
        rightMotor.stop();
    }
    public void obstacleTurnLeft(){
        rightMotor.setSpeed(90);   // Set to 180 for real deal
        leftMotor.setSpeed(90);    // Set to 180 for real deal
        rightMotor.forward();
        leftMotor.backward();
    }
    public void obstacleTurnRight(){
        leftMotor.setSpeed(90);    // Set to 180 for real deal
        rightMotor.setSpeed(90);   // Set to 180 for real deal
        rightMotor.backward();
        leftMotor.forward();
    }
    public void turnLeft(){
        rightMotor.setSpeed(180);   // Set to 360 for real deal
        leftMotor.setSpeed(0);    // Set to 90? for real deal
        rightMotor.forward();
        leftMotor.forward();
    }
    public void turnRight(){
        leftMotor.setSpeed(180);    // Set to 360 for real deal
        rightMotor.setSpeed(0);   // Set to 90? for real deal
        rightMotor.forward();
        leftMotor.forward();
    }
}
