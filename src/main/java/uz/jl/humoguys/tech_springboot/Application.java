package uz.jl.humoguys.tech_springboot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@Controller
public class Application {

    private final List<Todo> TODOS = new ArrayList<>();

    {
        TODOS.add(Todo.builder().title("Read a book").build());
        TODOS.add(Todo.builder().title("Learn HTML").build());
        TODOS.add(Todo.builder().title("Learn CSS").build());
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String hello(Model model) {
        LocalTime localTime = LocalTime.now();
        model.addAttribute("time", localTime);
        model.addAttribute("name", "Elmurodov Javohir");
        return "index";
    }


    @RequestMapping(value = "/todo", method = RequestMethod.GET)
    public String todoListPage(Model model) {
        model.addAttribute("todos", TODOS);
        return "todo/todo_list";
    }

    @RequestMapping(value = "/todo/create", method = RequestMethod.GET)
    public String todoCreatePage() {
        return "todo/todo_create";
    }

    @RequestMapping(value = "/todo/create", method = RequestMethod.POST)
    public String todoCreate(@ModelAttribute Todo todo) {
        TODOS.add(todo);
        return "redirect:/todo";
    }

    @RequestMapping(value = "/todo/delete/{id}", method = RequestMethod.GET)
    public String todoDeletePage(Model model, @PathVariable String id) {
        Todo deletingTodo = TODOS.stream().filter(todo -> todo.getId().equals(id)).findFirst().get();
        model.addAttribute("todo", deletingTodo);
        return "todo/todo_delete";
    }

    @RequestMapping(value = "/todo/delete/{id}", method = RequestMethod.POST)
    public String todoDelete(@PathVariable String id) {
        TODOS.removeIf(todo->todo.getId().equals(id));
        return "redirect:/todo";
    }

    @RequestMapping(value = "/todo/update/{id}", method = RequestMethod.GET)
    public String todoUpdatePage(@PathVariable String id, Model model) {
        Todo searchingTodo = TODOS.stream().filter(todo -> todo.getId().equals(id)).findFirst().get();
        model.addAttribute("todo", searchingTodo);
        return "todo/todo_update";
    }

    @RequestMapping(value = "/todo/update/{id}", method = RequestMethod.POST)
    public String todoUpdate(@PathVariable String id, @ModelAttribute Todo inputTodo) {
        Todo t = TODOS.stream().filter(todo -> todo.getId().equals(id)).findFirst().get();
        t.setTitle(inputTodo.getTitle());
        t.setDone(inputTodo.isDone());
        return "redirect:/todo";
    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Todo {
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String title;
    private boolean done;
}
