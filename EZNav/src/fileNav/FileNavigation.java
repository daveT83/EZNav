package fileNav;

import java.io.File;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

public class FileNavigation {
	public static String DEFAULT_DIRECTORY;						//the default location of the file navigation
	private final int PRED_DIRS = 25;							//the number of directory changes remembered
	
	private String curDir,predDir;			//current and previous directories
	private File curFolder;					//current folder
	private String[] curContents;			//the contents of the current directory
	private String[] predDirs;				//an Array of previous directories
	private String[] nextDirs;				//an Array of the next directories
	private String lastFolder;				//the last folder accessed
	
	/**
	 * Creates an instance of a FileNavigation() using the default directory.
	 */
	//default initialization
	public FileNavigation() {
		init(DEFAULT_DIRECTORY);
	}
	
	/**
	 * This creates an instance of a FileNavigation() by setting the current directory to a different directory than the specified one.
	 * @param curDirectory		A String that is to be considered the default directory
	 */
	//specify file initialization
	public FileNavigation(String curDirectory) {
		init(curDirectory);
	}
	
	/**
	 * performs all the actions necessary to initialize the FileNavigation object.
	 * @param curDirectory		A String that is to be considered the default directory
	 */
	//initializes the file navigation
	private void init(String curDirectory) {
		setDefaultDirectory();
		
		if(curDirectory != null && new File(curDirectory).exists()) {		//if its a valid file path
			curDir = curDirectory;
			predDir = curDirectory;
			curFolder = new File(curDirectory);
			curContents = curFolder.list();
			predDirs = new String[PRED_DIRS];
			nextDirs = new String[PRED_DIRS];
			lastFolder = curDirectory;
		}
		else {			//the directory doesn't exist
			init(DEFAULT_DIRECTORY);
		}
	}

	/**
	 * Returns the current directory.
	 * @return					The String of the current directory
	 */
	//returns the current directory
	public String getCurDir() {
		return curDir;
	}
	
	/**
	 * Returns the previous directory.
	 * @return					The String of the last directory accessed
	 */
	//returns the previous directory
	public String getPredDir() {
		return predDir;
	}
	
	/**
	 * Returns the last folder accessed
	 * @return a String of the last folder to be accessed
	 */
	//returns the last folder accessed
	public String getLastFolder() {
		return lastFolder;
	}
	
	/**
	 * Returns the contents of the current directory.
	 * @return					The String[] that contains the contents of the current directory
	 */
	//returns the array of other directories
	public String[] getContents() {
		return curContents;
	}
	
	/**
	 * Returns an array of the last 25 directories.
	 * @return					An Array of the previous directory changes
	 */
	//returns the array of previous directory changes
	public String[] getPredDirs() {
		return predDirs;
	}
	
	/**
	 * Returns an array of the next directories that were accessed.
	 * @return					An Array of the next directory changes
	 */
	//returns the array of next directory changes
	public String[] getNextDirs() {
		return nextDirs;
	}
	
	/**
	 * Changes the current directory. Doing this also resets the previous directory.
	 * @param curDirectory		The new directory
	 */
	//sets the current, previous directories
	public void setCurDir(String curDirectory) {
		if(curDir != null) {			//if the Directory has already been initialized
			if(new File(curDirectory).exists()) {		//if the directory exists
			predDir = curDir;
			curDir = curDirectory;
			curFolder = new File(curDirectory);
			curContents = curFolder.list();
			updatePredDirs(predDir);
		}
	}
		else {							//if the Directory hasn't been initialized
			predDir = curDirectory;
			curDir = curDirectory;
			curFolder = new File(curDirectory);
			curContents = curFolder.list();
		}
		
		if(curContents != null) {
			lastFolder = curDir;
		}
	}
	
	/**
	 * Changes the current directory and the previous directories to those specified.
	 * @param curDirectory		The new directory
	 * @param predDirectory		The previous directory
	 */
	//sets the current, previous directories
	public void setCurDir(String curDirectory,String predDirectory) {
		if(curDir != null) {			//if the Directory has already been initialized
			if(new File(curDirectory).exists()) {		//if the directory exists
				if(new File(predDirectory).exists()) {	//if the pred directory exists
					predDir = predDirectory;
				}
				else {
					predDirectory = curDirectory;
				}
				curDir = curDirectory;
				curFolder = new File(curDirectory);
				curContents = curFolder.list();
				updatePredDirs(predDir);
			}
		}
		else {							//if the Directory hasn't been initialized
			predDir = curDirectory;
			curDir = curDirectory;
			curFolder = new File(curDirectory);
			curContents = curFolder.list();
		}
		
		if(curContents != null) {
			lastFolder = curDir;
		}
	}
	
	/**
	 * Performs an "undo" action. This changes the current and previous directories to those accessed one move prior.
	 */
	//goes back one directory
	public void back() {
		String nextDir = curDir;
		if(predDirs[PRED_DIRS-1] != null) {			//if you can go back
			curDir = predDir;
			predDir = predDirs[PRED_DIRS-1];
			curFolder = new File(curDir);
			curContents = curFolder.list();
			shift(nextDirs);
			pop(predDirs);
			nextDirs[PRED_DIRS-1] = nextDir;
		}
		if(curContents != null) {
			lastFolder = curDir;
		}
		for(String e:predDirs) {
			System.out.println(e);
		}
	}
	
	/**
 	 * Performs a "redo" action. This changes the current and previous directories to those accessed one move ahead.
 	 * This will only work if the back method has been called prior.
	 */
	//goes forward one directory
	public void forward() {
		String lastDir = curDir;
		if(nextDirs[PRED_DIRS-1] != null) {			//if you can go forward
			predDir = curDir;
			curDir = nextDirs[PRED_DIRS-1];
			curFolder = new File(curDir);
			curContents = curFolder.list();
			shift(predDirs);
			pop(nextDirs);
			predDirs[PRED_DIRS-1] = lastDir;
		}
		
		if(curContents != null) {
			lastFolder = curDir;
		}
	}
	
	//removes the last item and shifts everything one to the right
	private void pop(String[] array) {
		for(int i = array.length - 1; i > 0; i--) {
			array[i] = array[i-1];
		}
		array[0] = null;
	}
	
	/**
	 * Resets the FileNavigation Object to the default settings.
	 */
	//returns to the home
	public void reset() {
		init(DEFAULT_DIRECTORY);
	}
	
	/**
	 * Resets the FileNavigation Object to the default settings. This also changes the current directory to the one specified.
	 * @param curDirectory		The new directory
	 */
	//returns the directory specified
	public void reset(String curDirectory) {
		init(curDirectory);
	}
	
	/**
	 * Updates the current array of previous and next directories.
	 * @param lastDir			A String of the last directory that was accessed 
	 */
	//updates the array of previous directories
	private void updatePredDirs(String lastDir) {
		if(!lastDir.equals(predDirs[PRED_DIRS-1])) {
			shift(predDirs);
			predDirs[PRED_DIRS-1] = lastDir;
		}
	}
	
	/**
	 * Shifts the contents of an array to the left.
	 * @param array		An Array that is to be shifted
	 */
	//shifts the contents of the array to the left
	private void shift(String[] array) {
		for(int i = 0; i < array.length-1; i++) {
			array[i] = array[i+1];
		}
		array[array.length-1] = null;
	}
	
	/**
	 * Searches for the requested File. Once found the the Icon associated with the file will be returned.
	 * @param filePath		The absolute path of the file 
	 * @return				An Icon that is associated with the given file
	 */
	//returns the icon associated with a given file
	public static Icon getIcon(String filePath) {
		return FileSystemView.getFileSystemView().getSystemIcon(new File(filePath));
	}	
	
	//sets the default directory
	private void setDefaultDirectory() {
		String os = System.getProperty("os.name");
		
		if(os.startsWith("Windows")) {
			DEFAULT_DIRECTORY = "C:/";
		}
		if(os.startsWith("Linux")) {
			DEFAULT_DIRECTORY = "/home";
		}
		else {
			DEFAULT_DIRECTORY = "/";
		}
	}
}
