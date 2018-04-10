package lse;

import java.io.FileNotFoundException;

public class lseTest {
	public static void main(String[] args) throws FileNotFoundException {
		LittleSearchEngine tst=new LittleSearchEngine();
		tst.makeIndex("testDoc.txt", "noisewords.txt");
//		tst.print();
	}
}
