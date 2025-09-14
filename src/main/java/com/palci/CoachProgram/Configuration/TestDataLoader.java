package com.palci.CoachProgram.Configuration;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.UserEntity;
import com.palci.CoachProgram.Data.Entities.WeightEntity;
import com.palci.CoachProgram.Data.Repositories.ClientRepository;
import com.palci.CoachProgram.Data.Repositories.TrainingRepository;
import com.palci.CoachProgram.Data.Repositories.UserRepository;
import com.palci.CoachProgram.Data.Repositories.WeightRepository;
import com.palci.CoachProgram.Models.DTO.TrainingDTO;
import com.palci.CoachProgram.Models.Enums.Program;
import com.palci.CoachProgram.Models.Services.TrainingService;
import com.palci.CoachProgram.Models.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
@Profile("!prod")
public class TestDataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TrainingService trainingService;
    @Autowired
    WeightRepository weightChangeRepository;






    @Override
    public void run(String... args) throws Exception {
        if (userRepository.existsByEmail("coach@coachapp.com")){
            return;
        }

        UserEntity user = new UserEntity();
        user.setAdmin(false);
        user.setEmail("coach@coachapp.com");
        user.setUsername("Test Coach");
        user.setPassword(passwordEncoder.encode("testcoach"));

        userRepository.save(user);

        // Clients
        ClientEntity client1 = new ClientEntity();
        client1.setFirstName("John");
        client1.setLastName("Smith");
        client1.setActive(true);
        client1.setProgram(Program.WEIGHT_LOSS);
        client1.setOriginalWeight(104.3);
        client1.setCurrentWeight(97.5);
        client1.setGoalWeight(90.0);
        client1.setSex("man");
        client1.setBirthDate(LocalDate.of(1990,12,11));
        client1.setOwner(user);

        clientRepository.save(client1);

        // Client 2
        ClientEntity client2 = new ClientEntity();
        client2.setFirstName("Emma");
        client2.setLastName("Johnson");
        client2.setActive(true);
        client2.setProgram(Program.MUSCLE_GAIN);
        client2.setOriginalWeight(58.0);
        client2.setCurrentWeight(64.2);
        client2.setGoalWeight(65.0);
        client2.setSex("woman");
        client2.setBirthDate(LocalDate.of(1995, 5, 20));
        client2.setOwner(user);
        clientRepository.save(client2);

        // Client 3
        ClientEntity client3 = new ClientEntity();
        client3.setFirstName("Robert");
        client3.setLastName("Taylor");
        client3.setActive(true);
        client3.setProgram(Program.MAINTAIN);
        client3.setOriginalWeight(76.5);
        client3.setGoalWeight(74.5);
        client3.setSex("man");
        client3.setBirthDate(LocalDate.of(1987, 8, 3));
        client3.setOwner(user);
        clientRepository.save(client3);

        // Training plans
        trainingService.createPlan(client1,user,4,3,LocalDate.now(), LocalTime.of(17,30));
        trainingService.createPlan(client2,user,10,5,LocalDate.now().plusDays(1),LocalTime.of(19,00));
        trainingService.createPlan(client3,user,8,2,LocalDate.now(),LocalTime.of(12,30));

        // Weight changes entry

        // Weight changes for client1 (John Smith)
        weightChangeRepository.save(new WeightEntity(client1, 104.3, 103.0, -1.3, LocalDate.now().minusDays(30)));
        weightChangeRepository.save(new WeightEntity(client1, 103.0, 101.8, -1.2, LocalDate.now().minusDays(24)));
        weightChangeRepository.save(new WeightEntity(client1, 101.8, 100.5, -1.3, LocalDate.now().minusDays(18)));
        weightChangeRepository.save(new WeightEntity(client1, 100.5, 99.0, -1.5, LocalDate.now().minusDays(12)));
        weightChangeRepository.save(new WeightEntity(client1, 99.0, 97.5, -1.5, LocalDate.now().minusDays(6)));

// Weight changes for client2 (Emma Johnson)
        weightChangeRepository.save(new WeightEntity(client2, 58.0, 59.0, 1.0, LocalDate.now().minusDays(30)));
        weightChangeRepository.save(new WeightEntity(client2, 59.0, 60.2, 1.2, LocalDate.now().minusDays(24)));
        weightChangeRepository.save(new WeightEntity(client2, 60.2, 61.5, 1.3, LocalDate.now().minusDays(18)));
        weightChangeRepository.save(new WeightEntity(client2, 61.5, 63.0, 1.5, LocalDate.now().minusDays(12)));
        weightChangeRepository.save(new WeightEntity(client2, 63.0, 64.2, 1.2, LocalDate.now().minusDays(6)));




    }
}
