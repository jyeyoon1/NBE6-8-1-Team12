package com.caffe;

import io.swagger.v3.oas.annotations.Operation;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Controller
public class HomeController {

    @SneakyThrows
    @GetMapping("/")
    @Operation(summary = "API 문서 페이지")
    public String apiMain() {
        return "redirect:/swagger-ui/index.html";
    }

    /**
     * @return 렌더링할 Thymeleaf 템플릿 이름 ("home")
     */
    @GetMapping("/api/home")
    @Operation(summary = "메인 페이지 뷰")
    public String home() {
        return "home";
    }
}
