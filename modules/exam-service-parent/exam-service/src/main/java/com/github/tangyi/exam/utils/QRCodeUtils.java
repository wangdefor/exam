package com.github.tangyi.exam.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;
import java.util.Random;

/**
 * 二维码工具类
 *
 * @author tangyi
 * @date 2019/3/22 15:24
 */
public class QRCodeUtils {

    private static final String CHARSET = "utf-8";

    private static final String FORMAT = "JPG";

    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;

    // LOGO宽度
    private static final int LOGO_WIDTH = 60;

    // LOGO高度
    private static final int LOGO_HEIGHT = 60;

    private static final String FORMAT_NAME = "JPG";

    // LOGO宽度
    private static final int WIDTH = 60;
    // LOGO高度
    private static final int HEIGHT = 60;

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param imgPath 图片路径
     */
    public static void encoderQRCode(String content, String imgPath) {
        QRCodeUtils.encoderQRCode(content, imgPath, "png", 7);
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param output  输出流
     */
    public static void encoderQRCode(String content, OutputStream output) {
        QRCodeUtils.encoderQRCode(content, output, "png", 7);
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param imgPath 图片路径
     * @param imgType 图片类型
     */
    public static void encoderQRCode(String content, String imgPath, String imgType) {
        QRCodeUtils.encoderQRCode(content, imgPath, imgType, 7);
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param output  输出流
     * @param imgType 图片类型
     */
    public static void encoderQRCode(String content, OutputStream output,
                                     String imgType) {
        QRCodeUtils.encoderQRCode(content, output, imgType, 7);
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param imgPath 图片路径
     * @param imgType 图片类型
     * @param size    二维码尺寸
     */
    public static void encoderQRCode(String content, String imgPath, String imgType,
                                     int size) {
        try {
            BufferedImage bufImg = QRCodeUtils.qRCodeCommon(content, size);
            File imgFile = new File(imgPath);
            // 生成二维码QRCode图片
            ImageIO.write(bufImg, imgType, imgFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param output  输出流
     * @param imgType 图片类型
     * @param size    二维码尺寸
     */
    public static void encoderQRCode(String content, OutputStream output,
                                     String imgType, int size) {
        try {
            BufferedImage bufImg = QRCodeUtils.qRCodeCommon(content, size);
            // 生成二维码QRCode图片
            ImageIO.write(bufImg, imgType, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content
     *            内容
     * @param imgPath
     *            LOGO地址
     * @param destPath
     *            存放目录
     * @param needCompress
     *            是否压缩LOGO
     * @throws Exception
     */
    public static String encodes(String content, String imgPath, String destPath,
                                boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtils.createImage(content, imgPath,
                needCompress);
        mkdirs(destPath);
        String file = new Random().nextInt(99999999)+".jpg";
        ImageIO.write(image, FORMAT_NAME, new File(destPath+"/"+file));
        return file;
    }


    /**
     * 生成二维码(QRCode)图片的公共方法
     *
     * @param content 存储内容
     * @param size    二维码尺寸
     * @return
     */
    private static BufferedImage qRCodeCommon(String content, int size) {
        BufferedImage image = null;
        try {
            image = createImage(content, null, Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 解析二维码（QRCode）
     *
     * @param imgPath 图片路径
     * @return
     */
    public static String decoderQRCode(String imgPath) {
        String content = null;
        try {
            InputStream stream = new FileInputStream(new File(imgPath));
            content = decoderQRCode(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 解析二维码（QRCode）
     *
     * @param input 输入流
     * @return
     */
    public static String decoderQRCode(InputStream input) {
        BufferedImage bufImg = null;
        String content = null;
        try {
            bufImg = ImageIO.read(input);

            if(bufImg == null){
                return null;
            }
            BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(
                    bufImg);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result;
            Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
            result = new MultiFormatReader().decode(bitmap, hints);
            content = result.getText();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 生成二维码（ZXing）
     *
     * @param content
     * @param logoPath
     * @param needCompress
     * @return
     * @throws Exception
     */
    private static BufferedImage createImage(String content, String logoPath, boolean needCompress) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE,
                hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (logoPath == null || "".equals(logoPath)) {
            return image;
        }
        // 插入图片
        QRCodeUtils.insertImage(image, logoPath, needCompress);
        return image;
    }

    /**
     * 插入LOGO （ZXing）
     *
     * @param source       二维码图片
     * @param logoPath     LOGO图片地址
     * @param needCompress 是否压缩
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String logoPath, boolean needCompress) throws Exception {
        File file = new File(logoPath);
        if (!file.exists()) {
            throw new Exception("logo file not found.");
        }
        Image src = ImageIO.read(new File(logoPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > LOGO_WIDTH) {
                width = LOGO_WIDTH;
            }
            if (height > LOGO_HEIGHT) {
                height = LOGO_HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 生成二维码(内嵌LOGO) （ZXing）
     * 二维码文件名随机，文件名可能会有重复
     *
     * @param content      内容
     * @param logoPath     LOGO地址
     * @param destPath     存放目录
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static String encode(String content, String logoPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtils.createImage(content, logoPath, needCompress);
        mkdirs(destPath);
        String fileName = new Random().nextInt(99999999) + "." + FORMAT.toLowerCase();
        ImageIO.write(image, FORMAT, new File(destPath + "/" + fileName));
        return fileName;
    }

    /**
     * 生成二维码(内嵌LOGO) （ZXing）
     * 调用者指定二维码文件名
     *
     * @param content      内容
     * @param logoPath     LOGO地址
     * @param destPath     存放目录
     * @param fileName     二维码文件名
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static String encode(String content, String logoPath, String destPath, String fileName, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtils.createImage(content, logoPath, needCompress);
        mkdirs(destPath);
        fileName = fileName.substring(0, fileName.indexOf(".") > 0 ? fileName.indexOf(".") : fileName.length())
                + "." + FORMAT.toLowerCase();
        ImageIO.write(image, FORMAT, new File(destPath + "/" + fileName));
        return fileName;
    }

    /**
     * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．
     * (mkdir如果父目录不存在则会抛出异常)
     *
     * @param destPath 存放目录
     */
    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    /**
     * 生成二维码(内嵌LOGO) （ZXing）
     *
     * @param content  内容
     * @param logoPath LOGO地址
     * @param destPath 存储地址
     * @throws Exception
     */
    public static String encode(String content, String logoPath, String destPath) throws Exception {
        return QRCodeUtils.encode(content, logoPath, destPath, false);
    }

    /**
     * 生成二维码 （ZXing）
     *
     * @param content      内容
     * @param destPath     存储地址
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static String encode(String content, String destPath, boolean needCompress) throws Exception {
        return QRCodeUtils.encode(content, null, destPath, needCompress);
    }

    /**
     * 生成二维码 （ZXing）
     *
     * @param content  内容
     * @param destPath 存储地址
     * @throws Exception
     */
    public static String encode(String content, String destPath) throws Exception {
        return QRCodeUtils.encode(content, null, destPath, false);
    }

    /**
     * 生成二维码(内嵌LOGO) （ZXing）
     *
     * @param content      内容
     * @param logoPath     LOGO地址
     * @param output       输出流
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String logoPath, OutputStream output, boolean needCompress)
            throws Exception {
        BufferedImage image = QRCodeUtils.createImage(content, logoPath, needCompress);
        ImageIO.write(image, FORMAT, output);
    }

    /**
     * 生成二维码 （ZXing）
     *
     * @param content 内容
     * @param output  输出流
     * @throws Exception
     */
    public static void encode(String content, OutputStream output) throws Exception {
        QRCodeUtils.encode(content, null, output, false);
    }

    /**
     * 解析二维码 （ZXing）
     *
     * @param file 二维码图片
     * @return
     * @throws Exception
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    /**
     * 解析二维码 （ZXing）
     *
     * @param path 二维码图片地址
     * @return
     * @throws Exception
     */
    public static String decode(String path) throws Exception {
        return QRCodeUtils.decode(new File(path));
    }

    public static void main(String[] args) {
        String imgPath = "C:\\Users\\xym\\Downloads\\二维码图片_5月7日10时43分36秒.png";
        String encoderContent = "yx73715248x1903081000221";
        QRCodeUtils.encoderQRCode(encoderContent, imgPath, "png");
        System.out.println("========encoder success");

        String decoderContent = QRCodeUtils.decoderQRCode(imgPath);
        System.out.println("解析结果如下：");
        System.out.println(decoderContent);
        System.out.println("========decoder success!!!");
    }
}
