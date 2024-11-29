package io.github.leonardosf98.todolist.task;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        try {
            LocalDateTime currrentDate = LocalDateTime.now();
            if (currrentDate.isAfter(taskModel.getStartAt())) {
                return ResponseEntity.badRequest().build();
            }
            TaskModel taskToAdd = this.taskRepository.save(taskModel);
            taskModel.setIdUser((UUID) request.getAttribute("idUser"));
            return ResponseEntity.ok().body("Criado com sucesso" + taskToAdd);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
