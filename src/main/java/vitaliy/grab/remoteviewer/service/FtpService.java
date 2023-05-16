package vitaliy.grab.remoteviewer.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vitaliy.grab.remoteviewer.model.RemoteFileData;
import vitaliy.grab.remoteviewer.persistence.FtpRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Oywayten 06.04.2023
 */
@Service
public class FtpService {

    private final String cachingFolder;
    private final FtpRepository ftpRepository;

    public FtpService(@Value("${download}") String cachingFolder, FtpRepository ftpRepository) {
        this.cachingFolder = cachingFolder;
        this.ftpRepository = ftpRepository;
    }

    public List<RemoteFileData> getPhotosPaths() throws IOException {
        return ftpRepository.getRemoteFileDataList();
    }

    public byte[] getPhotoByPath(String path) throws IOException {
        File file = new File(cachingFolder, path);
        if (!file.exists()) {
            ftpRepository.cachingPhotoByPath(path);
        }
        return FileUtils.readFileToByteArray(file);
    }


}
