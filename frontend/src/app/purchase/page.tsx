'use client';

import { useEffect, useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';

import { PurchaseInfo, PaymentOption, PurchasePageResBody, PurchaseCheckoutResBody } from '@/purchase/types/purchase-response'
import { PurchasePageReqBody } from '@/purchase/types/purchase-request'

export default function PurchasePage() {
    const router = useRouter();
    const searchParams = useSearchParams();
    const paramProducteId = searchParams.get('id');
    const paramQuantity = searchParams.get('quantity');

    const [purchaseInfo, setPurchaseInfo] = useState<PurchaseInfo | null>(null);
    const [topOpts, setTopOpts] = useState<PaymentOption[]>([]);
    const [selectedTopOptId, setSelectedTopOptId] = useState<number | null>(null);
    const [detailOpts, setDetailOpts] = useState<PaymentOption[]>([]);
    const [selectedDetailOptId, setSelectedDetailOptId] = useState<number | ''>('');

    useEffect(() => {
        if (!paramProducteId) return;
        const quantity = paramQuantity ? parseInt(paramQuantity, 10) : 1;
        
        fetch(`http://localhost:8080/api/purchases/purchaseInfo?productId=${paramProducteId}&quantity=${quantity}`)
            .then(res => res.json())
            .then((data: PurchasePageResBody) => {
                setPurchaseInfo(data.purchaseInfo);
                setTopOpts(data.paymentOptions);
            })
            .catch(err => console.error('구매 제품 정보 불러오기 실패:', err));
    }, [paramProducteId, paramQuantity]);

    useEffect(() => {
        if (Array.isArray(topOpts) && topOpts.length > 0 && selectedTopOptId === null) {
            setSelectedTopOptId(topOpts[0].id);
        }
    }, [topOpts, selectedTopOptId]);

    useEffect(() => {
        if(selectedTopOptId == null) return;

        fetch(`http://localhost:8080/api/v1/payments/options/${selectedTopOptId}`)
            .then(res => res.json())
            .then(data => {
                setDetailOpts(data);

                if(data.length > 0) {
                    setSelectedDetailOptId(data[0].id);
                } else {
                    setSelectedDetailOptId('');
                }
            })
            .catch(err => console.error('상세 결제수단 불러오기 실패:', err));
    }, [selectedTopOptId]);

    if(!purchaseInfo) return <div>Loading...</div>;

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const form = e.target as HTMLFormElement;

        const validateInput = (
            form: HTMLFormElement,
            name: string,
            label: string
          ): boolean => {
            const input = form.elements.namedItem(name) as HTMLInputElement;

            input.value = input.value.trim();
            if (input.value.length === 0) {
                alert(`${label}을(를) 입력해주세요.`);
                input.focus();
                return false;
            }
            return true;
        }

        if(!validateInput(form, "purchaser.name", "구매자 이름")) return;
        if(!validateInput(form, "purchaser.email", "구매자 이메일")) return;
        if(!validateInput(form, "receiver.name", "배송지 이름")) return;
        if(!validateInput(form, "receiver.phoneNumber", "배송지 연락처")) return;
        if(!validateInput(form, "receiver.postcode", "배송지 우편번호")) return;
        const postcodeStr = (form.elements.namedItem("receiver.postcode") as HTMLInputElement).value.trim();
        if (!/^\d{5}$/.test(postcodeStr)) {
            alert("우편번호는 숫자 5자리여야 합니다.");
            return;
        }
        if(!validateInput(form, "receiver.address", "배송지 주소")) return;
        if(!validateInput(form, "paymentOptionId", "상세 결제 수단")) return;
        if(selectedDetailOptId === '') {
            alert("상세 결제 수단을 선택해주세요.");
            return;
        }

        try {
            const userEmail = (form.elements.namedItem("purchaser.email") as HTMLInputElement).value.trim();

            const purchasePageReqBody: PurchasePageReqBody = {
                purchase: {
                  productId: purchaseInfo!.productId,
                  price: purchaseInfo!.price,
                  quantity: purchaseInfo!.quantity,
                  totalPrice: purchaseInfo!.totalPrice,
                },
                purchaser: {
                  name: (form.elements.namedItem("purchaser.name") as HTMLInputElement).value.trim(),
                  email: userEmail,
                },
                receiver: {
                  name: (form.elements.namedItem("receiver.name") as HTMLInputElement).value.trim(),
                  phoneNumber: (form.elements.namedItem("receiver.phoneNumber") as HTMLInputElement).value.trim(),
                  address: (form.elements.namedItem("receiver.address") as HTMLInputElement).value.trim(),
                  postcode: parseInt((form.elements.namedItem("receiver.postcode") as HTMLInputElement).value.trim(), 10),
                  email: userEmail
                },
                paymentOptionId: parseInt((form.elements.namedItem("paymentOptionId") as HTMLInputElement).value, 10)
            };

            const purchaseRes = await fetch(`http://localhost:8080/api/purchases/checkout`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8',
                },
                body: JSON.stringify(purchasePageReqBody),
            });
            if (!purchaseRes.ok) throw new Error("주문 실패");
            
            interface ServerResponse {
                resultCode: string;
                statusCode: number;
                msg: string;
                data: PurchaseCheckoutResBody;
            }
            
            const fullResponse: ServerResponse = await purchaseRes.json();
            const paymentRequestBody: PurchaseCheckoutResBody = fullResponse.data;
            console.log('주문 생성 성공:', fullResponse);
            
            const paymentRes = await fetch(`http://localhost:8080/api/v1/payments`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8',
                },
                body: JSON.stringify(paymentRequestBody),
            });
            if (!paymentRes.ok) throw new Error("결제 실패");

            const paymentData = await paymentRes.json();
            console.log('결제 정보 생성 성공:', paymentData);
            console.log('paymentData.data :', paymentData.data);
            console.log('stringify:', JSON.stringify(paymentData.data));

            router.push(`/payment?paymentData=${encodeURIComponent(JSON.stringify(paymentData.data))}`);
        } catch(err) {
            console.error('주문 실패:', err);
        }
    };

    return (
        <form
            onSubmit={handleSubmit}
            className="max-w-2xl mx-auto bg-white rounded-xl shadow-lg p-8 mt-10 space-y-8"
        >
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
                            <td className="py-2 px-3">{purchaseInfo.productName}</td>
                            <td className="py-2 px-3">
                                <img 
                                    src={purchaseInfo.imageUrl} 
                                    alt={purchaseInfo.productName} 
                                    className="w-32 h-32 object-cover mx-auto rounded-md border"
                                />
                            </td>
                            <td className="py-2 px-3">{purchaseInfo.price.toLocaleString()}원</td>
                            <td className="py-2 px-3">{purchaseInfo.quantity}</td>
                        </tr>
                    </tbody>
                </table>
                <h5 className="text-right text-lg font-semibold text-blue-700">
                    총 가격: {purchaseInfo.totalPrice.toLocaleString()}원
                </h5>
            </div>

            {/* 구매자 정보 */}
            <div>
                <h2 className="text-xl font-bold mb-4 text-gray-900">구매자 정보</h2>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <label className="flex flex-col font-medium text-gray-900">
                        <span className="mb-1">이름</span>
                        <input
                            type="text"
                            name="purchaser.name"
                            className="mt-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900"
                        />
                    </label>
                    <label className="flex flex-col font-medium text-gray-900">
                        <span className="mb-1">이메일</span>
                        <input
                            type="email"
                            name="purchaser.email"
                            className="mt-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900"
                        />
                    </label>
                </div>
            </div>

            {/* 배송지 정보 */}
            <div>
                <h2 className="text-xl font-bold mb-4 text-gray-900">배송지 정보</h2>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <label className="flex flex-col font-medium text-gray-900">
                        <span className="mb-1">이름</span>
                        <input
                            type="text"
                            name="receiver.name"
                            className="mt-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900"
                        />
                    </label>
                    <label className="flex flex-col font-medium text-gray-900">
                        <span className="mb-1">전화번호</span>
                        <input
                            type="tel"
                            name="receiver.phoneNumber"
                            className="mt-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900"
                        />
                    </label>
                </div>
                <div className="mt-4">
                    <div className="flex gap-2">
                        <label className="flex flex-col font-medium text-gray-900 w-32">
                            <span className="mb-1">우편번호</span>
                            <input
                                type="text"
                                name="receiver.postcode"
                                placeholder="우편번호"
                                inputMode="numeric"
                                pattern="[0-9]*"
                                className="mt-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900"
                            />
                        </label>
                        <label className="flex flex-col font-medium text-gray-900 flex-1">
                            <span className="mb-1">주소</span>
                            <input
                                type="text"
                                name="receiver.address"
                                className="mt-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900"
                            />
                        </label>
                    </div>
                </div>
            </div>

            {/* 결제 정보 */}
            <div>
                <h2 className="text-xl font-bold mb-4 text-gray-900">결제 정보</h2>
                <div className="flex flex-wrap gap-4 mb-4">
                    {topOpts.map((opt) => (
                        <label
                            key={opt.id}
                            className="flex items-center space-x-2 cursor-pointer px-3 py-2 border rounded-md hover:bg-gray-50 font-medium text-gray-900"
                        >
                            <input 
                                type="radio"
                                name="selectedTopOptId"
                                value={opt.id}
                                checked={selectedTopOptId === opt.id}
                                onChange={() => setSelectedTopOptId(opt.id)}
                                className="accent-blue-600"
                            />
                            <span>{opt.name}</span>
                        </label>
                    ))}
                </div>
                {detailOpts.length > 0 && (
                    <select
                        name="paymentOptionId"
                        value={selectedDetailOptId ?? ''}
                        onChange={(e) => setSelectedDetailOptId(parseInt(e.target.value, 10))}
                        required
                        className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900"
                    >
                        <option value="" disabled>
                            상세 수단 선택
                        </option>
                        {detailOpts.map((opt) => (
                            <option key={opt.id} value={opt.id}>
                                {opt.name}
                            </option>
                        ))}
                    </select>
                )}
            </div>

            {/* 구매 버튼 */}
            <div className="flex justify-end">
                <button
                    type="submit"
                    className="bg-blue-600 hover:bg-blue-700 text-white font-semibold px-8 py-3 rounded-lg shadow transition-colors"
                >
                    구매
                </button>
            </div>
        </form>
    );
}