package com.caffe;

import io.swagger.v3.oas.annotations.Operation;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@RestController
public class HomeController {
    @SneakyThrows
    @GetMapping(produces = TEXT_HTML_VALUE)
    @Operation(summary = "메인 페이지")
    public String apiMain() {
        return """
               <h1>API Server</h1>
               <div>
                    <a href="/swagger-ui/index.html">API 문서로 이동</a>
               </div>
               """;
    }
}
