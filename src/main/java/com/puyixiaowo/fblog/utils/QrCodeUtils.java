package com.puyixiaowo.fblog.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.codec.binary.Base64;
import spark.utils.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Moses
 * @date 2017-08-09
 */
public class QrCodeUtils {

    private static final String ENCODING = "UTF-8";

    public static boolean base64ToImg(String base64, String filepath) {

        if (StringUtils.isBlank(base64)) {
            return false;
        }
        FileOutputStream out = null;


        base64 = replaceBlank(base64);
        try {
            out = new FileOutputStream(new File(
                    filepath));
            byte[] decoderBytes = Base64.decodeBase64(base64);
            out.write(decoderBytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;

    }

    /**
     * 替换所有回车、空白符
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 解析二维码中链接值
     *
     * @param base64
     * @return
     */
    public static String parseQrCode(String base64) {
        String content = "";
        if (StringUtils.isBlank(base64)) {
            return content;
        }

        base64 = replaceBlank(base64);
        try {

            byte[] decoderBytes = Base64.decodeBase64(base64);

            ByteArrayInputStream in = new ByteArrayInputStream(decoderBytes);
            BufferedImage image = ImageIO.read(in);


            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, ENCODING);
            MultiFormatReader formatReader = new MultiFormatReader();
            Result result = formatReader.decode(binaryBitmap, hints);

            //设置返回值
            content = result.getText();

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return content;

    }


    public static void main(String[] args) {
        System.out.println(createQrcode(300,300,"http://www.hupubao.win"));
    }

    /**
     * 创建二维码
     *
     * @param width
     * @param height
     * @param contents
     * @return
     */
    public static String createQrcode(int width,
                                      int height,
                                      String contents) {

        String format = "jpg";

        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        // 指定纠错等级,纠错级别（L 7%、M 15%、Q 25%、H 30%）
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 内容所使用字符集编码
        hints.put(EncodeHintType.CHARACTER_SET, ENCODING);
//      hints.put(EncodeHintType.MAX_SIZE, 350);//设置图片的最大值
//      hints.put(EncodeHintType.MIN_SIZE, 100);//设置图片的最小值
        hints.put(EncodeHintType.MARGIN, 1);//设置二维码边的空度，非负数

        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE,
                    width,
                    height,
                    hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ImageIO.write(bufferedImage, format, os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(os.toByteArray());
    }

}
