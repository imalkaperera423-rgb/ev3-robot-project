package src;

import lejos.hardware.Button;
import lejos.utility.Delay;

public class Controller {
    public static void main(String[] args) {

        Motors motors = new Motors();
        LightSensor ls = new LightSensor();
        UltrasonicSensor uss = new UltrasonicSensor();
        boolean avoiding = false;

        long measureStart = 0;
        long measureDuration = 0;
        float obstacleTimeLeft = 0;
        float obstacleTimeRight = 0;

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
            if(SharedData.distance <= 0.08){SharedData.obstacle = true; avoiding = true;}
            else{SharedData.obstacle = false;}

            //Obstacle avoidance
           if(avoiding == true){
                usThread.setPriority(8);
                lsThread.setPriority(2);
                motors.obstacleTurnLeft();
                measureStart = System.currentTimeMillis();
                if(SharedData.distance >= 0.4){
                    measureDuration = System.currentTimeMillis() - measureStart;
                    obstacleTimeLeft = measureDuration;
                    motors.stop();
                    
                    if(avoiding == true){
                        motors.obstacleTurnRight();
                        Delay.msDelay(measureDuration);
                        measureStart = System.currentTimeMillis();
                        if(SharedData.distance >= 0.4){
                            measureDuration = System.currentTimeMillis() - measureStart;
                            obstacleTimeRight = measureDuration;
                            motors.stop();
                        }
                    }
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
}
