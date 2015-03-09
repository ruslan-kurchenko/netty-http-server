package com.henko.server.view.impl;

import com.henko.server.view.Page;
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

    private String generateContent() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>404 Not Found</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Not Found</h1>\n" +
                "    <p>The requested URL ~" + errorPath + " was not found on this server.</p>\n" +
                "    <hr>\n" +
                "    <address>Netty Server by Ruslan Kurchenko</address>\n" +
                "</body>\n" +
                "</html>";
    }
}
