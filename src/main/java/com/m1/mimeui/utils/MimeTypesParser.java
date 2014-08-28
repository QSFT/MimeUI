package com.m1.mimeui.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MimeTypesParser
{
    public void parse (final Reader mimeTypesReader,
                       final Map<String, List<String>> mimeTypesOut, final Map<String, String> fileExtensionsOut)
            throws IOException
    {
        final BufferedReader bufferedReader = new BufferedReader(mimeTypesReader);
        String line;

        // Iterate over the lines of the file.
        while ((line = bufferedReader.readLine()) != null)
        {
            if (line.charAt(0) == '#')
            {
                // Ignore commented out lines.
                continue;
            }

            // Split the lines on whitespace, ignoring leading and trailing whitespace.
            final String[] tokens = line.trim().split("\\s+");

            // Each line should have at least two tokens (maybe more).
            if (tokens.length < 2)
            {
                // Ignore blank lines or mime types that have no known file extensions.
                continue;
            }

            // The first token is always the mimeType.
            final String mimeType = tokens[0];

            // Index the mappings by both mime type and by file extension.
            mimeTypesOut.put(mimeType, Arrays.asList(Arrays.copyOfRange(tokens, 1, tokens.length)));

            // The rest of the tokens are the known file extensions. Iterate over them.
            for (int index = 1; index < tokens.length; index++)
            {
                // Note that I have no idea if file extensions are always distinct, nor do I particularly care.
                fileExtensionsOut.put(tokens[index], mimeType);
            }
        }
    }
}
