package cl.sportmedics.ms_workout.service;

import cl.sportmedics.ms_workout.dto.WorkoutDTO;
import cl.sportmedics.ms_workout.model.Workout;
import cl.sportmedics.ms_workout.repository.WorkoutRepository;
import cl.sportmedics.ms_workout.service.IWorkoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class WorkoutServiceImpl implements IWorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Workout> findAll() {
        log.info("MS-WORKOUT [Service]: Solicitando listado completo de rutinas a la base de datos");
        return workoutRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Workout findById(Long id) {
        log.info("MS-WORKOUT [Service]: Buscando rutina con ID: {}", id);
        return workoutRepository.findById(id).orElseThrow(() -> {
            log.error("MS-WORKOUT [Service]: Error - Rutina con ID {} no encontrada", id);
            return new NoSuchElementException("La rutina solicitada no existe");
        });
    }

    @Override
    @Transactional
    public Workout save(WorkoutDTO dto) {
        log.info("MS-WORKOUT [Service]: Intentando registrar una nueva rutina: {}", dto.getName());
        
        if (workoutRepository.existsByNameIgnoreCase(dto.getName())) {
            log.error("MS-WORKOUT [Service]: Error - Ya existe una rutina con el nombre: {}", dto.getName());
            throw new IllegalArgumentException("El nombre de la rutina ya se encuentra registrado");
        }

        Workout workout = Workout.builder()
                .name(dto.getName())
                .objective(dto.getObjective())
                .series(dto.getSeries())
                .repetitions(dto.getRepetitions())
                .level(dto.getLevel())
                .build();

        Workout guardado = workoutRepository.save(workout);
        log.info("MS-WORKOUT [Service]: Rutina guardada exitosamente con ID: {}", guardado.getId());
        return guardado;
    }

    @Override
    @Transactional
    public Workout update(Long id, WorkoutDTO dto) {
        log.info("MS-WORKOUT [Service]: Intentando actualizar la rutina con ID: {}", id);
        Workout existente = findById(id);

        existente.setName(dto.getName());
        existente.setObjective(dto.getObjective());
        existente.setSeries(dto.getSeries());
        existente.setRepetitions(dto.getRepetitions());
        existente.setLevel(dto.getLevel());

        Workout actualizado = workoutRepository.save(existente);
        log.info("MS-WORKOUT [Service]: Rutina con ID {} modificada correctamente", id);
        return actualizado;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.warn("MS-WORKOUT [Service]: Petición para eliminar rutina con ID: {}", id);
        Workout existente = findById(id);
        workoutRepository.delete(existente);
        log.info("MS-WORKOUT [Service]: Rutina con ID {} eliminada físicamente", id);
    }
}
