package com.henko.server.view.pages;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public interface Page {
    
    ByteBuf getContent(Charset charset);
}
