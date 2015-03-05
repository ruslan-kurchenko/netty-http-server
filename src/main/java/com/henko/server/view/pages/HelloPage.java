package com.henko.server.view.pages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class HelloPage implements Page{

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
                .append("<title>Hello</title>")
                .append("<style> h1 { text-align: center; } </style>")
                .append("</head>")
                .append("<body>")
                .append("<h1>Hello World!</h1>")
                .append("</body>")
                .append("</html>");

        return content.toString();
    }
}
