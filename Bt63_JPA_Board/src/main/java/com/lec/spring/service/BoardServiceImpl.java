package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Post;
import com.lec.spring.util.U;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

// 서비스와 리포지토리는 인터페이스를 먼저 만드는 것이 좋다

@Service
public class BoardServiceImpl implements BoardService {

    @Value("${app.upload.path}")
    private String uploadDir;

    @Value("${app.pagination.write_pages}")
    private int WRITE_PAGES;

    @Value("${app.pagination.page_rows}")
    private int PAGE_ROWS;



    // 특정 글(id) 에 첨부파일(들) 추가
    private void addFiles(Map<String, MultipartFile> files, Long id) {
        if (files == null) return;

        for (Map.Entry<String, MultipartFile> e : files.entrySet()) {

            // name="upfile##" 인 경우만 첨부파일 등록. (이유, 다른 웹에디터와 섞이지 않도록..ex: summernote)
            if (!e.getKey().startsWith("upfile")) continue;  // name="upfile##" 아닌경우 재끼

            // 첨부파일 정보 출력   // 학습목적으로 어떤 정보들을 불러올 수 있는지 보기 위하여 출력
            System.out.println("\n첨부파일 정보: " + e.getKey()); // name 값
            U.printfileInfo(e.getValue());  //MultipartFile 정보
            System.out.println();

            // 물리적인 파일 저장
            Attachment file = upload(e.getValue());

            // 성공하면 DB에도 저장
            if (file != null) {
                file.setPost_id(id);    // FK 설정
//                attachmentRepository.save(file);    // INSERT

                // TODO
            }
        }
    }

    // 물리적으로 서버에 파일 저장. 중복된 파일이름 -> rename 처리
    private Attachment upload(MultipartFile multipartFile) {
        Attachment attachment = null;

        // 첨부된 파일 없으면 pass
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) return null;

        // 원본파일명
        // ** C:\User\aaa\bbb/ddd/asdfsad.ddd -> 윈도우에서 쓰는 경로 '\'와 리눅스에서 쓰는 경로 '/' 가 함께 있는 경우
        //      cleanPath : "\" -> "/" 변경
        String sourceName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        // 저장될 파일 명
        String fileName = sourceName;

        // 파일이 중복되는지 확인
        File file = new File(uploadDir, fileName);
        if (file.exists()) {    // true 이면 이미 존재한느 파일명, 중복 된다면 다른 이름으로 변경하여 저장
            // a.txt => a_2378142783946.txt  : time stamp 값을 활용할거다!
            // "a" => "a_2378142783946"  : 확장자 없는 경우

            int pos = fileName.lastIndexOf(".");
            if (pos > -1) {    // 확장자가 있는경우
                String name = fileName.substring(0, pos);   // 파일의 '이름'
                String ext = fileName.substring(pos + 1);   // 파일의 '확장자'

                // 중복방지 회피를 위해 새로운 이름 (타임스탬프, 현재시간 ms) 를 파일명에 추가
                fileName = name + "_" + System.currentTimeMillis() + "." + ext;
            } else {    // 확장자가 없는 파일의 경우
                fileName += "_" + System.currentTimeMillis();
            }
        }
        System.out.println("filaName: " + fileName);

        // java.id.*
        // java.nio.*
        Path copyOfLocation = Paths.get(new File(uploadDir, fileName).getAbsolutePath());
        System.out.println(copyOfLocation);

        try {
            // inputStream을 가져와서
            // copyOfLocation (저장위치)로 파일을 쓴다.
            // copy의 옵션은 기존에 존재하면 REPLACE(대체한다), 오버라이딩 한다

            // java.nio.file.Files
            Files.copy(
                    multipartFile.getInputStream(),
                    copyOfLocation,
                    StandardCopyOption.REPLACE_EXISTING // 기존에 존재하면 덮어쓰기
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        attachment = Attachment.builder()
                .filename(fileName) // 저장된 이름
                .sourcename(sourceName) // 원본이름
                .build();

        return attachment;
    }


    //  [이미지 파일 여부 세팅]
    private void setImage(List<Attachment> fileList) {
        // upload 의 실제 물리적인 경로를 받아와야 한다.
        String realPath = new File(uploadDir).getAbsolutePath();

        for (Attachment attachment : fileList) {
            BufferedImage imgData = null;
            File f = new File(realPath, attachment.getFilename());  // 저장된 첨부파일에 대한  File 객체

            try {
                imgData = ImageIO.read(f);  // 파일이 없으면 exception, 파일이 있지만 이미지가 아닌경우 null 리턴
                // ※ ↑ 파일이 존재 하지 않으면 IOExcepion 발생한다
                //   ↑ 이미지가 아닌 경우는 null 리턴

                if(imgData != null) attachment.setImage(true);
            } catch (IOException e) {
                System.out.println("파일존재안함: " + f.getAbsolutePath() + " [" + e.getMessage() + "]");
            }
        }
    }



    // 특정 첨부파일을 물리적으로 삭제
    private void delFile(Attachment file) {
        String saveDirectory = new File(uploadDir).getAbsolutePath();

        File f = new File(saveDirectory, file.getFilename());
        System.out.println("삭제시도 --> " + f.getAbsolutePath());

        if (f.exists()) {
            if (f.delete()) {
                System.out.println("삭제 성공");
            } else {
                System.out.println("삭제 실패");
            }
        } else {
            System.out.println("파일이 존재하지 않습니다.");
        }
    }

    @Override
    public int write(Post post, Map<String, MultipartFile> files) {
        return 0;
    }

    @Override
    public Post detail(Long id) {
        return null;
    }

    @Override
    public List<Post> list() {
        return List.of();
    }

    @Override
    public List<Post> list(Integer page, Model model) {
        return List.of();
    }

    @Override
    public Post selectById(Long id) {
        return null;
    }

    @Override
    public int update(Post post, Map<String, MultipartFile> files, Long[] delfile) {
        return 0;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }
}   //end Service
