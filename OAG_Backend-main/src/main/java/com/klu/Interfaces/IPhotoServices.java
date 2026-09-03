package com.klu.Interfaces;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

public interface IPhotoServices {
    Map<String, Object> uploadImage(MultipartFile file) throws IOException;
    Map<String, Object> deleteImage(String publicId) throws IOException;
}
