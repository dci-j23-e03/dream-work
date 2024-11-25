package com.dreamwork.repository;

import com.dreamwork.model.job.JobAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAdRepository extends JpaRepository<JobAd, Long> {
//
//  @Query("SELECT j FROM JobAd j WHERE " +
//      "(:seniority IS NULL OR j.seniority = :seniority) AND " +
//      "(:city IS NULL OR LOWER(j.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
//      "(:techStack IS NULL OR LOWER(j.mainTechStack) LIKE LOWER(CONCAT('%', :techStack, '%'))) AND " +
//      "(:daysAgo IS NULL OR j.date >= :daysAgo)")
//  List<JobAd> findJobAdsByFilters(
//      @Param("seniority") String seniority,
//      @Param("city") String city,
//      @Param("daysAgo") LocalDate daysAgo,
//      @Param("techStack") String techStack
//  );
}
