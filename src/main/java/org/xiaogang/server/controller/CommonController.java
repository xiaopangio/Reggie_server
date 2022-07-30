package org.xiaogang.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xiaogang.server.common.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * className: CommonController
 * description:
 * author: xiaopangio
 * date: 2022/7/26 18:44
 * version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info(file.toString());
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString()+suffix;
        File newFile = new File(basePath);
        if(!newFile.exists()){
            newFile.mkdirs();
        }
        try {
            file.transferTo(new File(basePath+filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(filename);
    }
    @GetMapping("/download")
    public R<String> download(String name, HttpServletResponse response){
        FileInputStream fileInputStream=null;
        ServletOutputStream outputStream=null;
        try {
            fileInputStream = new FileInputStream(new File(basePath+name));
            response.setContentType("image/jpeg");
            outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while((len = fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fileInputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return R.success(name);
    }
}
