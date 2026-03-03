package org.example.shortlink.dto;

public class GenerateShortCodeResponse
{
    private String shortCode;

    public GenerateShortCodeResponse(String shortCode)
    {
        this.shortCode = shortCode;
    }

    public String getShortCode()
    {
        return shortCode;
    }
}
