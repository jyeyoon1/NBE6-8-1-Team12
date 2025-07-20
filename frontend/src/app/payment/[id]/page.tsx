"use client";

import React, { useState, useEffect } from "react";
import { useRouter, useParams } from "next/navigation";

export default function Page() {
    const router = useRouter();
    const params = useParams();
    const { id } = params;

    const [paymentItem, setPaymentItem] = useState<PaymentItem | null>(null);
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        if (!id) return;
        const fetchPayment = async () => {
            try {
                setIsLoading(true);
                setError('');
                const apiUrl = 'http://localhost:8080';
                const response = await fetch(`${apiUrl}/api/v1/payments/${id}`);
                if (!response.ok) {
                    setPaymentItem(null);
                    throw new Error("결제 내역을 불러오는 중 오류가 발생했습니다.");
                }
                const data: PaymentItem = await response.json();
                setPaymentItem(data);
            } catch (err) {
                setError(err instanceof Error ? err.message : "결제 내역을 불러오는 중 오류가 발생했습니다.");
            } finally {
                setIsLoading(false);
            }
        };
        fetchPayment();
    }, [id]);

    if (isLoading) {
        return <div className="p-8 max-w-lg mx-auto">
            <p className="text-gray-500 font-medium">결제 내역을 불러오는 중입니다...</p>
        </div>;
    }

    if (error) {
        return <div className="p-8 max-w-lg mx-auto">
            <p className="text-red-500 font-medium">{error}</p>
        </div>;
    }

    return (
        <div className="p-8 max-w-2xl mx-auto bg-gray-50 rounded-xl shadow-lg">
            <h1 className="text-3xl font-bold text-center mb-6">결제 상세 정보</h1>
            {paymentItem ? (
                <div className="bg-white shadow-md rounded-lg p-6 space-y-4">
                    <div className="flex justify-between">
                        <span className="font-semibold text-gray-600">결제번호</span>
                        <span className="text-gray-600">{paymentItem.id}</span>
                    </div>
                    <div className="flex justify-between">
                        <span className="font-semibold text-gray-600">주문번호</span>
                        <span className="text-gray-600">{paymentItem.purchaseId}</span>
                    </div>
                    <div className="flex justify-between">
                        <span className="font-semibold text-gray-600">결제수단</span>
                        <span className="text-gray-600">{   paymentItem.paymentOptionType} : {paymentItem.paymentOptionName}</span>
                    </div>
                    <div className="flex justify-between">
                        <span className="font-semibold text-gray-600">결제정보</span>
                        <span className="text-gray-600">{paymentItem.paymentInfo || ''}</span>
                    </div>
                    <div className="flex justify-between">
                        <span className="font-semibold text-gray-600">결제금액</span>
                        <span className="text-gray-600">{paymentItem.amount.toLocaleString()}원</span>
                    </div>
                    <div className="flex justify-between">
                        <span className="font-semibold text-gray-600">결제상태</span>
                        <span className="text-gray-600">{paymentItem.paymentStatus}</span>
                    </div>
                    <div className="flex justify-between">
                        <span className="font-semibold text-gray-600">결제일시</span>
                        <span className="text-gray-600">{new Date(paymentItem.date).toLocaleString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit' })}</span>
                    </div>
                </div>
            ) : (
                <div className="bg-white shadow-md rounded-lg p-10 text-center">
                    <h2 className="text-2xl font-bold text-gray-800">결제 정보 없음</h2>
                    <p className="text-gray-600 mt-2">요청하신 결제 내역을 찾을 수 없습니다.</p>
                </div>
            )}
            <button onClick={() => router.push('/products/list')} className="mt-6 w-full py-2 bg-gray-200 text-gray-800 font-semibold rounded-lg hover:bg-gray-300">
                상품 목록으로 이동
            </button>
        </div>
    )
}