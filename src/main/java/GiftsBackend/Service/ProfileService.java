package GiftsBackend.Service;


import GiftsBackend.Dtos.UpdateAboutDto;
import GiftsBackend.Dtos.UpdateEliasDto;
import GiftsBackend.Model.User;
import GiftsBackend.Model.UserProfile;
import GiftsBackend.Repository.ProfileRepository;
import GiftsBackend.Repository.UserRepository;
import com.cloudinary.Cloudinary;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
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
        Long profileId = user.get().getUserProfile().getId();
        Optional<UserProfile> userProfile = profileRepository.findById(profileId);

        UserProfile updateUserProfile = userProfile.get();

        String imageUrl = cloudinary.uploader()
                .upload(image.getBytes(),
                        Map.of("public_id", UUID.randomUUID().toString())).toString();


        updateUserProfile.setImageUrl(imageUrl);
        updateUserProfile.setType(image.getContentType());

        profileRepository.save(updateUserProfile);
        return imageUrl;


    }

    @Transactional
    public User getUserprofile(String email) throws ChangeSetPersister.NotFoundException {

        return userRepository.findByEmail(email).orElseThrow(ChangeSetPersister.NotFoundException::new);

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
}
