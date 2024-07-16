package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Post;
import com.lec.spring.domain.User;
import com.lec.spring.repository.AttachmentRepository;
import com.lec.spring.repository.PostRepository;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.util.U;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private PostRepository postRepository;
    private UserRepository userRepository;
    private AttachmentRepository attachmentRepository;


    @Autowired
    public void setPostRepository(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Autowired
    public void setAttachmentRepository(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }


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
                file.setPost(id);    // FK 설정
                attachmentRepository.saveAndFlush(file);    // INSERT
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

                if (imgData != null) attachment.setImage(true);
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
        // 현재 로그인한 작성자 정보
        User user = U.getLoggedUser();

        // 위 정보는 session 의 정보이고, 일단 DB 에서 다시 읽어들어온다.
        user = userRepository.findById(user.getId()).orElse(null);
        post.setUser(user); // 글 작성자 세팅

        // 글 저장
        post = postRepository.saveAndFlush(post);   // INSERT, id 값 받아온다.

        // 첨부파일 추가
        addFiles(files, post.getId());

        return 1;
    }

    @Override
    @Transactional
    public Post detail(Long id) {

        Post post = postRepository.findById(id).orElse(null);   // 글을 먼저 읽어오고
        if (post != null) {
            // 조회수 증가.
            post.setViewCnt(post.getViewCnt() + 1);
            postRepository.saveAndFlush(post);  // UPDATE

            // 특정 글의 첨부파일(들) 정보 가져오기
            List<Attachment> fileList = attachmentRepository.findByPost(post.getId());
            setImage(fileList);
            post.setFileList(fileList);
        }

        return post;
    }

    @Override
    public List<Post> list() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> list(Integer page, Model model) {
        // 현재 페이지
        if (page == null || page < 1) page = 1;   // 디폴트 1page

        // 페이징
        // writePages: 한 [페이징] 당 몇개의 페이지가 표시되나
        // pageRows: 한 '페이지'에 몇개의 글을 리스트 할것인가?
        HttpSession session = U.getSession();
        Integer writePages = (Integer) session.getAttribute("writePages");
        if (writePages == null) writePages = WRITE_PAGES;
        Integer pageRows = (Integer) session.getAttribute("pageRows");
        if (pageRows == null) pageRows = PAGE_ROWS;
        session.setAttribute("page", page);   // 현재 페이지 번호 -> session 에 저장

        // 주의 PageRequest.of() 의 page 값은 0-base 이다!
        Page<Post> pagePost = postRepository.findAll(PageRequest.of(page - 1, pageRows, Sort.by(Sort.Order.desc("id"))));

        long cnt = pagePost.getTotalElements();  // 글 목록 전체의 개수
        int totalPage = pagePost.getTotalPages();  // 총 몇 '페이지' 분량?


        // [페이징] 에 표시할 '시작페이지' 와 '마지막 페이지'
        int startPage = 0;
        int endPage = 0;

        // 해당 '페이지' 의 글 목록
        List<Post> list = null;


        if (cnt > 0) {  // 데이터가 최소 1개 이상 있는 경우만 페이징

            // page 값 보정
            if (page > totalPage) page = totalPage;


            // fromRow : 몇번째 데이터부터
            int fromRow = (page - 1) * pageRows;


            // [페이징] 에 표시할 '시작페이지' 와 '마지막페이지' 계산
            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;
            if (endPage >= totalPage) endPage = totalPage;


            // 해당 페이지의 글 목록 읽어오기
            list = pagePost.getContent();
            model.addAttribute("list", list);
        } else {
            page = 0;
        }

        // 하나의 객체로 만들어 보내줄 수도 있다.
        model.addAttribute("cnt", cnt);  // 전체 글 개수
        model.addAttribute("page", page); // 현재 페이지
        model.addAttribute("totalPage", totalPage);  // 총 '페이지' 수
        model.addAttribute("pageRows", pageRows);  // 한 '페이지' 에 표시할 글 개수


        // [페이징]
        model.addAttribute("url", U.getRequest().getRequestURI());  // 목록 url
        model.addAttribute("writePages", writePages); // [페이징] 에 표시할 숫자 개수
        model.addAttribute("startPage", startPage);  // [페이징] 에 표시할 시작 페이지
        model.addAttribute("endPage", endPage);   // [페이징] 에 표시할 마지막 페이지


        return list;
    }

    @Override
    public Post selectById(Long id) {
        Post post = postRepository.findById(id).orElse(null);

        if (post != null) {
            // 첨부파일 정보 가져오기 // mybatis 에서 사용하던 코드 그대로 가져와서 사용가능
            List<Attachment> fileList = attachmentRepository.findByPost(post.getId());
            setImage(fileList);   // 이미지 파일 여부 세팅
            post.setFileList(fileList);
        }

        return post;
    }

    @Override
    public int update(Post post, Map<String, MultipartFile> files, Long[] delfile) {
        int result = 0;

        // update 하고자 하는 객체를 먼저 읽어와야 한다.
        Post p = postRepository.findById(post.getId()).orElse(null);

        if (p != null) {
            p.setSubject(post.getSubject());
            p.setContent(post.getContent());
            p = postRepository.saveAndFlush(p); // UPDATE

            // 새로운 첨부파일 추가
            addFiles(files, post.getId());

            // 삭제할 첨부파일들은 삭제하기
            if (delfile != null) {
                for (Long fileId : delfile) {
                    Attachment file = attachmentRepository.findById(fileId).orElse(null);
                    if (file != null) {
                        delFile(file);  // 물리적으로 파일 삭제
                        attachmentRepository.delete(file);  // DB 에서 삭제
                    }
                }
            }
            result = 1;
        }

        return result;
    }

    @Override
    public int deleteById(Long id) {
        int result = 0;

        Post post = postRepository.findById(id).orElse(null);  // 존재하는 데이터인지 확인

        if (post != null) {  // 존재한다면 삭제 진행
            // 물리적으로 저장된 첨부파일(들) 부터 삭제
            List<Attachment> fileList = attachmentRepository.findByPost(id);

            if (fileList != null && fileList.size() > 0) {
                for (Attachment file : fileList) {
                    delFile(file);
                }
            }

            // 글 삭제 (참조하는 첨부파일, 댓글 등도 같이 삭제 될것이다.  ON DELETE CASCADE)
            postRepository.delete(post);
            result = 1;
        }

        return result;
    }
}   //end Service
