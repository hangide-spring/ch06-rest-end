package com.metacoding.blog.board;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service // 비즈니스 로직을 처리하는 계층 — 지금은 위임이 대부분이고, 존재 이유는 트랜잭션과 검증이 들어올 때 본격화된다
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public Board findById(Integer id) {
        Board board = boardRepository.findById(id);
        if (board == null) {
            // 상태코드별 실패 응답 정리는 9차시에서 — 지금은 전부 RuntimeException(500)으로 던져 둔다
            throw new RuntimeException("게시글을 찾을 수 없습니다 : " + id);
        }
        return board;
    }

    @Transactional // 쓰기 작업은 트랜잭션 안에서 — 원리는 트랜잭션을 배울 때 다룬다
    public Board save(String title, String content) {
        Board board = Board.builder().title(title).content(content).build();
        boardRepository.save(board);
        return board; // persist 즉시 INSERT(IDENTITY)라 id가 채워진 채 돌아간다
    }

    @Transactional
    public Board update(Integer id, String title, String content) {
        Board board = findById(id);
        board.update(title, content); // 저장 메서드 없음 — 트랜잭션이 끝날 때 더티 체킹이 UPDATE를 만든다
        return board;
    }

    @Transactional
    public void delete(Integer id) {
        Board board = findById(id);
        boardRepository.delete(board);
    }
}
