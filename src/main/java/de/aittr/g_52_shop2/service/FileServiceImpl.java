package de.aittr.g_52_shop2.service;

import de.aittr.g_52_shop2.exception_handling.exceptions.FileUploadException;
import de.aittr.g_52_shop2.service.interfaces.FileService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class FileServiceImpl implements FileService {

    @Override
    public String uploadAndGetUrl(MultipartFile file) {
        // Логика метода:
        // 1. Необходимые проверки
        Objects.requireNonNull(file, "MultipartFile must not be null");

        if (file.isEmpty()) {
            throw new FileUploadException("File is empty");
        }

        // 2. Сгенерировать уникальное имя файла
        String uniqueFileName = generateUniqueFileName(file);

        // 3. Создать запрос на загрузку файла в облако под уникальным именем
        // 4. Отправка запроса (фактическая загрузка файла в облако)
        // 5. Отправляем ещё запрос с целью получить ссылку на загруженный файл
        // 6. Возвращаем полученную ссылку как результат работы метода
        return "";
    }

    private String generateUniqueFileName(MultipartFile file) {
        // Какие есть варианты:
        // Представим, что уникальная часть имени файла - f7b4
        // 1. Файл пришёл вообще без имени
        // Результат - f7b4

        // 2. Файл пришёл с именем без расширения, например - cat
        // Результат - cat-f7b4

        // 3. Файл пришёл с именем с расширением, например - cat.jpeg
        // Результат - cat-f7b4.jpeg
        return file.getOriginalFilename();
    }
}
