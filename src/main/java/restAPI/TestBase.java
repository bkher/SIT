package restAPI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class TestBase {

	public Properties prop;
	public int STATUS_RESPONSE_CODE_200 = 200;
	public int STATUS_RESPONSE_CODE_400 = 400;
	public int STATUS_RESPONSE_CODE_404 = 404;
	public int STATUS_RESPONSE_CODE_401 = 401;
	public int STATUS_RESPONSE_CODE_500 = 500;

	public TestBase() {

		try {
			prop = new Properties();
			// FileInputStream fis = new
			// FileInputStream(("F:\\EclipsePractice\\SIT\\src\\main\\java\\com\\config\\config.properties"));
			FileInputStream fis = new FileInputStream(
					"//home//bhagatsinhkher//git//SIT//src//main//java//com//config//config.properties");
			prop.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
