package com.snapdeal.controller;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/** This controller is used to generate barcode image to be printed on paper. It takes code as path variable 
 * and generates it barcode image. **/

@Controller
@RequestMapping("/Barcode")
public class BarcodeController {

	public static final Logger LOGGER = Logger.getLogger(BarcodeController.class);

	/** Barcode generated using Barbecue API **/

	@RequestMapping("/generate/{id}")
	public void generateBarcode(@PathVariable("id") String code,HttpServletResponse response)
	{
		Barcode barcode = null;
		try {
			response.setContentType("text/jpg");
			barcode = BarcodeFactory.createCode128(code);
			Font font = new Font("Sans Serif", Font.BOLD, 10);
			barcode.setBarHeight(50);
			barcode.setFont(font);
			BufferedImage bufferedImage = BarcodeImageHandler.getImage(barcode);
			OutputStream out = response.getOutputStream();
			ImageIO.write(bufferedImage, "jpg", out);
			out.close();
		} catch (BarcodeException e) {
			LOGGER.error("Barcode Exception", e);
		} catch (OutputException e) {
			LOGGER.error("Output Exception", e);
		} catch (IOException e) {
			LOGGER.error("IO Exception", e);
		}
	}
}
