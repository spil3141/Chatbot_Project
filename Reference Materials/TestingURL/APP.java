import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.LDAPCertStoreParameters;
import java.util.LongSummaryStatistics;
/**
 * Write a description of class APP here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class APP
{
   public static boolean log(String logURL) {

        String charset = "UTF-8";
        LDAPCertStoreParameters logBuffer = null;
.........
        OutputStream output = null;
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        InputStream in = null;

        try {
            String query = String.format("log=%s", URLEncoder.encode(logData, charset));
            conn = (HttpURLConnection) new URL(logURL).openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept-Charset", charset);
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=" + charset);
            output = conn.getOutputStream();
            output.write(query.getBytes(charset));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (output != null) try { output.close(); } catch (IOException e) {e.printStackTrace();}
        }

        // Handle the response
        try {
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                in = conn.getInputStream();
            } else {
                in = conn.getErrorStream();
            }
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            logNote("reading response");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
            if (responseCode == 200) {
                return true;
            }
            else {
                return false;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (reader != null) try { reader.close(); } catch (IOException e) {e.printStackTrace();}
        }
    }

    private static void logNote(String reading_response) {
    }
    public static void main(String[] args){
        System.out.println(log("http://192.168.0.10/savelog/"));
    }
}
