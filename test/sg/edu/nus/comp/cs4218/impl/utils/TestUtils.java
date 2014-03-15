package sg.edu.nus.comp.cs4218.impl.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TestUtils {

	public static void delete(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				// directory is empty, then delete it
				if (file.list().length == 0) {
					file.delete();
				} else {
					// list all the directory contents
					String files[] = file.list();

					for (String temp : files) {
						// construct the file structure
						File fileDelete = new File(file, temp);
						// recursive delete
						delete(fileDelete);
					}

					// check the directory again, if empty then delete it
					if (file.list().length == 0) {
						file.delete();
					}
				}

			} else {
				// if file, then delete it
				file.delete();
			}
		}
	}

	public static boolean compare(File file1, File file2) {

		String s1 = "";
		String s3 = "";
		String y = "", z = "";
		try {
			@SuppressWarnings("resource")
			BufferedReader bfr = new BufferedReader(new FileReader(file1));
			@SuppressWarnings("resource")
			BufferedReader bfr1 = new BufferedReader(new FileReader(file2));
			while ((z = bfr1.readLine()) != null)
				s3 += z;

			while ((y = bfr.readLine()) != null)
				s1 += y;
		} catch (Exception e) {
			return false;
		}

		if (s3.equals(s1)) {
			return true;
		} else {
			return false;
		}

	}
}
