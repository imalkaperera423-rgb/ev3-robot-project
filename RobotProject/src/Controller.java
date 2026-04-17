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
        boolean firstCheck = false;

        long measureStart = 0;
        long obstacleTimeLeft = 0;
        long obstacleTimeRight = 0;

        Thread lsThread = new Thread(ls);
        Thread usThread = new Thread(uss);

        lsThread.setPriority(8);
        usThread.setPriority(3);
        lsThread.start();
        usThread.start();

        //Start delay
        Delay.msDelay(8000);
        motors.forward();
        
        while (!Button.ESCAPE.isDown()){
            //Obstacle detection
            if(SharedData.distance <= 0.15 && avoiding == false){SharedData.obstacle = true; measuringLeft = true;}
            else{SharedData.obstacle = false;}

            //Obstacle avoidance: measure left
           if(avoiding == false && measuringLeft == true){
                avoiding = true;
                usThread.setPriority(8);
                lsThread.setPriority(2);
                motors.obstacleTurnLeft();
                measureStart = System.currentTimeMillis();
           }

           if(SharedData.distance >= 0.6 && measuringLeft == true && firstCheck == false){
            firstCheck = true;
            Delay.msDelay(10);
           }
           if(SharedData.distance < 0.6 && measuringLeft == true && firstCheck == true){
            firstCheck = false;
           }

           if(SharedData.distance >= 0.6 && measuringLeft == true && firstCheck == true){
                    obstacleTimeLeft = System.currentTimeMillis() - measureStart;
                    motors.stop();
                    measuringStartRight = true;
                    measuringLeft = false;
                    firstCheck = false;
            }
            
            //Measure right
            if(measuringStartRight == true && avoiding == true){
                motors.obstacleTurnRight();
                measuringStartRight = false;
                Delay.msDelay(obstacleTimeLeft);
                motors.stop();
                Delay.msDelay(50);
                motors.obstacleTurnRight();
                measureStart = System.currentTimeMillis();
                measuringRight = true;
                }
                    
            if(SharedData.distance >= 0.6 && measuringRight == true && measuringStartRight == false && firstCheck == false){
                firstCheck = true;
                Delay.msDelay(10);
            }
            if(SharedData.distance < 0.6 && measuringRight == true && measuringStartRight == false && firstCheck == true){
                firstCheck = false;
            }

            if(SharedData.distance >= 0.6 && measuringRight == true && measuringStartRight == false && firstCheck == true){
                obstacleTimeRight = System.currentTimeMillis() - measureStart;
                motors.stop();
                measuringRight = false;
                measuringLeft = false;
                measuringComplete = true;
                firstCheck = false;
                Delay.msDelay(50);
                }
                        
            if(avoiding == true && measuringComplete == true && obstacleTimeLeft >= obstacleTimeRight){
                motors.turnRight();
                Delay.msDelay(5000);
                motors.stop();
                //Remove this
                avoiding = false;
                continue; 
                }   
            else if(avoiding == true && measuringComplete == true && obstacleTimeLeft < obstacleTimeRight){
                motors.turnLeft();
                Delay.msDelay(5000);
                motors.stop();
                //Remove this
                avoiding = false;
                continue; 
                }
              
             //Line following
            if(avoiding == false){ 
            usThread.setPriority(3);
            lsThread.setPriority(8);
            }
            //Make intensities >= 0.025 and <= 0.01 respectively for real deal
            if(SharedData.intensity >= 0.02 && avoiding == false){
                motors.turnLeft();
            }
            if (SharedData.intensity < 0.02 && avoiding == false){
                motors.turnRight();
            }
        }
    }
}

    
