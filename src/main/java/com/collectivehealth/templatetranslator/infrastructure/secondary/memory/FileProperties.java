package com.collectivehealth.templatetranslator.infrastructure.secondary.memory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
class FileProperties {


    private final Resource jsonFile;

    FileProperties(@Value("classpath:static/store/templatesRepository.json") Resource jsonFile) {
        this.jsonFile = jsonFile;
    }

    File file() throws IOException {
        File file = jsonFile.getFile();
        // Ensure the directory and file exist
        File parentDir = file.getParentFile();
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            throw new IOException("Failed to create the directory: " + parentDir.getAbsolutePath());
        }
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Failed to create the file: " + file.getAbsolutePath());
        }
        return file;
    }

}
