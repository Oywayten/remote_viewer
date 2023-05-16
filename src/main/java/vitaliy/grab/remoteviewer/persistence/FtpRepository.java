package vitaliy.grab.remoteviewer.persistence;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import vitaliy.grab.remoteviewer.model.RemoteFileData;

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
    private static final String EMPTY_STRING = "";
    private static final String DIR_SEP = "/";
    private static final Charset SERVER_CHARSET = StandardCharsets.ISO_8859_1;
    private static final Object ONE_DOT = ".";
    private static final String NEW_PATH_BUILD_FORMAT = "%s%s%s";
    private static final Object TWO_DOTS = "..";
    private final String cachingFolder;
    private final FTPClient ftp;

    @Value("${directory_name_for_search}")
    private String directoryNameForSearch;

    @Value("${prefix}")
    private String namePrefixForSearchPhoto;

    public FtpRepository(@Value("${download}") String cachingFolder, FTPClient ftp) {
        this.cachingFolder = cachingFolder;
        this.ftp = ftp;
    }

    private List<RemoteFileData> getRemoteFileDataListByStartPathAndDirectory(String startPath, String currentDir) throws IOException {
        if (EMPTY_STRING.equals(startPath)) {
            ftp.changeWorkingDirectory(EMPTY_STRING);
        }
        List<RemoteFileData> remoteFileDataList = new ArrayList<>();
        FTPFile[] ftpFiles = ftp.listFiles();
        String fileName;
        String newPath;
        boolean isTargetDir = directoryNameForSearch.equals(currentDir);
        for (FTPFile ftpFile : ftpFiles) {
            fileName = ftpFile.getName();
            newPath = String.format(NEW_PATH_BUILD_FORMAT, startPath, DIR_SEP, fileName);
            if (fileIsValidDir(fileName, ftpFile)) {
                ftp.changeWorkingDirectory(new String(fileName.getBytes(), SERVER_CHARSET));
                remoteFileDataList.addAll(getRemoteFileDataListByStartPathAndDirectory(newPath, fileName));
                ftp.changeToParentDirectory();
            } else if (fileIsValid(fileName, isTargetDir)) {
                remoteFileDataList.add(new RemoteFileData(newPath, fileName));
            }
        }
        return remoteFileDataList;
    }

    private boolean fileIsValid(String fileName, boolean isTargetDir) {
        return isTargetDir && fileName.startsWith(namePrefixForSearchPhoto);
    }

    private static boolean fileIsValidDir(String fileName, FTPFile ftpFile) {
        return ftpFile.isDirectory() && !Objects.equals(fileName, ONE_DOT) && !Objects.equals(fileName, TWO_DOTS);
    }

    public List<RemoteFileData> getRemoteFileDataList() throws IOException {
        return getRemoteFileDataListByStartPathAndDirectory(EMPTY_STRING, EMPTY_STRING);
    }


    public void cachingPhotoByPath(String path) throws IOException {
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        cachingPhotoByPathFromFtp(path, ftp);
    }

    private void cachingPhotoByPathFromFtp(String ftpFilePath, FTPClient ftp) throws IOException {
        Path newLocalFilePath = Paths.get(cachingFolder, ftpFilePath);
        Path localDirectories = newLocalFilePath.getParent();
        Files.createDirectories(localDirectories);
        Path ftpDirectories = Paths.get(ftpFilePath).getParent();
        for (Path directory : ftpDirectories) {
            String directoryName = new String(directory.toString().getBytes(), SERVER_CHARSET);
            ftp.changeWorkingDirectory(directoryName);
        }
        String fileName = newLocalFilePath.getFileName().toString();
        FileOutputStream out = new FileOutputStream(newLocalFilePath.toString());
        ftp.retrieveFile(fileName, out);
    }
}
