package uni.pu.fmi.data.service;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uni.pu.fmi.data.entity.SamplePerson;

import java.util.List;


public interface SamplePersonRepository
                extends
                JpaRepository<SamplePerson, Long>,
                JpaSpecificationExecutor<SamplePerson>
{
    List<SamplePerson> findAllByFirstName(String firstName);
    List<SamplePerson> findAllByLastNameContaining(String lastName);
}
