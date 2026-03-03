package org.example.shortlink.repository;

import org.example.shortlink.entity.ShortURL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShortURLRepository extends JpaRepository<ShortURL, Long>
{
    Optional<ShortURL> findByShortCode(String shortCode);

    Optional<ShortURL> findByOriginalUrlAndAppId(String originalUrl, String appId);

    List<ShortURL> findByAppId(String appId);

}
