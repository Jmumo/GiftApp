package GiftsBackend.Service;


import GiftsBackend.Dtos.UpdateAboutDto;
import GiftsBackend.Dtos.UpdateEliasDto;
import GiftsBackend.Dtos.UpdateNamesDto;
import GiftsBackend.Model.User;
import GiftsBackend.Model.UserProfile;
import GiftsBackend.Repository.ProfileRepository;
import GiftsBackend.Repository.UserRepository;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final Cloudinary cloudinary;

    public String UpdateProfileImage(MultipartFile image, String email) throws IOException {
        Optional<User> user = userRepository.findByEmail(email);
        System.out.println("user by email");
        Long profileId = user.get().getUserProfile().getId();
        System.out.println("profile by id");
        Optional<UserProfile> userProfile = profileRepository.findById(profileId);

        UserProfile updateUserProfile = userProfile.get();

        System.out.println(image.getOriginalFilename());
        System.out.println(image.getContentType());
        System.out.println(image.getSize());

        String imageUrl = cloudinary.uploader()
                .upload(image.getBytes(),
                        Map.of("public_id", UUID.randomUUID().toString())).get("url").toString();

        System.out.println("past image");


        updateUserProfile.setImageUrl(imageUrl);
        updateUserProfile.setType(image.getContentType());

        profileRepository.save(updateUserProfile);
        return imageUrl;

    }


    public Optional<User> getUserprofile(String email)  {

        return userRepository.findByEmail(email);

    }



    public String UpdateProfileElias(UpdateEliasDto updateEliasDto) {

        Optional<User> user = userRepository.findByEmail(updateEliasDto.getEmail());
        Long profileId = user.get().getUserProfile().getId();
        Optional<UserProfile> userProfile = profileRepository.findById(profileId);

        UserProfile updateUserProfile = userProfile.get();
        updateUserProfile.setElias(updateEliasDto.getElias());
        profileRepository.save(updateUserProfile);

        return updateUserProfile.getElias();
    }

    public String UpdateProfileAbout(UpdateAboutDto updateAboutDto) {

        var user = userRepository.findByEmail(updateAboutDto.getEmail());
        Long profileId = user.get().getUserProfile().getId();
        Optional<UserProfile> userProfile = profileRepository.findById(profileId);

        UserProfile updateUserProfile = userProfile.get();
        updateUserProfile.setAbout(updateAboutDto.getAbout());
        profileRepository.save(updateUserProfile);

        return updateUserProfile.getAbout();
    }

    public User UpdateNames(UpdateNamesDto updateNamesDto) {
        User user = fetchUser(updateNamesDto.getEmail());
        user.setFirstname(updateNamesDto.getFirstname());
        user.setLastname(updateNamesDto.getLastname());
        return userRepository.save(user);

    }


    private User fetchUser(String email){
        Optional<User> user = userRepository.findByEmail(email);
        return user.get();
    }
}
