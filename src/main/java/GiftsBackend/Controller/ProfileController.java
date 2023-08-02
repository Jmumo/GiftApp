package GiftsBackend.Controller;


import GiftsBackend.Dtos.*;
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
    public ResponseEntity<ImageResponseDto> uploadImage(@RequestParam("image") MultipartFile image,
                                                        @PathVariable String email) throws IOException {
        return ResponseEntity.ok(profileService.UpdateProfileImage(image, email));
    }




    @PostMapping("/update/Elias")
    public ResponseEntity<String> updateElias(@RequestBody UpdateEliasDto updateEliasDto) {

        return ResponseEntity.ok(profileService.UpdateProfileElias(updateEliasDto));
    }

    @PostMapping("/Update")
    public ResponseEntity<User> UpdateBio(@RequestBody UserBioDto userBioDto){
      return ResponseEntity.ok(profileService.UpdateUserBio(userBioDto));
    }

    @PostMapping("/update/usernames")
    public ResponseEntity<User> updateNames(@RequestBody UpdateNamesDto updateNamesDto) {

        return ResponseEntity.ok(profileService.UpdateNames(updateNamesDto));
    }


    @PostMapping("/update/about")
    public ResponseEntity<String> updateAbout(@RequestBody UpdateAboutDto updateAboutDto) {

        return ResponseEntity.ok(profileService.UpdateProfileAbout(updateAboutDto));
    }


    @GetMapping("/getprofile/{email}")
    public ResponseEntity<Optional<User>> getUserprofile(@PathVariable String email) {

        Optional<User> user = profileService.getUserprofile(email);

        return ResponseEntity.ok(user);

    }

}
