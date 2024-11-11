package com.dreamwork.repository;

import com.dreamwork.model.job.JobAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAdRepository extends JpaRepository<JobAd, Long> {

}
