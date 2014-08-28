package com.m1.mimeui.api;

public interface Logger
{
    /**
     * This event is triggered when an event occurs that may be useful for debugging. Services may want to log these
     * events as "DEBUG" or "INFO" level log messages in development or QA environments. In production environments, it
     * is typical for this event to be ignored.
     *
     * @param message A description of the debug event.
     */
    public void debug (final String message);

    /**
     * This event is triggered when any kind of exceptional event occurs that the framework was able to handle on its
     * own in a reasonable way. Services may want to log these events as "WARNING" or "ERROR" level log messages in any
     * environment. In production environments, it may be more appropriate to log these as "INFO" when large numbers of
     * messages are being processed.
     *
     * @param message A description of the error event.
     */
    public void error (final String message);

    /**
     * This event is triggered when any kind of exceptional event occurs that the framework was able to handle on its
     * own in a reasonable way. Services may want to log these events as "WARNING" or "ERROR" level log messages in any
     * environment. In production environments, it may be more appropriate to log these as "INFO" when large numbers of
     * messages are being processed.
     *
     * @param cause The exception associated with the event.
     * @param message A description of the error event.
     */
    public void error (final Throwable cause, final String message);

}
