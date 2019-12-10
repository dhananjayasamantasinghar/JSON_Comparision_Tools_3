package com.ct.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class AppReader {

	/**
	 * This method will read file content from location ex: "./static/pod.json".
	 * 
	 * @param fileLocation
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static String getContentFromFile(String fileLocation) throws URISyntaxException, IOException {
		URI uri = ClassLoader.getSystemResource(fileLocation).toURI();
		byte[] readAllBytes = Files.readAllBytes(Paths.get(uri));
		String content = new String(readAllBytes, StandardCharsets.ISO_8859_1);
		return content;

	}

	/**
	 * This method will read file content from location line by line ex:
	 * "./static/pod.json".
	 * 
	 * @param fileLocation
	 * @return
	 */
	public static Optional<List<String>> getDataLineByLine(String filePath) {
		try {
			URI uri = ClassLoader.getSystemResource(filePath).toURI();
			return Optional.ofNullable(Files.readAllLines(Paths.get(uri)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public static List<String> getPropertiesFileValueList(String path) throws IOException {
		Properties properties = new Properties();
		properties.load(ClassLoader.getSystemResourceAsStream(path));
		ArrayList<String> values = new ArrayList<>();

		properties.values().stream().forEach(e -> values.add(String.valueOf(e)));

		return values;

	}
}
