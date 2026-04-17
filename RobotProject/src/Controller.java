package src;

import lejos.hardware.Button;
import lejos.utility.Delay;

public class Controller {
    public static void main(String[] args) {

        Motors motors = new Motors();
        LightSensor ls = new LightSensor();
        UltrasonicSensor uss = new UltrasonicSensor();
        boolean avoiding = false;
        boolean measuringLeft = false;
        boolean measuringRight = false;
        boolean measuringStartRight = false;
        boolean measuringComplete = false;

        long measureStart = 0;
        long obstacleTimeLeft = 0;
        long obstacleTimeRight = 0;

        Thread lsThread = new Thread(ls);
        Thread usThread = new Thread(uss);

        lsThread.setPriority(8);
        usThread.setPriority(3);
        lsThread.start();
        usThread.start();

        Delay.msDelay(8000);

        motors.forward();
        
            while (!Button.ESCAPE.isDown()){
            //Obstacle detection
            if(SharedData.distance <= 0.08){SharedData.obstacle = true; measuringLeft = true;}
            else{SharedData.obstacle = false;}

            //Obstacle avoidance: measure left
           if(avoiding == false && measuringLeft == true){
                avoiding = true;
                usThread.setPriority(8);
                lsThread.setPriority(2);
                motors.obstacleTurnLeft();
                measureStart = System.currentTimeMillis();
           }
           if(SharedData.distance >= 0.5 && measuringLeft == true){
                    obstacleTimeLeft = System.currentTimeMillis() - measureStart;
                    motors.stop();
                    measuringStartRight = true;
                }
                    //Measure right
                    if(measuringStartRight == true && avoiding == true){
                        motors.obstacleTurnRight();
                        Delay.msDelay(obstacleTimeLeft);
                        motors.stop();
                        Delay.msDelay(500);
                        motors.obstacleTurnRight();
                        measureStart = System.currentTimeMillis();
                        measuringRight = true;
                        measuringStartRight = false;
                    }
                    
                    if(SharedData.distance >= 0.5 && measuringRight == true && measuringStartRight == false){
                         obstacleTimeRight = System.currentTimeMillis() - measureStart;
                        motors.stop();
                        measuringRight = false;
                        measuringLeft = false;
                        measuringComplete = true;
                        Delay.msDelay(500);
                    }
                        
                    if(avoiding == true && measuringComplete == true){
                        if(obstacleTimeLeft >= obstacleTimeRight){
                            motors.turnRight();
                        }
                        else{
                            motors.turnLeft();
                        }
                        continue;
                    
                    }
                }    

             //Line following 
            usThread.setPriority(3);
            lsThread.setPriority(8);
            //Make intensities >= 0.03 and <= 0.01 respectively for real deal
            if(SharedData.intensity >= 0.03){
                motors.turnLeft();
            }
            else{motors.forward();}
            if (SharedData.intensity <= 0.02){
                motors.turnRight();
            }
            else{motors.forward();}
        }

    }
