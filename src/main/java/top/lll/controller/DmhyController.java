package top.lll.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.lll.entity.SearchForm;
import top.lll.entity.AnimeInfo;
import top.lll.utils.R;


import java.util.Arrays;
import java.util.List;
import java.io.*;

@Slf4j
@RestController
@RequestMapping("dmhy")

public class DmhyController {

    @GetMapping("hello")
    public String hello() {
        return "hello dmhy";
    }

    @PostMapping("search")
    public R<AnimeInfo> search(@RequestBody SearchForm searchForm) {
        return getSearchResult(searchForm);
    }

    @PostMapping("download")
    public R<AnimeInfo> download(@RequestBody AnimeInfo[] animeInfos) {
        return getDownloadUrl(animeInfos);
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


    private R<AnimeInfo> getSearchResult(SearchForm searchForm) {
        try {
            String scrapyPath = "D:\\anaconda3\\envs\\spider\\Scripts\\scrapy.exe";
            String outputPath = "F:\\dev_codes\\project\\web_spider\\dmhy.json";
            String keywordParam = "keyword=" + searchForm.getInput();
            String sortIdParam = "sort_id=" + searchForm.getCategoryValue();
            String teamIdParam = "team_id=" + searchForm.getTeamValue();
            String orderParam = "order=" + searchForm.getSortValue();

            List<String> command = Arrays.asList(scrapyPath, "crawl", "dmhy_search", "-O", outputPath, "-a", keywordParam, "-a", sortIdParam, "-a", teamIdParam, "-a", orderParam);
            System.out.println(command);
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File("F:\\dev_codes\\project\\web_spider"));
            System.out.println(processBuilder.command());
            Process process = processBuilder.start();

            streamHandler(process.getInputStream(), false);
            streamHandler(process.getErrorStream(), true);

            // 等待 Scrapy 执行完毕
            int exitCode = process.waitFor();  // 等待 Scrapy 进程完成
            if (exitCode == 0) {
                System.out.println("Scrapy execution finished successfully.");
            } else {
                System.out.println("Scrapy execution failed with exit code: " + exitCode);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            List<AnimeInfo> jsonData = objectMapper.readValue(new File(outputPath), new TypeReference<List<AnimeInfo>>() {});
            return R.success(jsonData);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }

    public static void saveToJson(AnimeInfo[] animeInfos, String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(fileName), animeInfos);
            System.out.println("JSON 文件已成功保存为 " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private R<AnimeInfo> getDownloadUrl(AnimeInfo[] animeinfos) {
        try {
            String scrapyPath = "D:\\anaconda3\\envs\\spider\\Scripts\\scrapy.exe";
            String outputPath = "F:\\dev_codes\\project\\web_spider\\download.json";
            String jsonFilePath = "F:\\dev_codes\\project\\web_spider\\download.json";
            String jsonFileParam = "download_json_file="+jsonFilePath;

            saveToJson(animeinfos, jsonFilePath);

            List<String> command = Arrays.asList(scrapyPath, "crawl", "download_spider", "-O", outputPath, "-a", jsonFileParam);
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File("F:\\dev_codes\\project\\web_spider"));
            Process process = processBuilder.start();

            streamHandler(process.getInputStream(), false);
            streamHandler(process.getErrorStream(), true);

            // 等待 Scrapy 执行完毕
            int exitCode = process.waitFor();  // 等待 Scrapy 进程完成
            if (exitCode == 0) {
                System.out.println("Scrapy execution finished successfully.");
            } else {
                System.out.println("Scrapy execution failed with exit code: " + exitCode);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            List<AnimeInfo> jsonData = objectMapper.readValue(new File(outputPath), new TypeReference<List<AnimeInfo>>() {});
            return R.success(jsonData);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }
}