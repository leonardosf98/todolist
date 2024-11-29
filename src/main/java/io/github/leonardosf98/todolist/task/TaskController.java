package io.github.leonardosf98.todolist.task;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        try {
            LocalDateTime currrentDate = LocalDateTime.now();
            if (currrentDate.isAfter(taskModel.getStartAt())) {
                return ResponseEntity.badRequest().body("A data de início / término deve ser maior do que a data atual");
            } else if (taskModel.getEndAt().isBefore(taskModel.getStartAt())) {
                return ResponseEntity.badRequest().body("A data de início deve ser menor do que a data de término");
            }
            TaskModel taskToAdd = this.taskRepository.save(taskModel);
            taskModel.setIdUser((UUID) request.getAttribute("idUser"));
            return ResponseEntity.ok().body("Criado com sucesso\n" + taskToAdd);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
