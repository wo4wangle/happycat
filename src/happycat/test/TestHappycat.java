package happycat.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.HttpUtil;
import happycat.util.HappyBrowser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
//unit test for happycat
public class TestHappycat {
    private static int port = 18080;
    private static String ip = "127.0.0.1";
    @BeforeClass
    public static void beforeClass() {
        //check happycat is running
        if(NetUtil.isUsableLocalPort(port)) {
            System.err.println("please start happycat on port: " +port);
            System.exit(1);
        }
        else {
            System.out.println("happycat is running, start test");
        }
    }

    @Test
    public void testHelloTomcat() {
        String html = getContentString("/");
        Assert.assertEquals(html,"Hello happycat from LeWang");
    }

    @Test
    public void testaHtml() {
        //get text from ROOT/a.html
        String html = getContentString("/a.html");
        Assert.assertEquals(html,"Hello happycat from LeWang");
    }

    @Test
    public void testTimeConsumeHtml() throws InterruptedException {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20, 20, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(10));
        TimeInterval timeInterval = DateUtil.timer();

        for(int i = 0; i<3; i++){
            threadPool.execute(new Runnable(){
                public void run() {
                    getContentString("/timeConsume.html");
                }
            });
        }
        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);

        long duration = timeInterval.intervalMs();

        Assert.assertTrue(duration < 3000);
    }

    @Test
    public void testaIndex() {
        String html = getContentString("/a");
        Assert.assertEquals(html,"Hello happycat from index.html@a");
    }

    @Test
    public void testbIndex() {
        String html = getContentString("/b/");
        Assert.assertEquals(html,"Hello happycat from index.html@b");
    }

    @Test
    public void test404() {
        String response  = getHttpString("/not_exist.html");
        containAssert(response, "HTTP/1.1 404 Not Found");
    }
    @Test
    public void test500() {
        String response  = getHttpString("/500.html");
        containAssert(response, "HTTP/1.1 500 Internal Server Error");
    }

    @Test
    public void testaTxt() {
        String response  = getHttpString("/a.txt");
        containAssert(response, "Content-Type: text/plain");
    }

    @Test
    public void testPNG() {
        byte[] bytes = getContentBytes("/happycat.png");
        int pngFileLength = 178619;
        Assert.assertEquals(pngFileLength, bytes.length);
    }
    @Test
    public void testPDF() {
        String uri = "/Apache_Tomcat_Cookbook.pdf";
        String url = StrUtil.format("http://{}:{}{}", ip,port,uri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpUtil.download(url, baos, true);
        int pdfFileLength = 3754713;
        Assert.assertEquals(pdfFileLength, baos.toByteArray().length);
    }

    @Test
    public void testhello() {
        String html = getContentString("/j2ee/hello");
        Assert.assertEquals(html,"Hello happycat from HelloServlet");
    }

    @Test
    public void testJavawebHello() {
        String html = getContentString("/javaweb/hello");
        containAssert(html,"Hello happycat from HelloServlet@javaweb");
    }

    @Test
    public void testJavawebHelloSingleton() {
        String html1 = getContentString("/javaweb/hello");
        String html2 = getContentString("/javaweb/hello");
        Assert.assertEquals(html1,html2);
    }
    @Test
    public void testgetParam() {
        String uri = "/javaweb/param";
        String url = StrUtil.format("http://{}:{}{}", ip,port,uri);
        Map<String,Object> params = new HashMap<>();
        params.put("name","LeWang");
        String html = HappyBrowser.getContentString(url, params, true);
        Assert.assertEquals(html,"get name:LeWang");
    }
    @Test
    public void testpostParam() {
        String uri = "/javaweb/param";
        String url = StrUtil.format("http://{}:{}{}", ip,port,uri);
        Map<String,Object> params = new HashMap<>();
        params.put("name","LeWang");
        String html = HappyBrowser.getContentString(url, params, false);
        Assert.assertEquals(html,"post name:LeWang");
    }
    @Test
    public void testheader() {
        String html = getContentString("/javaweb/header");
        Assert.assertEquals(html,"happy browser / java1.8");
    }
    @Test
    public void testsetCookie() {
        String html = getHttpString("/javaweb/setCookie");
        containAssert(html,"Set-Cookie: name=LeWang(cookie); Expires=");
    }

    @Test
    public void testgetCookie() throws IOException {
        String url = StrUtil.format("http://{}:{}{}", ip,port,"/javaweb/getCookie");
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestProperty("Cookie","name=LeWang(cookie)");
        conn.connect();
        InputStream is = conn.getInputStream();
        String html = IoUtil.read(is, "utf-8");
        containAssert(html,"name:LeWang(cookie)");
    }
    @Test
    public void testSession() throws IOException {
        String jsessionid = getContentString("/javaweb/setSession");
        if(null!=jsessionid)
            jsessionid = jsessionid.trim();
        String url = StrUtil.format("http://{}:{}{}", ip,port,"/javaweb/getSession");
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestProperty("Cookie","JSESSIONID="+jsessionid);
        conn.connect();
        InputStream is = conn.getInputStream();
        String html = IoUtil.read(is, "utf-8");
        containAssert(html,"LeWang(session)");
    }

    @Test
    public void testGzip() {
        byte[] gzipContent = getContentBytes("/",true);
        byte[] unGzipContent = ZipUtil.unGzip(gzipContent);
        String html = new String(unGzipContent);
        Assert.assertEquals(html, "Hello happycat from LeWang");
    }

    @Test
    public void testJsp() {
        String html = getContentString("/javaweb/");
        Assert.assertEquals(html, "hello jsp@javaweb");
    }

    @Test
    public void testClientJump(){
        String http_servlet = getHttpString("/javaweb/jump1");
        containAssert(http_servlet,"HTTP/1.1 302 Found");

        String http_jsp = getHttpString("/javaweb/jump1.jsp");
        containAssert(http_jsp,"HTTP/1.1 302 Found");
    }

    @Test
    public void testServerJump(){
        String http_servlet = getHttpString("/javaweb/jump2");
        containAssert(http_servlet,"Hello happycat from HelloServlet@javaweb");
    }
    @Test
    public void testJavawebLeWangHelloStatic() {
        String html = getContentString("/javawebLeWang/hello");
        containAssert(html,"Hello happycat from HelloServlet@javaweb");
    }
    @Test
    public void testJavawebLeWangHelloHot() {
        String html = getContentString("/javawebLeWangCopy/hello");
        containAssert(html,"Hello happycat from HelloServlet@javaweb");
    }
    private byte[] getContentBytes(String uri) {
        return getContentBytes(uri,false);
    }
    private byte[] getContentBytes(String uri,boolean gzip) {
        String url = StrUtil.format("http://{}:{}{}", ip,port,uri);
        return HappyBrowser.getContentBytes(url,gzip);
    }
    private String getContentString(String uri) {
        String url = StrUtil.format("http://{}:{}{}", ip,port,uri);
        String content = HappyBrowser.getContentString(url);
        return content;
    }
    private String getHttpString(String uri) {
        String url = StrUtil.format("http://{}:{}{}", ip,port,uri);
        String http = HappyBrowser.getHttpString(url);
        return http;
    }
    private void containAssert(String html, String string) {
        boolean match = StrUtil.containsAny(html, string);
        Assert.assertTrue(match);
    }
}