package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tarefas")
public class Controller {
    
    @Autowired
    private TarefaRepository tarefaRepository;

    @GetMapping
    public ResponseEntity<Object> listaTarefas(){
        return ResponseEntity.status(200).body(
            tarefaRepository.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> retornaTarefa(@PathVariable("id") Long id){
        return ResponseEntity.status(200).body(
            tarefaRepository.findById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Object> salvaTarefa(@RequestBody Tarefa tarefa){
        return ResponseEntity.status(201).body(
            tarefaRepository.save(tarefa)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editTarefa(@RequestBody Tarefa tarefa, @PathVariable("id") Long id){
        tarefa.setId(id);
        return ResponseEntity.status(200).body(
            tarefaRepository.save(tarefa)
        );
    }    

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletaTarefa(@PathVariable("id") Long id){
        tarefaRepository.deleteById(id);
        return ResponseEntity.status(200).body(
            "Tarefa deletada"
        );
    }      
}