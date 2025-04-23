package demo.gpt.zakupki;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

public class ZakupkiGov {


    public Map<String, String> getFileFromFZ44(String aRegNumber) throws ParserConfigurationException, IOException, SAXException {

        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "https://zakupki.gov.ru/epz/contract/printForm/viewXml.html?contractReestrNumber=" + aRegNumber;
        ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
        InputSource is = new InputSource(new StringReader(response.getBody()));
        is.setEncoding("UTF-8");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder bBuilder = dbFactory.newDocumentBuilder();

        Document doc = bBuilder.parse(is);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getDocumentElement().getChildNodes();

        Map<String, String> fileList = new HashMap<>();
        getUrlFromData(fileList, nList);
        return fileList;
    }


    private void getUrlFromData(Map<String, String> fileList, NodeList aNode) {

        String fileName = "";

        if (aNode == null) return;


        for (int i = 0; i < aNode.getLength(); i++) {
            Node nNode = aNode.item(i);
            if ((nNode != null) && (nNode.getNodeType() == Node.ELEMENT_NODE)
                    && ((nNode.getNodeName() == "url") || (nNode.getNodeName() == "fileName"))) {

                Element eElement = (Element) nNode;
                if (nNode.getNodeName() == "fileName") {
                    fileName = eElement.getTextContent();
                }
                if (nNode.getNodeName() == "url") {
                    fileList.put(fileName, eElement.getTextContent());
                }

                System.out.println(nNode.getNodeName() + ": " + eElement.getTextContent());


            } else {
                getUrlFromData(fileList, (NodeList) nNode);
            }


        }


    }

    public InputStream downloadFile(String url, String aFileName) {

        RestTemplate restTemplate = new RestTemplate();
        byte[] fileContent = restTemplate.getForObject(url, byte[].class);

        InputStream result = new ByteArrayInputStream(fileContent);

        return result;

    }
}