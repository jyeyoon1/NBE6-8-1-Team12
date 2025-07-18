"use client";
import React, { useState, Suspense } from "react";
import { useSearchParams, useRouter } from "next/navigation";
import BankForm from "./components/bankForm/bankForm";
import CardForm from "./components/cardForm/cardForm";
import PgForm from "./components/pgForm/pgForm";

function PaymentGateway() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const paymentId = searchParams.get("id") ?? 3;
  const amount = searchParams.get("amount") ?? 20000;
  const optionType = searchParams.get("paymentOptionType") ?? "CARD";
  const optionName = searchParams.get("paymentOptionName") ?? "현대카드";

  const [paymentInfo, setPaymentInfo] = useState({});
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  const handleCancel = () => {
    router.back();
  };

  const handleGaytewayPayment = async () => {
    setIsLoading(true);
    setError('');

    let formattedPaymentInfo = "";
    if(optionType === "BANK") {
      formattedPaymentInfo = `예금주: ${paymentInfo.accountHolder}, 계좌번호: ${paymentInfo.accountNumber}`;
    }else if(optionType === "CARD") {
      formattedPaymentInfo = `카드번호: ${paymentInfo.cardNumber}, 유효기간: ${paymentInfo.expiryDate}, CVC: ${paymentInfo.cvc}`;
    }else if(optionType === "PG") {
      formattedPaymentInfo = `PG사: ${paymentInfo.pg}`;
    }
    
    try {
      const payload = {
        paymentInfo: formattedPaymentInfo,
      };

      const response = await fetch(`/api/v1/payments/${paymentId}/execute`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });
      const result = await response.json();
      if(result.resultCode.startsWith("200")) {
        alert("결제가 완료되었습니다.");
        //router.push('/purchase/success');
      }else {
        alert(result.msg);
      }
    } catch (error) {
      setError(error.msg);
    } finally {
      setIsLoading(false);
    }
  };


  const renderForm = () => {
    switch(optionType) {
      case "BANK":
        return <BankForm setPaymentInfo={setPaymentInfo} />;
      case "CARD":
        return <CardForm setPaymentInfo={setPaymentInfo} />;
      case "PG":
        return <PgForm paymentInfo={paymentInfo} />;
      default:
        return <div className="text-center">
          <p className="text-red-500 font-medium">지원하지 않는 결제 방식입니다.</p>
          <button onClick={handleCancel} className="mt-4 px-6 py-2 bg-gray-500 text-white rounded-lg font-semibold hover:bg-gray-600 transition-colors">
            취소하기
          </button>
        </div>;
    }
  };
  
  return (
    <div className="p-8 max-w-lg mx-auto">
      <h2 className="text-2xl font-bold mb-4">{optionName} 결제</h2>
      <p className="text-sm text-gray-500 mb-4">결제 금액: {amount}원</p>

      <div className="p-6 border rounded-lg bg-white min-h-[150px] flex items-center justify-center">
        {renderForm()}
      </div>

      {error && <p className="text-red-500 mb-4">{error}</p>}

      {['BANK', 'CARD', 'PG'].includes(optionType || '') && (
        <button onClick={handleGaytewayPayment} disabled={isLoading} className={`w-full mt-6 py-3 font-bold text-white bg-indigo-600 hover:bg-indigo-700 disabled:bg-gray-400
         rounded-lg ${optionType === 'PG' ? 'bg-green-600 hover:bg-green-700' : 'bg-indigo-600 hover:bg-indigo-700'}`}>
            {isLoading ? "결제 중..." : optionType === 'PG' ? "완료하기" : "결제하기"}
        </button>
      )}
    </div>
  );
}
export default function Page() {

  return (
    <>
      <Suspense fallback={<div>로딩 중...</div>}>
        <PaymentGateway />
      </Suspense>
    </>
  );
}