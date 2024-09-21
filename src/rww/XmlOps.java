package rww;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
// import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class XmlOps {

	public static void saveToXML(Properties props, String path) throws IOException {
		/*
		// create some properties values on demand
		Properties props = new Properties();
		props.setProperty("email.support", "donot-spam-me@nospam.com");
		props.setProperty("http.port", "8080");
		props.setProperty("http.server", "localhost");
		*/
		try (OutputStream output = new FileOutputStream(path)) {
			// convert the properties to an XML file
			String[] masPath=path.split("/");
			props.storeToXML(output, masPath[masPath.length-1], "UTF-8");// , StandardCharsets.UTF_8);
		}

	}

	public static Properties readFromXML(String path) throws IOException {

		Properties props = new Properties();
		try (InputStream input = new FileInputStream(path)) {
			// loads a XML file
			props.loadFromXML(input);
			return props;
		} catch (IOException e) {
			return null;
		}
	}
}