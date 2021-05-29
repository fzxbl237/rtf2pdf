import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RTFUtil {

    public static List<String> getRTFFilesFromPath(String path) throws Exception {
        File file = new File(path);
        File[] files = file.listFiles();
        List<String> list=new ArrayList<>();
        for (File file1 : files) {
            if(file1.isFile()){
                if(file1.getName().toUpperCase().endsWith(".RTF")){
                    list.add(file1.getName());
                }
            }
        }
        if (list.size()==0)
            throw new Exception("请检查当前路径下是否存在rtf文件");
        return list;
    }
}
