package hello.exception.api;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api2")
public class ApiExceptionV2Controller {

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String memberId;
        private String name;
    }

    @GetMapping("/members/{id}")
    public ApiExceptionController.MemberDto getMember(@PathVariable String id){
        if(id.equals("ex")){
            throw new RuntimeException("API 예외 테스트 메세지 - RuntimeException");
        }
        else if(id.equals("bad")){
            throw new IllegalArgumentException("잘못된 입력 값 - IllegalArgumentException");
        }
        else if(id.equals("user-ex")){
            throw new UserException("사용자 오류 - UserException");
        }

        return new ApiExceptionController.MemberDto(id , "hello !!!" + id);
    }
}
