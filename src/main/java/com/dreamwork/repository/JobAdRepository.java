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

/**
 * Repository interface for managing JobAd entities
 * <p>
 * Provides methods for performing CRUD operations and custom queries related to job ads. Extends
 * JpaRepository for basic database operations.
 */
@Repository
public interface JobAdRepository extends JpaRepository<JobAd, Long> {

  /**
   * Deletes all job ads posted by a specific recruiter.
   * <p>
   * This method performs a delete operation on all job ads associated with the given recruiter.
   *
   * @param recruiter the recruiter whose job ads should be deleted
   */
  @Modifying
  @Query("DELETE FROM JobAd j WHERE j.recruiter = :recruiter")
  void deleteAllJobAdsFromRecruiter(@Param("recruiter") Recruiter recruiter);

  /**
   * Retrieves job ads filtered by parameters, with pagination support.
   * <p>
   * This query allows searching for job ads by seniority, city, tech stack, and date, with each
   * filter being optional. It uses the Pageable parameter to return a paginated result.
   *
   * @param seniority     the seniority level of the job ad (can be null)
   * @param city          the city where the job is located (can be null)
   * @param mainTechStack the main technologies required for the job (can be null)
   * @param date          the minimum date for the job ad to be considered (can be null)
   * @param pageable      pagination information (e.g., page size, page number)
   * @return a paginated list of job ads matching the filter criteria
   */
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
