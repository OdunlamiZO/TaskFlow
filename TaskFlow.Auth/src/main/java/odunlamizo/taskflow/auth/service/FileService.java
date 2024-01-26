package odunlamizo.taskflow.auth.service;

import odunlamizo.taskflow.auth.dto.FileDto;
import odunlamizo.taskflow.auth.model.File;

public interface FileService {

    File save(FileDto fileDto);
    
}
