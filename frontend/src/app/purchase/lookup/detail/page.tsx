'use client';

import { cache, useEffect, useState } from 'react';
import { useSearchParams } from 'next/navigation';

import { PurchaserReqBody } from '@/purchase/types/purchase-request';
import { PurchaseDetailDto, PurchaseDto, PurchaseItemDetailDto, ReceiverResDto } from '@/purchase/types/purchase-response'; 


export default function PurchaseLookUpResultPage() {
    const searchParams = useSearchParams();
    const paramPurchaseId = searchParams.get('id'); // nullable
    const paramUserEmail = searchParams.get('email')

    const [purchase, setPurchase] = useState<PurchaseDto | null>(null);
    const [purchaseItems, setPurchaseItems] = useState<PurchaseItemDetailDto[]>([]);
    const [receiver, setReceiver] = useState<ReceiverResDto | null>(null);

    useEffect(() => {
        const fetchPurchaseDetail = async () => {
            if(!paramPurchaseId || !paramUserEmail) {
                alert("주문 정보 조회 실패");
                return;
            }
            const paramPurchaseIdNumber = parseInt(paramPurchaseId, 10);
            if(isNaN(paramPurchaseIdNumber)) {
                alert("유효하지 않은 주문번호");
                return;
            }
            
            try {
                const reqBody: PurchaserReqBody = {
                    userEmail: paramUserEmail,
                    purchaseId: paramPurchaseIdNumber
                };
                
                const res = await fetch(`http://localhost:8080/api/purchases/lookup/detail`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json; charset=utf-8',
                    },
                    body: JSON.stringify(reqBody)
                });

                if (!res.ok) throw new Error("응답 실패");
                
                interface ServerResponse {
                    resultCode: string;
                    statusCode: number;
                    msg: string;
                    data: PurchaseDetailDto;
                }
    
                const fullResponse: ServerResponse = await res.json();
                const data: PurchaseDetailDto = fullResponse.data;
                console.log("주문 상세 조회 서버 응답 데이터:", data);


                setPurchase(data.purchase);
                setPurchaseItems(data.purchaseItems);
                setReceiver(data.receiver);
            }
            catch(err) {
                console.error('주문 조회 실패:', err);
            }
        };
        fetchPurchaseDetail();
    }, [paramPurchaseId, paramUserEmail]);

    if(!purchase || !purchaseItems || !receiver) return <div>Loading...</div>;

    return (
        <div className="max-w-6xl w-full mx-auto bg-white rounded-xl shadow-lg p-8 mt-12 space-y-10">
            {/* 주문 정보 */}
            <section>
                <h2 className="text-2xl font-bold mb-4 text-gray-900">주문 정보</h2>
                <dl className="grid grid-cols-1 md:grid-cols-3 gap-y-2 gap-x-8 text-gray-800">
                    <div>
                        <dt className="text-sm text-gray-500 mb-1">주문번호</dt>
                        <dd className="font-medium">{purchase.purchaseId}</dd>
                    </div>
                    <div>
                        <dt className="text-sm text-gray-500 mb-1">주문자 이메일</dt>
                        <dd className="font-medium">{purchase.userEmail}</dd>
                    </div>
                    <div>
                        <dt className="text-sm text-gray-500 mb-1">주문일시</dt>
                        <dd className="font-medium">
                            {new Date(purchase.purchaseDate).toLocaleDateString()} {new Date(purchase.purchaseDate).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                        </dd>
                    </div>
                </dl>
            </section>

            {/* 구매 제품 정보 */}
            <section>
                <h2 className="text-2xl font-bold mb-4 text-gray-900">구매 제품</h2>
                <div className="space-y-4">
                    {purchaseItems.map((item) => (
                        <div key={item.purchaseItemId} className="flex items-center gap-8 bg-gray-50 rounded-lg p-4 border">
                            <img
                                src={item.imageUrl}
                                alt={item.productName}
                                className="w-40 h-40 object-cover rounded-md border"
                            />
                            <div className="flex-1">
                                <div className="text-lg font-semibold text-gray-900 mb-2">{item.productName}</div>
                                <div className="flex flex-col gap-1 text-gray-700">
                                    <div>
                                        <span className="text-gray-500 mr-2">가격</span>
                                        <span className="font-medium">{item.price.toLocaleString()}원</span>
                                    </div>
                                    <div>
                                        <span className="text-gray-500 mr-2">수량</span>
                                        <span className="font-medium">{item.quantity}개</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
                <div className="text-right mt-4">
                    <span className="text-lg font-bold text-blue-700">총 결제금액: {purchase.totalPrice.toLocaleString()}원</span>
                </div>
            </section>

            {/* 배송지 정보 */}
            <section>
                <h2 className="text-2xl font-bold mb-4 text-gray-900">배송지 정보</h2>
                <div className="w-full rounded-lg bg-gray-50 p-4">
                    <dl className="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-2 text-gray-800">
                        <div className="flex items-center">
                            <dt className="w-28 text-sm text-gray-500">수령인</dt>
                            <dd className="font-medium">{receiver.name}</dd>
                        </div>
                        <div className="flex items-center">
                            <dt className="w-28 text-sm text-gray-500">연락처</dt>
                            <dd className="font-medium">{receiver.phoneNumber}</dd>
                        </div>
                        <div className="flex items-center">
                            <dt className="w-28 text-sm text-gray-500">우편번호</dt>
                            <dd className="font-medium">{receiver.postcode}</dd>
                        </div>
                        <div className="flex items-center">
                            <dt className="w-28 text-sm text-gray-500">배송상태</dt>
                            <dd className="font-medium">
                                {receiver.status === "TEMPORARY" && "배송준비중"}
                                {receiver.status === "BEFORE_DELIVERY" && "배송전"}
                                {receiver.status === "DELIVERING" && "배송중"}
                                {receiver.status === "DELIVERED" && "배송완료"}
                            </dd>
                        </div>
                        <div className="flex items-center col-span-1 md:col-span-2">
                            <dt className="w-28 text-sm text-gray-500">주소</dt>
                            <dd className="font-medium break-words">{receiver.address}</dd>
                        </div>
                        <div className="flex items-center col-span-1 md:col-span-2">
                            <dt className="w-28 text-sm text-gray-500">배송 처리일시</dt>
                            <dd className="font-medium">
                                {new Date(receiver.modifyDate).toLocaleDateString()} {new Date(receiver.modifyDate).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                            </dd>
                        </div>
                    </dl>
                </div>
            </section>
        </div>
    );
}