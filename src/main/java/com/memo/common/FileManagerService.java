package com.memo.common;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component // controller도 아니고 bo, mapper도 아닐 때 사용하는 spring bean 어노테이션
public class FileManagerService {
    // 실제 업로드 된 이미지 파일이 저장될 경로 지정
    public final static String FILE_UPLOAD_PATH = "/Users/leesungmin/Desktop/activity/mega_it_academy/Backend/6_spring_project/m_images/"; // 상수값

    // i: multipart file, userLoginId(폴더명으로 사용)
    // o: image Path(String)
    public String uploadFile(MultipartFile file, String loginId) {
        // 폴더(디렉토리) 생성
        // 예: aaaa_1723468798/sun.png
        String directoryName = loginId + "_" + System.currentTimeMillis(); // aaaa_1723468798
        String filePath = FILE_UPLOAD_PATH + directoryName + "/"; // /Users/leesungmin/Desktop/activity/mega_it_academy/Backend/6_spring_project/m_images/aaaa_1723468798/

        // 폴더 생성
        File directory = new File(filePath);
        if (directory.mkdir() == false) {
            return null; // 폴더 생성을 실패하면 이미지 경로는 null로 리턴(에러 아님)
        }

        // 파일 업로드
        try {
            byte[] bytes = file.getBytes();
            // !!!!! 개인 프로젝트에서 파일명을 웬만하면 영문자로 변경할 것!!!!!!! (한글 업로드가 안 될 수 있어서)
            Path path = Paths.get(filePath + file.getOriginalFilename()); // /Users/leesungmin/Desktop/activity/mega_it_academy/Backend/6_spring_project/m_images/aaaa_1723468798/sun.png
            Files.write(path, bytes); // 이 줄을 지나는 순간 폴더 안에 이미지가 생긴다.
        } catch (IOException e) {
            e.printStackTrace(); // 에러 메시지 출력 => 에러는 보되, 중단이 되는 것은 아님
            return null; // 이미지 업로드 실패시 이미지 경로는 null로 리턴(에러 아님)
        }

        // 파일 업로드가 성공하면 이미지 url path를 리턴한다.
        // => 주소는 이렇게 될 것이다.(예언)
        // 예: /images/aaaa_17485928104/sun.png
        return "/images/" + directoryName + "/" + file.getOriginalFilename();
    }
}
