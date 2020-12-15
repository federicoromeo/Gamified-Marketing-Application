package utils;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;

public class Data
{
    public static boolean equalDates(Date date1, Date date2)
    {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        return formatter.format(date1).equals(formatter.format(date2));
    }

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
