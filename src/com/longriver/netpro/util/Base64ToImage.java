package com.longriver.netpro.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class Base64ToImage {
	
	public static void main(String[] args){
		String path = "R0lGODdheAAeAIUAAP////r6/+np/+jo/+Dg/9/f/9DQ/8nJ/8PD/6am/5+f/4mJ/4iI/3d3/3Z2/3Jy/2xs/2Zm/1pa/1lZ/0xM/0pK/0lJ/0VF/0ND/0BA/zo6/y8v/yws/ycn/yYm/yEh/xQU/wwM/woK/wgI/wQE/wIC/wAA/wAAxwAAwwAAnAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACwAAAAAeAAeAEAI/wABCBxIsKBBgyoAKFzIEIAKABAjSpxIsaLFixgzapSoAoDHjyBDigSgAoDJkyhTqlQBoCUAFSoAyJxJE4AKFQBy6tzJs6fPn0CDChVqwgQAE0hNKADAVAWApwBMSJUKoKpVqyaymgDA1YTXDyZUABibwgQAEwDSql3Ltq3bt3DjwlUBoC4AEwBMmJAAAICJCCYcABhMuLAJEwASK16c2IRjxwAiRzZBmTKAy5gzXzZhAoDnzyZMABhN2oQJAKhVAFjNurXr165VAJhNu7bt27hz697N+7YKAMCDCwegAoDx48iTK1/OXLkKACYAmJhOPQCA69izAzDBHYB3ACoAAP8wQR6AeQAcAJgwAaC9exPwAcifT7++/fv48+u3rwKAf4AABA4kONCECQAJFS40YQLAQ4gRAZigaAKACRMANJowAQCABhMmAIwkWRKACZQAVAIw0dIEAJgATMwEUNPmTZw5de7kCUCFCgBBhaoAUNQoABNJASxl2pSpCagApE4FYMIqAKxZs6oA0NUECRMAxIo1URaACgBpTawF0NbtW7hx5c6lW5euCgB59QJQAcDvX8CBBQ8mXNjwYcSJFS9m/FcFAMiRVQCgXBmACgCZNWtWAcDzZ9ChRYtWAUAFANSpVa9mnVoFANixZc+mXdv2bdy5VagA0Nv3b+DBhQ/vbcL/BADkyZUvB2DCBADo0aWboA7AOgAT2bMD4G6CggkTAMSPJ1/e/Hn06dWvL2/C/Xv3AOTPl2/C/n0TAPTv32/CBEAAAk2YAADABEIAG0yYOAHgIcSIJiYCAGDiYgMAAEwAMGECgAkAIkeSLGnyJMqUJk2wBODy5UsTMgHQrGmTpgkTAUyYsAAAgAkAJgCYKArgKNKkAEwwBeDUqYmoAKZONWECANasWreqUAHgK9iwYseCRQDABNq0aAGwbesWgAkTAOaqAGAXgAoAevWaMAHABIDAgU0QHmACAOLEKlQAaGziMYDIkU1QBmBZBQATmgFw7uz5M+jQokOrAGD6NGoA/yoAsG7t+jXs2LJn065t27UKALp38+7t+zfw4MKHEy9u/Djy5MdVAGju/Dn06M9VAKhuXQWA7NpVAOju3bsKAOLHky9fXgWA9OrXs2/v/j38+PLn03+vAgD+/Pr1q1ABACAAgQMJFjR4EGFChQsZClQBAGJEiRMpQlQBAGNGjRs5ZlQBAGRIkSJNlDRZEkBKlSpNADDx0gQAmSoAADBhAkABADt59vT5E2hQoTxVqABwVAUApUuZNnX6VKkKAFNNVLVaFUBWrVlVmPD6FUDYsCbIVgBwFoAJtSYAtAVgAq4JAHPp1rV7F29evXvpqlABAHBgwQBMmABwGHFixQBMmP8A8Bhy5AQmTACwDMDEAQYETADwDMBEaACjSZc2fRqACgCrWbderQJAbNmzade2bduECQAlTJgQAQA4ABUqABQ3cRy5CQDLmQN4AMCECQAATAAwAQCACQAmAAAw8R1AePHjTZgAYAJ9egAATLRvDwB+fPnz6de3f9+ECQD7AZjwDxCAwIEETZgAgDChQgAmTBgwAcCECQAUTZgAYAKAiRMAOnr8aCKkyJEkRQI4iTKlypUsW7I0ASCmTBM0Adi8edOECQA8e/oEYAKACRMATHQwAQCAiaUATJgAYAKA1KlUTZgAgDWrCRMAugIwAdYEgLFky5o9izatWRNsQwB4C8D/hFwAdOvWNYEXgN69fPWaMAEAgAkTAAovMAHAhGIAjBs7BmAiMoDJk01YBoAZs4nNADqrAAA6tOjRAFQAOI06terVJkwAeG0CgAkTAGrbvm3CBIDdvHvvNjHBhHAAxImbOG4CggkAzJurAAAdgInpAKpXN4EdgHbtJroD+A4+vPjx5MuPH4HChHoTANqrUAEgvnwAJkwAuI//vgoA/AGYAGhCoAkABQuaQIgQwEKGDReaMAFA4kQTJgAAUAFAowkTADx+BBkSpAoAJU2eRJnypAoALVuqABBT5kyaNW3aBOFBgAAAPX0CUKECAIALFzAAUKECgIoMGTAAUKECAAAMCVUBXMWaNauKgAA7";
		System.out.println(ToImage(path,"C:\\vcode\\toutiao.gif"));
	}
	//base64字符串转化成图片  
    public static boolean ToImage(String imgStr,String lpath)  
    {   //对字节数组字符串进行Base64解码并生成图片  
        if (imgStr == null) //图像数据为空  
            return false;  
        BASE64Decoder decoder = new BASE64Decoder();  
        try   
        {  
            //Base64解码  
            byte[] b = decoder.decodeBuffer(imgStr);  
            for(int i=0;i<b.length;++i)  
            {  
                if(b[i]<0)  
                {//调整异常数据  
                    b[i]+=256;  
                }  
            }  
            //生成jpeg图片  
            String imgFilePath = lpath;//新生成的图片  
            OutputStream out = new FileOutputStream(imgFilePath);      
            out.write(b);  
            out.flush();  
            out.close();  
            return true;  
        }   
        catch (Exception e)   
        {  
            return false;  
        }  
    }  
  //图片转化成base64字符串  
    public static String GetImageStr()  
    {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理  


        String imgFile = "c:\\360CloudUI\\tupian\\jt.jpg";//待处理的图片  

        InputStream in = null;  
        byte[] data = null;  
        //读取图片字节数组  
        try   
        {  
            in = new FileInputStream(imgFile);          
            data = new byte[in.available()];  
            in.read(data);  
            in.close();  
        }   
        catch (IOException e)   
        {  
            e.printStackTrace();  
        }  
        //对字节数组Base64编码  
        BASE64Encoder encoder = new BASE64Encoder();  
        return encoder.encode(data);//返回Base64编码过的字节数组字符串  
    }  
	
}
