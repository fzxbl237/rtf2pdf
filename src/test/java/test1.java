import org.apache.pdfbox.PDFReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.apache.pdfbox.util.Splitter;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test1 {

    @Test
    public void test() throws IOException {
            PDDocument document = PDDocument.load("C:\\Users\\User\\Desktop\\test1\\T14_1_2.pdf");
            Splitter splitter = new Splitter();
            splitter.setStartPage(1);
            splitter.setEndPage(1);
            List<PDDocument> split1 = splitter.split(document);

        PDFTextStripper s = new PDFTextStripper();
            String content = s.getText(split1.get(0));

        //String[] split = content.split(" "+System.lineSeparator());
        String[] split = content.split(System.lineSeparator()+"\s?"+System.lineSeparator());
        String s1=split[1].replace(System.lineSeparator(),"");
        System.out.println(s1);
        Pattern p= Pattern.compile("(\\d+\\.?)+");
        Matcher matcher = p.matcher(s1);
        String group="";
        if(matcher.find()) {
            group = matcher.group(0);
        }

        int[] array = Arrays.asList(group.split("\\.")).stream().mapToInt(Integer::parseInt).toArray();
        System.out.println(array[2]);


    }
}

