package fileNavWindow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import fileNav.FileNavigation;

public class FileManager {
	//Layouts
	public static final int DEFAULT_LAYOUT = 0;
	public static final int DOUBLE_LAYOUT = 1;
	
	//how the selected directory is displayed
	public static int DROP_DOWN = 0;
	public static int TEXT_AREA = 1;
	
	private final Dimension SIZE = new Dimension(450,350);		//size of the window
	private final String TITLE = "Select File";					//default window title
	
	private JFrame main;
	private JPanel back;
	private FileNavigation fileNav;
	private Directories dir;
	private Information information;
	private Navigation nav;
	private int layout;

	/**
	 * Creates an instance of a FileManager object with the default title for the window.
	 */
	public FileManager() {
		init(TITLE);
	}
	
	/**
	 * Creates an instance of a FileManager object with a custom title for the window.
	 * @param title		A String representing the title of the window.
	 */
	public FileManager(String title) {
		init(title);
	}
	
	/**
	 * Initializes all the settings need to use a FileMAnager object.
	 * @param title		A String representing the title of the window.
	 */
	//initializes window properties
	private void init(String title) {
		main = new JFrame(title);
		back = new JPanel();
		fileNav = new FileNavigation();
		layout = DEFAULT_LAYOUT;
		
		//gets the screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		
		//sets window properties
		main.setAlwaysOnTop(true);
		main.setPreferredSize(SIZE);
		main.setResizable(false);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setLocation((int)((width/2)-(SIZE.getWidth()/2)), (int)((height/2)-(SIZE.getHeight()/2)));
		
		//sets the layout for the window
		back.setLayout(new BorderLayout());
		
		//initializes the components of this window
		dir = new Directories(fileNav,layout,new Dimension((int) SIZE.getWidth()-15,250));
		nav = new Navigation(fileNav,dir);
		information = new Information(fileNav.getContents(),main,fileNav,dir);
	}
	
	//sets the layout of the window
	public void setLayout(int l) {
		layout = l;
		dir.setLayout(l);
	}
	
	/**
	 * Displays the window
	 * **NOTE** This will not return the file that is chosen. The best way to this is to use the 'run()' method.
	 */
	//displays the window
	public void dispWin() {		
		dir.add(back);
		nav.add(back);
		information.add(back);
						
		main.add(back);
		main.pack();
		main.setVisible(true);
	}
	
	/**
	 * Returns the currently selected item.
	 * @return		A String representing the item selected.
	 */
	//returns the value selected
	public String getItem() {
		return information.getItem();
	}
	
	/**
	 * Returns the file path.
	 * @return		A String representing the file path.
	 */
	//returns the filePath
	public String getItemPath(){
		TreePath paths = dir.findPath(dir.getRoot(),information.getItem());
		String path = "";
		if(paths != null) {
			path = paths.getPathComponent(0).toString();
			for(int i = 1; i < paths.getPathCount(); i++) {
				path = path + "/" + paths.getPathComponent(i).toString();
			}
		}
		return path;
	}
	
	/**
	 * Sets the current method of displaying the selected directory.
	 * @param selectionType	An int representing one of two layouts. Either a JTextArea or a JComboBox.
	 */
	//chooses which style to display the currently selected directory
	public void setDirectorySelect(int selectionType) {
		information.setSelectionType(selectionType);
	}
	
	/**
	 * Returns a boolean representing if the window is visible or not.
	 * @return		A boolean representing whether or not the window is visible.
	 */
	//returns the visibility of the JFrame
	public boolean isVisible() {
		return main.isVisible();
	}
	
	/**
	 * Closes the window and terminates all its processes. This should be called only after a file has been selected.
	 * (after the run() method has been called)
	 */
	//closes the window
	public void close() {
        main.dispatchEvent(new WindowEvent(main, WindowEvent.WINDOW_CLOSING));
	}
	
	/**
	 * Displays the file selection window and returns the name of the file selected and the file path.
	 * The file name is stored in position 0.
	 * The file path is stored in position 1.
	 * If there is no file selected an empty string will be stored in both positions of the String array.
	 * @return	A String array representing the file selected and the file path.
	 */
	//runs the file selection window
	public String[] runOnce() {
		dispWin();
		String[] results = new String[2];
		while(true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!main.isVisible()) {
				results[0] = getItem();
				results[1] = fileNav.getCurDir();
				if(results[0].length() == 0) {
					results[1] = "";
					
					//throws an error message and keeps the file selector open
					JOptionPane.showMessageDialog(null, "Invalid File Selected.","ERROR", JOptionPane.ERROR_MESSAGE);
				}
				return results;
			}
		}
	}
	
	/**
	 * Displays the file selection window and returns the name of the file selected and the file path.
	 * The file name is stored in position 0.
	 * The file path is stored in position 1.
	 * If there is no file selected an empty string will be stored in both positions of the String array.
	 * 
	 * The window is kept open until a valid file is selected.
	 * To properly use this method call run() then test if the file selector is visible, if it is not visible then close it.
	 * @return	A String array representing the file selected and the file path.
	 */
	//runs the file selection window
	public String[] run() {
		dispWin();
		String[] results = new String[2];
		while(true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!main.isVisible()) {
				results[0] = getItem();
				results[1] = fileNav.getCurDir();
				if(results[1].length() == 0 && information.getDirDisplay() == DROP_DOWN) {
					results[0] = "";
					
					//throws an error message and keeps the file selector open
					JOptionPane.showMessageDialog(null, "Invalid File Selected.","ERROR", JOptionPane.ERROR_MESSAGE);
					main.setVisible(true);
				}
				
				else if(results[0].length() == 0 && information.getDirDisplay() == TEXT_AREA) {
					results[1] = "";
					
					//throws an error message and keeps the file selector open
					JOptionPane.showMessageDialog(null, "Invalid File Selected.","ERROR", JOptionPane.ERROR_MESSAGE);
					main.setVisible(true);
				}
				return results;
			}
		}
	}
}
