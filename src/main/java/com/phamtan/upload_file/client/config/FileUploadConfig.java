package com.phamtan.upload_file.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The type File upload config.
 *
 * @author Pham Minh Tan
 */
@Configuration
@ConfigurationProperties(prefix = "file.client.upload")
public class FileUploadConfig {
    /**
     *  Size for the message delivered (default 10 MB)
     */
      private Integer chunkSize=1024*1024*10;
      private String domainServer;
    /**
     *  Port to connect to Grpc server  (default 9090 MB)
     */
      private Integer portServer = 9090;
    /**
     *  Max Size for the message delivered (default 50 MB)
     */
      private Integer maxSizePerMessage = 1024*1024*50;
    /**
     *  Time waiting for response from start call time
     *  Default 5 minutes;
     *  Time units is strict to Second
     */
    private Integer deadline= 60 *60 *5;

    public Integer getMaxSizePerMessage() {
        return maxSizePerMessage;
    }

    public void setMaxSizePerMessage(Integer maxSizePerMessage) {
        this.maxSizePerMessage = maxSizePerMessage;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }


    public void setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
    }


    public String getDomainServer() {
        return domainServer;
    }


    public void setDomainServer(String domainServer) {
        this.domainServer = domainServer;
    }


    public Integer getPortServer() {
        return portServer;
    }


    public void setPortServer(Integer portServer) {
        this.portServer = portServer;
    }
}
