"use client";

import Link from "next/link";

export default function ShippingListPage() {
  // 예시 데이터
  const shippingList = [
    { id: 1, orderNo: 'A123', recipient: '홍길동', status: '배송 중' },
    { id: 2, orderNo: 'B456', recipient: '김철수', status: '배송 완료' },
    { id: 3, orderNo: 'C789', recipient: '이영희', status: '배송 준비' },
  ];

  return (

    <main className="max-w-4xl mx-auto p-6">
    <h1> 만드는 중입니다!</h1>
      <h1 className="text-2xl font-bold mb-4">배송 목록</h1>

      <table className="min-w-full border-collapse border border-gray-300">
        <thead>
          <tr>
            <th className="border border-gray-300 px-4 py-2">주문 번호</th>
            <th className="border border-gray-300 px-4 py-2">수령인</th>
            <th className="border border-gray-300 px-4 py-2">배송 상태</th>
          </tr>
        </thead>
        <tbody>
          {shippingList.map((item) => (
            <tr key={item.id} className="text-center">
              <td className="border border-gray-300 px-4 py-2">{item.orderNo}</td>
              <td className="border border-gray-300 px-4 py-2">{item.recipient}</td>
              <td className="border border-gray-300 px-4 py-2">{item.status}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="mt-6">
        <Link href="/orders" className="text-blue-500 hover:underline">
          주문 목록으로 돌아가기
        </Link>
        <h3> 만드는 중입니다!</h3>
      </div>
    </main>
  );
}
