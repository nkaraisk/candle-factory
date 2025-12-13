package gr.ckaraiskos.candlefactory.candle.repository;

import gr.ckaraiskos.candlefactory.candle.entity.Customer;
import gr.ckaraiskos.candlefactory.candle.entity.Product;
import gr.ckaraiskos.candlefactory.candle.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findAllByCustomerOrderByDate(Customer customer);

    List<Sale> findAllByProductTypeOrderByDate(Product productType);

    List<Sale> findAllByCustomerAndProductType(Customer customer, Product productType);

    List<Sale> getAllByDate(LocalDate date);

    List<Sale> getAllByProductTypeAndDate(Product productType, LocalDate date);

    List<Sale> findAllByCustomerAndDate(Customer customer, LocalDate date);

    List<Sale> findAllByCustomerAndProductTypeAndDate(Customer customer, Product productType, LocalDate date);
}
