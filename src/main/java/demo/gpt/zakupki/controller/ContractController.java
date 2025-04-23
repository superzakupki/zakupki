package demo.gpt.zakupki.controller;

import demo.gpt.zakupki.ZakupkiService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ContractController {

    ZakupkiService zakupkiService;

    @SneakyThrows
    @GetMapping("/reg.number/{regNumber}")
    public ResponseEntity<String> getContract(@PathVariable("regNumber") String regNumber) {


        Map<String, String> contract = zakupkiService.getTextByContract(regNumber);
        String url = zakupkiService.getContentUrl(contract);

        return ResponseEntity.ok(url);

    }

}
