package cl.sportmedics.ms_workout.service;


import cl.sportmedics.ms_workout.dto.WorkoutDetailDTO;
import cl.sportmedics.ms_workout.dto.WorkoutRequestDTO;
import cl.sportmedics.ms_workout.dto.WorkoutResponseDTO;
import cl.sportmedics.ms_workout.entity.Workout;
import cl.sportmedics.ms_workout.entity.WorkoutDetail;
import cl.sportmedics.ms_workout.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository repository;

    @Override
    @Transactional
    public WorkoutResponseDTO create(WorkoutRequestDTO dto) {
        log.info("Creando nuevo entrenamiento para el miembro ID: {}", dto.getMemberId());

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new RuntimeException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        Workout workout = Workout.builder()
                .memberId(dto.getMemberId())
                .teacherId(dto.getTeacherId())
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .active(dto.getActive())
                .build();

        if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
            for (WorkoutDetailDTO detailDTO : dto.getDetails()) {
                WorkoutDetail detail = WorkoutDetail.builder()
                        .exerciseName(detailDTO.getExerciseName())
                        .muscleGroup(detailDTO.getMuscleGroup())
                        .sets(detailDTO.getSets())
                        .reps(detailDTO.getReps())
                        .restSeconds(detailDTO.getRestSeconds())
                        .dayOfWeek(detailDTO.getDayOfWeek().toUpperCase())
                        .build();
                workout.addDetail(detail); 
            }
        }

        Workout saved = repository.save(workout);
        log.info("Entrenamiento ID: {} creado con éxito con {} ejercicios.", saved.getId(), saved.getDetails().size());
        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutResponseDTO> getAll() {
        log.info("Consultando todos los entrenamientos.");
        return repository.findAll().stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public WorkoutResponseDTO getById(Long id) {
        log.info("Consultando entrenamiento ID: {}", id);
        return repository.findById(id).map(this::mapToResponseDTO)
                .orElseThrow(() -> new RuntimeException("Entrenamiento no encontrado."));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Eliminando entrenamiento ID: {}", id);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Entrenamiento no encontrado.");
        }
        repository.deleteById(id);
    }

    private WorkoutResponseDTO mapToResponseDTO(Workout workout) {
        WorkoutResponseDTO dto = new WorkoutResponseDTO();
        dto.setId(workout.getId());
        dto.setMemberId(workout.getMemberId());
        dto.setTeacherId(workout.getTeacherId());
        dto.setName(workout.getName());
        dto.setStartDate(workout.getStartDate());
        dto.setEndDate(workout.getEndDate());
        dto.setActive(workout.getActive());

        List<WorkoutDetailDTO> detailDTOs = workout.getDetails().stream().map(detail -> {
            WorkoutDetailDTO detailDTO = new WorkoutDetailDTO();
            detailDTO.setExerciseName(detail.getExerciseName());
            detailDTO.setMuscleGroup(detail.getMuscleGroup());
            detailDTO.setSets(detail.getSets());
            detailDTO.setReps(detail.getReps());
            detailDTO.setRestSeconds(detail.getRestSeconds());
            detailDTO.setDayOfWeek(detail.getDayOfWeek());
            return detailDTO;
        }).collect(Collectors.toList());

        dto.setDetails(detailDTOs);
        return dto;
    }
}