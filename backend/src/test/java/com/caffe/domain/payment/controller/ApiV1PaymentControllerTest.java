package com.caffe.domain.payment.controller;

import com.caffe.domain.payment.dto.PaymentOptionDto;
import com.caffe.domain.payment.entity.Payment;
import com.caffe.domain.payment.entity.PaymentOption;
import com.caffe.domain.payment.entity.PaymentStatus;
import com.caffe.domain.payment.service.PaymentService;
import com.caffe.domain.purchase.entity.Purchase;
import com.caffe.domain.purchase.service.PurchaseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ApiV1PaymentControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PurchaseService purchaseService;

    @Test
    @DisplayName("결제 생성")
    void t1() throws Exception {
        Purchase purchase = purchaseService.getPurchaseById(3);
        PaymentOption paymentOption = paymentService.getPaymentOption(4).get();

        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/payments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "purchaseId": %d,
                                            "paymentOptionId": %d,
                                            "amount": %d
                                          }
                                          """.formatted(purchase.getId(), paymentOption.getId(), (int)purchase.getTotalPrice()))
                )
                .andDo(print());

        Payment payment = paymentService.findLatest().get();
        resultActions
                .andExpect(handler().handlerType(ApiV1PaymentController.class))
                .andExpect(handler().methodName("request"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultCode").value("201-1"))
                .andExpect(jsonPath("$.msg").value("결제번호 %d 가 생성되었습니다.".formatted(payment.getId())))
                .andExpect(jsonPath("$.data.id").value(payment.getId()))
                .andExpect(jsonPath("$.data.paymentOptionType").value(paymentOption.getParent().getName()))
                .andExpect(jsonPath("$.data.paymentOptionName").value(paymentOption.getName()))
                .andExpect(jsonPath("$.data.amount").value(payment.getAmount()))
                .andExpect(jsonPath("$.data.status").value(String.valueOf(payment.getStatus())))
                .andExpect(jsonPath("$.data.createDate").value(Matchers.startsWith(payment.getCreateDate().toString().substring(0,18))))
                .andExpect(jsonPath("$.data.modifyDate").value(Matchers.startsWith(payment.getModifyDate().toString().substring(0,18))))
                .andExpect(jsonPath("$.data.purchaseId").value(purchase.getId()));
    }

    @Test
    @DisplayName("결제 요청")
    void t2() throws Exception {
        Payment payment = paymentService.findLatest().get();
        ResultActions resultActions = mvc
                .perform(
                        put("/api/v1/payments/%d/execute".formatted(payment.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "paymentInfo": "111-111-1110111011"
                                        }
                                          """)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(ApiV1PaymentController.class))
                .andExpect(handler().methodName("request"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("주문번호 %d의 결제가 성공했습니다.".formatted(payment.getPurchase().getId())))
                .andExpect(jsonPath("$.data.id").value(payment.getId()))
                .andExpect(jsonPath("$.data.paymentOptionType").value(payment.getPaymentOption().getParent().getName()))
                .andExpect(jsonPath("$.data.paymentOptionName").value(payment.getPaymentOption().getName()))
                .andExpect(jsonPath("$.data.paymentInfo").value("111-111-1110111011"))
                .andExpect(jsonPath("$.data.amount").value(payment.getAmount()))
                .andExpect(jsonPath("$.data.status").value(String.valueOf(PaymentStatus.SUCCESS)))
                .andExpect(jsonPath("$.data.createDate").value(Matchers.startsWith(payment.getCreateDate().toString().substring(0,18))))
                .andExpect(jsonPath("$.data.modifyDate").value(Matchers.startsWith(payment.getModifyDate().toString().substring(0,18))))
                .andExpect(jsonPath("$.data.purchaseId").value(payment.getPurchase().getId()));
    }

    @Test
    @DisplayName("결제 수정 요청")
    void t3() throws Exception {
        int id = 1;
        PaymentOption paymentOption = paymentService.getPaymentOption(8).get();

        ResultActions resultActions = mvc
                .perform(
                        put("/api/v1/payments/"+id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "paymentOptionId": %d,
                                            "paymentInfo": "901823-123984-231-2122",
                                            "amount": %d
                                          }
                                          """.formatted(paymentOption.getId(), 15000))
                )
                .andDo(print());

        Payment payment = paymentService.findById(id).get();
        resultActions
                .andExpect(handler().handlerType(ApiV1PaymentController.class))
                .andExpect(handler().methodName("update"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultCode").value("201-1"))
                .andExpect(jsonPath("$.msg").value("주문번호 %d의 결제가 성공했습니다.".formatted(payment.getPurchase().getId())))
                .andExpect(jsonPath("$.data.id").value(id))
                .andExpect(jsonPath("$.data.paymentOptionType").value(paymentOption.getParent().getName()))
                .andExpect(jsonPath("$.data.paymentOptionName").value(paymentOption.getName()))
                .andExpect(jsonPath("$.data.paymentInfo").value("901823-123984-231-2122"))
                .andExpect(jsonPath("$.data.amount").value(15000))
                .andExpect(jsonPath("$.data.status").value(String.valueOf(payment.getStatus())))
                .andExpect(jsonPath("$.data.createDate").value(Matchers.startsWith(payment.getCreateDate().toString().substring(0,18))))
                .andExpect(jsonPath("$.data.modifyDate").value(Matchers.startsWith(payment.getModifyDate().toString().substring(0,18))))
                .andExpect(jsonPath("$.data.purchaseId").value(payment.getPurchase().getId()));
    }

    @Test
    @DisplayName("결제 삭제")
    void t4() throws Exception {
        int id = 1;
        ResultActions resultActions = mvc
                .perform(
                        delete("/api/v1/payments/"+id)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(ApiV1PaymentController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("결제번호 %d 가 삭제되었습니다.".formatted(id)));
    }

    @Test
    @DisplayName("결제 취소")
    void t5() throws Exception {
        int id = 1;
        ResultActions resultActions = mvc
                .perform(
                        put("/api/v1/payments/%d/cancel".formatted(id))
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(ApiV1PaymentController.class))
                .andExpect(handler().methodName("cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("결제번호 %d 가 취소되었습니다.".formatted(id)));
    }

    @Test
    @DisplayName("결제 단건 조회")
    void t6() throws Exception {
        int id = 2;
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/payments/"+id)
                )
                .andDo(print());

        Payment payment = paymentService.findById(id).get();
        resultActions
                .andExpect(handler().handlerType(ApiV1PaymentController.class))
                .andExpect(handler().methodName("getOne"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(payment.getId()))
                .andExpect(jsonPath("$.paymentOptionType").value(payment.getPaymentOption().getParent().getName()))
                .andExpect(jsonPath("$.paymentOptionName").value(payment.getPaymentOption().getName()))
                .andExpect(jsonPath("$.paymentInfo").value(payment.getPaymentInfo()))
                .andExpect(jsonPath("$.amount").value(payment.getAmount()))
                .andExpect(jsonPath("$.status").value(String.valueOf(payment.getStatus())))
                .andExpect(jsonPath("$.createDate").value(Matchers.startsWith(payment.getCreateDate().toString().substring(0,18))))
                .andExpect(jsonPath("$.modifyDate").value(Matchers.startsWith(payment.getModifyDate().toString().substring(0,18))))
                .andExpect(jsonPath("$.purchaseId").value(payment.getPurchase().getId()));
    }

    @Test
    @DisplayName("결제 단건 실패")
    void t7() throws Exception {
        int id = Integer.MAX_VALUE;
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/payments/"+id)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(ApiV1PaymentController.class))
                .andExpect(handler().methodName("getOne"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.resultCode").value("404-1"))
                .andExpect(jsonPath("$.msg").value("No value present"));
    }

    @Test
    @DisplayName("결제 다건 조회")
    void t8() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/payments")
                )
                .andDo(print());

        List<Payment> payments = paymentService.getAll();

        resultActions
                .andExpect(handler().handlerType(ApiV1PaymentController.class))
                .andExpect(handler().methodName("getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(payments.size()));

        for(int i=0;i<payments.size();i++) {
            Payment payment = payments.get(i);
            resultActions
                    .andExpect(jsonPath("$[%d].id".formatted(i)).value(payment.getId()))
                    .andExpect(jsonPath("$[%d].paymentOptionType".formatted(i)).value(payment.getPaymentOption().getParent().getName()))
                    .andExpect(jsonPath("$[%d].paymentOptionName".formatted(i)).value(payment.getPaymentOption().getName()))
                    .andExpect(jsonPath("$[%d].paymentInfo".formatted(i)).value(payment.getPaymentInfo()))
                    .andExpect(jsonPath("$[%d].amount".formatted(i)).value(payment.getAmount()))
                    .andExpect(jsonPath("$[%d].status".formatted(i)).value(String.valueOf(payment.getStatus())))
                    .andExpect(jsonPath("$[%d].createDate".formatted(i)).value(Matchers.startsWith(payment.getCreateDate().toString().substring(0,18))))
                    .andExpect(jsonPath("$[%d].modifyDate".formatted(i)).value(Matchers.startsWith(payment.getModifyDate().toString().substring(0,18))))
                    .andExpect(jsonPath("$[%d].purchaseId".formatted(i)).value(payment.getPurchase().getId()));
        }
    }

    @Test
    @DisplayName("결제 방법 조회")
    void t9() throws Exception {
        int optionParentId = 1;
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/payments/options/"+optionParentId)
                )
                .andDo(print());

        List<PaymentOptionDto> paymentOptions = paymentService.getDetailPaymentOptions(optionParentId);

        resultActions
                .andExpect(handler().handlerType(ApiV1PaymentController.class))
                .andExpect(handler().methodName("getDetailPaymentOptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(paymentOptions.size()));

        for(int i=0;i<paymentOptions.size();i++) {
            PaymentOptionDto paymentOptionDto = paymentOptions.get(i);
            resultActions
                    .andExpect(jsonPath("$[%d].id".formatted(i)).value(paymentOptionDto.id()))
                    .andExpect(jsonPath("$[%d].parentId".formatted(i)).value(paymentOptionDto.parentId()))
                    .andExpect(jsonPath("$[%d].name".formatted(i)).value(paymentOptionDto.name()))
                    .andExpect(jsonPath("$[%d].type".formatted(i)).value(String.valueOf(paymentOptionDto.type())))
                    .andExpect(jsonPath("$[%d].code".formatted(i)).value(paymentOptionDto.code()))
                    .andExpect(jsonPath("$[%d].sortSeq".formatted(i)).value(paymentOptionDto.sortSeq()));
        }
    }

    @Test
    @DisplayName("결제 요청 - 409")
    void t10() throws Exception {
        int id = 1;
        Payment payment = paymentService.findById(1).get();
        payment.isSuccess(true);
        ResultActions resultActions = mvc
                .perform(
                        put("/api/v1/payments/%d/execute".formatted(1))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "paymentInfo": "111-111-1110111011"
                                        }
                                          """)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(ApiV1PaymentController.class))
                .andExpect(handler().methodName("request"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.resultCode").value("409-1"))
                .andExpect(jsonPath("$.msg").value("결제번호 %d 는 이미 결제되었습니다.".formatted(payment.getId())));
    }
}
