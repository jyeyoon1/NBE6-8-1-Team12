"use client";
import React, { useState, Suspense, useEffect } from "react";
import { useSearchParams, useRouter } from "next/navigation";
import BankForm from "../components/bankForm/bankForm";
import CardForm from "../components/cardForm/cardForm";
import PgForm from "../components/pgForm/pgForm";
import { PaymentData } from "../types/paymentData";

function PaymentGateway({ } ) {
  const router = useRouter();
  const searchParams = useSearchParams();

  const [paymentData, setPaymentData] = useState<PaymentData | null>(null);
  const [paymentInfo, setPaymentInfo] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');
  const [validationErrors, setValidationErrors] = useState({});
  const apiUrl = 'http://localhost:8080';

  useEffect(() => {
    try {
      const paymentDataString = searchParams.get('paymentData');
      
      if(!paymentDataString) {
        throw new Error("결제 정보가 올바르지 않습니다.");
      }
      const parsedData = JSON.parse(paymentDataString ?? `{
        "id": 2,
        "amount": 50000,
        "paymentOptionType": "PG",
        "paymentOptionName": "Toss"
      }`);
      setPaymentData({
        id: parseInt(parsedData.id),
        amount: parsedData.amount,
        optionType: parsedData.paymentOptionType,
        optionName: parsedData.paymentOptionName
      });
    } catch (err) {
      console.error("paymentData 오류", err);
      setError("결제 정보를 불러오는 중 오류가 발생했습니다.");
    }finally {
      setIsLoading(false);
    }
  }, [searchParams]);

  if(!paymentData) {
    return <div>결제 정보를 불러오는 중입니다...</div>;
  }
  const validatePaymentInfo = () => {
    const errors = {};
    if(paymentData.optionType === "BANK") {
      if(!paymentInfo.accountHolder) errors.accountHolder = "예금주명을 입력해주세요.";
      if(!paymentInfo.accountNumber) errors.accountNumber = "계좌번호를 입력해주세요.";
    }else if(paymentData.optionType === "CARD") {
      if(!paymentInfo.cardNumber) errors.cardNumber = "카드번호를 입력해주세요.";
      if(!paymentInfo.expiryDate) errors.expiryDate = "유효기간을 입력해주세요.";
      if(!paymentInfo.cvc) errors.cvc = "CVC를 입력해주세요.";
    }
    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  }

  const handleGaytewayPayment = async () => {
    setError('');
    if(!validatePaymentInfo()) return;

    setIsLoading(true);

    let formattedPaymentInfo = "";
    if(paymentData.optionType === "BANK") {
      formattedPaymentInfo = `예금주: ${paymentInfo.accountHolder}, 계좌번호: ${paymentInfo.accountNumber}`;
    }else if(paymentData.optionType === "CARD") {
      formattedPaymentInfo = `카드번호: ${paymentInfo.cardNumber}, 유효기간: ${paymentInfo.expiryDate}, CVC: ${paymentInfo.cvc}`;
    }else if(paymentData.optionType === "PG") {
      const tempApprovalCode = `PG_APPROVAL_${paymentData.id}_${Date.now()}`
      formattedPaymentInfo = `PG사: ${tempApprovalCode}`;
    }

    
    
    try {
      const payload = {
        paymentInfo: formattedPaymentInfo,
      };

      const response = await fetch(`${apiUrl}/api/v1/payments/${paymentData.id}/execute`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });
      const result = await response.json();
      if(result.resultCode.startsWith("200")) {
        alert("결제가 완료되었습니다.");
        router.push(`/purchase/lookup/detail?id=${result.data.purchaseId}&email=${encodeURIComponent(result.data.userEmail)}`);
      }else {
        throw new Error(result.msg);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message: "결제 중 오류가 발생했습니다.");
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancel = async () => {
    if(!confirm("결제를 취소하시겠습니까?")) return;
    setIsLoading(true);
    setError('');
    try {
      const response = await fetch(`${apiUrl}/api/v1/payments/${paymentData.id}`, {
        method: "DELETE",
      });
      const result = await response.json();
      if(result.resultCode.startsWith("200")) {
        alert("결제가 취소되었습니다.");
        router.push(`/purchase`);
      }else {
        throw new Error(result.msg);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : "결제 중 오류가 발생했습니다.");
    } finally {
      setIsLoading(false);
    }
  };

  const renderForm = () => {
    switch(paymentData.optionType) {
      case "BANK":
        return <BankForm setPaymentInfo={setPaymentInfo} validationErrors={validationErrors} />;
      case "CARD":
        return <CardForm setPaymentInfo={setPaymentInfo} validationErrors={validationErrors} />;
      case "PG":
        return <PgForm optionName={paymentData.optionName} />;
      default:
        return <div className="text-center">
          <p className="text-red-500 font-medium">지원하지 않는 결제 방식입니다.</p>
          <button onClick={handleCancel} className="mt-4 px-6 py-2 bg-gray-500 text-white rounded-lg font-semibold hover:bg-gray-600 transition-colors">
            취소하기
          </button>
        </div>;
    }
  };
  
  if(error) {
    return <div className="p-8 max-w-lg mx-auto">
      <p className="text-red-500 font-medium">{error}</p>
    </div>;
  }

  return (
    <div className="p-8 max-w-lg mx-auto">
      <h2 className="text-2xl font-bold mb-4">{paymentData.optionName} 결제 정보 입력</h2>
      <p className="text-sm text-gray-500 mb-4">결제 금액: {Number(paymentData.amount).toLocaleString()}원</p>

      <div className="p-6 border rounded-lg bg-white min-h-[150px] flex items-center justify-center">
        {renderForm()}
      </div>

      <div className="mt-6 space-y-3">
        {['BANK', 'CARD', 'PG'].includes(paymentData.optionType || '') && (
          <button onClick={handleGaytewayPayment} disabled={isLoading} className={`w-full mt-6 py-3 font-bold text-white bg-indigo-600 hover:bg-indigo-700 disabled:bg-gray-400
            rounded-lg ${paymentData.optionType === 'PG' ? 'bg-green-600 hover:bg-green-700' : 'bg-indigo-600 hover:bg-indigo-700'}`}>
              {isLoading ? "결제 중..." : paymentData.optionType === 'PG' ? "완료하기" : "결제하기"}
          </button>
        )}
        <button onClick={handleCancel} disabled={isLoading} className="w-full py-2 font-semibold text-gray-700 bg-gray-200 hover:bg-gray-300 disabled:bg-gray-300">
          취소하기
        </button>
      </div>
    </div>
  );
}
export default function Page() {

  return (
  <Suspense fallback={<div className="p-8 text-center">로딩 중...</div>}>
    <PaymentGateway />
  </Suspense>
  );
}