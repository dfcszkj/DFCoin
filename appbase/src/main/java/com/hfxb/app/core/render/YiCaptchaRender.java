package com.hfxb.app.core.render;

import com.hfxb.app.core.common.Constants;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.render.Render;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class YiCaptchaRender extends Render {
	
	private final int WIDTH = 80, HEIGHT = 30, FONT_SIZE = 20;
	private final String[] strArr = {"3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y"};
	
	public YiCaptchaRender(){}
	
	public void render() {
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		String vCode = drawGraphic(image);
		
		//将验证码放入session中
		request.getSession().setAttribute(Constants.VERIFY_CODE, vCode);
		
		response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        
        ServletOutputStream sos = null;
        try {
			sos = response.getOutputStream();
			ImageIO.write(image, "jpeg",sos);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			if (sos != null)
				try {sos.close();} catch (IOException e) {e.printStackTrace();}
		}
	}

	private String drawGraphic(BufferedImage image){
		// 获取图形上下文
		Graphics g = image.createGraphics();
		// 设定背景色
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		// 设定字体
		g.setFont(new Font("Times New Roman", Font.PLAIN, FONT_SIZE));

		// 随机产生255条干扰线，使图象中的认证码不易被其它程序探测到
		// 生成随机类
		Random random = new Random();
		g.setColor(getRandColor(200, 250));
		for (int i = 0; i < 255; i++) {
			int x = random.nextInt(WIDTH);
			int y = random.nextInt(HEIGHT);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		// 取随机产生的认证码
		String sRand = "";
		for (int i = 0; i < Constants.config.getInt("captcha.length", 4); i++) {
			String rand = String.valueOf(strArr[random.nextInt(strArr.length)]);
			sRand += rand;
			// 将认证码显示到图象中
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			g.drawString(rand, 13 * i + 6, 16);
		}

		// 图象生效
		g.dispose();
		
		return sRand;
	}
	
	/*
	 * 给定范围获得随机颜色
	 */
	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 校验
	 * @param controller
	 * @param inputRandomCode   前端传过来的验证码（明文）
	 * @return
	 */
	public  boolean validate(Controller controller, String inputRandomCode) {
		if (StrKit.isBlank(inputRandomCode))
			return false;
		try {
			return inputRandomCode.equals((String)controller.getSessionAttr(Constants.VERIFY_CODE));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}


