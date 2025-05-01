package demo.gpt.zakupki;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ZakupkiService {

    ZakupkiGov zakupkiGov;

    public Map<String, String> getTextByContract(String aRegNumber) throws ParserConfigurationException, IOException, SAXException {
        ZakupkiGov zakupkiGov = new ZakupkiGov();
        Map<String, String> fileList = zakupkiGov.getFileFromFZ44(aRegNumber);
        System.out.println("fileList.size()=" + fileList.size());
        System.out.println("fileList=" + fileList);
        Map<String, String> result = fileList.entrySet().stream()
                .filter(entry -> entry.getKey().toLowerCase().contains("контракт"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("result=" + result);
        System.out.println("result.size=" + result.size());
        return result;

    }

    public InputStream getContent(Map<String, String> input) throws IOException {

        Optional<Map.Entry<String, String>> result = input.entrySet().stream()
                .filter(entry -> entry.getKey().substring(input.size() - 3).contains("docx"))
                .findFirst();

        if (result.isEmpty()) {
            System.out.println("result is empty");

            return null;
        } else {

            String fileName = result.get().getKey();
            String url = result.get().getValue();

            System.out.println("filename=" + fileName);

            try (InputStream content = zakupkiGov.downloadFile(url, fileName)) {
                return content;
//                return new String(content.readAllBytes());
//                return new String(content.readAllBytes(), Charset.forName("windows-1251"));
//                return new String(content.readAllBytes(), StandardCharsets.UTF_8);
            }
        }
    }

    public String extractTextFromDocx(InputStream docxInputStream) throws IOException, ParserConfigurationException, SAXException {
        // Сохраняем InputStream во временный файл, чтобы открыть его как ZipFile
        File tempFile = File.createTempFile("docx_temp", ".docx");
        try (OutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = docxInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

        try (ZipFile zipFile = new ZipFile(tempFile)) {
            ZipEntry documentXmlEntry = zipFile.getEntry("word/document.xml");
            if (documentXmlEntry == null) {
                throw new FileNotFoundException("word/document.xml not found in the DOCX file");
            }

            try (InputStream xmlStream = zipFile.getInputStream(documentXmlEntry)) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlStream);

                NodeList nodeList = doc.getElementsByTagName("w:t");
                StringBuilder textBuilder = new StringBuilder();

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    textBuilder.append(node.getTextContent());
                }

                return textBuilder.toString();
            }
        } finally {
            tempFile.delete();
        }
    }
}
