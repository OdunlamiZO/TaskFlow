package odunlamizo.taskflow.auth.service;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import odunlamizo.taskflow.auth.dto.FileDto;
import odunlamizo.taskflow.auth.model.File;
import odunlamizo.taskflow.auth.repository.FileRepository;

@Service
public class FileServiceImpl implements FileService {

    private static final int MAX_FILE_SIZE = 2 * 1024;

    @Autowired
    private FileRepository fileRepository;

    @Override
    public File save(FileDto fileDto) {
        File file = new File();
        byte[] decodedData = Base64.getDecoder().decode(fileDto.getData());
        if (decodedData.length > MAX_FILE_SIZE) {
            // throw exception
        }
        file.setData(decodedData);
        file.setMimeType(fileDto.getMimeType());
        return fileRepository.save(file);
    }

}
