package com.example.TelegramBot.api.model;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileDownloadResponseHandler implements ResponseHandler<File> {

    private final File target;

    public FileDownloadResponseHandler(File target) {
        this.target = target;
    }

    @Override
    public File handleResponse(HttpResponse response) throws IOException {
        InputStream source = response.getEntity().getContent();
        FileUtils.copyInputStreamToFile(source, this.target);
        return this.target;
    }
}
