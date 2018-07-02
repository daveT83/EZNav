package fileNavWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import fileNav.FileNavigation;

public class Information {	
	//how the selected directory is displayed
	public static int DROP_DOWN = 0;
	public static int TEXT_AREA = 1;
	
	private JButton select,cancel;
	private JComboBox<String> curSelected;
	private JTextArea curDirSelected;
	private JPanel main;
	private JFrame frame;
	private FileNavigation fileNav;
	private String itemSelected;
	private String curDir;
	private int dirDisplay;
	Directories dir;
	
	/**
	 * Creates an instance of an Information object.
	 * @param items		A String[] that are the items to be contained.
	 * @param jf		A JFrame in which the information JPanel is to be assigned to.
	 * @param f			A FileNavigation object.
	 */
	public Information(String[] items,JFrame jf,FileNavigation f,Directories d){
		select = new JButton("Select");
		cancel = new JButton("Cancel");
		main = new JPanel();
		fileNav = f;
		frame = jf;
		itemSelected = "";
		curDir = f.getCurDir();
		curSelected = new JComboBox<String>(items);
		curDirSelected = new JTextArea();
		dirDisplay = TEXT_AREA;
		curDirSelected.setEditable(false);
		dir = d;
	}
	
	/**
	 * Adds the information JPanel to the assigned JPanel.
	 * @param frame		A JPanel that the information JPanel is to be attached to.
	 */
	//adds the information bar to the window
	public void add(JPanel frame) {
		updateSelection();
		cancelAction();
		selectAction();
		textAction();
		sizeElements();
		if(dirDisplay == DROP_DOWN) {
			main.add(curSelected);
		}
		else if(dirDisplay == TEXT_AREA) {
			main.add(curDirSelected);
		}
		main.add(select);
		main.add(cancel);
		frame.add(main,BorderLayout.SOUTH);
	}
	
	/**
	 * Sets the text for the 'Cancel' button.
	 * @param text		A String representing the new text for the 'Cancel' button.
	 */
	//sets the cancel button text
	public void setCancelText(String text) {
		cancel.setText(text);
	}
	
	/**
	 * Sets the text for the 'Select' button.
	 * @param text		A String representing the new text for the 'Select' button.
	 */
	//sets the text for the select button
	public void setSelectText(String text) {
		select.setText(text);
	}
	
	public int getDirDisplay() {
		return dirDisplay;
	}
	
	/**
	 * Size the elements of the information section
	 */
	//sets the sizes
	private void sizeElements() {
		if(dirDisplay == DROP_DOWN) {
			curSelected.setPreferredSize(new Dimension(220,25));
		}
		else if(dirDisplay == TEXT_AREA) {
			curDirSelected.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			curDirSelected.setPreferredSize(new Dimension(220,20));
		}
	}
	
	/**
	 * Sets the how the currently selected directory id to be displayed. By default it is a JTextArea.
	 * @param selection		An int that will choose either a JTextArea or a JComboBox.
	 */
	//sets the selection type
	public void setSelectionType(int selection) {
		dirDisplay = selection;
	}
	
	/**
	 * Sets the action for the 'Cancel button.
	 */
	//adds functionality to cancel
	private void cancelAction() {
		cancel.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		    }
		});
	}
	
	/**
	 * Sets the action for the 'Select' button.
	 */
	//adds functionality to select
	private void selectAction() {
		select.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	boolean disp = false;
		    	
		    	if(dirDisplay == DROP_DOWN) {
		    		String[] temp = fileNav.getContents();

					if (temp != null) {			//the file selected is a folder
						curSelected.removeAllItems();
						curSelected.addItem(itemSelected);
						for(int i = 0; i < temp.length; i++) {
							if(!temp[i].equals(itemSelected)) {
								curSelected.addItem(temp[i]);
							}
						}
					}
		    	}
		    	else if(dirDisplay == TEXT_AREA) {
		    		//gets the File selected
		    		itemSelected = curDirSelected.getText();
		    		if(itemSelected == null) {
		    			itemSelected = "";
		    		}
		    		if(curDirSelected.getName() == null) {
		    			curDirSelected.setName("");
		    		}
		    	}
		    	frame.setVisible(disp);
		    }
		});
	}
	
	//adds functionality to the JTextField
	private void textAction() {
		curDirSelected.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyPressed(KeyEvent e) {
	            if(e.getKeyCode() == KeyEvent.VK_ENTER){		//enter is pressed
	            	
	            	//gets the File selected
		    		itemSelected = curDirSelected.getText();
		    		if(itemSelected == null) {
		    			itemSelected = "";
		    		}
		    		if(curDirSelected.getName() == null) {
		    			curDirSelected.setName("");
		    		}
	            }
	        }
	
	    });
	}
	
	/**
	 * Returns the current item selected
	 * @return		A String representing the current item selected.
	 */
	//returns the value that was selected
	public String getItem() {
		return itemSelected;
	}
	
	/**
	 * Updates the current item selected.
	 */
	//updates the path
	private void updateSelection() {
		class Up extends Thread{
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(!curDir.equals(fileNav.getCurDir())) {
						if(dirDisplay == DROP_DOWN) {
							curDir = fileNav.getPredDir();
							String[] temp = fileNav.getContents();
							curDir = fileNav.getCurDir();
							if (temp != null) {			//the file selected is a folder
								curSelected.removeAllItems();
								for(int i = 0; i < temp.length; i++) {
									curSelected.addItem(temp[i]);
								}
							}
							else {						//the file selected is a single file
								String item = "";
								for(int i = curDir.length() -1; i > 0; i--) {		//gets the last file from the path
									if(curDir.charAt(i) != '/') {
										item = curDir.charAt(i) + item;
									}
									else {
										break;
									}
								}	
								curSelected.setSelectedItem(item);
							}
						}
						else if(dirDisplay == TEXT_AREA) {
							curDir = fileNav.getCurDir();
				    		String temp = fileNav.getCurDir();
				    		String str = "";
				    		for(int i = temp.length()-1; i >= 0; i--) {
				    			if(temp.charAt(i) == '/') {
				    				break;
				    			}
				    			str = temp.charAt(i) + str;
				    		}
							curDirSelected.setName(fileNav.getCurDir());
							curDirSelected.setText(str);
						}
						}
					}
				}
			}
		Up update = new Up();
		update.start();
	}

}
