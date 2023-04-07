package vitaliy.grab.remoteviewer.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author Oywayten 07.04.2023
 */
@Configuration
@Slf4j
public class FtpConfiguration {
    private static final String LOCAL_CHARSET = StandardCharsets.UTF_8.displayName();
    private static final String SESSION_CONNECTED = "session connected";
    private static final String EXCEPTION_IN_CONNECTING = "Exception in connecting to FTP Server";

    @Value("${host}")
    private String host;

    @Value("${port}")
    private int port;

    @Value("${user}")
    private String user;

    @Value("${password}")
    private String password;

    @Bean
    public FTPClient getFtp() throws IOException {
        FTPClient ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        ftp.connect(host, port);
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new IOException(EXCEPTION_IN_CONNECTING);
        }
        ftp.login(user, password);
        ftp.setControlEncoding(LOCAL_CHARSET);
        log.info(SESSION_CONNECTED);
        return ftp;
    }
}





