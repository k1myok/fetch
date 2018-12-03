package com.longriver.netpro.webview;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.longriver.netpro.webview.vcode.RuoKuai;

public class CodeImgUtils {

	public static String getCode(WebDriver driver,WebElement imgcode,String imgname,String typeid){
		String code = "";
		try{
		//让整个页面截图
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		//获取页面上元素的位置
		Point point = imgcode.getLocation();
		
		int width = imgcode.getSize().getWidth();
		int height = imgcode.getSize().getHeight();
		
		//裁剪整个页面的截图，以获得元素的屏幕截图
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		//将元素截图复制到磁盘
		File file = new File("C:\\"+imgname);
		FileUtils.copyFile(screenshotAs, file);
		
		code = RuoKuai.createByPostNew(typeid, "C:\\"+imgname);
		
		}catch(Exception e){
		
			e.printStackTrace();
		}
		return code;
	}
	
}
