package org.example.shortlink.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.shortlink.common.ApiResponse;
import org.example.shortlink.dto.GenerateRequest;
import org.example.shortlink.dto.GenerateShortCodeResponse;
import org.example.shortlink.entity.ShortURL;
import org.example.shortlink.service.ShortURLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/short-url")
public class ShortURLController {

    @Autowired
    private ShortURLService shortURLService;

    // 生成短链
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<GenerateShortCodeResponse>> generateShortUrl(
            @RequestBody GenerateRequest request)
    {
        String shortCode = shortURLService.generateShortCode(
                request.getUrl(),
                request.getAppId()
        );

        GenerateShortCodeResponse response =
                new GenerateShortCodeResponse(shortCode);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 短链跳转
    @GetMapping("/{shortCode}")
    public void redirectToOriginalUrl(@PathVariable String shortCode,
                                      HttpServletResponse response) throws IOException {
        try {
            String originalUrl = shortURLService.getOriginalUrl(shortCode);
            response.sendRedirect(originalUrl);
        } catch (RuntimeException e) {
            response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    // 禁用
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/disable/{shortCode}")
    public ResponseEntity<ApiResponse<Void>> disable(@PathVariable String shortCode) {
        shortURLService.disableShortCode(shortCode);
        return ResponseEntity.ok(ApiResponse.success(null, "已禁用"));
    }

    // 启用
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/enable/{shortCode}")
    public ResponseEntity<ApiResponse<Void>> enable(@PathVariable String shortCode) {
        shortURLService.enableShortCode(shortCode);
        return ResponseEntity.ok(ApiResponse.success(null, "已启用"));
    }

    // 删除
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{shortCode}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String shortCode) {
        shortURLService.deleteShortCode(shortCode);
        return ResponseEntity.ok(ApiResponse.success(null, "已删除"));
    }
}