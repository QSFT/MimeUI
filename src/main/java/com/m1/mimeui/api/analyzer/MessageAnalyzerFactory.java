package com.m1.mimeui.api.analyzer;

public interface MessageAnalyzerFactory<E>
{
    public MessageAnalyzer<E> getMessageAnalyser ();
}
