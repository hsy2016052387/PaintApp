package FileIO;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import PaintKit.AbstractDrawInfo;
import PaintKit.AbstractPen;
import PaintKit.CircleDrawInfo;
import PaintKit.DrawInfoSimpleFactory;
import PaintKit.ErasorDrawInfo;
import PaintKit.PaintBoard;
import PaintKit.Pen;
import PaintKit.PenDrawInfo;
import PaintKit.Point;
import PaintKit.RetangleDrawInfo;
import PaintKit.StraightLineDrawInfo;


public class XmlManager {
    private Context context;

    public XmlManager(Context context){
        this.context = context;
    }


    /**
     * 保存为将List<PaintBoard>保存为xml文件 格式如下：
     * <PaintBoards>
     *         <PaintBoard>
     *             <...DrawInfo>
     *                 <Pen>
     *                     <Size> size </>
     *                     <Color> color </>
     *                 </>
     *                 <Type> type </>
     *                 <Points>
     *                     <Point>
     *                         <X> x </>
     *                         <Y> y </>
     *                     </>
     *                     .
     *                     .
     *                     .
     *                 </>
     *             </...DrawInfo>
     *             .
     *             .
     *             .
     *         </PaintBoard>
     *         .
     *         .
     *         .
     *     </PaintBoards>
     * @param mPaintBoardList 把接收到的list对象序列化
     * @param fileName 要保存的文件名称
     */
    public void savaToXMl(List<PaintBoard> mPaintBoardList,String fileName){
        if (fileName == null || fileName.length() == 0) {
            Toast.makeText(context,"文件名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("==>","开始序列化");
        // 创建负责序列化的对象
        XmlSerializer serializer =  Xml.newSerializer();
        File path = new File(Environment.getExternalStorageDirectory().getPath());
        File file = new File(path + "/" + fileName + ".xml");
        FileOutputStream os;
        try {
            // 判断sd卡是否存在
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                // 检查文件是否存在，存在则停止
                if (file.exists()) {
                    Toast.makeText(context,"文件已存在，请重新命名",Toast.LENGTH_SHORT).show();
                    return;
                }
                file.createNewFile();
                os = new FileOutputStream(file);
                // 指定xml文件保存的路径
                // OutputStream os = context.openFileOutput("test.xml", Context.MODE_PRIVATE);
                // 设置指定目录
                serializer.setOutput(os, "UTF-8");

                // 开始写入Xml格式数据
                // 开始文档
                // 参数一：指定编码格式   参数二：是不是独立的xml(这个xml与其他xml是否有关联)
                serializer.startDocument("UTF-8", true);

                // 开始根标签
                // 参数一：命名空间   参数二：标签名称
                serializer.startTag(null, "PaintBoards");
                for (PaintBoard paintBoard: mPaintBoardList) {
                    // 开始PaintBoard标签
                    serializer.startTag(null,"PaintBoard");

                    for (AbstractDrawInfo abstractDrawInfo:paintBoard.getmDrawList()) {
                        // 开始DrawInfo标签(由pen type points组成)
                        serializer.startTag(null,abstractDrawInfo.getType());
                        // 开始pen标签（由size color组成）
                        serializer.startTag(null,"Pen");
                        // 设置size
                        serializer.startTag(null,"Size");
                        serializer.text(abstractDrawInfo.getmPen().getmSize() + "");
                        serializer.endTag(null,"Size");


                        // 设置color
                        serializer.startTag(null,"Color");
                        serializer.text(abstractDrawInfo.getmPen().getmColor() + "");
                        serializer.endTag(null,"Color");

                        // 结束pen标签
                        serializer.endTag(null,"Pen");

                        // 设置type
                        serializer.startTag(null,"Type");
                        serializer.text(abstractDrawInfo.getType());
                        serializer.endTag(null,"Type");

                        // 开始Points标签
                        serializer.startTag(null,"Points");
                        for (Point point: abstractDrawInfo.getmPointList()) {
                            // 开始Point标签（由x y组成）
                            serializer.startTag(null,"Point");
                            // 设置x
                            serializer.startTag(null,"X");
                            serializer.text(point.x+"");
                            serializer.endTag(null,"X");
                            // 设置y
                            serializer.startTag(null,"Y");
                            serializer.text(point.y+"");
                            serializer.endTag(null,"Y");
                            // 结束Point标签
                            serializer.endTag(null,"Point");
                        }
                        // 结束Points标签
                        serializer.endTag(null,"Points");
                        // 结束DrawInfo标签
                        serializer.endTag(null,abstractDrawInfo.getType());
                    }
                    // 结束PaintBoard标签
                    serializer.endTag(null, "PaintBoard");
                }
                // 结束根标签
                serializer.endTag(null, "PaintBoards");

                // 结束文档
                serializer.endDocument();

                os.close();

                Log.i("==>","序列化成功");
                Toast.makeText(context,"保存XML成功",Toast.LENGTH_SHORT).show();
            }else {
                Log.i("==>","不存在sd卡");
            }

        } catch (Exception e) {
            Log.i("==>","序列化失败");
            e.printStackTrace();
        }

    }

    /**
     * 反序列化
     * @param path xml文件的路径
     * @return 返回反序列化后生成的List<PaintBoard>
     */
    public List<PaintBoard> pullXmlToPaintBoardList(String path) {
        Log.i("MainActivity", " 开始反序列化");
        XmlPullParser xmlPullParser = Xml.newPullParser();
        List<PaintBoard> paintBoardList = new ArrayList<>();
        List<Point> pointList = null;
        PaintBoard paintBoard = null;
        AbstractDrawInfo abstractDrawInfo = null;
        AbstractPen pen = null;
        Point point = null;
        try {
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file);
            xmlPullParser.setInput(fileInputStream,"UTF-8");

            int type = xmlPullParser.getEventType();
            while (type != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        switch (xmlPullParser.getName()) {
                            case "PaintBoard":
                                paintBoard = new PaintBoard();
                                break;
                            case "Pen":
                                pen = new Pen();
                                break;
                            case "Size":
                                pen.setmSize(Integer.parseInt(xmlPullParser.nextText()));
                                break;
                            case "Color":
                                pen.setmColor(Integer.parseInt(xmlPullParser.nextText()));
                                break;
                            case "Type":
                                abstractDrawInfo = DrawInfoSimpleFactory.createConcreDrawInfo(xmlPullParser.nextText());
                                abstractDrawInfo.setmPen(pen);
                                break;
                            case "Points":
                                pointList = new ArrayList<>();
                                abstractDrawInfo.setmPointList(pointList);
                                break;
                            case "Point":
                                point = new Point();
                                break;
                            case "X":
                                point.x = Float.parseFloat(xmlPullParser.nextText());
                                break;
                            case "Y":
                                point.y = Float.parseFloat(xmlPullParser.nextText());
                                break;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if("Point".equals(xmlPullParser.getName())) {
                            pointList.add(point);
                        }else if ((xmlPullParser.getName()).contains("DrawInfo")) {
                            paintBoard.getmDrawList().add(abstractDrawInfo);
                        }else if ("PaintBoard".equals(xmlPullParser.getName())) {
                            paintBoardList.add(paintBoard);
                        }
                    break;
                }
                type = xmlPullParser.next();
            }
            Log.i("MainActivity","反序列化成功");
        }catch (Exception e) {
            e.printStackTrace();
            Log.i("MainActivity","反序列失败");
            Toast.makeText(context,"请选择合适的xml文件",Toast.LENGTH_SHORT).show();
        }
        return paintBoardList;
    }
}
