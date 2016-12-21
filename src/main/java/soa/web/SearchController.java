package soa.web;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SearchController {

    @Autowired private ProducerTemplate producerTemplate;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value="/search")
    @ResponseBody
    public Object search(@RequestParam("q") String q) {
        String [] stringArray = q.split("max:");
        Integer numberFormatted = -1;
        try{
            numberFormatted = Integer.parseInt(stringArray[1]);
        }catch (Exception ignored) {}

        if (stringArray.length == 2 && (numberFormatted != -1)){
            Map<String,Object> headers = new HashMap<>();
            headers.put("CamelTwitterKeywords", stringArray[0]);
            headers.put("CamelTwitterCount", numberFormatted);
            return producerTemplate.requestBodyAndHeaders("direct:search", "", headers);
        }else{
            return producerTemplate.requestBodyAndHeader("direct:search", "", "CamelTwitterKeywords", q);
        }
    }
}