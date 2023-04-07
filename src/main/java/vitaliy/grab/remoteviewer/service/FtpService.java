package vitaliy.grab.remoteviewer.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vitaliy.grab.remoteviewer.model.RemoteFile;
import vitaliy.grab.remoteviewer.persistence.FtpRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Oywayten 06.04.2023
 */
@Service
public class FtpService {

    private final String dataStore;
    private final FtpRepository ftpRepository;

    public FtpService(@Value("${download}") String dataStore, FtpRepository ftpRepository) {
        this.dataStore = dataStore;
        this.ftpRepository = ftpRepository;
    }

    public List<RemoteFile> getFilePaths() throws IOException {
        return ftpRepository.getFilePaths();
    }

    public byte[] getPhoto(String fileName) throws IOException {
        File file = new File(dataStore, fileName);
        if (!file.exists()) {
            ftpRepository.downloadFile(fileName);
        }
        return FileUtils.readFileToByteArray(file);
    }


}
