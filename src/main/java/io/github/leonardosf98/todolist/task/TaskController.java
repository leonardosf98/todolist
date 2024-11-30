package io.github.leonardosf98.todolist.task;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @GetMapping("/")
    public List<TaskModel> listALl(HttpServletRequest request) {
        var userId = request.getAttribute("idUser");
        return this.taskRepository.findByIdUser((UUID) userId);
    }

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        try {
            LocalDateTime currrentDate = LocalDateTime.now();
            if (currrentDate.isAfter(taskModel.getStartAt())) {
                return ResponseEntity.badRequest().body("A data de início / término deve ser maior do que a data atual");
            } else if (taskModel.getEndAt().isBefore(taskModel.getStartAt())) {
                return ResponseEntity.badRequest().body("A data de início deve ser menor do que a data de término");
            }
            taskModel.setIdUser((UUID) request.getAttribute("idUser"));
            TaskModel taskToAdd = this.taskRepository.save(taskModel);
            return ResponseEntity.ok().body("Criado com sucesso\n" + taskToAdd);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
