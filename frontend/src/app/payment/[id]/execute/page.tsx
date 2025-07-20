"use client";
import React, { useState, Suspense, useEffect } from "react";
import { useRouter, useParams } from "next/navigation";
import BankForm from "../../components/bankForm/bankForm";
import CardForm from "../../components/cardForm/cardForm";
import PgForm from "../../components/pgForm/pgForm";
import { PaymentData, PaymentOption, PaymentUpdateRequest } from "../../types/paymentData";
import OptionsForm from "@/payment/components/options/optionsForm";

function PaymentGateway({ }) {
  const router = useRouter();
  const params = useParams();

  const [viewMode, setViewMode] = useState('execute'); // execute, options
  const [paymentData, setPaymentData] = useState<PaymentData | null>(null);
  const [paymentInfo, setPaymentInfo] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');
  const [validationErrors, setValidationErrors] = useState({});
  const [isPaymentComplete, setIsPaymentComplete] = useState(false);
  const apiUrl = 'http://localhost:8080';

  useEffect(() => {

    const paymentId = parseInt(params.id as string);
    if (!paymentId) {
      throw new Error("결제 ID가 올바르지 않습니다.");
      setIsLoading(false);
      return;
    }
    const fetchPaymentData = async () => {
      setIsLoading(true);
      try {
        const response = await fetch(`${apiUrl}/api/v1/payments/${paymentId}`);
        if (response.ok) {
          const result = await response.json();
          console.log('result :', result);
          setPaymentData({
            id: result.id,
            amount: result.amount,
            optionType: result.paymentOptionType,
            optionName: result.paymentOptionName
          });
        } else {
          throw new Error("결제 정보를 불러올 수 없습니다.");
        }
      } catch (err) {
        console.error("paymentData 오류", err);
        setError("결제 정보를 불러오는 중 오류가 발생했습니다.");
      } finally {
        setIsLoading(false);
      }
    };

    fetchPaymentData();
  }, [params.id]);

  if (!paymentData) {
    return <div>결제 정보를 불러오는 중입니다...</div>;
  }

  const validatePaymentInfo = () => {
    const errors = {};
    if (paymentData.optionType === "BANK") {
      if (!paymentInfo.accountHolder) errors.accountHolder = "예금주명을 입력해주세요.";
      if (!paymentInfo.accountNumber) errors.accountNumber = "계좌번호를 입력해주세요.";
    } else if (paymentData.optionType === "CARD") {
      if (!paymentInfo.cardNumber) errors.cardNumber = "카드번호를 입력해주세요.";
      if (!paymentInfo.expiryDate) errors.expiryDate = "유효기간을 입력해주세요.";
      if (!paymentInfo.cvc) errors.cvc = "CVC를 입력해주세요.";
    }
    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  }

  const handleGaytewayPayment = async () => {
    setError('');
    if (!validatePaymentInfo()) return;

    setIsLoading(true);

    let formattedPaymentInfo = "";
    if (paymentData.optionType === "BANK") {
      formattedPaymentInfo = `예금주: ${paymentInfo.accountHolder}, 계좌번호: ${paymentInfo.accountNumber}`;
    } else if (paymentData.optionType === "CARD") {
      formattedPaymentInfo = `카드번호: ${paymentInfo.cardNumber}, 유효기간: ${paymentInfo.expiryDate}, CVC: ${paymentInfo.cvc}`;
    } else if (paymentData.optionType === "PG") {
      const tempApprovalCode = `PG_APPROVAL_${paymentData.id}_${Date.now()}`
      formattedPaymentInfo = `${paymentData.optionName}: ${tempApprovalCode}`;
    }



    try {
      const payload = {
        paymentInfo: formattedPaymentInfo,
      };

      console.log('payload :', payload);
      const response = await fetch(`${apiUrl}/api/v1/payments/${paymentData.id}/execute`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });
      const result = await response.json();
      if (result.resultCode.startsWith("200")) {
        setIsPaymentComplete(true);
        alert("결제가 완료되었습니다.");
        router.push(`/purchase/lookup/detail?id=${result.data.purchaseId}&email=${encodeURIComponent(result.data.userEmail)}`);
      } else {
        throw new Error(result.msg);
        router.back();
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : "결제 중 오류가 발생했습니다.");
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancel = async () => {
    if (!confirm("결제를 취소하시겠습니까?")) return;
    setIsLoading(true);
    setError('');
    try {
      const response = await fetch(`${apiUrl}/api/v1/payments/${paymentData.id}`, {
        method: "DELETE",
      });
      const result = await response.json();
      if (result.resultCode.startsWith("200")) {
        alert("결제가 취소되었습니다.");
      } else {
        throw new Error(result.msg);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : "결제 취소 중 오류가 발생했습니다.");
    } finally {
      setIsLoading(false);
      router.back();
    }
  };

  const handleUpdatePaymentOption = async (option: PaymentOption) => {
    setIsLoading(true);
    setError('');
    try {
      const payload: PaymentUpdateRequest = {
        paymentOptionId: option.id,
        paymentInfo: "",
        amount: paymentData.amount,
      };
      const response = await fetch(`${apiUrl}/api/v1/payments/${paymentData.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });
      const result = await response.json();
      if (result.resultCode.startsWith("200")) {
        alert("결제 수단이 변경되었습니다.");
        const updatedPaymentData = result.data;
        setPaymentData({
          id: updatedPaymentData.id,
          amount: updatedPaymentData.amount,
          optionType: updatedPaymentData.paymentOptionType,
          optionName: updatedPaymentData.paymentOptionName,
        });
        setPaymentInfo({});
        setValidationErrors({});
        setViewMode('execute');
      } else {
        throw new Error(result.msg);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : "결제 수단 변경 중 오류가 발생했습니다.");
    } finally {
      setIsLoading(false);
    }
  };

  const renderForm = () => {
    switch (paymentData.optionType) {
      case "BANK":
        return <BankForm paymentInfo={paymentInfo} setPaymentInfo={setPaymentInfo} validationErrors={validationErrors} />;
      case "CARD":
        return <CardForm paymentInfo={paymentInfo} setPaymentInfo={setPaymentInfo} validationErrors={validationErrors} />;
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

  if (isLoading) return <div className="p-8 max-w-lg mx-auto bg-gray-50 rounded-sm shadow-sm">처리 중입니다.</div>;
  if (error) {
    return <div className="p-8 max-w-lg mx-auto bg-gray-50 rounded-sm shadow-sm">
      <p className="text-red-500 font-medium">{error}</p>
      <button onClick={() => setError('')} className="mt-4 px-6 py-2 bg-gray-500 text-white rounded-lg font-semibold hover:bg-gray-600 transition-colors">
        확인
      </button>
    </div>;
  }
  if (!paymentData) return <div className="p-8 max-w-lg mx-auto bg-gray-50 rounded-sm shadow-sm">결제 정보를 불러오는 중입니다...</div>;

  return (
    <div className="p-8 max-w-lg mx-auto bg-gray-50 rounded-xl shadow-lg">
      <div className="p-6 border rounded-lg bg-white min-h-[250px] flex items-center justify-center">
        {viewMode === 'execute' ? (
          <div className="w-full">
            <h2 className="text-2xl font-bold mb-4">{paymentData.optionName} 결제 정보 입력</h2>
            <p className="text-sm text-gray-500 mb-4">결제 금액: {Number(paymentData.amount).toLocaleString()}원</p>
            <div className="p-6 border rounded-lg bg-white min-h-[150px] flex items-center justify-center">
              {renderForm()}
            </div>
          </div>
        ) : (
          <OptionsForm onUpdate={handleUpdatePaymentOption} onCancel={() => setViewMode('execute')} apiUrl={apiUrl} />
        )}
      </div>

      {viewMode === 'execute' && (
        <div className="mt-6 space-y-3">
          {['BANK', 'CARD', 'PG'].includes(paymentData.optionType || '') && (
            <button onClick={handleGaytewayPayment} disabled={isLoading || isPaymentComplete} className={`w-full mt-6 py-3 font-bold text-white 
            bg-indigo-600 hover:bg-indigo-700 disabled:bg-gray-400 rounded-lg 
            ${isPaymentComplete ? 'bg-gray-400 cursor-not-allowed' : paymentData.optionType === 'PG' ? 'bg-green-600 hover:bg-green-700' : 'bg-indigo-600 hover:bg-indigo-700'}`}>
              {isPaymentComplete ? "결제 완료" : isLoading ? "결제 중..." : paymentData.optionType === 'PG' ? "완료하기" : "결제하기"}
            </button>
          )}
          <button onClick={() => setViewMode('options')} disabled={isLoading || isPaymentComplete} className="w-full py-2 font-semibold text-gray-700 bg-gray-200 hover:bg-gray-300 disabled:cursor-not-allowed">
            결제 수단 변경
          </button>
          <button onClick={handleCancel} disabled={isLoading} className="w-full py-2 font-semibold text-gray-700 bg-gray-200 hover:bg-gray-300 disabled:bg-gray-300">
            결제 취소
          </button>
        </div>
      )}
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