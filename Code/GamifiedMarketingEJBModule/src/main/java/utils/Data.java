package utils;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;

public class Data
{
    /**
     * Compare two instances of date, potentially expressed in different formats
     * @param date1 first Date to compare
     * @param date2 second Date to compare
     * @return true if the dates are the same
     */
    public static boolean equalDates(Date date1, Date date2)
    {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        return formatter.format(date1).equals(formatter.format(date2));
    }

    /**
     * Converts a string to a Date object
     * @param s a string representing a date
     * @return a Date object
     */
    public static Date stringToDate(String s)
    {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        try
        {
            return formatter.parse(s);
        }
        catch(ParseException e)
        {
            System.err.println("Invalid date string");
            return null;
        }
    }

    /**
     * Hides complexity of reading a file coming from a servlet
     * @param filePart a Part object
     * @return an array of byte representing the file
     */
    public static byte[] filepartToByte(Part filePart)
    {
        InputStream fileContent;

        try
        {
            fileContent = filePart.getInputStream();
            return IOUtils.toByteArray(fileContent);
        }
        catch(Exception e)
        {
            System.err.println("Error in reading the image");
            return null;
        }

    }
}
