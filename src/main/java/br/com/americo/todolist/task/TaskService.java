package br.com.americo.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.americo.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	public ResponseEntity saveTask(TaskModel taskModel, HttpServletRequest request) {

		var idUser = request.getAttribute("idUser");
		taskModel.setIdUser((UUID) idUser);

		var currentDate = LocalDateTime.now();
		if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("A data de início / data de término deve ser maior que a data atual");
		}

		if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("A data de início deve ser menor que a data de término");
		}

		TaskModel task = taskRepository.save(taskModel);
		return ResponseEntity.status(HttpStatus.OK).body(task);
	}

	public ResponseEntity updateTask(@RequestBody TaskModel taskModel, @PathVariable UUID id,
			HttpServletRequest request) {

		var task = this.taskRepository.findById(id).orElse(null);

		if (task == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
		}

		var idUser = request.getAttribute("idUser");

		if (!task.getIdUser().equals(idUser)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Usuário não tem permissão para alterar está tarefa");
		}

		Utils.CopyNonNullProperties(taskModel, task);
		var taskUpdate = this.taskRepository.save(task);
		return ResponseEntity.ok().body(taskUpdate);
	}

}
