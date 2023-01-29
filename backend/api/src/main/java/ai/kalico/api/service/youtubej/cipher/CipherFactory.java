package com.kalico.api.service.youtubej.cipher;


import com.kalico.api.service.youtubej.YoutubeException;

public interface CipherFactory {

    Cipher createCipher(String jsUrl) throws YoutubeException;

    void addInitialFunctionPattern(int priority, String regex);

    void addFunctionEquivalent(String regex, CipherFunction function);
}
