package io.github.leonardosf98.todolist.task;

import io.github.leonardosf98.todolist.utils.Utils;
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
            return ResponseEntity.ok().body(taskToAdd);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
        var idUser = (UUID) request.getAttribute("idUser");
        var taskToEdit = this.taskRepository.findById(id);
        System.out.println(taskToEdit);
        if (idUser.equals(taskModel.getIdUser())) {
            try {
                if (taskToEdit == null) {
                    return ResponseEntity.badRequest().body("Tarefa não encontrada");
                }
                Utils.copyNonNullProperties(taskModel, taskToEdit);
                taskModel.setId(id);
                this.taskRepository.save(taskModel);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        }
        return ResponseEntity.status(401).body("Usuário não autorizado para esta ação.");
    }
}
