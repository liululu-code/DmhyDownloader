package top.lll;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.lll.mapper")
public class DmhyDownloaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmhyDownloaderApplication.class, args);
    }

}
