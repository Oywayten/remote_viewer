package vitaliy.grab.remoteviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RemoteViewerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RemoteViewerApplication.class, args);
        System.out.println("Go to http://localhost:8090/photos");
        System.out.println("Go to http://localhost:8090/photo?path=/htdocs/ftp/%D0%90%D1%80%D1%85%D0%B8%D0%B2/%D1%84%D0%BE%D1%82%D0%BE%D0%B3%D1%80%D0%B0%D1%84%D0%B8%D0%B8/GRP327_willian-justen-de-vasconcellos-PwD4mF7MtSg-unsplash.jpg");
        System.out.println("Go to http://localhost:8090/photo?path=/htdocs/ftp/%D0%90%D1%80%D1%85%D0%B8%D0%B2/%D1%84%D0%BE%D1%82%D0%BE%D0%B3%D1%80%D0%B0%D1%84%D0%B8%D0%B8/GRP327_vitali-adutskevich-ITPlwLHwS4k-unsplash.jpg");
    }

}
