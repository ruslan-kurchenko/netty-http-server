package com.henko.server.controller;

import com.henko.server.view.impl.ErrorPage;
import com.henko.server.view.impl.HelloPage;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

import static com.henko.server.controller.PageRegistry.*;

public class Controller {
    
    public ByteBuf getPageContent(String path, Charset charset) {
        switch (path) {
            case PAGE_HELLO: return processPageHello(charset);
            case PAGE_STATUS: return processPageStatus(charset);
            
            default: return processPageError(path, charset);
        }
    }

    private ByteBuf processPageStatus(Charset charset) {
        return null;
    }

    private ByteBuf processPageHello(Charset charset) {
        return new HelloPage().getContent(charset);
    }
    
    private ByteBuf processPageError(String path, Charset charset) {
        return new ErrorPage(path).getContent(charset);
    }
}
