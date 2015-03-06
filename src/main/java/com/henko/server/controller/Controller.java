package com.henko.server.controller;

import com.henko.server.domain.MockServerStatus;
import com.henko.server.view.pages.impl.ErrorPage;
import com.henko.server.view.pages.impl.HelloPage;
import com.henko.server.view.pages.impl.StatusPage;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

import static com.henko.server.view.PageRegistry.*;

public class Controller {
    
    public ByteBuf getPageContent(String path, Charset charset) {
        switch (path) {
            case PAGE_HELLO: return processPageHello(charset);
            case PAGE_STATUS: return processPageStatus(charset);
            
            default: return processPageError(path, charset);
        }
    }

    private ByteBuf processPageStatus(Charset charset) {
        return new StatusPage(new MockServerStatus()).getContent(charset);
    }

    private ByteBuf processPageHello(Charset charset) {
        return new HelloPage().getContent(charset);
    }
    
    private ByteBuf processPageError(String path, Charset charset) {
        return new ErrorPage(path).getContent(charset);
    }
}
