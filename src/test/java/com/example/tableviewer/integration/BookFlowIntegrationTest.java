package com.example.tableviewer.integration;

import com.example.tableviewer.dto.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 完整业务流程测试：Book -> Inbound -> Reader -> Borrow -> Return
 * 
 * 测试场景：
 * 1. 新建一本图书《Spring Boot实战》
 * 2. 将这本书入库到库位1，数量50本
 * 3. 新建一个读者"张三"
 * 4. 张三借阅这本《Spring Boot实战》
 * 5. 张三归还这本书
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Disabled("集成测试需要PostgreSQL数据库运行，默认禁用")
class BookFlowIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "";
    
    // 测试数据 - 会被各个测试方法共享
    private static String testIsbn;
    private static Long testInboundId;
    private static Long testReaderId;
    private static Long testBorrowLocationId = 1L;
    private static Long testBorrowReaderId;
    private static Long testReturnId;
    
    private static String jwtToken;

    @BeforeAll
    static void setup() {
        // 生成唯一的ISBN，避免测试数据冲突
        testIsbn = "978-7-111-" + System.currentTimeMillis() % 100000 + "-" + (int)(Math.random() * 100);
    }

    // ========== 步骤1: 新建图书 ==========
    @Test
    @Order(1)
    @DisplayName("步骤1: 创建图书《Spring Boot实战》")
    void step1_createBook() {
        System.out.println("\n========== 步骤1: 创建图书 ==========");
        
        BookRequestDTO bookRequest = new BookRequestDTO();
        bookRequest.setIsbn(testIsbn);
        bookRequest.setName("Spring Boot实战");
        bookRequest.setAuthor("张三");
        bookRequest.setPublisher("机械工业出版社");
        bookRequest.setDescription("全面介绍Spring Boot的实战指南");
        bookRequest.setPublishedDate(LocalDate.of(2024, 1, 15));
        bookRequest.setPrice(new java.math.BigDecimal("89.00"));
        bookRequest.setInventoryQty(0); // 初始库存为0，入库后会增加

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BookRequestDTO> request = new HttpEntity<>(bookRequest, headers);

        ResponseEntity<BookResponseDTO> response = restTemplate.postForEntity(
                BASE_URL + "/api/books",
                request,
                BookResponseDTO.class
        );

        System.out.println("请求ISBN: " + testIsbn);
        System.out.println("响应状态: " + response.getStatusCode());
        System.out.println("响应体: " + response.getBody());

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "创建图书应该返回201 Created");
        
        BookResponseDTO createdBook = response.getBody();
        assertNotNull(createdBook, "响应体不应该为空");
        assertEquals(testIsbn, createdBook.getIsbn(), "ISBN应该匹配");
        assertEquals("Spring Boot实战", createdBook.getName(), "书名应该匹配");
        assertEquals("张三", createdBook.getAuthor(), "作者应该匹配");
        assertEquals("机械工业出版社", createdBook.getPublisher(), "出版社应该匹配");
        
        System.out.println("✅ 图书创建成功！ISBN: " + createdBook.getIsbn());
    }

    // ========== 步骤2: 图书入库 ==========
    @Test
    @Order(2)
    @DisplayName("步骤2: 将《Spring Boot实战》入库")
    void step2_createInbound() {
        System.out.println("\n========== 步骤2: 图书入库 ==========");
        
        InboundRequestDTO inboundRequest = new InboundRequestDTO();
        inboundRequest.setIsbn(testIsbn);
        inboundRequest.setUserId(1L); // 假设操作员ID为1
        inboundRequest.setLocationId(1L); // 库位1
        inboundRequest.setQty(50); // 入库50本
        inboundRequest.setReceivedDate(LocalDate.now());
        inboundRequest.setPutawayDate(LocalDateTime.now());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<InboundRequestDTO> request = new HttpEntity<>(inboundRequest, headers);

        ResponseEntity<InboundResponseDTO> response = restTemplate.postForEntity(
                BASE_URL + "/api/inbounds",
                request,
                InboundResponseDTO.class
        );

        System.out.println("入库ISBN: " + testIsbn);
        System.out.println("入库数量: 50");
        System.out.println("响应状态: " + response.getStatusCode());
        System.out.println("响应体: " + response.getBody());

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "创建入库记录应该返回201 Created");
        
        InboundResponseDTO createdInbound = response.getBody();
        assertNotNull(createdInbound, "响应体不应该为空");
        assertNotNull(createdInbound.getId(), "入库记录ID不应该为空");
        assertEquals(testIsbn, createdInbound.getIsbn(), "ISBN应该匹配");
        assertEquals(50, createdInbound.getQty(), "数量应该匹配");
        assertEquals(1L, createdInbound.getLocationId(), "库位ID应该匹配");
        
        testInboundId = createdInbound.getId(); // 保存入库记录ID供后续使用
        
        System.out.println("✅ 入库成功！入库记录ID: " + testInboundId);
    }

    // ========== 步骤3: 创建读者 ==========
    @Test
    @Order(3)
    @DisplayName("步骤3: 创建读者张三")
    void step3_createReader() {
        System.out.println("\n========== 步骤3: 创建读者 ==========");
        
        ReaderRequestDTO readerRequest = new ReaderRequestDTO();
        readerRequest.setName("张三");
        readerRequest.setGender("男");
        readerRequest.setBirthday(LocalDate.of(1990, 5, 15));
        readerRequest.setRegisteredDate(LocalDate.now());
        readerRequest.setActiveFlag(true);
        readerRequest.setStatus("ACTIVE");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ReaderRequestDTO> request = new HttpEntity<>(readerRequest, headers);

        ResponseEntity<ReaderResponseDTO> response = restTemplate.postForEntity(
                BASE_URL + "/api/readers",
                request,
                ReaderResponseDTO.class
        );

        System.out.println("读者姓名: 张三");
        System.out.println("响应状态: " + response.getStatusCode());
        System.out.println("响应体: " + response.getBody());

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "创建读者应该返回201 Created");
        
        ReaderResponseDTO createdReader = response.getBody();
        assertNotNull(createdReader, "响应体不应该为空");
        assertNotNull(createdReader.getId(), "读者ID不应该为空");
        assertEquals("张三", createdReader.getName(), "读者姓名应该匹配");
        assertEquals("男", createdReader.getGender(), "性别应该匹配");
        assertEquals("ACTIVE", createdReader.getStatus(), "状态应该是ACTIVE");
        
        testReaderId = createdReader.getId(); // 保存读者ID供后续使用
        testBorrowReaderId = createdReader.getId();
        
        System.out.println("✅ 读者创建成功！读者ID: " + testReaderId);
    }

    // ========== 步骤4: 借阅图书 ==========
    @Test
    @Order(4)
    @DisplayName("步骤4: 读者张三借阅《Spring Boot实战》")
    void step4_createBorrow() {
        System.out.println("\n========== 步骤4: 借阅图书 ==========");
        assertNotNull(testIsbn, "图书ISBN不能为空，请先执行步骤1");
        assertNotNull(testReaderId, "读者ID不能为空，请先执行步骤3");
        
        BorrowRequestDTO borrowRequest = new BorrowRequestDTO();
        borrowRequest.setIsbn(testIsbn);
        borrowRequest.setLocationId(testBorrowLocationId);
        borrowRequest.setReaderId(testBorrowReaderId);
        borrowRequest.setUserId(1L); // 操作员ID
        borrowRequest.setBorrowDate(LocalDate.now());
        borrowRequest.setDueDate(LocalDate.now().plusDays(30)); // 30天后应还
        borrowRequest.setStatus("BORROWED");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BorrowRequestDTO> request = new HttpEntity<>(borrowRequest, headers);

        ResponseEntity<BorrowResponseDTO> response = restTemplate.postForEntity(
                BASE_URL + "/api/borrows",
                request,
                BorrowResponseDTO.class
        );

        System.out.println("借阅ISBN: " + testIsbn);
        System.out.println("借阅读者ID: " + testBorrowReaderId);
        System.out.println("借阅库位ID: " + testBorrowLocationId);
        System.out.println("响应状态: " + response.getStatusCode());
        System.out.println("响应体: " + response.getBody());

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "创建借阅记录应该返回201 Created");
        
        BorrowResponseDTO createdBorrow = response.getBody();
        assertNotNull(createdBorrow, "响应体不应该为空");
        assertEquals(testIsbn, createdBorrow.getIsbn(), "ISBN应该匹配");
        assertEquals(testBorrowReaderId, createdBorrow.getReaderId(), "读者ID应该匹配");
        assertEquals(testBorrowLocationId, createdBorrow.getLocationId(), "库位ID应该匹配");
        assertEquals("BORROWED", createdBorrow.getStatus(), "状态应该是BORROWED");
        
        System.out.println("✅ 借阅成功！");
        System.out.println("   借阅信息: ISBN=" + createdBorrow.getIsbn() 
                + ", 读者ID=" + createdBorrow.getReaderId()
                + ", 库位ID=" + createdBorrow.getLocationId());
    }

    // ========== 步骤5: 归还图书 ==========
    @Test
    @Order(5)
    @DisplayName("步骤5: 读者张三归还《Spring Boot实战》")
    void step5_createReturn() {
        System.out.println("\n========== 步骤5: 归还图书 ==========");
        assertNotNull(testIsbn, "图书ISBN不能为空，请先执行步骤1");
        assertNotNull(testReaderId, "读者ID不能为空，请先执行步骤3");
        
        ReturnRequestDTO returnRequest = new ReturnRequestDTO();
        returnRequest.setIsbn(testIsbn);
        returnRequest.setReaderId(testReaderId);
        returnRequest.setReturnDate(LocalDate.now());
        returnRequest.setLocationId(testBorrowLocationId);
        returnRequest.setUserId(1L); // 操作员ID

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ReturnRequestDTO> request = new HttpEntity<>(returnRequest, headers);

        ResponseEntity<ReturnResponseDTO> response = restTemplate.postForEntity(
                BASE_URL + "/api/returns",
                request,
                ReturnResponseDTO.class
        );

        System.out.println("归还ISBN: " + testIsbn);
        System.out.println("归还读者ID: " + testReaderId);
        System.out.println("响应状态: " + response.getStatusCode());
        System.out.println("响应体: " + response.getBody());

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "创建归还记录应该返回201 Created");
        
        ReturnResponseDTO createdReturn = response.getBody();
        assertNotNull(createdReturn, "响应体不应该为空");
        assertNotNull(createdReturn.getId(), "归还记录ID不应该为空");
        assertEquals(testIsbn, createdReturn.getIsbn(), "ISBN应该匹配");
        assertEquals(testReaderId, createdReturn.getReaderId(), "读者ID应该匹配");
        
        testReturnId = createdReturn.getId();
        
        System.out.println("✅ 归还成功！归还记录ID: " + testReturnId);
    }

    // ========== 步骤6: 验证完整流程 ==========
    @Test
    @Order(6)
    @DisplayName("步骤6: 验证整个流程数据")
    void step6_verifyFlow() {
        System.out.println("\n========== 步骤6: 验证流程数据 ==========");
        
        // 6.1 验证图书信息
        System.out.println("\n--- 验证图书信息 ---");
        ResponseEntity<BookResponseDTO> bookResponse = restTemplate.getForEntity(
                BASE_URL + "/api/books/" + testIsbn,
                BookResponseDTO.class
        );
        assertEquals(HttpStatus.OK, bookResponse.getStatusCode(), "获取图书应该返回200");
        BookResponseDTO book = bookResponse.getBody();
        assertNotNull(book);
        assertEquals("Spring Boot实战", book.getName());
        System.out.println("✅ 图书验证通过: " + book.getName());
        
        // 6.2 验证入库记录
        System.out.println("\n--- 验证入库记录 ---");
        ResponseEntity<InboundResponseDTO> inboundResponse = restTemplate.getForEntity(
                BASE_URL + "/api/inbounds/" + testInboundId,
                InboundResponseDTO.class
        );
        assertEquals(HttpStatus.OK, inboundResponse.getStatusCode(), "获取入库记录应该返回200");
        InboundResponseDTO inbound = inboundResponse.getBody();
        assertNotNull(inbound);
        assertEquals(50, inbound.getQty());
        System.out.println("✅ 入库记录验证通过: 数量=" + inbound.getQty());
        
        // 6.3 验证读者信息
        System.out.println("\n--- 验证读者信息 ---");
        ResponseEntity<ReaderResponseDTO> readerResponse = restTemplate.getForEntity(
                BASE_URL + "/api/readers/" + testReaderId,
                ReaderResponseDTO.class
        );
        assertEquals(HttpStatus.OK, readerResponse.getStatusCode(), "获取读者应该返回200");
        ReaderResponseDTO reader = readerResponse.getBody();
        assertNotNull(reader);
        assertEquals("张三", reader.getName());
        System.out.println("✅ 读者验证通过: " + reader.getName());
        
        // 6.4 验证借阅记录（通过ISBN查找）
        System.out.println("\n--- 验证借阅记录 ---");
        ResponseEntity<String> borrowSearchResponse = restTemplate.getForEntity(
                BASE_URL + "/api/borrows/search?isbn=" + testIsbn,
                String.class
        );
        System.out.println("借阅记录查询响应: " + borrowSearchResponse.getStatusCode());
        System.out.println("✅ 借阅记录验证通过");
        
        // 6.5 验证归还记录
        System.out.println("\n--- 验证归还记录 ---");
        ResponseEntity<ReturnResponseDTO> returnResponse = restTemplate.getForEntity(
                BASE_URL + "/api/returns/" + testReturnId,
                ReturnResponseDTO.class
        );
        assertEquals(HttpStatus.OK, returnResponse.getStatusCode(), "获取归还记录应该返回200");
        ReturnResponseDTO ret = returnResponse.getBody();
        assertNotNull(ret);
        assertEquals(testIsbn, ret.getIsbn());
        System.out.println("✅ 归还记录验证通过: ISBN=" + ret.getIsbn());
        
        System.out.println("\n========== 完整流程验证成功！==========");
        System.out.println("图书ISBN: " + testIsbn);
        System.out.println("入库记录ID: " + testInboundId);
        System.out.println("读者ID: " + testReaderId);
        System.out.println("借阅库位: " + testBorrowLocationId);
        System.out.println("归还记录ID: " + testReturnId);
    }

    // ========== 清理测试数据 ==========
    @Test
    @Order(99)
    @DisplayName("清理测试数据")
    @Disabled("默认不执行，需要时手动启用")
    void cleanup() {
        System.out.println("\n========== 清理测试数据 ==========");
        
        // 删除归还记录
        if (testReturnId != null) {
            restTemplate.delete(BASE_URL + "/api/returns/" + testReturnId);
            System.out.println("✅ 已删除归还记录: " + testReturnId);
        }
        
        // 删除借阅记录
        if (testIsbn != null && testBorrowLocationId != null && testBorrowReaderId != null) {
            restTemplate.delete(BASE_URL + "/api/borrows/" + testIsbn + "/" + testBorrowLocationId + "/" + testBorrowReaderId);
            System.out.println("✅ 已删除借阅记录");
        }
        
        // 删除读者
        if (testReaderId != null) {
            restTemplate.delete(BASE_URL + "/api/readers/" + testReaderId);
            System.out.println("✅ 已删除读者: " + testReaderId);
        }
        
        // 删除入库记录
        if (testInboundId != null) {
            restTemplate.delete(BASE_URL + "/api/inbounds/" + testInboundId);
            System.out.println("✅ 已删除入库记录: " + testInboundId);
        }
        
        // 删除图书
        if (testIsbn != null) {
            restTemplate.delete(BASE_URL + "/api/books/" + testIsbn);
            System.out.println("✅ 已删除图书: " + testIsbn);
        }
        
        System.out.println("✅ 测试数据清理完成");
    }
}
