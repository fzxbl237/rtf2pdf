import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.Splitter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class PDFUtil {
    // 不保存待定的更改。
    private static final int wdDoNotSaveChanges = 0;
    // word转PDF 格式
    private static final int wdFormatPDF = 17;
    // 线程池队列
    private static BlockingQueue<Dispatch> dispatchPool = new LinkedBlockingQueue<>();
    // 线程池大小
    private static int MAX_DISPATCH_SIZE = 5;


    static {
        for (int i = 0; i < MAX_DISPATCH_SIZE; i++) {
            ActiveXComponent axc = new ActiveXComponent("Word.Application");
            axc.setProperty("Visible", false);
            Dispatch dispatch = axc.getProperty("Documents").toDispatch();
            dispatchPool.add(dispatch);
        }
    }

    /**
     * @param source word路径
     * @param target 生成的pdf路径
     * @return
     */
    public static boolean word2pdf(String source, String target) {

        //ComThread.InitMTA();
        long start = System.currentTimeMillis();
        Dispatch dispatch = null;
        try {
            dispatch = getDispatch();
            Dispatch doc = Dispatch.call(dispatch, "Open", source, false, true).toDispatch();
            File toFile = new File(target);
            if (toFile.exists()) {
                toFile.delete();
            }
            Dispatch.call(doc, "SaveAs", target, wdFormatPDF);
            Dispatch.call(doc, "Close", false);
            long end = System.currentTimeMillis();
            System.out.println(("转换完成，用时：" + (end - start) + "ms"));
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (dispatch != null) {
                relaseDispatch(dispatch);
            }
        }
    }
    /**
     * @param fileName pdf文件名,e.g. T1.pdf
     * @param prefix 生成的pdf路径
     * @param map 储存title信息的map集合
     * @return
     */
    public static void getPDFTitle(String fileName,String prefix, Map<String,String> map) throws IOException {
        PDDocument document = PDDocument.load(prefix+"\\"+fileName);
        Splitter splitter = new Splitter();
        splitter.setStartPage(1);
        splitter.setEndPage(1);
        List<PDDocument> split1 = splitter.split(document);

        PDFTextStripper s = new PDFTextStripper();
        String content = s.getText(split1.get(0));

        String[] split = content.split(System.lineSeparator()+"\s?"+System.lineSeparator());
        String s1=split[1].replace(System.lineSeparator(),"");
        map.put(fileName.split("\\.")[0],s1);
    }

    public static PdfOutline createOutline(
            PdfOutline outline, PdfDocument pdf, String title, String name) {
        if (outline ==  null) {
            outline = pdf.getOutlines(false);
            outline = outline.addOutline(title);
            outline.addDestination(
                    PdfDestination.makeDestination(new PdfString(name)));
            return outline;
        }
        PdfOutline kid = outline.addOutline(title);
        kid.addDestination(PdfDestination.makeDestination(new PdfString(name)));
        return outline;
    }


    @SneakyThrows
    public static Dispatch getDispatch() {
        return dispatchPool.take();
    }

    @SneakyThrows
    public static void relaseDispatch(Dispatch dispatch) {
        dispatchPool.add(dispatch);
    }
}