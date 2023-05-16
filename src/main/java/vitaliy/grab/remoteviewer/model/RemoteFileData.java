package vitaliy.grab.remoteviewer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Oywayten 05.04.2023
 */
@Data
@AllArgsConstructor
public class RemoteFileData {
    private String path;
    private String name;
}
