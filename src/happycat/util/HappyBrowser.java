package happycat.util;

import cn.hutool.http.HttpUtil;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// serve as a tiny browser, send http request and get http response
public class HappyBrowser {

    public static void main(String[] args) throws Exception {
        String url = "https://stackoverflow.com/";//need vpn in china,just test
        String httpString= getHttpString(url,false);
        System.out.println(httpString);
    }

    public static byte[] getContentBytes(String url, Map<String,Object> params, boolean isGet) {
        return getContentBytes(url, false,params,isGet);
    }

    public static byte[] getContentBytes(String url, boolean gzip) {
        return getContentBytes(url, gzip,null,true);
    }

    public static byte[] getContentBytes(String url) {
        return getContentBytes(url, false,null,true);
    }

    public static String getContentString(String url, Map<String,Object> params, boolean isGet) {
        return getContentString(url,false,params,isGet);
    }

    public static String getContentString(String url, boolean gzip) {
        return getContentString(url, gzip, null, true);
    }

    public static String getContentString(String url) {
        return getContentString(url, false, null, true);
    }

    public static String getContentString(String url, boolean gzip, Map<String,Object> params, boolean isGet) {
        byte[] result = getContentBytes(url, gzip,params,isGet);
        if(null==result)
            return null;
        try {
            return new String(result,"utf-8").trim();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] getContentBytes(String url, boolean gzip, Map<String,Object> params, boolean isGet) {
        // return http response body
        // 1.check the doubleReturn start index
        // 2.cut the body part with body start index to the end
        byte[] response = getHttpBytes(url,gzip,params,isGet);
        byte[] doubleReturn = "\r\n\r\n".getBytes();

        //find the body start index
        int pos = -1;
        for (int i = 0; i < response.length-doubleReturn.length; i++) {
            byte[] temp = Arrays.copyOfRange(response, i, i + doubleReturn.length);

            if(Arrays.equals(temp, doubleReturn)) {
                pos = i;
                break;
            }
        }
        if(-1==pos)//if there is no body, body is null
            return null;

        pos += doubleReturn.length;//throw the doubleReturn part

        byte[] result = Arrays.copyOfRange(response, pos, response.length);//get the body part
        return result;
    }

    public static String getHttpString(String url,boolean gzip) {
        return getHttpString(url, gzip, null, true);
    }

    public static String getHttpString(String url) {
        return getHttpString(url, false, null, true);
    }

    public static String getHttpString(String url,boolean gzip, Map<String,Object> params, boolean isGet) {
        byte[]  bytes=getHttpBytes(url,gzip,params,isGet);
        return new String(bytes).trim();
    }

    public static String getHttpString(String url, Map<String,Object> params, boolean isGet) {
        return getHttpString(url,false,params,isGet);
    }

    public static byte[] getHttpBytes(String url,boolean gzip, Map<String,Object> params, boolean isGet) {
        // get http response with specified url and parameters
        // 1.new socket to connect a sever with port 80(http protocol)
        // 2.build http request string
        // 3.send the request string with PrintWriter that wrapped socket outPutStream
        // 4.get socket inputStream byte and return
        String method = isGet?"GET":"POST";
        byte[] result = null;
        try {
            URL u = new URL(url);
            Socket client = new Socket();
            int port = u.getPort();
            if(-1==port)
                port = 80;
            InetSocketAddress inetSocketAddress = new InetSocketAddress(u.getHost(), port);
            client.connect(inetSocketAddress, 1000);
            Map<String,String> requestHeaders = new HashMap<>();

            requestHeaders.put("Host", u.getHost()+":"+port);
            requestHeaders.put("Accept", "text/html");
            requestHeaders.put("Connection", "close");
            requestHeaders.put("User-Agent", "happy browser / java1.8");

            if(gzip)
                requestHeaders.put("Accept-Encoding", "gzip");

            String path = u.getPath();
            if(path.length()==0)
                path = "/";

            if(null!=params && isGet){
                String paramsString = HttpUtil.toParams(params);
                path = path + "?" + paramsString;
            }

            String firstLine = method + " " + path + " HTTP/1.1\r\n";

            StringBuffer httpRequestString = new StringBuffer();
            httpRequestString.append(firstLine);
            Set<String> headers = requestHeaders.keySet();
            for (String header : headers) {
                String headerLine = header + ":" + requestHeaders.get(header)+"\r\n";
                httpRequestString.append(headerLine);
            }

            if(null!=params && !isGet){
                String paramsString = HttpUtil.toParams(params);
                httpRequestString.append("\r\n");
                httpRequestString.append(paramsString);
            }

            PrintWriter pWriter = new PrintWriter(client.getOutputStream(), true);
            //use PrintWriter to manipulate io with print like method
            pWriter.println(httpRequestString);
            InputStream is = client.getInputStream();

            result = readBytes(is,true);
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                result = e.toString().getBytes("utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }

        return result;

    }

    public static byte[] readBytes(InputStream is, boolean fully) throws IOException {
        //read socket InputStream bytes and return
        int buffer_size = 1024;
        byte buffer[] = new byte[buffer_size];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while(true) {
            int length = is.read(buffer);
            if(-1==length)
                break;
            baos.write(buffer, 0, length);
            if(!fully && length!=buffer_size)
                break;
        }
        byte[] result =baos.toByteArray();
        return result;
    }
}