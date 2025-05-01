package demo.gpt.zakupki;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;
import java.util.Map;

@SpringBootApplication
public class ZakupkiApplication implements CommandLineRunner {

	@Autowired
	ZakupkiGov zakupkiGov;

	@Autowired
	ZakupkiService zakupkiService;

	public static void main(String[] args) {
		SpringApplication.run(ZakupkiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Map<String, String> content = zakupkiService.getTextByContract("3332906500724000016");
		zakupkiService.getContent(content);

		InputStream text = zakupkiService.getContent(content);
		System.out.println(text);

		String decoded = zakupkiService.extractTextFromDocx(text);
		System.out.println(decoded);


	}
}
