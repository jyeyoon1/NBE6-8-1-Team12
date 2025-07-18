'use client';

import { cache, useEffect, useState } from 'react';
import { useSearchParams } from 'next/navigation';

import { PurchaserReqBody } from '@/purchase/types/purchase-request';
import { PurchaseDetailDto, PurchaseDto, PurchaseItemDetailDto, ReceiverResDto } from '@/purchase/types/purchase-response'; 


export default function PurchaseLookUpResultPage() {
    const searchParams = useSearchParams();
    const paramPurchaseId = searchParams.get('id'); // nullable
    const paramUserEmail = searchParams.get('email')

    const [purchaseDto, setPurchaseDto] = useState<PurchaseDto | null>(null);
    const [purchaseItemDetailDto, setPurchaseItemDetailDto] = useState<PurchaseItemDetailDto | null>(null);
    const [receiverResDto, setReceiverResDto] = useState<ReceiverResDto | null>(null);

    useEffect(() => {
        const fetchPuchaseDetail = async () => {
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


                setPurchaseDto(data.purchase);
                setPurchaseItemDetailDto(data.purchaseItem);
                setReceiverResDto(data.receiver);
            }
            catch(err) {
                console.error('주문 조회 실패:', err);
            }
        };
        fetchPuchaseDetail();
    }, [paramPurchaseId, paramUserEmail]);

    if(!purchaseDto || !purchaseItemDetailDto || !receiverResDto) return <div>Loading...</div>;

    return (
        <div className="max-w-3xl mx-auto bg-white rounded-xl shadow-lg p-8 mt-12 space-y-10 border border-gray-200">
            {/* 주문 정보 */}
            <section>
                <h2 className="text-2xl font-bold mb-4 text-gray-900">주문 정보</h2>
                <div className="flex flex-col gap-2 text-gray-800">
                    <div className="flex gap-8">
                        <div>
                            <div className="text-sm text-gray-500 mb-1">주문번호</div>
                            <div className="font-medium">{purchaseDto.purchaseId}</div>
                        </div>
                        <div>
                            <div className="text-sm text-gray-500 mb-1">이메일</div>
                            <div className="font-medium">{purchaseDto.userEmail}</div>
                        </div>
                    </div>
                    <div>
                        <div className="text-sm text-gray-500 mb-1">주문일시</div>
                        <div className="font-medium">
                            {new Date(purchaseDto.purchaseDate).toLocaleDateString()} {new Date(purchaseDto.purchaseDate).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                        </div>
                    </div>
                </div>
            </section>

            {/* 구매 제품 정보 */}
            <section>
                <h2 className="text-2xl font-bold mb-4 text-gray-900">구매 제품</h2>
                <div className="flex items-center gap-6 bg-gray-50 rounded-lg p-4 border">
                    <img
                        src={purchaseItemDetailDto.imageUrl}
                        alt={purchaseItemDetailDto.productName}
                        className="w-32 h-32 object-cover rounded-md border"
                    />
                    <div className="flex-1">
                        <div className="text-lg font-semibold text-gray-900 mb-2">{purchaseItemDetailDto.productName}</div>
                        <div className="flex flex-col gap-1 text-gray-700">
                            <div>
                                <span className="text-gray-500 mr-2">가격</span>
                                <span className="font-medium">{purchaseItemDetailDto.price.toLocaleString()}원</span>
                            </div>
                            <div>
                                <span className="text-gray-500 mr-2">수량</span>
                                <span className="font-medium">{purchaseItemDetailDto.quantity}개</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="text-right mt-4">
                    <span className="text-lg font-bold text-blue-700">총 결제금액: {purchaseDto.totalPrice.toLocaleString()}원</span>
                </div>
            </section>

            {/* 배송지 정보 */}
            <section>
                <h2 className="text-2xl font-bold mb-4 text-gray-900">배송지 정보</h2>
                <div className="grid grid-cols-2 gap-4 text-gray-800">
                    <div>
                        <div className="text-sm text-gray-500 mb-1">수령인</div>
                        <div className="font-medium">{receiverResDto.name}</div>
                    </div>
                    <div>
                        <div className="text-sm text-gray-500 mb-1">연락처</div>
                        <div className="font-medium">{receiverResDto.phoneNumber}</div>
                    </div>
                    <div>
                        <div className="text-sm text-gray-500 mb-1">우편번호</div>
                        <div className="font-medium">{receiverResDto.postcode}</div>
                    </div>
                    <div className="col-span-2">
                        <div className="text-sm text-gray-500 mb-1">주소</div>
                        <div className="font-medium break-words">{receiverResDto.address}</div>
                    </div>
                    <div>
                        <div className="text-sm text-gray-500 mb-1">배송상태</div>
                        <div className="font-medium">
                            {receiverResDto.status === "TEMPORARY" && "배송준비중"}
                            {receiverResDto.status === "BEFORE_DELIVERY" && "배송전"}
                            {receiverResDto.status === "DELIVERING" && "배송중"}
                            {receiverResDto.status === "DELIVERED" && "배송완료"}
                        </div>
                    </div>
                    <div>
                        <div className="text-sm text-gray-500 mb-1">최종수정일</div>
                        <div className="font-medium">
                            {new Date(receiverResDto.modifyDate).toLocaleDateString()} {new Date(receiverResDto.modifyDate).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                        </div>
                    </div>
                </div>
            </section>
        </div>
    );
}