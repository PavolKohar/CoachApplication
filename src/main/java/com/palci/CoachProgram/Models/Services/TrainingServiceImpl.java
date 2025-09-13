package com.palci.CoachProgram.Models.Services;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.TrainingEntity;
import com.palci.CoachProgram.Data.Entities.TrainingPlanEntity;
import com.palci.CoachProgram.Data.Entities.UserEntity;
import com.palci.CoachProgram.Data.Repositories.TrainingPlanRepository;
import com.palci.CoachProgram.Data.Repositories.TrainingRepository;
import com.palci.CoachProgram.Models.DTO.TrainingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class TrainingServiceImpl implements TrainingService{

    @Autowired
    TrainingRepository trainingRepository;
    @Autowired
    TrainingPlanRepository trainingPlanRepository;

    private final Queue<String> twoSplit = new LinkedList<>(Arrays.asList("Full body","Full body"));
    private final Queue<String> threeSplit = new LinkedList<>(Arrays.asList("UpperBody","LowerBody", "FullBody"));
    private final Queue<String> fourSplit = new LinkedList<>(Arrays.asList("UpperBody","LowerBody","UpperBody","LowerBody"));
    private final Queue<String> fiveSplit = new LinkedList<>(Arrays.asList("UpperBody","LowerBody","Pull","Push","Legs"));
    private final Queue<String> sixSplit = new LinkedList<>(Arrays.asList("Push","Pull","Leg","Push","Pull","Leg"));

    private String chooseWorkout(Queue<String> split){
        String workout = split.poll();
        split.offer(workout);
        return workout;
    }

    @Override
    public void createPlan(ClientEntity client, UserEntity owner, int weeks, int workoutsPerWeek, LocalDate startDate, LocalTime time) {
        int totalWorkouts = weeks * workoutsPerWeek;
        LocalDate endDate = startDate.plusWeeks(weeks);
        int days = (int) ChronoUnit.DAYS.between(startDate,endDate);

        Queue<String> trainingSplit;
        switch (workoutsPerWeek){
            case 2-> trainingSplit = twoSplit;
            case 3-> trainingSplit = threeSplit;
            case 4-> trainingSplit = fourSplit;
            case 5-> trainingSplit = fiveSplit;
            case 6-> trainingSplit = sixSplit;
            default -> trainingSplit =fourSplit;
        }

        double result = (double) days / (double) totalWorkouts;
        int whole = (int) result;
        double decimal = result - whole;

        decimal = Math.round(decimal * 100.0) / 100.0;

        LocalDate trainingDay = startDate;

        double decimalAccumulator = 0.0;

        List<TrainingEntity> trainings = new ArrayList<>();
        TrainingPlanEntity planEntity = new TrainingPlanEntity();

        for (int i = 0; i<totalWorkouts;i++){
            decimalAccumulator += decimal;

            int extraDay = 0;

            // Ak sme prekročili 1.0 v akumulátore, pridaj 1 deň a zníž akumulátor
            if (decimalAccumulator >= 1.0) {
                extraDay = 1;
                decimalAccumulator -= 1.0;
            }

            // Výsledný posun je celá časť + prípadne 1 navyše z desatinnej
            int daysToAdd = whole + extraDay;

            //Creating new entity inside for cycle
            TrainingEntity trainingEntity = new TrainingEntity();

            trainingEntity.setClient(client);
            trainingEntity.setUser(client.getOwner());
            trainingEntity.setDate(trainingDay);
            trainingEntity.setWorkout(chooseWorkout(trainingSplit));
            trainingEntity.setTime(time);
            trainingEntity.setPlan(planEntity);
            trainingEntity.setDone(false);

            trainings.add(trainingEntity);



            // Posuň dátum
            trainingDay = trainingDay.plusDays(daysToAdd);
        }


        planEntity.setTrainings(trainings);
        planEntity.setClient(client);
        planEntity.setUser(client.getOwner());
        planEntity.setStartDate(startDate);
        planEntity.setEndDate(endDate);
        planEntity.setTotalWorkouts(totalWorkouts);
        planEntity.setDone(false);
        planEntity.setDoneWorkouts(0);
        trainingPlanRepository.save(planEntity);
        trainingRepository.saveAll(trainings);

    }

    @Override
    public List<TrainingEntity> getTodayTrainings(UserEntity user){
        return trainingRepository.findAllByUserOrderByDateAsc(user)
                .stream()
                .filter(t->t.getDate().isEqual(LocalDate.now()))
                .filter(t-> !t.isDone())
                .toList();
    }

    @Override
    public List<String> getTodayTrainingsString(UserEntity user) {
        List<TrainingEntity> entities = getTodayTrainings(user);
        List<String> stringList = new ArrayList<>();
        String oneClient;

        for (TrainingEntity entity : entities){
            oneClient = entity.getClient().getFullName();
            oneClient += " " + entity.getTime().toString();
            stringList.add(oneClient);
        }

        return stringList;
    }

    @Override
    public void updateTraining(long trainingId, TrainingDTO trainingDTO) {
        TrainingEntity trainingEntity = trainingRepository.findById(trainingId).orElseThrow();
        TrainingPlanEntity plan = trainingEntity.getPlan();

        trainingEntity.setTime(trainingDTO.getTime());
        trainingEntity.setDone(trainingDTO.isDone());
        trainingEntity.setWorkout(trainingDTO.getWorkout());
        trainingEntity.setDate(trainingDTO.getDate());
        plan.updateProgress();

        trainingRepository.save(trainingEntity);
        trainingPlanRepository.save(plan);

    }

    @Override
    public TrainingDTO toDto(TrainingEntity entity) {
        TrainingDTO dto = new TrainingDTO();

        dto.setClient(entity.getClient());
        dto.setTrainingId(entity.getTrainingId());
        dto.setDate(entity.getDate());
        dto.setTime(entity.getTime());
        dto.setWorkout(entity.getWorkout());
        dto.setUser(entity.getUser());
        dto.setDone(entity.isDone());

        return dto;
    }
}
