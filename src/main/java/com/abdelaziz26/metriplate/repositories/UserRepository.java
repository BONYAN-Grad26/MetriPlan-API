package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.user.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<@NonNull User, @NonNull Long> {
    Optional<User> findByEmail(String email);

    @Query("""
           SELECT DISTINCT u.email
           FROM User u
           WHERE exists (
               SELECT dp
               FROM WeeklyPlan dp
               WHERE dp.user = u
                 AND dp.endDate = CURRENT_DATE
                 )
           """)
    List<String> findUsersWhosePlansEndDateIsToday();
}
