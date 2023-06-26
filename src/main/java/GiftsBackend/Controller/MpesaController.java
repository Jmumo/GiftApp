package GiftsBackend.Controller;


import GiftsBackend.Dtos.AccessTokenResponse;
import GiftsBackend.Service.DarajaApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("mobile-money")
public class MpesaController {

    private final DarajaApi darajaApi;
    @GetMapping(path = "/token", produces = "application/json")
    public ResponseEntity<AccessTokenResponse> getAccessToken() {

        System.out.println("in the controller");
        return ResponseEntity.ok(darajaApi.getAccessToken());
    }

}
