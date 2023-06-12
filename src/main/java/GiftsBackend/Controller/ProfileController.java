package GiftsBackend.Controller;


import GiftsBackend.Dtos.UpdateAboutDto;
import GiftsBackend.Dtos.UpdateEliasDto;
import GiftsBackend.Model.User;
import GiftsBackend.Service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/profile")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/update/{email}")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image,
                             @PathVariable String email) throws IOException {
        System.out.println("in controller");
        return ResponseEntity.ok(profileService.UpdateProfileImage(image, email));
    }

    @PostMapping("/update/Elias")
    public ResponseEntity<String> updateElias(@RequestBody UpdateEliasDto updateEliasDto) {

        return ResponseEntity.ok(profileService.UpdateProfileElias(updateEliasDto));
    }


    @PostMapping("/update/about")
    public ResponseEntity<String> updateAbout(@RequestBody UpdateAboutDto updateAboutDto) {

        return ResponseEntity.ok(profileService.UpdateProfileAbout(updateAboutDto));
    }


    @GetMapping("/getprofile/{email}")
    public ResponseEntity<Optional<User>> getUserprofile(@PathVariable String email) throws ChangeSetPersister.NotFoundException {


        System.out.println("email");
        System.out.println(email);
        Optional<User> user = profileService.getUserprofile(email);

        return ResponseEntity.ok(user);

    }

}
