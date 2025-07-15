package com.caffe;

import io.swagger.v3.oas.annotations.Operation;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Controller
public class HomeController {
    /*
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
     */

    /**
     * 메인 페이지를 렌더링합니다.
     * @return 렌더링할 Thymeleaf 템플릿 이름 ("home")
     */
    @GetMapping("/") // 루트 경로로 매핑합니다.
    @Operation(summary = "메인 페이지 뷰")
    public String home() {
        // "templates/home.html" 파일을 찾아 렌더링하라는 의미입니다.
        return "home";
    }
}
