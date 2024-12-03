package com.dreamwork.repository;

import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.job.Seniority;
import com.dreamwork.model.user.Recruiter;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAdRepository extends JpaRepository<JobAd, Long> {

@Query("SELECT j FROM JobAd j WHERE " +
    "(:seniority IS NULL OR j.seniority = :seniority) AND " +
    "(:city IS NULL OR LOWER(j.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
//      "(:datePosted IS NULL OR j.date >= :datePosted) AND " +
    "(:techStack IS NULL OR LOWER(j.mainTechStack) LIKE LOWER(CONCAT('%', :techStack, '%')))")
List<JobAd> findJobAdsByFilters(@Param("seniority") Seniority seniority,
                                @Param("city") String city,
//                                  @Param("datePosted") LocalDate datePosted,
                                @Param("techStack") String techStack);

  @Modifying
  @Query("DELETE FROM JobAd j WHERE j.recruiter = :recruiter")
  void deleteAllJobAdsFromRecruiter(@Param("recruiter") Recruiter recruiter);
}
