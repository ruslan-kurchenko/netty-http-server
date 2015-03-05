package com.henko.server.controller;

import com.henko.server.view.PageRegistry;
import com.henko.server.view.pages.ErrorPage;
import com.henko.server.view.pages.HelloPage;
import com.henko.server.view.pages.Page;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

import static com.henko.server.view.PageRegistry.*;

public class Controller {
    
    public ByteBuf getPageContent(String path, Charset charset) {
        ByteBuf pageContent = null;
        
        switch (path) {
            case HELLO_PAGE: pageContent = processHelloPage(charset);
        }
        if (pageContent == null) pageContent = processErrorPage(path, charset);
        
        return pageContent;
    }
    
    private ByteBuf processHelloPage(Charset charset) {
        return new HelloPage().getContent(charset);
    }
    
    private ByteBuf processErrorPage(String path, Charset charset) {
        return new ErrorPage(path).getContent(charset);
    }
}
