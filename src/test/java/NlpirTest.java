import com.google.common.io.Resources;
import com.lingjoin.divideWords.NlpirMethod;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NlpirTest {
	Logger logger= Logger.getLogger(NlpirTest.class.getName());
    
	@Test
	/**
	 * 分词测试
	 */
    public void testParticiple() throws IOException {
    	NlpirMethod.Nlpir_init();

        String sSrc = IOUtils.toString(Resources.getResource("test.txt"));

    	logger.debug("文章内容为---->"+sSrc);
    	String data= NlpirMethod.NLPIR_ParagraphProcess(sSrc, 1);
    	logger.debug(data);
    }
	@Test
	public void testKeyWord() throws IOException {
		NlpirMethod.Nlpir_init();

        String sSrc = IOUtils.toString(Resources.getResource("word_seg_test.txt"));
    	logger.debug("文章内容为---->"+sSrc);
    	String data= NlpirMethod.NLPIR_GetKeyWords(sSrc, 10, false);
    	logger.debug(data);
	}
	@Test
	public void testNewWord() throws IOException {
		NlpirMethod.Nlpir_init();
        String sSrc = IOUtils.toString(Resources.getResource("word_seg_test.txt"));

        logger.debug("文章内容为---->"+sSrc);
    	String data= NlpirMethod.NLPIR_GetNewWords(sSrc, 10, false);
    	logger.debug(data);
	}
	@Test
	public void testAddUserWord(){
		NlpirMethod.Nlpir_init();
		int i= NlpirMethod.NLPIR_AddUserWord("国防");
		if(1==i){
			logger.debug("用户词添加成功");
		}else{
			logger.debug("用户词添加失败");
		}
	}
	@Test
	public void testImportUserDict(){
		
		NlpirMethod.Nlpir_init();
    	int addDictNum= NlpirMethod.NLPIR_ImportUserDict("userdictutf8.txt");
    	logger.debug("添加用户自定义词个数为："+addDictNum);

       }
}
