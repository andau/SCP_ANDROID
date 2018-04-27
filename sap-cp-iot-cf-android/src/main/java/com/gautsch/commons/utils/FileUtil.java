package com.gautsch.commons.utils;

import org.lukhnos.nnio.file.Path;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileUtil {

	public static Properties readProperties(InputStream is)
	throws IOException {

		if (is == null) {
			throw new IOException("The input stream was null");
		}

		Properties properties = new Properties();

		try {
			properties.load(is);
		}
		catch (IOException e) {
			throw new IOException("Unable to read from a configuration file", e);
		}
		finally {
			closeStream(is);
		}

		return properties;
	}

	public static void deletePath(Path path) {
		if (path == null) {
			return;
		}

		File file = path.toFile();
		if (file != null && file.isDirectory()) {
			for (File item : file.listFiles()) {
				//deletePath(item.getPath());
			}
		}

		deleteFile(file);
	}

	public static void deleteFile(File file) {
		if (file == null || !file.exists()) {
			return;
		}

		boolean deleted = file.delete();

		if (!deleted)
		{
		Console.printText(String.format("Delete of file %s failed",file.getName()));
		}
	}

	public static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			}
			catch (IOException e) {
				// close silently
			}
		}
	}

}
