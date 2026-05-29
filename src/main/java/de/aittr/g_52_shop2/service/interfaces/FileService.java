package de.aittr.g_52_shop2.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    String uploadAndGetUrl(MultipartFile file) throws IOException;
}
