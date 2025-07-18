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
        <div>
            {/* 구매자 정보 */}
            <div>
                <h2 className="text-xl font-bold mb-4 text-gray-900">구매자 정보</h2>
                <div>
                    <span className="mb-1">주문번호</span>
                    <span>{purchaseDto.purchaseId}</span>
                </div>
                <div>
                    <span className="mb-1">이메일</span>
                    <span>{purchaseDto.userEmail}</span>
                </div>
            </div>

            {/* 구매 제품 정보 */}
            <div>
                <h2 className="text-xl font-bold mb-4 text-black">구매 제품 정보</h2>
                <table className="w-full border rounded-lg overflow-hidden mb-3">
                    <thead>
                        <tr className="bg-gray-100 text-black">
                            <th className="py-2 px-3">제품명</th>
                            <th className="py-2 px-3">이미지</th>
                            <th className="py-2 px-3">제품 가격</th>
                            <th className="py-2 px-3">제품 수량</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr className="text-center text-black">
                            <td className="py-2 px-3"></td>
                            <td className="py-2 px-3">
                                <img 
                                    src={purchaseItemDetailDto.imageUrl} 
                                    alt={purchaseItemDetailDto.imageUrl} 
                                    className="w-32 h-32 object-cover mx-auto rounded-md border"
                                />
                            </td>
                            <td className="py-2 px-3">{purchaseItemDetailDto.price.toLocaleString()}원</td>
                            <td className="py-2 px-3">{purchaseItemDetailDto.quantity}</td>
                        </tr>
                    </tbody>
                </table>
                <h5 className="text-right text-lg font-semibold text-blue-700">
                    총 가격: {purchaseDto.totalPrice.toLocaleString()}원
                </h5>
            </div>

            {/* 배송지 정보 */}
            <div>
                <h2 className="text-xl font-bold mb-4 text-gray-900">배송지 정보</h2>
                <div>
                    <div>
                        <span className="mb-1">이름</span>
                        <span>{receiverResDto.name}</span>
                    </div>
                    <div>
                        <span className="mb-1">연락처</span>
                        <span>{receiverResDto.phoneNumber}</span>
                    </div>
                </div>
                <div>
                    <div>
                        <span className="mb-1">우편번호</span>
                        <span>{receiverResDto.postcode}</span>
                    </div>
                    <div>
                        <span className="mb-1">주소</span>
                        <span>{receiverResDto.address}</span>
                    </div>
                    <div>
                        <span className="mb-1">배송 상태</span>
                        <span>{receiverResDto.status}</span>
                    </div>
                </div>
            </div>
        </div>
    );
}