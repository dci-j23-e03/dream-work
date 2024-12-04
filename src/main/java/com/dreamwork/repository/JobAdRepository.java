package com.dreamwork.repository;

import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.job.Seniority;
import com.dreamwork.model.user.Recruiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAdRepository extends JpaRepository<JobAd, Long> {

  @Modifying
  @Query("DELETE FROM JobAd j WHERE j.recruiter = :recruiter")
  void deleteAllJobAdsFromRecruiter(@Param("recruiter") Recruiter recruiter);

  @Query("SELECT j FROM JobAd j WHERE "
      + "(:seniority IS NULL OR j.seniority = :seniority) AND "
      + "(:city IS NULL OR LOWER(j.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND "
      + "(:mainTechStack IS NULL OR LOWER(j.mainTechStack) LIKE "
      + "LOWER(CONCAT('%', :mainTechStack, '%'))) AND "
      + "(:date IS NULL OR j.date >= CAST(:date AS DATE))")
  Page<JobAd> findAllJobAdsByFiltering(
      @Param("seniority") Seniority seniority, @Param("city") String city,
      @Param("mainTechStack") String mainTechStack, @Param("date") String date,
      Pageable pageable);
}
