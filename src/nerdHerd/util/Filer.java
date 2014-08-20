package nerdHerd.util;


import com.sun.squawk.io.BufferedReader;
import com.sun.squawk.microedition.io.FileConnection;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import javax.microedition.io.Connector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author student
 */
public class Filer {

    private OutputStream theFile;
    private FileConnection fc;
    private String fileName;
    private BufferedReader input;
    private StringBuffer currentString;
    private double matchNumber = 0;
    
    public Filer(){
        matchNumber = 0;
        SmartDashboard.putDouble("MatchNumber", -9001.0);
    }
    
    public void connect(){
        while(SmartDashboard.getDouble("MatchNumber", -9001.0) ==  -9001.0){
            Timer.delay(0.001);
            System.out.println("SmartDashboard");
            matchNumber = SmartDashboard.getDouble("MatchNumber", -9001.0);
        }
            try {
                    try{
                        fileName = "file:///Match" + matchNumber + ".txt";
                        fc = (FileConnection)Connector.open(fileName, Connector.READ_WRITE);
                        fc.create();
                    }catch(IOException e){        
                        System.out.println(e);
                    }
                    theFile = Connector.openOutputStream(fileName);
                } catch (Exception e) {
                }
            System.out.println("FileName: " + fileName);
    } 
    
    public void println(String line){
        byte bytes[] = (line + "\n").getBytes();
        try{
            theFile.write(bytes);
        }catch(Exception e){
        }
    }
    
    public void print(String line){
        byte bytes[] = (line).getBytes();
        try{
            theFile.write(bytes);
        }catch(Exception e){
        }
    }
    
    public void close(){
        try{
            theFile.close();
        }catch(Exception e){
        }
    }
}
