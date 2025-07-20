'use client';

import { useEffect, useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';

import { PurchaseItemInfo, PaymentOption, PurchasePageResBody, PurchaseCheckoutResBody } from '@/purchase/types/purchase-response'
import { PurchasePageReqBody } from '@/purchase/types/purchase-request'

interface CartItem {
    productId: number;
    quantity: number;
}

export default function PurchasePage() {
    const router = useRouter();
    const searchParams = useSearchParams();

    // 단일 상품 파라미터 (상세 페이지에서 바로 구매 버튼 클릭 시)
    const paramProductId = searchParams.get('id');
    const paramQuantity = searchParams.get('quantity');
    // 여러 상품 파라미터 (장바구니 목록)
    const paramCartItems = searchParams.get('cartItems');

    // 구매 제품 목록 요청 정보
    const [cartItemReqBody, setCartItemReqBody] = useState<CartItem[]>([]);
    // 구매 제품 상세 목록 응답 정보
    const [purchaseItems, setPurchaseItems] = useState<PurchaseItemInfo[]>([]);

    // 결제 수단
    const [topOpts, setTopOpts] = useState<PaymentOption[]>([]);
    const [selectedTopOptId, setSelectedTopOptId] = useState<number | null>(null);
    const [detailOpts, setDetailOpts] = useState<PaymentOption[]>([]);
    const [selectedDetailOptId, setSelectedDetailOptId] = useState<number | null>(null);

    // 구매할 제품 목록 세팅
    useEffect(() => {
        // url로 넘어온 cartItems 우선 세팅 (장바구니 페이지에서 구매 버튼 클릭 시시)
        if (paramCartItems) {
            try {
                const parsed = JSON.parse(paramCartItems);
                if (Array.isArray(parsed) && parsed.length > 0) {
                    setCartItemReqBody(parsed);
                    return;
                }
            } catch (e) {
                console.error('cartItems 파싱 실패:', e);
            }
        }

        // 단일 상품 목록에 세팅 (장바구니 목록이 없을 경우에만 단일 제품 세팅)
        if (paramProductId) {
            const qty = paramQuantity ? parseInt(paramQuantity, 10) : 1;
            setCartItemReqBody([{ productId: parseInt(paramProductId, 10), quantity: qty }]);
            return;
        }
    }, [paramCartItems, paramProductId, paramQuantity]);

    // 구매할 제품 상세 조회
    useEffect(() => {
        if (cartItemReqBody.length === 0) return;

        fetch(`http://localhost:8080/api/purchases/purchaseInfo`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json; charset=utf-8',
            },
            body: JSON.stringify(cartItemReqBody),
        })
            .then(res => res.json())
            .then((data: PurchasePageResBody) => {
                setPurchaseItems(data.purchaseItems);
                setTopOpts(data.paymentOptions);
            })
            .catch(err => console.error('구매 제품 정보 불러오기 실패:', err));
    }, [cartItemReqBody]);

    useEffect(() => {
        if (Array.isArray(topOpts) && topOpts.length > 0 && selectedTopOptId === null) {
            setSelectedTopOptId(topOpts[0].id);
        }
    }, [topOpts, selectedTopOptId]);

    useEffect(() => {
        if (selectedTopOptId == null) return;

        fetch(`http://localhost:8080/api/v1/payments/options/${selectedTopOptId}`)
            .then(res => res.json())
            .then(data => {
                setDetailOpts(data);

                if (data.length > 0) {
                    setSelectedDetailOptId(data[0].id);
                } else {
                    setSelectedDetailOptId(null);
                }
            })
            .catch(err => console.error('상세 결제수단 불러오기 실패:', err));
    }, [selectedTopOptId]);

    // 구매 시 로컬스토리지에서 구매한 제품 제거
    const updateCart = (purchaseItems: PurchaseItemInfo[]) => {
        const cartStr = localStorage.getItem("cart");
        if (!cartStr) return;

        const cart: { productId: number; quantity: number }[] = JSON.parse(cartStr);

        // 구매한 제품 제외 필터
        const updatedCart = cart.filter(item =>
            !purchaseItems.some(p => p.productId === item.productId)
        );
        localStorage.setItem("cart", JSON.stringify(updatedCart));
    };


    if (!purchaseItems || purchaseItems.length === 0) return <div>Loading...</div>;

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        // 재고 검증 (백엔드 API 사용)
        for (const item of purchaseItems) {
            try {
                const res = await fetch(`http://localhost:8080/api/products/${item.productId}`);
                if (!res.ok) {
                    alert(`${item.productName}의 정보를 가져올 수 없습니다.`);
                    return;
                }
                const data = await res.json();
                const product = data.data;

                if (product.status === 'OUT_OF_STOCK') {
                    alert(`${item.productName}은(는) 재고가 소진되었습니다.`);
                    return;
                }

                if (product.status === 'NOT_FOR_SALE') {
                    alert(`${item.productName}은(는) 판매가 중지되었습니다.`);
                    return;
                }

                if (item.quantity > product.totalQuantity) {
                    alert(`${item.productName}의 재고가 부족합니다. (재고: ${product.totalQuantity}개, 요청: ${item.quantity}개)`);
                    return;
                }
            } catch (error) {
                console.error(`${item.productName} 재고 확인 중 오류:`, error);
                alert(`${item.productName}의 재고를 확인할 수 없습니다.`);
                return;
            }
        }

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

        if (!validateInput(form, "purchaser.name", "구매자 이름")) return;
        if (!validateInput(form, "purchaser.email", "구매자 이메일")) return;
        if (!validateInput(form, "receiver.name", "배송지 이름")) return;
        if (!validateInput(form, "receiver.phoneNumber", "배송지 연락처")) return;
        if (!validateInput(form, "receiver.postcode", "배송지 우편번호")) return;
        const postcodeStr = (form.elements.namedItem("receiver.postcode") as HTMLInputElement).value.trim();
        if (!/^\d{5}$/.test(postcodeStr)) {
            alert("우편번호는 숫자 5자리여야 합니다.");
            return;
        }
        if (!validateInput(form, "receiver.address", "배송지 주소")) return;
        if (!validateInput(form, "paymentOptionId", "상세 결제 수단")) return;
        if (selectedDetailOptId === null) {
            alert("상세 결제 수단을 선택해주세요.");
            return;
        }

        try {
            const userEmail = (form.elements.namedItem("purchaser.email") as HTMLInputElement).value.trim();

            const purchasePageReqBody: PurchasePageReqBody = {
                purchaseItems: purchaseItems.map(item => ({
                    productId: item!.productId,
                    price: item!.price,
                    quantity: item!.quantity,
                    totalPrice: item!.totalPrice,
                })),
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

            const paymentRes = await fetch(`http://localhost:8080/api/v1/payments`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8',
                },
                body: JSON.stringify(paymentRequestBody),
            });
            if (!paymentRes.ok) throw new Error("결제 실패");

            updateCart(purchaseItems);

            const paymentData = await paymentRes.json();
            router.push(`/payment/${paymentData.data.id}/execute`);
        } catch (err) {
            console.error('주문 실패:', err);
        }
    };

    return (
        <div className="flex justify-center w-full mt-12">
            <form
                onSubmit={handleSubmit}
                className="w-full max-w-3xl bg-white rounded-xl shadow-lg p-8 mt-8 space-y-8"
            >
                {/* 구매 제품 정보 */}
                <div>
                    <h2 className="text-xl font-bold mb-4 text-black">구매 제품 정보</h2>
                    <div className="border border-gray-200 rounded-lg overflow-hidden mb-3 shadow-md shadow-gray-100">
                        <table className="w-full">
                            <thead>
                                <tr className="bg-gray-50 text-black">
                                    <th className="py-2 px-3">제품명</th>
                                    <th className="py-2 px-3">이미지</th>
                                    <th className="py-2 px-3">제품 가격</th>
                                    <th className="py-2 px-3">제품 수량</th>
                                </tr>
                            </thead>
                            <tbody>
                                {purchaseItems.map((item, idx) => (
                                    <tr
                                        className="text-center text-black border-t border-gray-200 last:border-b last:border-gray-200"
                                        style={{ borderTopWidth: idx === 0 ? 0.5 : 0.5, borderBottomWidth: idx === purchaseItems.length - 1 ? 0.5 : 0 }}
                                        key={item.productId}
                                    >
                                        <td className="py-2 px-3">{item.productName}</td>
                                        <td className="py-2 px-3">
                                            <img
                                                src={item.imageUrl}
                                                alt={item.productName}
                                                className="w-32 h-32 object-cover mx-auto rounded-md border"
                                            />
                                        </td>
                                        <td className="py-2 px-3">{item.price.toLocaleString()}원</td>
                                        <td className="py-2 px-3">{item.quantity}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                    <h5 className="text-right text-lg font-semibold text-blue-700">
                        총 가격: {purchaseItems.reduce((sum, item) => sum + item.totalPrice, 0).toLocaleString()}원
                    </h5>
                </div>

                {/* 구매자 정보 */}
                <div>
                    <h2 className="text-xl font-bold mb-4 text-gray-900">구매자 정보</h2>
                    <div className="bg-gray-50 rounded-lg p-6 border border-gray-200">
                        <div className="flex flex-col md:flex-row gap-2">
                            <label className="flex-1 flex flex-col font-medium text-gray-900">
                                <span className="mb-1">이름</span>
                                <input
                                    type="text"
                                    name="purchaser.name"
                                    className="mt-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900 bg-white w-full"
                                />
                            </label>
                            <label className="flex-1 flex flex-col font-medium text-gray-900">
                                <span className="mb-1">이메일</span>
                                <input
                                    type="email"
                                    name="purchaser.email"
                                    className="mt-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900 bg-white w-full"
                                />
                            </label>
                        </div>
                    </div>
                </div>

                {/* 배송지 정보 */}
                <div>
                    <h2 className="text-xl font-bold mb-4 text-gray-900">배송지 정보</h2>
                    <div className="bg-gray-50 rounded-lg p-6 border border-gray-200">
                        <div className="flex flex-col md:flex-row gap-2">
                            <label className="flex-1 flex flex-col font-medium text-gray-900">
                                <span className="mb-1">이름</span>
                                <input
                                    type="text"
                                    name="receiver.name"
                                    className="mt-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900 bg-white w-full"
                                />
                            </label>
                            <label className="flex-1 flex flex-col font-medium text-gray-900">
                                <span className="mb-1">전화번호</span>
                                <input
                                    type="tel"
                                    name="receiver.phoneNumber"
                                    className="mt-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900 bg-white w-full"
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
                                        className="mt-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900 bg-white"
                                    />
                                </label>
                                <label className="flex flex-col font-medium text-gray-900 flex-1">
                                    <span className="mb-1">주소</span>
                                    <input
                                        type="text"
                                        name="receiver.address"
                                        className="mt-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900 bg-white"
                                    />
                                </label>
                            </div>
                        </div>
                    </div>
                </div>

                {/* 결제 정보 */}
                <div>
                    <h2 className="text-xl font-bold mb-4 text-gray-900">결제 정보</h2>
                    <div className="bg-gray-50 rounded-lg p-6 border border-gray-200">
                        <div className="flex flex-col md:flex-row gap-2">
                            {/* 라디오 버튼 3개가 이름 입력칸과 동일한 너비로 각각 분할 */}
                            <div className="flex flex-row flex-1 gap-2 min-w-0">
                                {topOpts.map((opt, idx) => (
                                    <label
                                        key={opt.id}
                                        className="flex items-center space-x-2 cursor-pointer px-3 py-2 border rounded-md hover:bg-gray-50 font-medium text-gray-900 w-full"
                                        style={{ minWidth: 0, flex: 1 }}
                                    >
                                        <input
                                            type="radio"
                                            name="selectedTopOptId"
                                            value={opt.id}
                                            checked={selectedTopOptId === opt.id}
                                            onChange={() => setSelectedTopOptId(opt.id)}
                                            className="accent-blue-600"
                                        />
                                        <span className="truncate">{opt.name}</span>
                                    </label>
                                ))}
                            </div>
                            {/* 상세 셀렉트는 전화번호 입력칸과 동일한 크기 (w-full, max-w-[320px]) */}
                            {detailOpts.length > 0 && (
                                <div className="flex-1 md:ml-2 max-w-[320px] min-w-0">
                                    <select
                                        name="paymentOptionId"
                                        value={selectedDetailOptId ?? ''}
                                        onChange={(e) => setSelectedDetailOptId(e.target.value ? parseInt(e.target.value, 10) : null)}
                                        required
                                        className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900 bg-white"
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
                                </div>
                            )}
                        </div>
                    </div>
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
        </div>
    );
}