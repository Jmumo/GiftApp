package GiftsBackend.Repository;

import GiftsBackend.Model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<UserProfile ,Long> {

}
