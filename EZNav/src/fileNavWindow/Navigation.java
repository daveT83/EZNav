package fileNavWindow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import fileNav.FileNavigation;

public class Navigation {	
	private JPanel main;
	private JButton back,forward,reset;
	private JTextArea path;
	private String home;
	private FileNavigation fileNav;
	private String backImage;
	private String forwardImage;
	private String homeImage;
	private Directories dirs;
	
	/**
	 * Creates an instance of a Navigation object
	 * @param f		A FileNavigation object
	 * @param d		A Directories object
	 */
	public Navigation(FileNavigation f,Directories d) {
		main = new JPanel();
		back = new JButton("");
		forward = new JButton("");
		reset = new JButton("");
		path = new JTextArea(f.getCurDir());
		fileNav = f;
		home = f.getCurDir();
		dirs = d;
		backImage = "./images/back.jpeg";
		forwardImage = "./images/forward.jpeg";
		homeImage = "./images/reset.jpeg";
		
		//disables editing
		path.setEditable(false);
	}
	
	/**
	 * Adds a Navigation JPanel to the JPanel
	 * (The top part of the file manager)
	 * @param frame		A JPanel in which to put the navigation panel.
	 */
	//adds the elements to the JPanel
	public void add(JPanel frame) {
		sizeElements();
		addImages();
		updatePath();
		addFunc();
		main.add(back);
		main.add(forward);
		main.add(path);
		main.add(reset);
		frame.add(main,BorderLayout.NORTH);
	}
	
	/**
	 * Sizes all the elements for the navigation section.
	 */
	//sizes the elements
	private void sizeElements() {
		back.setPreferredSize(new Dimension(20,20));
		forward.setPreferredSize(new Dimension(20,20));		
		reset.setPreferredSize(new Dimension(20,20));
		path.setPreferredSize(new Dimension(300,20));
	}

	/**
	 * Sets the path for the new forward button image
	 * @param path		A String representing the path to the image being used for the forward button
	 */
	//sets the forward button
	public void setForward(String path) {
		forwardImage = path;
	}
	
	/**
	 * Sets whether of not the top bar is editable. By default the bar is not editable.
	 * @param b		A boolean determining if the top bar is editable
	 */
	//sets the top bar to be editable or not
	public void setBarEditable(boolean b) {
		path.setEditable(b);
	}
	
	/**
	 * Sets the path for the new home button image
	 * @param path		A String representing the path to the image being used for the home button
	 */
	//sets the home button
	public void setHome(String path) {
		homeImage = path;
	}
	
	/**
	 * Sets the path for the new back button image
	 * @param path		A String representing the path to the image being used for the back button
	 */
	//sets the back button
	public void setBack(String path) {
		backImage = path;
	}
	
	/**
	 * Sets the icons for the back, forward, and return home buttons.
	 */
	//adds images
	private void addImages() {
		ImageIcon b = new ImageIcon(backImage);
		ImageIcon f = new ImageIcon(forwardImage);
		ImageIcon r = new ImageIcon(homeImage);
		back.setIcon(b);
		forward.setIcon(f);
		reset.setIcon(r);
	}
	
	/**
	 * updates the file path that is displayed.
	 */
	//updates the path
	private void updatePath() {
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
					if(!path.getText().equals(fileNav.getCurDir())) {
						path.setText(fileNav.getCurDir());
					}
				}
			}
		}
		Up update = new Up();
		update.start();
	}
	
	/**
	 * Adds functionality to all of the buttons.
	 */
	//sets button functionality
	private void addFunc() {
		//reset button functionality
		reset.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	fileNav.reset(home);
		    	dirs.collapseAll();
		    	dirs.clearSingleDirs();
		    }
		});
		
		//back button functionality
		back.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	fileNav.back();
		    	dirs.updateEmptyDirs();
		    	System.out.println(fileNav.getCurDir());
		    }
		});
		
		//forward button functionality
		forward.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	fileNav.forward();
		    	dirs.updateEmptyDirs();
		    }
		});
	}

}
