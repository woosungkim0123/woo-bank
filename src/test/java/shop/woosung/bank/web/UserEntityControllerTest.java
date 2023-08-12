package shop.woosung.bank.web;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import shop.woosung.bank.util.dummy.DummyUserObject;

@Sql("classpath:sql/initAutoIncrementReset.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UserEntityControllerTest extends DummyUserObject {
//
//    @Autowired
//    private MockMvc mvc;
//    @Autowired
//    private UserJpaRepository userJpaRepository;
//    @Autowired
//    private ObjectMapper om;
//    @Autowired
//    private EntityManager em;
//
//    @BeforeEach
//    public void setUp() {
//        userJpaRepository.save(newUser("test1", "1234", "test1@naver.com", "테스터일", UserRole.CUSTOMER));
//        em.clear();
//    }
//
//    @Test
//    public void join_success_test() throws Exception {
//        // given
//        JoinReqDto joinReqDto = new JoinReqDto();
//        joinReqDto.setUsername("test2");
//        joinReqDto.setPassword("1234");
//        joinReqDto.setEmail("test2@naver.com");
//        joinReqDto.setFullname("테스트이");
//        String requestBody = om.writeValueAsString(joinReqDto);
//
//        // when
//        ResultActions resultActions = mvc.perform(
//                post("/api/join")
//                        .content(requestBody)
//                        .contentType(MediaType.APPLICATION_JSON)
//        );
//        // then
//        resultActions.andExpect(status().isCreated());
//    }
//
//    @Test
//    public void join_fail_test() throws Exception {
//        // given
//        JoinReqDto joinReqDto = new JoinReqDto();
//        joinReqDto.setUsername("test1");
//        joinReqDto.setPassword("1234");
//        joinReqDto.setEmail("test2@naver.com");
//        joinReqDto.setFullname("테스트일");
//        String requestBody = om.writeValueAsString(joinReqDto);
//
//        // when
//        ResultActions resultActions = mvc.perform(
//                post("/api/join")
//                        .content(requestBody)
//                        .contentType(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isBadRequest());
//    }

}