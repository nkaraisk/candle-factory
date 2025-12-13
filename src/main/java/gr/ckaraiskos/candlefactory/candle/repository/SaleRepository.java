package gr.ckaraiskos.candlefactory.candle.repository;

import gr.ckaraiskos.candlefactory.candle.entity.Customer;
import gr.ckaraiskos.candlefactory.candle.entity.Product;
import gr.ckaraiskos.candlefactory.candle.entity.Sale;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findAllByCustomerOrderByDate(Customer customer);

    List<Sale> findAllByProductTypeOrderByDate(Product product);

    List<Sale> getAllByDate(LocalDate date);

    List<Sale> findAllByCustomerAndProductType(Customer customer, Product product);

    List<Sale> findAllByCustomerAndDate(Customer customer, LocalDate date);

    List<Sale> getAllByProductTypeAndDate(Product product, LocalDate date);

    List<Sale> findAllByCustomerAndProductTypeAndDate(Customer customer, Product product, LocalDate date);
}
