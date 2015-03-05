package com.henko.server.view.pages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class ErrorPage implements Page {
    
    private final String errorPath;
    
    public ErrorPage(String errorPath) {
        this.errorPath = errorPath;
    }
    
    @Override
    public ByteBuf getContent(Charset charset) {
        return Unpooled.copiedBuffer(generateContent(), charset);
    }

    private String generateContent(){
        StringBuilder content = new StringBuilder()
                .append("<!DOCTYPE html>")
                .append("<html lang=\"en\">")
                .append("<head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<title>404 Not Found</title>")
                .append("</head>")
                .append("<body>")
                .append("<h1>Not Found</h1>")
                .append("<p>The requested path ")
                .append(errorPath)
                .append(" was not found on this server.</p>")
                .append("</body>")
                .append("</html>");

        return content.toString();
    }
}
