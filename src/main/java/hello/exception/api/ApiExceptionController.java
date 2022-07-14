package hello.exception.api;

import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@RestController
public class ApiExceptionController {

    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable String id){
        if(id.equals("ex")){
            throw new RuntimeException("API 예외 테스트 메세지 - RuntimeException");
        }
        else if(id.equals("bad")){
            throw new IllegalArgumentException("잘못된 입력 값 - IllegalArgumentException");
        }
        else if(id.equals("user-ex")){
            throw new UserException("사용자 오류 - UserException");
        }

        return new MemberDto(id , "hello !!!" + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String memberId;
        private String name;
    }
}
