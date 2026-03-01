package org.example.shortlink.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.shortlink.entity.ShortURL;
import org.example.shortlink.service.ShortURLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/short-url")
public class ShortURLController
{
    @Autowired
    private ShortURLService shortURLService;

    // 生成短链
    @PostMapping("/generate")
    public ResponseEntity<Map<String,String>> generateShortUrl(@RequestParam String url,@RequestParam(required = false) String appId)
    {
        String shortCode = shortURLService.generateShortCode(url,appId);

        Map<String,String> response = new HashMap<>();
        response.put("shortCode", shortCode);
        response.put("shortUrl","http://localhost:8080/" + shortCode);

        return ResponseEntity.ok(response);
    }

    //跳转
    @GetMapping("/{shortCode}")
    public void redirectToOriginalURL(@PathVariable String shortCode, HttpServletResponse response) throws IOException
    {
        try
        {
            String originalURL = shortURLService.getOriginalUrl(shortCode);
            response.sendRedirect(originalURL);
        } catch (RuntimeException e)
        {
            response.sendError(HttpStatus.NOT_FOUND.value(),e.getMessage());
        }
    }
}
