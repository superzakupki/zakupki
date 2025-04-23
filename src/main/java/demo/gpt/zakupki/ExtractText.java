package demo.gpt.zakupki;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Text;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.List;

public class ExtractText {

    public String extractFromFile(InputStream inputStream, String fileName) throws Docx4JException, JAXBException {

        String result = "";

        if (fileName == null) {
            return null;
        }

        String ext = "";

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex >= 0) {
            ext = fileName.substring(dotIndex + 1);
        }

        switch (ext) {

            case "pdf":
                result = readFileDocx(inputStream);
                break;
//            case "docx":
//                result = readFilePDF(inputStream);
//                break;
//            case "png":
//                result = readFileOCR(inputStream);
//                break;
        }
        return result;

    }

    private String readFileDocx(InputStream inputStream) throws Docx4JException, JAXBException {
        String result = "";

        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(inputStream);
        MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();


        String textNodesXPath = "//w:t";
        List<Object> textNodes = mainDocumentPart
                .getJAXBNodesViaXPath(textNodesXPath, true);
        for (Object obj : textNodes) {
            Text text = (Text) ((JAXBElement) obj).getValue();
            String textValue = text.getValue();
            result = result + "\n" + text.getValue();
            System.out.println(textValue);
        }


//
//        String textNodesXPath = "//w:t";
//        List<Object> paragraphs = mainDocumentPart.getJAXBNodesViaXPath(textNodesXPath, true);
//        for (Object obj : paragraphs) {
//            org.docx4j.wml.Text text = (Text) obj;
//            result = result + "\n" +text.getValue();
//            System.out.println(text.getValue());
//        }
        return result;

    }

}
