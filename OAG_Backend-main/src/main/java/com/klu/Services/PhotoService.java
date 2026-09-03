package com.klu.Services;

import com.klu.Interfaces.IPhotoServices;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class PhotoService implements IPhotoServices {

    private final Cloudinary cloudinary;

    public PhotoService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public Map uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null.");
        }

        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "artgallery",
                        "transformation", new Transformation()
                                .width(500)
                                .height(500)
                                .crop("fill")
                                .gravity("face")
                ));
    }

    @Override
    public Map deleteImage(String publicId) throws IOException {
        if (publicId == null || publicId.isEmpty()) {
            throw new IllegalArgumentException("Public ID is null or empty.");
        }
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
