package fileNavWindow;

class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileManager test = new FileManager();
		test.setLayout(FileManager.DOUBLE_LAYOUT);
		//test.setDirectorySelect(FileManager.DROP_DOWN);
		String[] s = test.run();		
		System.out.println(s[0]+" : "+s[1]);
		if(!test.isVisible()) {
			test.close();
		}
	}

}
