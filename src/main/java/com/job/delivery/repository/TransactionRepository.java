package com.job.delivery.repository;

import com.job.delivery.entity.Carrier;
import com.job.delivery.entity.Product;
import com.job.delivery.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

//    @Query("SELECT t.offer.place.region, COUNT(t) FROM Transaction t GROUP BY t.offer.place.region")
//    List<Object[]> countTransactionsPerRegion();

    @Query(value = "SELECT COUNT(*) AS transaction_count, r.region_name " +
            "FROM transaction t " +
            "JOIN request req ON t.request_id = req.request_id " +
            "JOIN place p ON req.place_id = p.place_id " +
            "JOIN region r ON p.region_id = r.region_id " +
            "GROUP BY r.region_id, t.transaction_id " +
            "HAVING transaction_count = (" +
            "    SELECT COUNT(*) " +
            "    FROM transaction t2 " +
            "    JOIN request req2 ON t2.request_id = req2.request_id " +
            "    JOIN place p2 ON req2.place_id = p2.place_id " +
            "    JOIN region r2 ON p2.region_id = r2.region_id " +
            "    WHERE t2.transaction_id = t.transaction_id" +
            ") " +
            "ORDER BY transaction_count, r.region_name ASC",
            nativeQuery = true)
    List<Object[]> getRegionsWithSameTransactionCount();

    @Query("SELECT c.carrierName as carrierName, SUM(t.score) as totalScore "
            + "FROM Transaction t "
            + "JOIN t.carrier c "
            + "WHERE c IN :carriers "
            + "GROUP BY c "
            + "HAVING SUM(t.score) >= :minimumScore "
            + "ORDER BY c.carrierName ASC")
    List<Object[]> getScorePerCarrier(@Param("carriers") List<Carrier> carriers, @Param("minimumScore") int minimumScore);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.product=:product")
    Long countByProduct(Product product);

//    @Query("SELECT p.productId as productId, COUNT(t.transactionId) as transactionCount "
//            + "FROM Transaction t "
//            + "JOIN t.product p "
//            + "WHERE t.score > 0 "
//            + "GROUP BY p "
//            + "ORDER BY p.productName ASC")
//    List<Map<String, Object>> getTransactionCountPerProduct();


}
