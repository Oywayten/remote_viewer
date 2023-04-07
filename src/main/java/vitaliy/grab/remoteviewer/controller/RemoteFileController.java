package vitaliy.grab.remoteviewer.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vitaliy.grab.remoteviewer.model.RemoteFile;
import vitaliy.grab.remoteviewer.service.FtpService;

import java.io.IOException;
import java.util.List;

/**
 * @author Oywayten 05.04.2023
 */
@Slf4j
@RestController
@AllArgsConstructor
public class RemoteFileController {

    private final FtpService ftpService;

    @GetMapping("/photos")
    public List<RemoteFile> getPhotos() throws IOException {
        return ftpService.getFilePaths();
    }

    @GetMapping(
            value = "/photo",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getPhoto(@RequestParam(name = "path") String path) throws IOException {
        return ftpService.getPhoto(path);
    }
}
