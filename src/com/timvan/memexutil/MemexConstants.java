package src.com.timvan.memexutil;

/**
 *  将不同的功能的常量类进一步分类,在接口里定义静态内部类
 * @Author TimVan
 * @Date : 2018年06月05日01:03:57
 * */
public interface MemexConstants {

    /**发布版本中的信息
     */
     class RealeseInfos{

        public static final String SOFT_NAME_CN = "斗图神器";
        public static final String SOFT_NAME_EN = "MemeX";
        public static final String versionInfo = "v0.5";
         public static final String releaseDate =
                 "2018年6月11日16:54:12";
         public static final String authorName = "Tim Van";

    }

    /**状态栏中的固定信息
     */
    class StatusBarString{
        /**由于打开表情包商店很慢，会有状态的加载提示信息
         */
        public static final String WAIT_FOR_STORE_STRING
                = "◆ 商店加载需要一点时间，优化成这样你还想让我怎样";

        /**状态栏的初始化信息
         */
        public static final String INIT_STATUS_BAR_STRING
                = "◆ 点击中间表情包可切换本地图片，右键有惊喜";

        /**完成表情包生成之后的提示信息
         */
        public static final String FINISH_STATUS_BAR_STRING
                = "◆ 表情包已复制到剪切板，您可以直接粘贴；新图片保存在桌面";

    }

}
