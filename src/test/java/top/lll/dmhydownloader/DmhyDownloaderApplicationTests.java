package top.lll.dmhydownloader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.lll.entity.AnimeInfo;
import top.lll.utils.R;

import java.io.*;
import java.util.List;

@SpringBootTest
class DmhyDownloaderApplicationTests {

    @Test
    void contextLoads() {
            try {
                String scrapyPath = "D:\\anaconda3\\envs\\spider\\Scripts\\scrapy.exe";
                String spiderPath = "F:\\dev_codes\\project\\web_spider\\web_spider\\spiders\\dmhy_spider.py";
                String outputPath = "F:\\dev_codes\\project\\web_spider\\dmhy.json";
                String keywordParam = "keyword=" + "大室家";
                String sortIdParam = "sort_id=" + "2";
                String teamIdParam = "team_id=" + "283";
                String orderParam = "order=" + "date-asc";

//                List<String> command = Arrays.asList(scrapyPath, "runspider", spiderPath, "-O", outputPath, "-a", keywordParam, "-a", sortIdParam, "-a", teamIdParam, "-a", orderParam);
//                System.out.println(command);
//                ProcessBuilder processBuilder = new ProcessBuilder(command);
//                processBuilder.directory(new File("F:\\dev_codes\\project\\web_spider"));
//                System.out.println(processBuilder.command());
//                Process process = processBuilder.start();
//
//                streamHandler(process.getInputStream(), false);
//                streamHandler(process.getErrorStream(), true);
//
//                // 等待 Scrapy 执行完毕
//                int exitCode = process.waitFor();  // 等待 Scrapy 进程完成
//                if (exitCode == 0) {
//                    System.out.println("Scrapy execution finished successfully.");
//                } else {
//                    System.out.println("Scrapy execution failed with exit code: " + exitCode);
//                }

                ObjectMapper objectMapper = new ObjectMapper();
                List<AnimeInfo> jsonData = objectMapper.readValue(new File(outputPath), new TypeReference<List<AnimeInfo>>() {
                });
//                System.out.println(jsonData);
//                String jsonOutput = readFile(outputPath);
                System.out.println(R.success(jsonData));
            } catch (Exception e) {
                System.out.println(R.error());
            }
        }

    private void streamHandler(InputStream inputStream, boolean isErrorStream) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (isErrorStream) {
                        System.err.println(line);
                    } else {
                        System.out.println(line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }


}
