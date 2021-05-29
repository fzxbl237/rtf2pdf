import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class test {
    @Test
    public void test() throws Exception {
        String path="C:\\Users\\User\\Desktop\\test1";
        List<String> list = RTFUtil.getRTFFilesFromPath(path);

        long start = System.currentTimeMillis();
        Map<String,String> map=new HashMap<>();
        for (String s : list) {
            PDFUtil.word2pdf(path+"\\"+s,path+"\\"+s.replace(".rtf",".pdf"));
            PDFUtil.getPDFTitle(s.replace(".rtf",".pdf"),path,map);
            PdfOutline outline = null;
            PdfDocument pdfDocument=new PdfDocument(new PdfWriter(path+"\\"+s.replace(".rtf",".pdf")));
            PDFUtil.createOutline(outline,pdfDocument,"hahahha","表");
        }
        long end= System.currentTimeMillis();
        System.out.println("总共："+(end-start)+"ms");
        System.out.println(map);
    }

    }


