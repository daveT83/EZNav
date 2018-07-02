package fileNavWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import fileNav.FileNavigation;

public class Directories {
	//Layouts
	public static final int DEFAULT_LAYOUT = 0;
	public static final int DOUBLE_LAYOUT = 1;
	
	private final Set<Character> INTS = new HashSet<Character>(Arrays.asList('0','1','2','3','4','5','6','7','8','9'));
	private final String DUMMY_DIR = "";
	
	private JPanel main;
	private JPanel files;
	private JPanel treePane;
	private JTree tree;
	private JScrollPane treeScroll;
	private JScrollPane fileScroll;
	private FileNavigation fileNav;
	private String curDir;
	private String parentDir;
	private Set<String> loadedDirs;
	private ArrayList<String> emptyDirs;
	private ArrayList<String> emptyDirsSaved;
	private int layout;
	private Dimension size;

	/**
	 * creates an instance of a Directories object.
	 * @param f		A FileNavigation object.
	 * @param l		An int representing the layout of the file explorer.
	 * @param s		The Dimension of the JPanel
	 */
	public Directories(FileNavigation f,int l,Dimension s) {
		DefaultMutableTreeNode root;
		fileNav = f;
		main = new JPanel((LayoutManager) new FlowLayout(FlowLayout.LEFT));
		files = new JPanel();
		treePane = new JPanel((LayoutManager) new FlowLayout(FlowLayout.LEFT));
		treeScroll = new JScrollPane(treePane);
		fileScroll = new JScrollPane(files);
		root = new DefaultMutableTreeNode(FileNavigation.DEFAULT_DIRECTORY);
		curDir = fileNav.getCurDir();
		parentDir = "";
		loadedDirs = new HashSet<String>();
		layout = l;
		size = s;
		emptyDirs = new ArrayList<String>();
		emptyDirsSaved = new ArrayList<String>();
				
		//builds the file tree
		tree = new JTree(root);
		tree.setModel(new DefaultTreeModel(root));
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		//makes the root appear as a folder if necessary
		if(fileNav.getContents() != null) {
			((DefaultTreeModel) tree.getModel()).insertNodeInto(new DefaultMutableTreeNode(DUMMY_DIR),
					(DefaultMutableTreeNode)tree.getModel().getRoot(),0);
		}
						
		//sets what treeListener
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				updateEmptyDirs();
			}
			});
	}
	
	/**
	 * updates the files displayed depending on the layout.
	 */
	//updates the empty directories displayed
	public void updateEmptyDirs() {
		int width;
		String temp = findPath();
		if(layout == DEFAULT_LAYOUT) {
			width = (int) size.getWidth();
		}
		else {
			files.removeAll();
			
			//adds the files to the file selection screen
			for(int i = 0; i < emptyDirs.size(); i++) {
				addFile(emptyDirs.get(i));
			}
			
			//clears the list of empty directories if necessary
			if(emptyDirs.size() > 0) {
				files.setPreferredSize(new Dimension(300,emptyDirs.size()*20));
				emptyDirsSaved = emptyDirs;
				emptyDirs = new ArrayList<String>();
			}
			
			//updates the files displayed when necessary
			else {
				for(int i = 0; i < emptyDirsSaved.size(); i++) {
					if(new File(fileNav.getCurDir()+"/"+emptyDirsSaved.get(i)).exists()) {
						addFile(emptyDirsSaved.get(i));
					}
				}
			}
			
			width = getLongestString((DefaultMutableTreeNode) tree.getModel().getRoot(),"").length()*10;
		}
        constructTree(temp);
        loadedDirs.add(temp);
        parentDir = fileNav.getCurDir();
        fileNav.setCurDir(temp,fileNav.getCurDir());
        treePane.setPreferredSize(new Dimension(width,countChildren((DefaultMutableTreeNode) tree.getModel().getRoot())*21));
        treeScroll.getVerticalScrollBar().setValue(treeScroll.getVerticalScrollBar().getValue()+1);
        treeScroll.getVerticalScrollBar().setValue(treeScroll.getVerticalScrollBar().getValue()-1);

        //updates the files displayed
		files.revalidate();
		files.repaint();
		files.setVisible(true);
	}
	
	/**
	 * Finds the String with the most characters. If there are no nodes in the tree it returns an empty String ("")
	 * @param r		A DefaultMutableNode that is the root
	 * @param s		The string the is to be compared
	 * @return		The String with the most characters in the JTree
	 */
	//returns the longest string
	private String getLongestString(DefaultMutableTreeNode r,String s) {
		String str = r.toString();
		String temp;
		if(tree.isExpanded(new TreePath(r.getPath()))) {
			for(int i = 0; i < r.getChildCount(); i++) {
				temp = getLongestString((DefaultMutableTreeNode) r.getChildAt(i),str);
				if(temp.length() > str.length()) {
					str = temp;
				}
			}
		}
		return str;
	}
	
	/**
	 * Returns an ArrayList<String> of empty directories 
	 * @return		An ArraList<String> of empty directories
	 */
	//gets the empty directories
	public ArrayList<String> getEmptyDirs(){
		return emptyDirsSaved;
	}
	
	/**
	 * Finds the number of nodes currently displayed
	 * @param r
	 * @return
	 */
	//counts the number of children
	private int countChildren(DefaultMutableTreeNode r) {
		int count = 0;
		if(tree.isExpanded(new TreePath(r.getPath()))) {
			count = tree.getModel().getChildCount(r);
			for(int i = 0; i < r.getChildCount(); i++) {
				count = count + countChildren((DefaultMutableTreeNode) r.getChildAt(i));
			}
		}
		return count;
	}
	
	/**
	 * Sets the current layout to either a single layout or a double layout.
	 * @param l		A layout.
	 */
	//sets the layout
	public void setLayout(int l) {
		layout = l;
	}
	
	/**
	 * Adds the directory browser to the assigned JPanel.
	 * @param frame		A JPanel that the directory browser will be assigned to.
	 * @param size		The Dimension of the JPanel.
	 */
	//adds the directory browser to the window
	public void add(JPanel frame) {		
		setSelectedPath();
		
		//sets treeScroll behavior
		treeScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		treeScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		treeScroll.getVerticalScrollBar().setUnitIncrement(6);
		treeScroll.getHorizontalScrollBar().setUnitIncrement(6);
		
		if(layout == DEFAULT_LAYOUT) {		//adds the JTree
				treePane.setPreferredSize(new Dimension((int)size.getWidth()-20,(int)size.getHeight()-20));
				treePane.setBackground(Color.WHITE);
				
				//adds the elements to the window
				treePane.add(tree);
				treeScroll.setPreferredSize(size);
				treeScroll.revalidate();
				main.add(treeScroll,BorderLayout.CENTER);
		}
		else {
			//sets the layout for files
			files.setLayout((LayoutManager) new BoxLayout(files,BoxLayout.Y_AXIS));
			
			//sets the treePane size
			treePane.setPreferredSize(new Dimension((int)((size.getWidth()*2)/5)-20,(int) size.getHeight()-20));
			treeScroll.setPreferredSize(new Dimension((int)((size.getWidth()*2)/5),(int) size.getHeight()));
			treeScroll.getVerticalScrollBar().setVisible(false);
			treePane.setBackground(Color.WHITE);
			
			//sets the files background
			files.setBackground(Color.WHITE);
			
			//sets fileScroll behavior
			fileScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			fileScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			fileScroll.getVerticalScrollBar().setUnitIncrement(6);
			fileScroll.getHorizontalScrollBar().setUnitIncrement(6);
			
			files.setPreferredSize(new Dimension((int)((size.getWidth()*2.5)/5)-20,(int) size.getHeight()-20));
			fileScroll.setPreferredSize(new Dimension((int)((size.getWidth()*2.5)/5)+38,(int) size.getHeight()));

			//adds the elements to the window
			treePane.add(tree);
			main.add(treeScroll);
			main.add(fileScroll);
		}
		frame.add(main,BorderLayout.CENTER);
	}
	
	/**
	 * Adds a new node to the JTree and updates the tree in the GUI
	 * @param child		The child that is to be added.
	 * @param children	An array of children to be added to the JTree.
	 * @param root		The parent node to be added.
	 * @return			The newly added child node.
	 */
	//adds the node to the tree
	private DefaultMutableTreeNode addObject(String child,String[] children,DefaultMutableTreeNode root) {
	    DefaultMutableTreeNode parentNode = null;
	    TreePath parentPath = tree.getSelectionPath();

	    if (parentPath == null) {
	        //There is no selection. Default to the root node.
	        parentNode = root;
	    } else {
	        parentNode = (DefaultMutableTreeNode)(parentPath.getLastPathComponent());
	    }

	    return addObject(parentNode,child,children,true);
	}
	
	/**
	 * Adds a new node to the JTree and updates the tree in the GUI
	 * @param child				The child that is to be added.
	 * @param children			An array of children to be added to the JTree.
	 * @param root				The parent node to be added.
	 * @param shouldBeVisible	If the new nodes should be displayed to the GUI.
	 * @return					The newly added child node.
	 */
	//adds a new node
	private DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,String child,String[] children,boolean shouldBeVisible) {
	    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
	    boolean valid = true;
	    
	    for(int i = parent.toString().length()-1; i > 0;i--) {
	    	if(parent.toString().charAt(i) == '.' && !INTS.contains(parent.toString().charAt(i+1))) {
	    		valid = false;
	    		break;
	    	}
	    }
	    if(valid) {
	    	try {
	    		((DefaultTreeModel) tree.getModel()).insertNodeInto(childNode,parent,parent.getChildCount());
	    	}
	    	catch(ArrayIndexOutOfBoundsException e) {}
	    	valid = true;
	    }
	    
	    for(int i = child.length()-1; i > 0;i--) {
	    	if(child.charAt(i) == '.' && !INTS.contains(child.charAt(i+1))) {
	    		valid = false;
	    		break;
	    	}
	    }

	    //adds the child's children
	    if(valid && children != null) {
	    	((DefaultTreeModel) tree.getModel()).insertNodeInto(new DefaultMutableTreeNode(DUMMY_DIR),childNode,childNode.getChildCount());
	    }

	    if(parent.getChildCount() > 0 && parent.getChildAt(0).toString().equals(DUMMY_DIR)) {
	    	parent.remove(0);
	    }
	    if(layout == DOUBLE_LAYOUT && childNode.getChildCount() == 0) {		//hides all files without children in the JTree
	    	emptyDirs.add(childNode.toString());
	    	childNode.removeFromParent();
	    }
	    return childNode;
	}
	
	/**
	 * Returns the path of the selected node
	 * @return		A String representing the path of the selected node
	 */
	//finds the path of the selected node
	private String findPath() {
		TreePath[] paths = tree.getSelectionPaths();
		String temp = "";
		
		if(paths != null) {
        for (int i = 0; i < paths.length; i++) {
        	
        	//gets the absolute path of the selected directory
        	for(int j = 0; j < paths[i].getPathCount(); j++) {
        		if(j == 0) {
            		temp = temp + paths[i].getPathComponent(j);
        		}
        		else {
        			temp = temp + "/" + paths[i].getPathComponent(j);
        		}
        	}
        }
		}
		return temp;
	}	
	
	/**
	 * Constructs the JTree 2 file directories deep at a time.
	 * @param r		A String representing the name of the root node
	 */
	//builds the JTree
	private void constructTree(String r) {
		FileNavigation builder = new FileNavigation(r);		//used to find the files and their children
		String[] children = builder.getContents();
		String temp = findPath();
		
		if(!loadedDirs.contains(temp) && children != null && children.length > 0) {
			
			//adds the new node(s)
			for(int i = 0; i < children.length; i++) {
				builder.setCurDir(fileNav.getPredDir()+"/"+children[i]);
				addObject(children[i],builder.getContents(),new DefaultMutableTreeNode(r));
			}
		}
	}
	
	/**
	 * Updates the current directory based on what is currently selected.
	 */
	//sets the selected path as the current directory
	private void setSelectedPath() {
		class updateSelected extends Thread{
			@Override
			public void run() {
				String nodeName = "";
				
				while(true) {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(fileNav.getCurDir() != curDir) {
						curDir = fileNav.getCurDir();
						
						//finds the new node
						for(int i = curDir.length()-1; i >= 0; i--) {
							nodeName = curDir.charAt(i) + nodeName;
						}
						
						tree.clearSelection();
						tree.setSelectionPath(findPath((DefaultMutableTreeNode)tree.getModel().getRoot(),nodeName));
					}
				}
			}
		}
		
		updateSelected update = new updateSelected();
		update.start();
	}
	
	/**
	 *	Returns the TreePath of a given node.
	 * @param root	A DefaultMutableTreeNode that is the root of the tree.
	 * @param s		A String representing the name of the node that you want to find the path of.
	 * @return		A TreePath of the desired node.
	 */
	//finds the path of a given node
	public TreePath findPath(DefaultMutableTreeNode root, String s) {
	    @SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
	    while (e.hasMoreElements()) {
	        DefaultMutableTreeNode node = e.nextElement();
	        if (node.toString().equalsIgnoreCase(s)) {
	            return new TreePath(node.getPath());
	        }
	    }
	    return null;
	}
	
	/**
	 * Returns the root of the JTree.
	 * @return		The root of the tree.
	 */
	//returns the root node
	public DefaultMutableTreeNode getRoot() {
		return (DefaultMutableTreeNode) tree.getModel().getRoot();
	}
	
	/**
	 * Removes all the contents of the JPanel that displays the empty directories.
	 */
	//removes all contents from the files JPanel
	public void clearSingleDirs() {
		files.removeAll();
		files.revalidate();
		files.repaint();
		files.setVisible(true);
		
	}
	
	/**
	 * Collapses all the nodes back to the root node
	 */
	//collapses all the nodes back to the root
	public void collapseAll() {
		int width;
	    for(int i = tree.getRowCount()-1; i >= 0; i--){
	      tree.collapseRow(i);
	      }
	    
	    //resizes the JScrollPane
		if(layout == DEFAULT_LAYOUT) {
			width = 350;
		}
		else {
			width = getLongestString((DefaultMutableTreeNode) tree.getModel().getRoot(),"").length()*5;
		}
        treePane.setPreferredSize(new Dimension(width,countChildren((DefaultMutableTreeNode) tree.getModel().getRoot())*21));
        treeScroll.getVerticalScrollBar().setValue(treeScroll.getVerticalScrollBar().getValue()+1);
    }
	
	//adds a file to the file selection screen
	private void addFile(String file) {
		JButton tab = new JButton(file);
		
		//sets button properties
		tab.setBackground(Color.WHITE);
		tab.setBorderPainted(false);
		
		//adds functionality to each button
		tab.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {	    	
		    	//resets the colors of all other buttons
		    	for(Component button:files.getComponents()) {
		    		button.setBackground(Color.WHITE);
		    		button.setForeground(Color.BLACK);
		    	}
		    	
		    	//highlights the selected directory
		    	tab.setBackground(Color.BLUE);
		    	tab.setForeground(Color.WHITE);
		    	
		    	//changes the directory
		    	fileNav.setCurDir(parentDir+"/"+tab.getText(),fileNav.getCurDir());
		    }
		});
		
		//adds the file tab to the file selection
		files.add(tab);
		files.setVisible(true);
		fileScroll.setVisible(true);
	}
}
