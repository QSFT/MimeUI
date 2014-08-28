package com.m1.mimeui.renderer.contentviewer;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.renderer.ContentViewer;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.Method;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TextCalendarContentViewer implements ContentViewer
{
    @Override
    public Collection<String> getSupportedMimeTypes ()
    {
        return Collections.singletonList("text/calendar");
    }

    @Override
    public void render (final IncorporatedPart part, final PrintWriter htmlOut)
            throws MimeUIException
    {
        final CalendarBuilder calendarBuilder = new CalendarBuilder();

        try
        {
            final Calendar calendar = calendarBuilder.build(part.getInputStream());

            //final Method method = calendar.getMethod();

            for (final Component component : (List<Component>)calendar.getComponents())
            {
                component.getName();
                component.getProperty(Property.DTSTART).getParameter(Parameter.VALUE).getValue();
            }
        }
        catch (final IOException e)
        {
            throw new MimeUIException("Exception while reading the calendar object.", e);
        }
        catch (final ParserException e)
        {
            throw new MimeUIException("Exception while parsing the calendar object.", e);
        }
    }

    public static void main (final String... args)
            throws Exception
    {
        final String ical = "BEGIN:VCALENDAR\n" +
                "PRODID:-//ACME/DesktopCalendar//EN\n" +
                "METHOD:REQUEST\n" +
                "X-LIC-NOTE: #I1. Reschedules C1\n" +
                "VERSION:2.0\n" +
                "BEGIN:VEVENT\n" +
                "ORGANIZER:Mailto:B@example.com\n" +
                "ATTENDEE;ROLE=CHAIR;PARTSTAT=ACCEPTED;CN=BIG A:Mailto:A@example.com\n" +
                "ATTENDEE;RSVP=TRUE;CUTYPE=INDIVIDUAL;CN=B:Mailto:B@example.com\n" +
                "ATTENDEE;RSVP=TRUE;CUTYPE=INDIVIDUAL;CN=C:Mailto:C@example.com\n" +
                "ATTENDEE;RSVP=TRUE;CUTYPE=INDIVIDUAL;CN=Hal:Mailto:D@example.com\n" +
                "ATTENDEE;RSVP=FALSE;CUTYPE=ROOM:conf_Big@example.com\n" +
                "ATTENDEE;ROLE=NON-PARTICIPANT;RSVP=FALSE:Mailto:E@example.com\n" +
                "DTSTAMP:19970611T190000Z\n" +
                "DTSTART:19970701T200000Z\n" +
                "DTEND:19970701T2000000Z\n" +
                "SUMMARY:Conference\n" +
                "UID:calsrv.example.com-873970198738777@example.com\n" +
                "SEQUENCE:2\n" +
                "STATUS:CONFIRMED\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR";

        final SimpleDateFormat dateFormat = new SimpleDateFormat();
        final CalendarBuilder calendarBuilder = new CalendarBuilder();

        final Calendar calendar = calendarBuilder.build(new StringReader(ical));

        final Method method = calendar.getMethod();

        System.out.println(method.getValue());  // REQUEST, e.g.
        System.out.println(method == Method.REQUEST);

        for (final Component component : (List<Component>)calendar.getComponents())
        {
            System.out.println(component.getName()); // VEVENT, e.g.

            if (component instanceof VEvent)
            {
                final VEvent vevent = (VEvent) component;

                System.out.println(vevent.getSummary());
                System.out.println(vevent.getDescription());
                System.out.println(dateFormat.format(vevent.getStartDate().getDate()));
            }

            for (final Property property : (List<Property>) component.getProperties(Property.ATTENDEE))
            {
                final Attendee attendee = (Attendee) property;

                System.out.println(attendee.getCalAddress().getSchemeSpecificPart());
                System.out.println(attendee.getParameter(Parameter.CN));
                System.out.println(attendee.getName());
                System.out.println(attendee.getValue());
            }
        }

        System.out.println("END");
        System.exit(0);
    }
}
