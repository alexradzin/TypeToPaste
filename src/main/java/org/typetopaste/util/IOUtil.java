package org.typetopaste.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO utilities
 * @author alex
 *
 */
public class IOUtil {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static final String DEFAULT_CHARSET = "UTF8";
    
    /**
     * Copies input stream to output
     * @param input
     * @param output
     * @return
     * @throws IOException
     */
    public static long copy(InputStream input, OutputStream output) throws IOException {
    	return copy(input, output, new byte[DEFAULT_BUFFER_SIZE]);
    }
    
    /**
     * Copies input stream to output using given buffer
     * @param input
     * @param output
     * @param buffer
     * @return
     * @throws IOException
     */
    public static long copy(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count = 0;
        int n = 0;
        while ( (n = input.read(buffer)) >= 0) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }


    /**
     * Reads input stream as a string using UTF8 charset and returns the result. 
     * @param input
     * @return string that is read from the input stream
     * @throws IOException
     * @see {@link #readAllAsString(InputStream, String)}
     */
    public static String readAllAsString(InputStream input) throws IOException {
    	return readAllAsString(input, DEFAULT_CHARSET);
    }
    
    
    /**
     * Reads input stream as a string using given charset and returns the result.
     * @param input
     * @param charsetName
     * @return
     * @throws IOException
     */
    public static String readAllAsString(InputStream input, String charsetName) throws IOException {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	copy(input, baos);
    	baos.close();
    	
    	return new String(baos.toByteArray(), charsetName);
    }
}
