package org.example.shortlink.service;

import org.example.shortlink.entity.ShortURL;
import org.example.shortlink.repository.ShortURLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ShortURLService
{
    @Autowired
    private ShortURLRepository shortURLRepository;

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int CODE_LENGTH = 8;

    public String generateShortCode(String originalUrl, String appId) {

        // 检查是否已存在
        Optional<ShortURL> existing =
                shortURLRepository.findByOriginalUrlAndAppId(originalUrl, appId);

        if (existing.isPresent()) {
            return existing.get().getShortCode();
        }

        // 生成新的短码
        String shortCode;
        do {
            shortCode = generateRandomCode();
        } while (shortURLRepository.findByShortCode(shortCode).isPresent());

        ShortURL shortURL = new ShortURL();
        shortURL.setOriginalUrl(originalUrl);
        shortURL.setShortCode(shortCode);
        shortURL.setCreatedAt(LocalDateTime.now());
        shortURL.setAppId(appId);
        shortURLRepository.save(shortURL);

        return shortCode;
    }

    public String getOriginalUrl(String shortCode) {

        Optional<ShortURL> shortUrlOpt =
                shortURLRepository.findByShortCode(shortCode);

        if (shortUrlOpt.isPresent()) {

            ShortURL shortURL = shortUrlOpt.get();

            if (shortURL.getEnabled()) {
                shortURL.setClickCount(shortURL.getClickCount() + 1);
                shortURLRepository.save(shortURL);
                return shortURL.getOriginalUrl();
            }

            throw new RuntimeException("短码已被禁用");
        }

        throw new RuntimeException("短码不存在");
    }

    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = (int) (Math.random() * BASE62.length());
            sb.append(BASE62.charAt(index));
        }

        return sb.toString();
    }
}
