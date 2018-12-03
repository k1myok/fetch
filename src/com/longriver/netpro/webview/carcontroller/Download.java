package com.longriver.netpro.webview.carcontroller;  
  
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;  
import java.io.File;
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.net.HttpURLConnection;  
import java.net.URL;  

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
  
public class Download  
{  
    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException  
    {  
        URL url = new URL(urlStr);  
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
  
        conn.setConnectTimeout(3000);  
  
        conn.setRequestProperty("User-Agent",  
                "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
  
        InputStream inputStream = conn.getInputStream();  
  
        byte[] getData = readInputStream(inputStream);  
  
        java.io.File saveDir = new java.io.File(savePath);  
        if (!saveDir.exists()) {  
            saveDir.mkdir();  
        }  
        java.io.File file = new java.io.File(saveDir + java.io.File.separator + fileName);  
        FileOutputStream fos = new FileOutputStream(file);  
        fos.write(getData);  
        if (fos != null) {  
            fos.close();  
        }  
        if (inputStream != null) {  
            inputStream.close();  
        }  
    }  
  
    public static byte[] readInputStream(InputStream inputStream)  
            throws IOException  
    {  
        byte[] buffer = new byte[1024 * 1024];  
        int len = 0;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        while ((len = inputStream.read(buffer)) != -1) {  
            bos.write(buffer, 0, len);  
        }  
        bos.close();  
        return bos.toByteArray();  
    }  
    
    @Test
    public void getParam(){
    	
    	try {
//			downLoadFromUrl("http://118.190.172.70/network/pages/images/pic/222.mp4","1.mp4","d://pic");
//    		downloadVideo("http://118.190.172.70/network/pages/images/pic/222.mp4","222.mp4");
			getDondow("http://118.190.172.70/network/pages/images/pic/222.mp4","d://pic//1.mp4");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    static CloseableHttpClient client = HttpClients.createDefault(); 
    public static void downloadVideo(String downloadUrl, String fileName) {  
        try {  
            System.out.println("strart download video:" + fileName);  
            HttpGet httpGet = new HttpGet(downloadUrl);  
            HttpResponse httpResponse1 = client.execute(httpGet);  
  
            InputStream in = httpResponse1.getEntity().getContent();  
            byte[] buffer = new byte[1024 * 1024];  
            int n = -1;  
            // byte[] result =  
            // EntityUtils.toByteArray(httpResponse1.getEntity());  
            BufferedOutputStream bw = null;  
            File f = new File("d://pic//" + fileName);  
            if (!f.getParentFile().exists())  
                f.getParentFile().mkdirs();  
            bw = new BufferedOutputStream(new FileOutputStream(f));  
            while ((n = in.read(buffer)) != -1) {  
                bw.write(buffer, 0, n);  
            }  
  
            bw.close();  
            System.out.println("finished!");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    private static void getDondow(String url,String pathName)throws Exception{
    	URL ul = new URL(url);
    	HttpURLConnection conn = (HttpURLConnection) ul.openConnection();
    	conn.setReadTimeout(5*1000);
    	conn.setConnectTimeout(5*1000);
    	BufferedInputStream bi = new BufferedInputStream(conn.getInputStream());
    	FileOutputStream bs = new FileOutputStream(pathName);
    	System.out.println("文件大约："+(conn.getContentLength()/1024)+"K");
    	byte[] by = new byte[1024];
    	int len = 0;
    	while((len=bi.read(by))!=-1){
    	bs.write(by,0,len);
    	}
    	bs.close();
    	bi.close();
    	}
}  