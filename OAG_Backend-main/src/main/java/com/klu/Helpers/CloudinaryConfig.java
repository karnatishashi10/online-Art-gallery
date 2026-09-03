package com.klu.Helpers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    private final CloudinarySettings settings;

    public CloudinaryConfig(CloudinarySettings settings) {
        this.settings = settings;
    }

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = ObjectUtils.asMap(
                "cloud_name", settings.getCloudName(),
                "api_key", settings.getApiKey(),
                "api_secret", settings.getApiSecret()
        );
        return new Cloudinary(config);
    }
}
