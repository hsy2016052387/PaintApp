package FileIO;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import PaintKit.DrawInfo;
import PaintKit.PaintBoard;
import PaintKit.Point;


public class XMLManager {
    private Context context;

    public XMLManager(Context context){
        this.context = context;
    }
    //保存为将List<PaintBoard>保存为xml文件 格式如下：
    /*
    <PaintBoards>
        <PaintBoard>
            <DrawInfo>
                <Pen>
                    <Size> size </>
                    <Color> color </>
                </>
                <Type> type</>
                <Points>
                    <Point>
                        <X> x </>
                        <Y> y </>
                    </>
                    .
                    .
                    .
                </>
            </DrawInfo>
            .
            .
            .
        </PaintBoard>
        .
        .
        .
    </PaintBoards>
    */
    public void savaToXMl(List<PaintBoard> mPaintBoardList){
        Log.i("==>","开始序列化");

        try {
            //创建负责序列化的对象
            XmlSerializer serializer =  Xml.newSerializer();
            //指定xml文件保存的路径
            OutputStream os = context.openFileOutput("test.xml", Context.MODE_PRIVATE);
            // 设置指定目录
            serializer.setOutput(  os, "UTF-8");

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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
