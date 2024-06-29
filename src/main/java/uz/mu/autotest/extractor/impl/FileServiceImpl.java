package uz.mu.autotest.extractor.impl;

import org.springframework.stereotype.Service;
import uz.mu.autotest.extractor.FileService;

import java.io.File;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }
}