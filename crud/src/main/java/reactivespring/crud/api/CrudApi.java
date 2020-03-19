package reactivespring.crud.api;


import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactivespring.crud.models.Person;
import reactivespring.crud.repository.PersonRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin()
@RequestMapping("/crud")
@Slf4j
public class CrudApi {

    PersonRepo personRepo;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex){
    log.error("This is error Message",ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @Autowired
    public CrudApi(PersonRepo personRepo){
        this.personRepo=personRepo;
    }

    @GetMapping("/get")
    public Flux<Person> getPerson(){
       return personRepo.findAll().take(12).switchIfEmpty(Flux.just(new Person(null,"sohel",
               "java")));
    }


    @PostMapping("/post")
    public Mono<Person> postPerson(@RequestBody Person person){
        Mono<Person> personMono=personRepo.save(person);
        return personMono;
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<Mono<Person>> putPerson(@PathVariable("id") String id, @RequestBody Person person){
        Mono<Person> person1=personRepo.findById(id).map(x->{
            x.setName(person.getName());
            x.setTech(person.getTech());
            return x;
        });

        return ResponseEntity.accepted().body(person1);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Mono<Person>> getPersonById(@PathVariable String id){
        return ResponseEntity.ok().body(personRepo.findById(id));
    }


    @DeleteMapping("/delete/{id}")
    public Mono<?> deletePerson(@PathVariable("id") String id){

//        return personRepo.findById(id).map(x->{
//            if(x instanceof)
//        })
        return personRepo.deleteById(id).switchIfEmpty(Mono.error(new RuntimeException("The user does not exist")));

    }
}
// host: 172.23.0.2

//COPY build/libs/crud-0.0.1-SNAPSHOT.jar /demo.jar