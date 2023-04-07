package vitaliy.grab.remoteviewer.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Oywayten 05.04.2023
 */
@Slf4j
@Repository
public class FtpRepository {
    private static final String START_DIR = "";
    private static final String DIR_SEP = "/";
    private static final Charset SERVER_CHARSET = StandardCharsets.ISO_8859_1;
    private static final Object ONE_DOT = ".";
    private static final String NEW_PATH_BUILD = "%s%s%s";
    private static final Object TWO_DOTS = "..";
    private final String dataStore;
    private final FTPClient ftp;

    public FtpRepository(@Value("${download}") String dataStore, FTPClient ftp) {
        this.dataStore = dataStore;
        this.ftp = ftp;
    }

    @Value("${photos}")
    private String photos;

    @Value("${prefix}")
    private String prefix;

    private List<String> recursiveFilePath(String directory) throws IOException {
        List<String> result = new ArrayList<>();
        String path = ftp.printWorkingDirectory();
        FTPFile[] ftpFiles = ftp.listFiles();
        String fileName;
        String newPath;
        boolean isTarget = photos.equals(directory);
        for (FTPFile ftpFile : ftpFiles) {
            fileName = ftpFile.getName();
            newPath = String.format(NEW_PATH_BUILD, path, DIR_SEP, fileName);
            if (ftpFile.isDirectory() && !Objects.equals(fileName, ONE_DOT) && !Objects.equals(fileName, TWO_DOTS)) {
                ftp.changeWorkingDirectory(new String(fileName.getBytes(), SERVER_CHARSET));
                result.addAll(recursiveFilePath(fileName));
            } else if (isTarget && fileName.startsWith(prefix)) {
                result.add(new String(newPath.getBytes(), StandardCharsets.US_ASCII));
            }
        }
        return result;
    }

    public List<String> getFilePaths() throws IOException {
        return recursiveFilePath(START_DIR);
    }


    public void downloadFile(String path) throws IOException {
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        downloadFile(ftp, path);
    }

    private void downloadFile(FTPClient ftp, String path) throws IOException {
        Path newFilePath = Paths.get(dataStore, path);
        Path parent = newFilePath.getParent();
        Files.createDirectories(parent);
        String fileName = newFilePath.getFileName().toString();
        FileOutputStream out = new FileOutputStream(newFilePath.toString());
        for (Path path1 : Paths.get(path).getParent()) {
            ftp.changeWorkingDirectory(new String(path1.toString().getBytes(), SERVER_CHARSET));
        }
        ftp.retrieveFile(fileName, out);
    }
}