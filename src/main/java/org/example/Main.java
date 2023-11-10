package org.example;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.*;

public class Main {
    public static void main(String[] args) throws IOException {
        imageStamp();
    }

    public static void scaleImage() throws IOException {
        BufferedImage orgImg = ImageIO.read(new File("overlay.jpg"));

        double scale = 0.5;

        int newWidth = (int) (orgImg.getWidth() * scale);
        int newHeight = (int) (orgImg.getHeight());

        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, orgImg.getType());

        scaledImage.createGraphics().drawImage(orgImg, 0, 0, newWidth, newHeight, null);
        File output = new File("scaledImage.jpg");
        ImageIO.write(scaledImage, "jpg", output);

    }

    public static void overlayImage () throws IOException {
        BufferedImage overlay = ImageIO.read(new File("images.jpg"));
        BufferedImage image = ImageIO.read(new File("overlay.jpg"));
        // create the new image, canvas size is the max. of both image sizes
        int w = Math.max(image.getWidth(), overlay.getWidth());
        int h = Math.max(image.getHeight(), overlay.getHeight());
        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.drawImage(overlay, 500, 0, null);

        ImageIO.write(combined, "PNG", new File("combined.png"));
    }

    public static void stamp() throws IOException {
        PDDocument document = Loader.loadPDF(new File("260KB.pdf"));
        PDPage page = document.getPage(0);
        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
        PDType0Font font = PDType0Font.load(document, new File("THSarabunNew Bold.ttf"));
        contentStream.setFont(font, 25);
        contentStream.setNonStrokingColor(0, 0, 0);
        contentStream.beginText();
        contentStream.newLineAtOffset(300, 750);
        contentStream.showText("พระอภัยมณีเป่าปี่ฮี้ฮีอะปัดชะวอกกิ้ง");
        contentStream.endText();
        contentStream.close();
        document.save(new File("output_document.pdf"));
        document.close();
    }

    public static void imageStamp() throws IOException {
        PDDocument document = Loader.loadPDF(new File("260KB.pdf"));
        PDPage page = document.getPage(0);
        PDImageXObject image = PDImageXObject.createFromFile("images.jpg", document);
        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
        float x = 100;
        float y = 100;
        float scale = 0.5f;
        contentStream.drawImage(image, x, y, image.getWidth() * scale, image.getHeight() * scale);
        contentStream.close();
        document.save(new File("output_document.pdf"));
        document.close();
    }
}