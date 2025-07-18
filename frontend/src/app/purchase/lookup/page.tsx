"use client";

import { useRouter } from "next/navigation";

import { PurchaserReqBody } from "@/purchase/types/purchase-request";
import { PurchaseLookupResBody } from "@/purchase/types/purchase-response";

export default function PurchaseLookUpPage() {
  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const form = e.target as HTMLFormElement;

    const userEmailInput = form.elements.namedItem(
      "userEmail",
    ) as HTMLInputElement;
    const purchaseIdInput = form.elements.namedItem(
      "purchaseId",
    ) as HTMLInputElement;

    userEmailInput.value = userEmailInput.value.trim();
    if (userEmailInput.value.length === 0) {
      alert("이메일을 입력해주세요.");
      userEmailInput.focus();
      return;
    }

    purchaseIdInput.value = purchaseIdInput.value.trim();
    if (purchaseIdInput.value.length === 0) {
      alert("주문번호를 입력해주세요.");
      purchaseIdInput.focus();
      return;
    }
    const purchaseIdNumber = parseInt(purchaseIdInput.value);
    if (isNaN(purchaseIdNumber)) {
      alert("주문번호는 숫자여야 합니다.");
      purchaseIdInput.focus();
      return;
    }

    const reqBody: PurchaserReqBody = {
      userEmail: userEmailInput.value,
      purchaseId: purchaseIdNumber,
    };

    try {
      const res = await fetch(`http://localhost:8080/api/purchases/lookup`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json; charset=utf-8",
        },
        body: JSON.stringify(reqBody),
      });

      if (!res.ok) throw new Error("응답 실패");

      const data: PurchaseLookupResBody = await res.json();

      // data 처리 확인 필요
      //router.push(`/purchase/lookup/detail?id=${data.purchaseId}&email=${encodeURIComponent(data.userEmail)}`);
    } catch (err) {
      console.error("주문 조회 실패:", err);
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="max-w-2xl mx-auto bg-white rounded-xl shadow-lg p-8 mt-16 space-y-8 border border-gray-200"
    >
      <div>
        <h2 className="text-xl font-bold mb-6 text-gray-900 border-b border-gray-200 pb-4">
          주문 조회
        </h2>
        <div className="flex flex-col gap-4 mt-2">
          <div className="flex items-center gap-4">
            <label
              htmlFor="userEmail"
              className="font-medium text-gray-900 w-20"
            >
              이메일
            </label>
            <input
              id="userEmail"
              type="email"
              name="userEmail"
              required
              className="flex-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900"
            />
          </div>
          <div className="flex items-center gap-4">
            <label
              htmlFor="purchaseId"
              className="font-medium text-gray-900 w-20"
            >
              주문번호
            </label>
            <input
              id="purchaseId"
              type="text"
              name="purchaseId"
              inputMode="numeric"
              pattern="[0-9]*"
              required
              className="flex-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900"
            />
          </div>
        </div>
      </div>
      <div className="flex justify-end">
        <button
          type="submit"
          className="bg-blue-600 hover:bg-blue-700 text-white font-semibold px-8 py-3 rounded-lg shadow transition-colors"
        >
          조회
        </button>
      </div>
    </form>
  );
}
