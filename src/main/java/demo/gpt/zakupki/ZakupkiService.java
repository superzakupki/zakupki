package demo.gpt.zakupki;

import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ZakupkiService {

    public Map<String, String> getTextByContract(String aRegNumber) throws ParserConfigurationException, IOException, SAXException {
        ZakupkiGov zakupkiGov = new ZakupkiGov();
        Map<String, String> fileList = zakupkiGov.getFileFromFZ44(aRegNumber);
        System.out.println("fileList.size()=" + fileList.size());
        System.out.println("fileList=" + fileList);
        Map result = fileList.entrySet().stream()
                .filter(entry -> entry.getKey().toLowerCase().contains("контракт"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("result=" + result);
        System.out.println("result.size=" + result.size());
        return result;

    }

    public  String getContentUrl(Map<String, String> input) {

        var result = input.entrySet().stream()
                .filter(entry -> entry.getKey().substring(input.size() - 3).contains("docx"))
                .findFirst();

        System.out.println(result);
        return result.get().getValue();


    }
}
