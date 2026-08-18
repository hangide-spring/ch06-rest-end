package com.metacoding.blog.board;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Controller가 아니라 @RestController — 반환값이 뷰 이름이 아니라 응답 본문(JSON)이 된다
@RestController
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/boards")
    public ResponseEntity<List<Board>> list() {
        System.out.println("GET /boards 요청 → 목록 JSON 응답");
        return ResponseEntity.ok(boardService.findAll()); // 200
    }

    @GetMapping("/boards/{id}")
    public ResponseEntity<Board> detail(@PathVariable("id") int id) {
        System.out.println("GET /boards/" + id + " 요청 → 상세 JSON 응답");
        return ResponseEntity.ok(boardService.findById(id)); // 200
    }

    @PostMapping("/boards")
    public ResponseEntity<Board> save(@RequestBody Board board) {
        System.out.println("POST /boards 요청 → title: " + board.getTitle());
        Board saved = boardService.save(board.getTitle(), board.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved); // 201 — 자원이 새로 만들어졌다
    }

    @PutMapping("/boards/{id}")
    public ResponseEntity<Board> update(@PathVariable("id") int id, @RequestBody Board board) {
        System.out.println("PUT /boards/" + id + " 요청 → title: " + board.getTitle());
        return ResponseEntity.ok(boardService.update(id, board.getTitle(), board.getContent())); // 200
    }

    @DeleteMapping("/boards/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        System.out.println("DELETE /boards/" + id + " 요청");
        boardService.delete(id);
        return ResponseEntity.ok().build(); // 200 — 본문 없이 상태만
    }
}
