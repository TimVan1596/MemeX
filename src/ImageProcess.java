package src;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 *@author TimVan
 *ImageProcess 自用图像处理类
 */
public class ImageProcess {

    /**
     * scaleImg（）； 图像缩放方法,将图片缩放到适合控件的大小
     * ImageIcon image为显示的图片
     * int conWidth,int conHeight 分别是图片的长和宽
     */
    public static Map<String, Integer> scaleImage(ImageIcon image,double conWidth,double conHeight){

            Map<String, Integer> imgWidthAndHeight = new HashMap<String, Integer>();

            double imgWidth = image.getIconWidth();
            double imgHeight = image.getIconHeight();
//            System.out.println("imgWidth = "+imgWidth);
//            System.out.println("imgHeight = "+imgHeight);
        double reImgWidth;
        double reImgHeight;

        if(conWidth/imgWidth<=conHeight/imgHeight)
        {
            reImgWidth=imgWidth*(conWidth/imgWidth);
            reImgHeight=imgWidth*(conWidth/imgWidth);
        }else{
            reImgWidth=imgWidth*(conHeight/imgHeight);
            reImgHeight=imgWidth*(conHeight/imgHeight);
        }

//        if (imgWidth < conWidth && imgHeight < conHeight){
//
//        }

            imgWidthAndHeight.put("width",(int)reImgWidth);
            imgWidthAndHeight.put("height",(int)reImgHeight);

            return imgWidthAndHeight;
        }
}
