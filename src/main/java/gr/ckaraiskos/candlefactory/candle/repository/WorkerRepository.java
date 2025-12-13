package gr.ckaraiskos.candlefactory.candle.repository;

import gr.ckaraiskos.candlefactory.candle.entity.Worker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {

    Optional<Worker> findWorkerByFirstNameAndLastNameAndPhoneNumber(String firstName, String lastName, String phoneNumber);

    List<Worker> findWorkerByFirstNameAndLastName(String firstName, String lastName);

    List<Worker> findWorkerByFirstName(String firstName);

    List<Worker> findWorkerByLastName(String lastName);

    Optional<Worker> findWorkerByPhoneNumber(String phoneNumber);

    Optional<Worker> findWorkerById(Long id);

    void deleteWorkerById(Long id);
}
