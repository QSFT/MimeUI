package com.m1.mimeui.api;

/**
 * Used by the *Api classes to indicate that an operation could not be completed as requested. Callers should make no
 * assumptions as to the state of any objects that were passed to the method that throws this exception unless otherwise
 * noted in that API's documentation.
 */
public class MimeUIException extends Exception
{
    public MimeUIException (final String message) {super(message);}
    public MimeUIException (final String message, final Throwable cause) {super(message, cause);}
}
