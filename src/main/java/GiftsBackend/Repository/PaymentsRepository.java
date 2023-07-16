package GiftsBackend.Repository;

import GiftsBackend.Model.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository <Payments,Long> {

    Payments findByMerchantRequestID(String merchantRequestId);

}
