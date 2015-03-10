package com.henko.server.view;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class ErrorPage implements Page {

    private final String _errorPath;

    public ErrorPage(String errorPath) {
        this._errorPath = errorPath;
    }

    @Override
    public ByteBuf getContent(Charset charset) {
        return Unpooled.copiedBuffer(_generateContent(), charset);
    }

    private String _generateContent() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>404 Not Found</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Not Found</h1>\n" +
                "    <p>The requested URL ~" + _errorPath + " was not found on this server.</p>\n" +
                "    <hr>\n" +
                "    <address>Netty Server by Ruslan Kurchenko</address>\n" +
                "</body>\n" +
                "</html>";
    }
}
