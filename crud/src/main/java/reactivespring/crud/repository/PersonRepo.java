package reactivespring.crud.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactivespring.crud.models.Person;

public interface PersonRepo extends ReactiveMongoRepository<Person,String> {
}
