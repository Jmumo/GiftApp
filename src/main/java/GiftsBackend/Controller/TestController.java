package GiftsBackend.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {
     @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("this is test secured endpoint with github actions aws");
    }

}
