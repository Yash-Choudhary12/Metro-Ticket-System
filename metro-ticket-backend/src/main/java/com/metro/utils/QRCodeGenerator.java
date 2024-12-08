package com.metro.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeGenerator {

	public static void generateQRCode(String fileData, String filePath, int width, int height) throws Exception {
		// Set up encoding hints for error correction and character set
		Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L); // L: Low (7% error correction)
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		// Create the BitMatrix that holds the QR code data (fileData)
		BitMatrix bitMatrix = new MultiFormatWriter().encode(fileData, BarcodeFormat.QR_CODE, width, height, hints);

		// Convert the BitMatrix to an image
		BufferedImage image = toBufferedImage(bitMatrix);

		// Save the generated QR code as an image file
		ImageIO.write(image, "PNG", new File(filePath));

	}

	/*
	  Convert the BitMatrix to a BufferedImage.
	 */
	private static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Set the background to white and the QR code pattern to black
		image.createGraphics();
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);
		graphics.setColor(Color.BLACK);

		// Draw the QR code matrix
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					image.setRGB(x, y, Color.BLACK.getRGB());
				}
			}
		}
		return image;
	}
}
