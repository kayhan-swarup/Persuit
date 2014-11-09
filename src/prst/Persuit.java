package prst;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Persuit extends JPanel{
	
	private double shooterX,shooterY;
	private double targetX,targetY;
	private double distance;
	public static Timer timer = new Timer();
	public static UpdateUITask updater;
	int nSeconds;
	double angle;
	public JLabel timeLabel = new JLabel();
	public JButton button = new JButton("Browse file for INPUT");
	public JButton showButton = new JButton();
	public Ellipse2D.Double target;
	public Rectangle2D.Double shooter;
	public String content="";
	public static double[][] targetLocations;
	public static ArrayList<Double> [] targetCoords = new ArrayList [2];
	NumberFormat formatter;  
	public Persuit(){
		formatter = new DecimalFormat("#0.0");
		targetCoords[0] = new ArrayList<Double>();
		targetCoords[1] = new ArrayList<Double>();
		
		button.setText("Brose file for input");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				read();
			}
		});
		showButton.setEnabled(false);
		showButton.setText("Show Result");
		showButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Runtime rt = Runtime.getRuntime();
				String path = "d:/persuit_record.txt";
				try {
					Process p = rt.exec("notepad "+path);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		shooterX=0;shooterY=50;
		targetX=100;targetY=0;
		shooter = new Rectangle2D.Double(getShooterX(),getShooterY(),20,30);
		target = new Ellipse2D.Double(getTargetX(),getTargetY(),40,20);
		updater = new UpdateUITask();
		initFile();
	}
	
	public void setAll(int s,double x,double y){
		this.nSeconds=s;
		this.targetX=x;
		this.targetY=y;
//		content += "\n"+"\nAt "+nSeconds+" seconds, Distance: "+formatter.format(distance)+"\n";
		target.setFrame(getTargetX(), getTargetY(), 40, 20);
		writeFile("\nTime: "+nSeconds+" s,     Target's Location: ["+formatter.format(targetX)+","+formatter.format(targetY)+"]     Distance: "+formatter.format(distance));
		
		repaint();
	}
	public void moveToTarget(){
		angle = Math.tanh((shooterY-targetY)/(shooterX/targetX));
		if(angle<0)
			angle*=-1;
		distance= Math.pow((Math.pow(shooterX-targetX,2)+Math.pow(shooterY-targetY, 2)), 0.5);
		double tempDis = distance - distance*5/100;
		double delX = (distance*Math.cos(angle)-tempDis*Math.cos(angle));
		double delY = (distance*Math.sin(angle)-tempDis*Math.sin(angle));
		if(shooterX<targetX)
			setShooterX(shooterX+delX);
		else setShooterX(shooterX-delX);
		if(shooterY<targetY)
			setShooterY(shooterY+delY);
		else setShooterY(shooterY-delY);
		
		shooter.setFrame(getShooterX(),getShooterY(),20,30);
		
		writeFile("\nShooter's Location: ["+formatter.format(shooterX)+","+formatter.format(shooterY)+"]");
//		content+="\nShooter's Location: "+shooterX+","+shooterY;
		repaint();
		
	}
	
	@Override
	public void paint(java.awt.Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(Color.BLUE);
		g2d.fill(shooter);
		g2d.setColor(Color.RED);
		g2d.fill(target);
//		setOpaque(false);
		setBackground(Color.GREEN);
		g2d.dispose();
	};
	
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(1200,700);
	};
	
	
	
public double getShooterX() {
		return shooterX*2.5;
	}





	public double getShooterY() {
		return 600-shooterY*2.5;
	}





	public double getTargetX() {
		return targetX*2.5;
	}





	public double getTargetY() {
		return 600-targetY*2.5;
	}





	public double getDistance() {
		return distance;
	}





	public void setShooterX(double shooterX) {
		this.shooterX = shooterX;
	}





	public void setShooterY(double shooterY) {
		this.shooterY = shooterY;
	}





	public void setTargetX(double targetX) {
		this.targetX = targetX;
	}





	public void setTargetY(double targetY) {
		this.targetY = targetY;
	}





	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	
	public static void main(String[] args){
		
		
		
		JFrame frame = new JFrame("Persuit");
		
		frame.setLayout(null);
//		frame.setSize(1600, 800);
		frame.setVisible(true);
		Persuit p = new Persuit();
		p.setBounds(0,0,1200,700);
		p.timeLabel.setBounds(400,700,300,40);
		p.button.setBounds(900,700,250,50);
		p.showButton.setBounds(50,700,100,50);
		frame.add(p);
		frame.add(p.timeLabel);
		frame.add(p.button);
		frame.add(p.showButton);
		frame.setSize(1200, 800);
		
	}


	private String promptForFile(){
		  JFileChooser fc=new JFileChooser();
		  int returnVal=fc.showOpenDialog(this);
		  if (returnVal == JFileChooser.APPROVE_OPTION) {
		    return fc.getSelectedFile().getAbsolutePath();
		  }
		 else {
		    return null;
		  }
		}
	public void initFile(){
		PrintWriter writer =null;
		try {
			writer= new PrintWriter(new FileWriter("d:/persuit_record.txt",false));
			writer.print("---------PERSUIT----------");
			writer.println();
			writer.close();
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void writeFile(String str){
		PrintWriter writer =null;
		try {
			writer= new PrintWriter(new FileWriter("d:/persuit_record.txt",true));
			writer.print(str);
			writer.println();
			writer.close();
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void read(){
		String path = promptForFile();
		try {
			FileInputStream fis = new FileInputStream(path);
			 DataInputStream in = new DataInputStream(fis);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Read File Line By Line
			  int i=0;
			  while ((strLine = br.readLine()) != null) {
			  // Print the content on the console
				  Scanner sc = new Scanner(strLine);
				  sc.nextInt();
				  targetCoords[0].add(sc.nextDouble());
				  targetCoords[1].add(sc.nextDouble());
			  System.out.println (strLine);
			  }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(updater,0, 100);
	}
	

private class UpdateUITask extends TimerTask {

        
        int counter = 0;
        public UpdateUITask(){
        	nSeconds = 0;
        }

        @Override
        public void run() {
            EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                	counter++;
                	
                		
                	
                	if(counter/10>nSeconds){
                		nSeconds=counter/10;
                		setAll(nSeconds,targetCoords[0].get(nSeconds),targetCoords[1].get(nSeconds));
                		moveToTarget();
                		}
                	else if(counter/10==nSeconds){
                		moveToTarget();
                    	}
                    
                    	timeLabel.setText("Time: "+String.valueOf(counter/10) +"s,  Distance: "+formatter.format(distance));
                    	if(counter/10 + 1>=targetCoords[0].size()){
//                    		writeFile(content);
                    		showButton.setEnabled(true);
                    		cancel();
                    	}	
                    	
                	}
                
                
                    
                
            });
        }
    }




}
