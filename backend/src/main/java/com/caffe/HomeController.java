package com.caffe;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @SneakyThrows
    @GetMapping
    public String main() {
        return """
               <h1>API Server</h1>
               <div>
                    <a href="/swagger-ui/index.html">API 문서로 이동</a>
               </div>
               """;
    }
}
