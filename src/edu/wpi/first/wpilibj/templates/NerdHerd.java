/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import nerdHerd.util.Filer;
/**
 * The VM is configured to automatically run this class,and to call the
 * functions corresponding to each mode,as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project,you must also update the manifest file in the resource
 * directory.
 */
public class NerdHerd extends IterativeRobot {
    CANJaguar motor1,motor2,motor3;
    Encoder encode;
    Joystick joy;
    Timer time;
    boolean shooting = false;
    boolean retracting = false;
    double power = 0;
    double desired = 63;
    double error = 0;
    double p = .2;
    double can1Volt,can1Current,can2Volt,can2Current,can3Volt,can3Current;
    double currentTime;
    private Filer filer;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
      encode = new Encoder(1,2);
      encode.start();
      encode.reset();
        try{
        motor1 = new CANJaguar(2);
        motor2 = new CANJaguar(5);
        motor3 = new CANJaguar(4);
        }catch(Exception e){
            System.out.println(e);
        }
        time = new Timer();
        time.start();
        joy = new Joystick(1);
        filer = new Filer();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    
    public void teleopInit(){
        filer.connect();
        filer.println("Time" + "," + "CAN1Volt" + "," + "CAN2Volt" + "," + "CAN3Volt" + "," + "CAN1Current" + "," + "CAN2Current" + "," + "CAN3Current" + "," + "Encoder Pulses");
        time.reset();
    }
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        shooting = joy.getRawButton(3);
        
       try{
        can1Volt = motor1.getOutputVoltage();
        can1Current = motor1.getOutputCurrent();
        can2Volt = motor2.getOutputVoltage();
        can2Current = motor2.getOutputCurrent();
        can3Current = motor3.getOutputCurrent();
        can3Volt = motor3.getOutputVoltage();
       }catch(Exception e){
            System.out.println(e);
       }
      
     if(shooting == true && retracting == false){
        Shoot();
     }else{
        Retract();   
     }
     motor1.set(power);
     motor2.set(power);
     motor3.set(power);
       double m_time = time.get();
       System.out.println(m_time + "," + can1Volt + "," + can1Current + "," + can2Volt + "," + can2Current + "," + can3Volt + "," + can3Current + "," + encode.get());
       filer.println(m_time + "," + can1Volt + "," + can2Volt + "," + can3Volt + "," + can1Current + "," + can2Current + "," + can3Current  + "," + encode.get());
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    /*
    private double getRPM(){
        double rpm = 0;                                           //Declares an array of integers which the function will return.
        currentTime = time.get();                                 //Sets currentTime to the time calculatged from FPGA
        rpm = encode.getRaw() / (15000*currentTime);              //dAngle*(60000000us/360deg)/dTime = RPM                                                        //Resets encoder.                                                       //Resets the right wheel encoder.
        time.reset();
        encode.reset();
        return rpm;
    
    }
    */
    private void Shoot(){
       
        if(encode.get()>= desired){
            shooting = false;
            retracting = true;
        }else{
        error = desired-encode.get();
        power = Threshold(p*error);
        }
    }
    private double Threshold(double value){
        if(value > 1.0){
            return 1.0;
        }else if(value<-1){
            return -1;
        }else{
            return value;
        }
    }
    private void Retract(){
        if(encode.get()>0){
           power = -.1;
           
        }else if(encode.get() <= 0){
            power = 0;
            shooting = false;
            retracting = false;
        }
    }
}