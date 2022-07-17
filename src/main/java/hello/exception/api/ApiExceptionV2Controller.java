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

    /**
     * @ExceptionHandler 를 사용하게 되면 해당 컨트롤러에서 발생되는 지정된 예외를 잡아 정상 흐름으로 처리하게 된다
     * 이 때 응답은 정상 (200)으로 나가게 된다
     *
     * @ResponseStatus 를 추가하여 응답 코드를 설정할 수 있다
     *
     * 실행 흐름
     * 1. 컨트롤러를 호출한 결과 IllegalArgumentException 예외가 컨트롤러 밖으로 던져진다.
     * 2. 예외가 발생했으로 ExceptionResolver 가 작동한다.
     * 3. 가장 우선순위가 높은 ExceptionHandlerExceptionResolver 가 실행된다.
     * 4. ExceptionHandlerExceptionResolver 는 해당 컨트롤러에 IllegalArgumentException 을 처리할 수 있는 @ExceptionHandler 가 있는지 확인한다.
     * 5. illegalExHandle() 를 실행한다.
     * 6. @RestController 이므로 illegalExHandle() 에도 @ResponseBody 가 적용된다.
     *    따라서 HTTP 컨버터가 사용되고, 응답이 다음과 같은 JSON으로 반환된다.
     * 7. @ResponseStatus(HttpStatus.BAD_REQUEST) 를 지정했으므로 HTTP 상태 코드 400으로 응답한다.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e){
        log.error("[exception handler] ex" , e);
        return new ErrorResult("BAD" , e.getMessage());
    }


    /**
     * @ExceptionHandler(UserException.class)
     *
     * 1. @ExceptionHandler 에 예외를 지정하지 않으면 해당 메서드 파라미터 예외를 사용한다.
     * 2. 여기서는 UserException 을 사용한다.
     * 3. ResponseEntity 를 사용해서 HTTP 메시지 바디에 직접 응답한다. 물론 HTTP 컨버터가 사용된다.
     * 4. ResponseEntity 를 사용하면 HTTP 응답 코드를 프로그래밍해서 동적으로 변경할 수 있다.
     *    앞서 살펴본 @ResponseStatus 는 애노테이션이므로 HTTP 응답 코드를 동적으로 변경할 수 없다.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e){
        log.error("[exception handler] ex" , e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult , HttpStatus.BAD_REQUEST);
    }

    /**
     * 1. 모든 RuntimeException은 HttpStatus.INTERNAL_SERVER_ERROR로 처리된다
     * 2. throw new RuntimeException("잘못된 사용자") 이 코드가 실행되면서 , 컨트롤러밖으로 RuntimeException 이 던져진다.
     * 3. RuntimeException 은 Exception 의 자식 클래스이다. 따라서 이 메서드가 호출된다.
     *    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) 로 HTTP 상태 코드를 500으로 응답한다
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e){
        log.error("[exception handler] ex" , e);
        return new ErrorResult("Exception" , "내부 오류");
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
