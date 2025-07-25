"use client";

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "../contexts/AuthContext";
import { PageResponse } from "../types/pageResponse";
import { PaymentItem } from "./types/paymentData";

export default function Page() {
  const router = useRouter();
  const { isAuthenticated } = useAuth();
  const [pageData, setPageData] = useState<PageResponse<PaymentItem> | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {

    if (isAuthenticated === null) {
      setIsLoading(true);
      return;
    }

    const fetchPayments = async () => {
      setIsLoading(true);
      setError('');
      try {
        const apiUrl = 'http://localhost:8080';
        const response = await fetch(`${apiUrl}/api/v1/payments?page=${currentPage}&size=5&sortField=id&sortOrder=desc`);
        if (!response.ok) {
          throw new Error("결제 내역을 불러오는 중 오류가 발생했습니다.");
        }
        const data: PageResponse<PaymentItem> = await response.json();
        setPageData(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : "결제 내역을 불러오는 중 오류가 발생했습니다.");
      } finally {
        setIsLoading(false);
      }
    };
    fetchPayments();
  }, [isAuthenticated, currentPage]);

  const handlePageChange = (page: number) => {
    if (pageData && page >= 0 && page < pageData.totalPages) {
      setCurrentPage(page);
    }
  };

  if (isLoading || isAuthenticated === null) {
    return <div className="p-8 max-w-lg mx-auto">
      <p className="text-gray-500 font-medium">결제 내역을 불러오는 중입니다...</p>
    </div>;
  }

  if (!isAuthenticated) {
    return <div className="p-8 max-w-lg mx-auto">
      <p className="text-gray-500 font-medium">접근 권한이 없습니다.</p>
      <p className="text-gray-500 font-medium">로그인 후 이용해주세요.</p>
      <button onClick={() => router.push('/member/login')} className="mt-4 px-4 py-2 bg-gray-800 text-white rounded hover:bg-gray-700 transition-colors">
        로그인 페이지로 이동
      </button>
    </div>;
  }

  if (!pageData) {
    return <div className="p-8 max-w-lg mx-auto">
      <p className="text-gray-500 font-medium">결제 내역을 불러오는 중입니다...</p>
    </div>;
  }

  if (error) {
    return <div className="p-8 max-w-lg mx-auto">
      <p className="text-red-500 font-medium">{error}</p>
    </div>;
  }

  //p-8 max-w-4x1 mx-auto
  return (
    <div className="bg-gray-100 rounded-xl shadow-lg mx-auto"
      style={{
        width: "1020px",
        minWidth: "1020px",
        maxWidth: "1020px",
        padding: "2.5rem",
      }}>
      <h1 className="text-3xl font-bold text-center mb-8">결제 내역</h1>
      <div className="bg-white rounded-xl shadow-md overflow-hidden">
        <table className="min-w-full leading-normal">
          <thead>
            <tr className="bg-gray-200 text-left text-gray-600 uppercase text-sm">
              <th className="px-5 py-3">결제번호</th>
              <th className="px-5 py-3">주문번호</th>
              <th className="px-5 py-3">결제수단</th>
              <th className="px-5 py-3">결제정보</th>
              <th className="px-5 py-3">결제금액</th>
              <th className="px-5 py-3">결제상태</th>
              <th className="px-5 py-3">결제일시</th>
            </tr>
          </thead>
          <tbody>
            {pageData.content.map((payment) => (
              <tr key={payment.id} className="border-b border-gray-200 hover:bg-gray-50">
                <td className="px-5 py-4 text-sm">{payment.id}</td>
                <td className="px-5 py-4 text-sm">{payment.purchaseId}</td>
                <td className="px-5 py-4 text-sm">{payment.paymentOptionType} : {payment.paymentOptionName}</td>
                <td className="px-5 py-4 text-sm">{payment.paymentInfo}</td>
                <td className="px-5 py-4 text-sm">{payment.amount.toLocaleString()}원</td>
                <td className="px-5 py-4 text-sm">
                  <span className={`px-2 py-1 font-semibold rounded-full leading-tight ${payment.paymentStatus === 'SUCCESS' ? 'bg-green-100 text-green-600' : payment.paymentStatus === 'FAILED' ? 'bg-red-100 text-red-600' : payment.paymentStatus === 'PENDING' ? 'bg-yellow-100 text-yellow-600' : 'bg-gray-100 text-gray-600'}`}>
                    {payment.paymentStatus}
                  </span>
                </td>
                <td className="px-5 py-4 text-sm">{new Date(payment.date).toLocaleString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit' })}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* 페이징 UI */}
      <div className="flex justify-center items-center mt-8 space-x-2">
        <button
          onClick={() => {
            if (pageData.pageNumber > 0) {
              handlePageChange(pageData.pageNumber - 1);
            }
          }}
          disabled={currentPage === 0}
          className={`px-3 py-2 rounded ${pageData.pageNumber === 0
            ? 'bg-gray-100 text-gray-300 cursor-not-allowed'
            : 'bg-gray-200 hover:bg-gray-400'
            }`}
        >
          이전
        </button>
        <span className="px-3 py-2 bg-blue-500 text-white rounded font-bold">
          {pageData.pageNumber + 1}
        </span>
        <button
          onClick={() => {
            if (!pageData.isLast) {
              handlePageChange(pageData.pageNumber + 1);
            }
          }}
          disabled={pageData.isLast}
          className={`px-3 py-2 rounded ${pageData.isLast
            ? 'bg-gray-100 text-gray-300 cursor-not-allowed'
            : 'bg-gray-200 hover:bg-gray-400'
            }`}
        >
          다음
        </button>
      </div>
    </div>
  );
}