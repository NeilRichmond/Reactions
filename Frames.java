package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

//Set up the Frames class with listeners so we can get user input

public class Frames implements ActionListener , ItemListener
{
	
	public int iTargetSpeed = 200;														//The default value for target speed
	public int iTargetSize = 20;														//The default value for target size
	
	public boolean bEnableTeleport = false;												//Default setting for teleporting
	public boolean bEnableRecoil = false;												//Default setting for recoil
	
	public static String frameTitle = "Reactions";
	
	JPanel reactionsPanel = new JPanel();												//Main JPanel
	JPanel textPanel, buttonPanel, textInputPanel, radioButtons, checkBoxPanel;			//Define all panels and features
	JLabel speedLabel, sizeLabel, teleportLabel, recoilLabel;
	JTextField speedField, sizeField;
	JButton btnLaunch;
	JCheckBox teleportCB, recoilCB;
		
	public static void startPanels()
	{		
		//System.out.println("Running createAndShowGUI");
		createAndShowGUI();		
	}
	
	public void panelLabels()
	{		
		//All panels dealing with displaying text is done in this function
		
		//Panel to stick JLabels on
		JPanel textPanel = new JPanel();
		textPanel.setLayout(null);
		textPanel.setLocation(0, 0);
		textPanel.setSize(150, 125);
		reactionsPanel.add(textPanel);
				
		//Labels
		JLabel speedLabel = new JLabel("Target Speed");
		speedLabel.setLocation(10,5);
		speedLabel.setSize(100,30);
		speedLabel.setHorizontalAlignment(0);
		textPanel.add(speedLabel);
				
		JLabel sizeLabel = new JLabel("Target Size");
		sizeLabel.setLocation(4,35);
		sizeLabel.setSize(100,30);
		sizeLabel.setHorizontalAlignment(0);
		textPanel.add(sizeLabel);
		
		JLabel teleportLabel = new JLabel("Enable Teleporting?");
		teleportLabel.setLocation(4,65);
		teleportLabel.setSize(120,30);
		teleportLabel.setHorizontalAlignment(0);
		textPanel.add(teleportLabel);
		
		JLabel recoilLabel = new JLabel("Simulate Recoil?");
		recoilLabel.setLocation(4,95);
		recoilLabel.setSize(120,30);
		recoilLabel.setHorizontalAlignment(0);
		textPanel.add(recoilLabel);
		
		//System.out.println("Finished panelLabels");		
	}
	
	public void panelButtons()
	{		
		//All panels dealing with displaying buttons is done in this function

		//Panel for buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		buttonPanel.setLocation(0,125);
		buttonPanel.setSize(300, 60);
		reactionsPanel.add(buttonPanel);
		
		JButton btnLaunch = new JButton("Launch Game");
		btnLaunch.setLocation(75,10);
		btnLaunch.setSize(150,40);
		btnLaunch.addActionListener(this);
		btnLaunch.setActionCommand("btnLaunch");
		buttonPanel.add(btnLaunch);
		
		//System.out.println("Finished panelButtons");		
	}
	
	public void panelTextInput()
	{		
		//All panels dealing with displaying text boxes is done in this function
		
		JPanel textInputPanel = new JPanel();
		textInputPanel.setLayout(null);
		textInputPanel.setLocation(150,0);
		textInputPanel.setSize(150, 60);
		reactionsPanel.add(textInputPanel);
		
		speedField = new JTextField(9);
		speedField.setLocation(0,0);
		speedField.setSize(150,30);
		speedField.setToolTipText("The speed of the target. Capped at 1000.");
		textInputPanel.add(speedField);
		
		sizeField = new JTextField(9);
		sizeField.setLocation(0,30);
		sizeField.setSize(150,30);
		sizeField.setToolTipText("THe size of the target. Capped at 100.");
		textInputPanel.add(sizeField);
		
		//System.out.println("Finished panelTextInput");		
	}
	
	public void panelCheckBoxes()
	{
		//All panels dealing with displaying text boxes is done in this function
		
		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(null);
		checkBoxPanel.setLocation(150, 60);
		checkBoxPanel.setSize(150, 60);;
		reactionsPanel.add(checkBoxPanel);
		
		teleportCB = new JCheckBox("");
		teleportCB.setLocation(5, 5);
		teleportCB.setSize(30, 30);	
		teleportCB.addItemListener(this);
		teleportCB.setToolTipText("Do you want the target teleport to a random location after you score a hit?");
		checkBoxPanel.add(teleportCB);
		
		recoilCB = new JCheckBox("");
		recoilCB.setLocation(5, 35);
		recoilCB.setSize(30,30);
		recoilCB.addItemListener(this);
		recoilCB.setToolTipText("Simulates recoil by scaling the target as you fire.");
		checkBoxPanel.add(recoilCB);
		
		//System.out.println("Finished panelCheckBoxes");		
	}
	
	public JPanel createContentPane()
	{		
		//Main panel to put everything on, bottom level panel
		//We will load each type of panel feature through function calls
		
		reactionsPanel.setLayout(null);		
		panelLabels();
		panelButtons();
		panelTextInput();
		panelCheckBoxes();
		
		//System.out.println("Loaded panels.");
						
		reactionsPanel.setOpaque(true);
		return reactionsPanel;				
	}
	
	public static void createAndShowGUI()
	{		
		JFrame frame = new JFrame(frameTitle);						//Create the new JFrame using the predefined title
		Frames reactionsPanel = new Frames();						//Create the main panel
		
		//System.out.println("Constructed main JFrame");
		
		frame.setContentPane(reactionsPanel.createContentPane());		
		frame.setSize(300,230);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		//System.out.println("Finished constructing panels");
	}
	
	public void startGame(int iTargetSpeedOutput, int iTargetSizeOutput, boolean bEnableTeleportOutput, boolean bEnableRecoilOutput)
	{		
		//System.out.println("startGame called");
		//System.out.println("iTargetSpeedOutput = " + iTargetSpeed + ", iTargetSizeOutput = " + iTargetSizeOutput);
		//System.out.println("bEnableTeleportOutput = " + bEnableTeleportOutput + ", bEnableRecoilOutput = " + bEnableRecoilOutput);
		
		//create a new Game, and call the start class with the variables
		
		Game game = new Game();
		game.start(iTargetSpeedOutput, iTargetSizeOutput, bEnableTeleportOutput, bEnableRecoilOutput);		
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		/*
		 * This function is called when the checkboxes change state
		 * The item that called the function sends data
		 * We use this data to determine which box is was
		 */
		
		if(e.getItemSelectable() == recoilCB)
		{			
			//System.out.println("Recoil...");
			if(e.getStateChange() == ItemEvent.SELECTED)				//If it is SELECTED, meaning ticked, set the value to TRUE
			{				
				bEnableRecoil = true;
				//System.out.println("EnableRecoil " + bEnableRecoil);				
			} else														//If it is not selected, unticked, => false
			{				
				bEnableRecoil = false;
				//System.out.println("EnableRecoil " + bEnableRecoil);
			}			
		}		
		if(e.getItemSelectable() == teleportCB)
		{			
			//System.out.println("Teleport...");			
			if(e.getStateChange()==ItemEvent.SELECTED)
			{				
				bEnableTeleport = true;
				//System.out.println("EnableTeleport " + bEnableTeleport);
				
			} else
			{				
				bEnableTeleport = false;
				//System.out.println("EnableTeleport " + bEnableTeleport);
			}			
		}		
	}
		
	public void actionPerformed(ActionEvent e)
	/*
	 * Called when the "Launch Game" button is pressed
	 * We need to check which button it was, although we only have one button
	 */
	{		
		//System.out.println("ActionEvent recieved");		
		if("btnLaunch".equals(e.getActionCommand()))
		{			
			//System.out.println("btnLaunch as event");	
			
			/*
			 * This  gets the user input from the Size text input field
			 * if there is something there, we need to find out if it's a value or not
			 * We try to parse it as an int. If it is text, or a float, or anything other than an int, we catch the exception
			 * If it is an int, there is no problem, and iTargetSize now holds the value
			 * We then do the same thing for the speed
			 */
			if(!(sizeField.getText() == ""))
			{
				try
				{					
					iTargetSize = Integer.parseInt(sizeField.getText().trim());					
				} catch (NumberFormatException q)
				{
				}				
			}				
			if(!(speedField.getText() == null))
			{				
				try
				{					
					iTargetSpeed = Integer.parseInt(speedField.getText().trim());					
				}catch(NumberFormatException q)
				{
				}
			}
			//System.out.println(iTargetSize);
			//System.out.println(iTargetSpeed);
			
			//We now call our startGame function, passing through all the required variables which the user can change
			
			startGame(iTargetSpeed, iTargetSize, bEnableTeleport, bEnableRecoil);						
		}else
		{		
		//System.out.println("No response...");
		}				
		//System.out.println("End of ActionEvent");
	}
	
	public static void scoreBox(float x, int y, int z)
	{		
		//Called when the game window closes, and displays the score
		JOptionPane.showMessageDialog(null, "You scored " + x + ".\nYou hit the target " + (int) y + "/" + (int) z + " times.");		
	}

}
