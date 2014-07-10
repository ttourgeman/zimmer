package org.home.zimmer.test;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.*;
import java.util.*;




public class SimpleReadTest implements Runnable, SerialPortEventListener {
    static CommPortIdentifier portId;
    static Enumeration portList;

    static InputStream inputStream;
    static SerialPort serialPort;
    static Thread readThread;

    public static void main(String[] args) {
        portList = CommPortIdentifier.getPortIdentifiers();
        
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            System.out.println("Posrt name: "+portId.getName());
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                 if (portId.getName().equals("COM6")) {
			//                if (portId.getName().equals("/dev/term/a")) {
                	 try {
                         serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
                         try {
							serialPort.setSerialPortParams(9600,
							         SerialPort.DATABITS_8,							      
							         SerialPort.STOPBITS_1,
							         SerialPort.PARITY_NONE
							         );
							serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
							serialPort.setDTR(false);
							serialPort.setRTS(false);
							
						} catch (UnsupportedCommOperationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                     
                     } catch (PortInUseException e) {System.out.println(e);}
                	 try {
                		 byte[] msg = new byte[2];
                 		msg[0] = '6';
                 		msg[1] = '4';
                		 OutputStream output = serialPort.getOutputStream();
                		 InputStream input = serialPort.getInputStream();
                		 try {
  							Thread.sleep(5000);
  						} catch (InterruptedException e1) {
  							// TODO Auto-generated catch block
  							e1.printStackTrace();
  						}
                		System.out.println("Sending command...");
                		
                		 output.write('e');
                		 output.flush(); 
                		output.write('[');
                		 output.flush();
                		 /*
                		 try {
							Thread.sleep(100);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						*/
                		  byte[] readBuffer = new byte[2];
                		 try {
                             
                                 int numBytes = input.read(readBuffer);
                                
                                 System.out.println("Got for comm "+numBytes+" bytes");
                                 String s = "";
                             for(byte c : readBuffer) {
                            	 s += c;
                             }
                             System.out.println(s);
                         } catch (IOException e) {System.out.println(e);}
                		 serialPort.close();
                		 output.close();
                		input.close();
                		
                	 }
                	 catch (IOException e) {System.out.println(e);              	 }
                  //  SimpleRead reader = new SimpleRead();
                }
            }
        }
        System.out.println("Test End");
    }

    public SimpleReadTest() {
        try {
            serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
        } catch (PortInUseException e) {System.out.println(e);}
        try {
            inputStream = serialPort.getInputStream();
        } catch (IOException e) {System.out.println(e);}
	try {
            serialPort.addEventListener(this);
	} catch (TooManyListenersException e) {System.out.println(e);}
        serialPort.notifyOnDataAvailable(true);
        try {
            serialPort.setSerialPortParams(9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {System.out.println(e);}
        readThread = new Thread(this);
        readThread.start();
    }
    
    public void run() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {System.out.println(e);}
    }

    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
        case SerialPortEvent.BI:
        case SerialPortEvent.OE:
        case SerialPortEvent.FE:
        case SerialPortEvent.PE:
        case SerialPortEvent.CD:
        case SerialPortEvent.CTS:
        case SerialPortEvent.DSR:
        case SerialPortEvent.RI:
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            break;
        case SerialPortEvent.DATA_AVAILABLE:
            byte[] readBuffer = new byte[20];

            try {
                while (inputStream.available() > 0) {
                    int numBytes = inputStream.read(readBuffer);
                }
                System.out.print(new String(readBuffer));
            } catch (IOException e) {System.out.println(e);}
            break;
        }
    }
}
