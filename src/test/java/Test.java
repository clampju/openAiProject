import com.openai.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> xx = new ArrayList<>();
        xx.add("画");
        xx.add("找");
        xx.add("生成...图");
        System.out.println(CommonUtil.mateImageCreatePrefix("sdfsdfdf",xx));
    }
}
