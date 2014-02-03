package main;

import javax.swing.*;

public class Reactions {
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){			
			public void run(){				
				//System.out.println("Running Frames.startPanels");
				Frames.startPanels();				
			}			
		});
	}	
}
