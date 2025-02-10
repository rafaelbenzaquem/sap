package br.jus.trf1.sap;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@SpringBootTest
class SapApplicationTests {

	@Test
	void contextLoads() throws IOException, URISyntaxException {
		URL fileURL = this.getClass().getResource("/");
		File file = new File(fileURL.toURI());
		System.out.println(file.exists());
		printPaths(file);
		// relative path
		file = new File("test.xsd");
		printPaths(file);
		// complex relative paths
		file = new File("/Users/pankaj/../pankaj/test.txt");
		printPaths(file);
		// URI paths
		file = new File(new URI("file:///Users/pankaj/test.txt"));
		printPaths(file);

	}


	private static void printPaths(File file) throws IOException {
		System.out.println("Absolute Path: " + file.getAbsolutePath());
		System.out.println("Canonical Path: " + file.getCanonicalPath());
		System.out.println("Path: " + file.getPath());
	}

}
