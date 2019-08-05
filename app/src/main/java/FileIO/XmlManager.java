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

import PaintKit.AbstractPen;
import PaintKit.DrawInfo;
import PaintKit.PaintBoard;
import PaintKit.Pen;
import PaintKit.Point;


public class XmlManager {
    private Context context;

    public XmlManager(Context context){
        this.context = context;
    }


    /**
     * 保存为将List<PaintBoard>保存为xml文件 格式如下：
     * <PaintBoards>
     *         <PaintBoard>
     *             <DrawInfo>
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
     *             </DrawInfo>
     *             .
     *             .
     *             .
     *         </PaintBoard>
     *         .
     *         .
     *         .
     *     </PaintBoards>
     * @param mPaintBoardList 把接收到的list对象序列化
     */
    public void savaToXMl(List<PaintBoard> mPaintBoardList){
        Log.i("==>","开始序列化");
        //创建负责序列化的对象
        XmlSerializer serializer =  Xml.newSerializer();
        File path = new File(Environment.getExternalStorageDirectory().getPath());
        File file = new File(path + "/" + "test.xml");
        FileOutputStream os;
        try {
            // 判断sd卡是否存在
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                // 检查文件是否存在，存在则删除再创建
                if (file.exists()){
                    file.delete();
                }
                file.createNewFile();
                os = new FileOutputStream(file);
                //指定xml文件保存的路径
                //OutputStream os = context.openFileOutput("test.xml", Context.MODE_PRIVATE);
                // 设置指定目录
                serializer.setOutput(os, "UTF-8");

                // 开始写入Xml格式数据
                // 开始文档
                // 参数一：指定编码格式   参数二：是不是独立的xml(这个xml与其他xml是否有关联)
                serializer.startDocument("UTF-8", true);

                // 开始根标签
                // 参数一：命名空间   参数二：标签名称
                serializer.startTag(null, "PaintBoards");
                for(PaintBoard paintBoard: mPaintBoardList){
                    //开始PaintBoard标签
                    serializer.startTag(null,"PaintBoard");

                    for(DrawInfo drawInfo:paintBoard.getmDrawList()){
                        //开始DrawInfo标签(由pen type points组成)
                        serializer.startTag(null,"DrawInfo");
                        //开始pen标签（由size color组成）
                        serializer.startTag(null,"Pen");
                        //设置size
                        serializer.startTag(null,"Size");
                        serializer.text(drawInfo.getmPen().getmSize()+"");
                        serializer.endTag(null,"Size");


                        //设置color
                        serializer.startTag(null,"Color");
                        serializer.text(drawInfo.getmPen().getmColor()+"");
                        serializer.endTag(null,"Color");

                        //结束pen标签
                        serializer.endTag(null,"Pen");

                        //设置type
                        serializer.startTag(null,"Type");
                        serializer.text(drawInfo.getType());
                        serializer.endTag(null,"Type");

                        //开始Points标签
                        serializer.startTag(null,"Points");
                        for(Point point: drawInfo.getmPointList()){
                            //开始Point标签（由x y组成）
                            serializer.startTag(null,"Point");
                            //设置x
                            serializer.startTag(null,"X");
                            serializer.text(point.x+"");
                            serializer.endTag(null,"X");
                            //设置y
                            serializer.startTag(null,"Y");
                            serializer.text(point.y+"");
                            serializer.endTag(null,"Y");
                            //结束Point标签
                            serializer.endTag(null,"Point");
                        }
                        //结束Points标签
                        serializer.endTag(null,"Points");
                        //结束DrawInfo标签
                        serializer.endTag(null,"DrawInfo");
                    }
                    //结束PaintBoard标签
                    serializer.endTag(null, "PaintBoard");
                }
                // 结束根标签
                serializer.endTag(null, "PaintBoards");

                //结束文档
                serializer.endDocument();

                os.close();

                Log.i("==>","序列化成功");
                Toast.makeText(context,"保存XML成功",Toast.LENGTH_SHORT).show();
            }else{
                Log.i("==>","不存在sd卡");
            }

        } catch (Exception e) {
            Log.i("==>","序列化失败");
            e.printStackTrace();
        }

    }

    public List<PaintBoard> pullXmlToPaintBoardList(String path){
        Log.i("MainActivity", " 开始反序列化");
        XmlPullParser xmlPullParser = Xml.newPullParser();
        List<PaintBoard> paintBoardList = new ArrayList<>();
        PaintBoard paintBoard = null;
        DrawInfo drawInfo = null;
        AbstractPen pen = null;
        Point point = null;
        try {
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file);
            xmlPullParser.setInput(fileInputStream,"UTF-8");

            int type = xmlPullParser.getEventType();
            while (type != XmlPullParser.END_DOCUMENT) {
                switch (type){
                    case XmlPullParser.START_TAG:
                        if ("PaintBoard".equals(xmlPullParser.getName())) {
                            paintBoard = new PaintBoard();
                        }else if ("DrawInfo".equals(xmlPullParser.getName())) {
                            drawInfo = new DrawInfo();
                        }else if ("Pen".equals(xmlPullParser.getName())) {
                            pen = new Pen();
                        }else if ("Size".equals(xmlPullParser.getName())) {
                            pen.setmSize(Integer.parseInt(xmlPullParser.getText()));
                        }else if ("Color".equals(xmlPullParser.getName())) {
                            pen.setmColor(Integer.parseInt(xmlPullParser.getText()));
                            drawInfo.setmPen(pen);
                        }else if ("Type".equals(xmlPullParser.getName())) {
                            drawInfo.setType(xmlPullParser.getText());
                        }else if ("Point".equals(xmlPullParser.getName())) {
                            point = new Point();
                        }else if ("X".equals(xmlPullParser.getName())) {
                            point.x = Float.parseFloat(xmlPullParser.getText());
                        }else if ("Y".equals(xmlPullParser.getName())) {
                            point.y = Float.parseFloat(xmlPullParser.getText());
                        }
                        break;

                    case XmlPullParser.END_TAG:
                    break;
                }
                type = xmlPullParser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.i("MainActivity","反序列失败");
        }
        return paintBoardList;
    }

}
