package fileNav;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FileNavigationTest {
	FileNavigation test;
	String dir = "C:/";


	@Test
	void testFileNavigation() {
		test = new FileNavigation();
		assertEquals(test.getCurDir(),test.getPredDir());
		assertEquals(test.getCurDir(),"C://");
		assertEquals(test.getPredDirs().length,25);
		for(int i = 0; i < 25; i++) {
			assertEquals(test.getPredDirs()[i],null);
		}
		assertEquals(test.getPredDirs(),test.getNextDirs());
	}

	@Test
	void testFileNavigationStringI() {
		//invalid directory
		test = new FileNavigation("oops");
		assertEquals(test.getCurDir(),test.getPredDir());
		assertEquals(test.getCurDir(),"C://");
		assertEquals(test.getPredDirs().length,25);
		for(int i = 0; i < 25; i++) {
			assertEquals(test.getPredDirs()[i],null);
		}
		assertEquals(test.getPredDirs(),test.getNextDirs());
	}
	@Test
	void testFileNavigationStringV() {
		//valid directory
		test = new FileNavigation(dir);
		assertEquals(test.getCurDir(),test.getPredDir());
		assertEquals(test.getCurDir(),dir);
		assertEquals(test.getPredDirs().length,25);
		for(int i = 0; i < 25; i++) {
			assertEquals(test.getPredDirs()[i],null);
		}
		assertEquals(test.getPredDirs(),test.getNextDirs());
	}

	@Test
	void testSetCurDirString() {
		test = new FileNavigation();
		test.setCurDir(dir);
		assertEquals(test.getCurDir(),dir);
	}

	@Test
	void testSetCurDirStringStringIN() {		
		test = new FileNavigation();
		test.setCurDir(dir,"oops");
		assertEquals(test.getCurDir(),dir);
		assertEquals(test.getPredDir(),"C://");
	}
	
	@Test
	void testSetCurDirStringStringI() {		
		test = new FileNavigation(dir);
		test.setCurDir(dir,"oops");
		assertEquals(test.getCurDir(),dir);
		assertEquals(test.getPredDir(),dir);
	}
	
	@Test
	void testSetCurDirStringStringV() {		
		test = new FileNavigation();
		test.setCurDir(dir,"C://");
		assertEquals(test.getCurDir(),dir);
		assertEquals(test.getPredDir(),"C://");
	}

	@Test
	void testBackE() {
		test = new FileNavigation(dir);
		test.back();
		assertEquals(test.getCurDir(),dir);
		assertEquals(test.getPredDir(),dir);		
	}
	
	@Test
	void testBackS(){
		test = new FileNavigation("C://");
		String[] temp = new String[test.getPredDirs().length];

		for(int i = 0; i < 10; i++) {
			test.setCurDir(dir);
			if(i == 9) {
				temp[temp.length-1] = null;
				temp[temp.length-(i+2)] = "C://";
				temp[temp.length-(i+1)] = dir;
			}
			else {
				temp[temp.length-(i+1)] = dir;
			}
		}
		
		test.back();		
		assertEquals(test.getCurDir(),dir);
		assertEquals(test.getPredDir(),dir);
		assertEquals(test.getPredDirs().length,temp.length);		
		for(int i = 0; i < temp.length; i++) {
			assertEquals(test.getPredDirs()[i],temp[i]);
		}
	}
	
	@Test
	void testBackF() {
		test = new FileNavigation("C://");
		String[] temp = new String[test.getPredDirs().length];
		for(int i = 0; i < 40; i++) {
			test.setCurDir(dir);
			if(i < test.getPredDirs().length-1) {
				temp[i] = dir;
			}
		}
		
		test.back();		
		assertEquals(test.getCurDir(),dir);
		assertEquals(test.getPredDir(),dir);
		assertEquals(test.getPredDirs().length,temp.length);
		for(int i = 0; i < temp.length; i++) {
			assertEquals(test.getPredDirs()[i],temp[i]);
		}
	}

	@Test
	void testForwardE() {
		test = new FileNavigation(dir);
		test.forward();
		assertEquals(test.getCurDir(),dir);
		assertEquals(test.getPredDir(),dir);
	}
	
	@Test
	void testForwardS() {
		test = new FileNavigation("C://");
		String[] temp = new String[test.getPredDirs().length];

		for(int i = 0; i < 10; i++) {
			test.setCurDir(dir);
		}
		
		for(int i = 0; i < 10; i++) {
			test.back();
			if(i == 9) {
				temp[temp.length-1] = null;
				temp[temp.length-(i+2)] = "C://";
				temp[temp.length-(i+1)] = dir;
			}
			else {
				temp[temp.length-(i+1)] = dir;
			}
		}
		
		assertEquals(test.getCurDir(),dir);
		assertEquals(test.getPredDir(),dir);
		assertEquals(test.getNextDirs().length,temp.length);		
		for(int i = 0; i < temp.length; i++) {
			assertEquals(test.getNextDirs()[i],temp[i]);
		}	}
	
	@Test
	void testForwardF() {
		test = new FileNavigation("C://");
		String[] temp = new String[test.getPredDirs().length];
		for(int i = 0; i < 40; i++) {
			test.setCurDir(dir);
		}
		
		for(int i = 0; i < 40; i++) {
			test.back();		
			if(i < test.getPredDirs().length-1) {
				temp[i] = dir;
			}
		}
		
		assertEquals(test.getCurDir(),dir);
		assertEquals(test.getPredDir(),dir);
		assertEquals(test.getNextDirs().length,temp.length);
		for(int i = 0; i < temp.length; i++) {
			assertEquals(test.getNextDirs()[i],temp[i]);
		}	
	}

	@Test
	void testReset() {
		test = new FileNavigation();
		test.reset();
		assertEquals(test.getCurDir(),"C://");
		assertEquals(test.getPredDir(),"C://");	
	}

	@Test
	void testResetString() {
		test = new FileNavigation();
		test.reset(dir);
		assertEquals(test.getCurDir(),dir);
		assertEquals(test.getPredDir(),dir);		}
}
