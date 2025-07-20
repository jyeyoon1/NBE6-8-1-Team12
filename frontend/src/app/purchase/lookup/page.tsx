'use client';

import { useRouter } from 'next/navigation';

import { PurchaserReqBody } from '@/purchase/types/purchase-request';
import { PurchaseLookupResBody, ServerResponse } from '@/purchase/types/purchase-response';

export default function PurchaseLookUpPage() {
  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const form = e.target as HTMLFormElement;

    const userEmailInput = form.elements.namedItem("userEmail") as HTMLInputElement;
    const purchaseIdInput = form.elements.namedItem("purchaseId") as HTMLInputElement;

    userEmailInput.value = userEmailInput.value.trim();
    if (userEmailInput.value.length === 0) {
      alert("이메일을 입력해주세요.");
      userEmailInput.focus();
      return;
    }

    // 이메일 형식 유효성 검증
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(userEmailInput.value)) {
      alert("올바른 이메일 형식을 입력해주세요.");
      userEmailInput.focus();
      return;
    }

    purchaseIdInput.value = purchaseIdInput.value.trim();
    if (purchaseIdInput.value.length === 0) {
      alert("주문번호를 입력해주세요.");
      purchaseIdInput.focus();
      return;
    }
    const purchaseIdNumber = parseInt(purchaseIdInput.value, 10);
    if (isNaN(purchaseIdNumber)) {
      alert("주문번호는 숫자여야 합니다.");
      purchaseIdInput.focus();
      return;
    }

    const reqBody: PurchaserReqBody = {
      userEmail: userEmailInput.value,
      purchaseId: purchaseIdNumber
    };

    try {
      const res = await fetch(`http://localhost:8080/api/purchases/lookup`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json; charset=utf-8',
        },
        body: JSON.stringify(reqBody)
      });
      if(!res.ok) console.log("주문 조회 실패");

      const fullResponse: ServerResponse<PurchaseLookupResBody> = await res.json();

      if (!fullResponse.resultCode.startsWith("200") || !fullResponse.data) {
        alert(fullResponse.msg || "주문 조회에 실패했습니다.");
        return;
      }

      const data: PurchaseLookupResBody = fullResponse.data;

      router.push(`/purchase/lookup/detail?id=${data.purchaseId}&email=${encodeURIComponent(data.userEmail)}`);
    }
    catch (err) {
      console.error('주문 조회 실패:', err);
    }
  }

  return (
    <div className="bg-gray-200 pt-20 min-h-screen w-full flex items-center justify-center px-4">
      <div className="bg-white rounded-2xl shadow-lg w-full max-w-md p-10">
        <div className="mb-5 text-center">
          <h5 className="font-bold text-xl">주문 조회</h5>
          <hr className="mt-3 border-gray-300" />
        </div>
        <form onSubmit={handleSubmit}>
          <input
            id="userEmail"
            type="email"
            name="userEmail"
            placeholder="이메일을 입력하세요"
            required
            className="w-full mb-4 px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
          <input
            id="purchaseId"
            type="text"
            name="purchaseId"
            placeholder="주문번호를 입력하세요"
            inputMode="numeric"
            pattern="[0-9]*"
            required
            className="w-full mb-6 px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
          <button
            type="submit"
            className="w-full bg-gray-800 hover:bg-gray-900 text-white py-3 rounded-lg text-lg cursor-pointer font-semibold transition-colors"
          >
            조회
          </button>
        </form>
      </div>
    </div>
  );
}