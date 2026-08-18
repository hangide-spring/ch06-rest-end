package com.metacoding.blog.board;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest; // 스프링 부트 4의 새 패키지 위치
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// 웹 계층만 잘라 검증하는 슬라이스 테스트 — Service·Repository·DB는 로드되지 않는다
@WebMvcTest(BoardController.class)
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc; // 서버를 띄우지 않고 가짜 요청을 흘려보내는 도구

    @MockitoBean // 컨트롤러가 의존하는 서비스를 가짜 객체로 대체한다 (구 @MockBean — 스프링 부트 4에서 교체됨)
    private BoardService boardService;

    @Test
    public void list_test() throws Exception {
        // given — 가짜 서비스가 돌려줄 데이터를 준비한다
        Board board = Board.builder().id(1).title("제목1").content("내용1").build();
        given(boardService.findAll()).willReturn(List.of(board));

        // when & then — 상태 코드와 핵심 필드만 얇게 검증한다
        mvc.perform(get("/boards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("제목1"));
    }

    @Test
    public void save_test() throws Exception {
        // given
        Board saved = Board.builder().id(4).title("새글제목").content("새글내용").build();
        given(boardService.save(anyString(), anyString())).willReturn(saved);

        // when & then — JSON 본문은 Content-Type: application/json 이어야 한다
        mvc.perform(post("/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title":"새글제목","content":"새글내용"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4));
    }
}
