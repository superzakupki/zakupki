package demo.gpt.zakupki;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class ZakupkiApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ZakupkiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		ZakupkiService zakupkiService = new ZakupkiService();
		Map<String, String> content = zakupkiService.getTextByContract("3332906500724000016");
		zakupkiService.getContentUrl(content);


	}
}
