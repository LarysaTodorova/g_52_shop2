package de.aittr.g_52_shop2.service;

import de.aittr.g_52_shop2.config.DOProperties;
import de.aittr.g_52_shop2.exception_handling.exceptions.FileUploadException;
import de.aittr.g_52_shop2.service.interfaces.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final DOProperties properties;
    private final S3Client client;

    public FileServiceImpl(DOProperties properties, S3Client client) {
        this.properties = properties;
        this.client = client;
    }

    @Override
    public String uploadAndGetUrl(MultipartFile file) throws IOException {
        // Логика метода:
        // 1. Необходимые проверки
        Objects.requireNonNull(file, "MultipartFile must not be null");

        if (file.isEmpty()) {
            throw new FileUploadException("File is empty");
        }

        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new FileUploadException("File is not an image");
        }

        // 2. Сгенерировать уникальное имя файла
        String uniqueFileName = generateUniqueFileName(file);

        // 3. Создать запрос на загрузку файла в облако под уникальным именем
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(properties.getBucket())
                .key(uniqueFileName)
                .contentType(file.getContentType())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        // 4. Отправка запроса (фактическая загрузка файла в облако)
        RequestBody requestBody = RequestBody.fromInputStream(file.getInputStream(), file.getSize());
        client.putObject(request, requestBody);

        // 5. Отправляем ещё запрос с целью получить ссылку на загруженный файл
        // 6. Возвращаем полученную ссылку как результат работы метода
        return client.utilities()
                .getUrl(
                        x -> x.bucket(properties.getBucket()).key(uniqueFileName)
                ).toString();
    }

    private String generateUniqueFileName(MultipartFile file) {
        // Какие есть варианты:
        // Представим, что уникальная часть имени файла - f7b4
        String randomPart = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();

        // 1. Файл пришёл вообще без имени
        // Результат - f7b4
        if (fileName == null) {
            return randomPart;
        }
        // CAT PICTURE.jpeg  ->  cat-picture.jpeg
        String normalisedFileName = fileName.trim().replace(" ", "-").toLowerCase();

        // 2. Файл пришёл с именем без расширения, например - cat
        // Результат - cat-f7b4
        // cat.jpeg -> 3
        // cat -> -1
        int dotIndex = normalisedFileName.lastIndexOf(".");

        if (dotIndex == -1) {
            // cat -> cat-f7b4
            return String.format("%s-%s", normalisedFileName, randomPart);
        }

        // 3. Файл пришёл с именем с расширением, например - cat.jpeg
        // Результат - cat-f7b4.jpeg
        // cat.jpeg -> cat
        String fileNameWithoutExtension = normalisedFileName.substring(0, dotIndex);
        // cat.jpeg -> .jpeg
        String extension = normalisedFileName.substring(dotIndex);
        // cat.jpeg -> cat-f7b4.jpeg
        return String.format("%s-%s%s", fileNameWithoutExtension, randomPart, extension);
    }
}
