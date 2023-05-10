package com.ssafy.nanumi.db.repository;

import com.ssafy.nanumi.api.response.MatchInterface;
import com.ssafy.nanumi.db.entity.Match;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Optional;


public interface MatchRepository extends CrudRepository<Match, Long> {
    @Query(value =
            "SELECT users.id AS UserId, " +
                    "users.nickname As UserNickName, "+
                    "matches.create_date AS CreateDate, " +
                    "matches.id AS MatchId, " +
                    "matches.product_id AS ProductId, " +
                    "users.profile_url AS ProfileUrl " +
                    "FROM users " +
                    "LEFT JOIN matches ON users.id = matches.receiver_id " +
                    "WHERE matches.product_id=:productId " +
                    "ORDER BY matches.create_date ASC " +
                    "LIMIT 3"
            , nativeQuery = true)
    ArrayList<MatchInterface> getMatchListByProduct(@Param("productId") long productId);

    @Query(value =
            "SELECT m " +
                    "FROM Match m " +
                    "WHERE m.product.id = :productId " +
                    "AND (m.user.id = :sendUserId OR m.user.id = :receiveUserId) " +
                    "AND m.isMatching = true"
    )
    Optional<Match> findMatchByProductAndUsers(@Param("productId") long productId,
                                               @Param("sendUserId") long sendUserId,
                                               @Param("receiveUserId") long receiveUserId);

}