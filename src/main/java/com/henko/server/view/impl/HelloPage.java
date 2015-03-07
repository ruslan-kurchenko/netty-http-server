package com.henko.server.view.impl;

import com.henko.server.view.Page;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class HelloPage implements Page {

    @Override
    public ByteBuf getContent(Charset charset) {
        return Unpooled.copiedBuffer(generateContent(), charset);
    }
    
    private String generateContent(){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Hello</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1 style=\"text-align: center;\">Hello World!</h1>\n" +
                "</body>\n" +
                "</html>";
    }
}
