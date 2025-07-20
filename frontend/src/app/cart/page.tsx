"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

interface LocalCartItem {
    productId: number;
    quantity: number;
}

interface CartItem {
    productId: number;
    quantity: number;
    productName: string;
    price: number;
    imageUrl: string;
    totalQuantity: number; // 재고 수량 추가
    status: 'ON_SALE' | 'OUT_OF_STOCK' | 'NOT_FOR_SALE'; // 상품 상태 추가
}

export default function CartPage() {
    const router = useRouter();
    const [cartItems, setCartItems] = useState<CartItem[]>([]);
    const [selected, setSelected] = useState<number[]>([]); // 선택된 상품의 productId 배열

    // localStorage에서 cart 불러오기 + 상품 정보 fetch
    useEffect(() => {
        const fetchCartItems = async () => {
            try {
                const cartStr = localStorage.getItem("cart");
                const localCart: LocalCartItem[] = cartStr ? JSON.parse(cartStr) : [];

                if (localCart.length === 0) {
                    setCartItems([]);
                    return;
                }

                // 각 상품 최신 정보 fetch (백엔드 API 사용)
                const fetchedItems = await Promise.all(
                    localCart.map(async (item) => {
                        try {
                            const res = await fetch(`http://localhost:8080/api/products/${item.productId}`);
                            if (!res.ok) {
                                console.warn(`제품 ID ${item.productId}의 정보를 가져오지 못했습니다. (상태: ${res.status})`);
                                return []; // 유효하지 않은 아이템은 빈 배열 반환
                            }
                            const data = await res.json();
                            // 백엔드 응답 구조에 맞게 수정
                            if (data && data.data) {
                                return [{
                                    productId: item.productId,
                                    quantity: item.quantity,
                                    productName: data.data.productName,
                                    price: data.data.price,
                                    imageUrl: data.data.imageUrl,
                                    totalQuantity: data.data.totalQuantity,
                                    status: data.data.status,
                                }];
                            } else {
                                console.warn(`제품 ID ${item.productId}의 유효한 데이터가 없습니다.`);
                                return []; // 유효하지 않은 데이터는 빈 배열 반환
                            }
                        } catch (fetchError) {
                            console.error(`제품 ID ${item.productId}의 정보를 가져오는 중 오류 발생:`, fetchError);
                            return []; // 오류 발생 시 빈 배열 반환
                        }
                    })
                )
                    // flatMap으로 배열 평탄화 및 빈 배열 제거
                    .then(results => results.flatMap(result => result));

                setCartItems(fetchedItems);

                // 유효한 아이템만 남기고 localStorage를 업데이트
                const updatedLocalCart = fetchedItems.map(({ productId, quantity }) => ({ productId, quantity }));
                localStorage.setItem("cart", JSON.stringify(updatedLocalCart));
            } catch (error) {
                console.error("장바구니 데이터를 불러오는데 실패했습니다:", error);
                setCartItems([]);
                localStorage.removeItem("cart");
            }
        };
        fetchCartItems();
    }, []);

    // 수량 변경 (실시간 재고 확인)
    const updateQuantity = async (productId: number, newQuantity: number) => {
        if (newQuantity < 1) return;

        const item = cartItems.find(item => item.productId === productId);
        if (!item) return;

        try {
            // 백엔드에서 최신 재고 정보 확인
            const res = await fetch(`http://localhost:8080/api/products/${productId}`);
            if (!res.ok) {
                alert('상품 정보를 가져올 수 없습니다.');
                return;
            }

            const data = await res.json();
            const currentProduct = data.data;

            // 상품 상태 검증
            if (currentProduct.status === 'OUT_OF_STOCK') {
                alert('재고가 소진된 상품입니다.');
                return;
            }

            if (currentProduct.status === 'NOT_FOR_SALE') {
                alert('판매 중지된 상품입니다.');
                return;
            }

            // 실시간 재고 검증
            if (newQuantity > currentProduct.totalQuantity) {
                alert(`재고가 부족합니다. 최대 ${currentProduct.totalQuantity}개까지 구매 가능합니다.`);
                return;
            }

            // 재고 정보 업데이트
            const updated = cartItems.map((item) =>
                item.productId === productId
                    ? {
                        ...item,
                        quantity: newQuantity,
                        totalQuantity: currentProduct.totalQuantity,
                        status: currentProduct.status
                    }
                    : item
            );
            setCartItems(updated);

            // localStorage에도 반영
            try {
                localStorage.setItem(
                    "cart",
                    JSON.stringify(updated.map(({ productId, quantity }) => ({ productId, quantity })))
                );
            } catch (error) {
                console.error("장바구니 저장에 실패했습니다:", error);
                alert("장바구니 저장에 실패했습니다. 다시 시도해주세요.");
            }
        } catch (error) {
            console.error('재고 확인 중 오류:', error);
            alert('재고를 확인할 수 없습니다. 다시 시도해주세요.');
        }
    };

    // 상품 삭제
    const removeItem = (productId: number) => {
        if (!confirm("이 상품을 장바구니에서 삭제하시겠습니까?")) return;
        const updated = cartItems.filter((item) => item.productId !== productId);
        setCartItems(updated);
        // localStorage에도 반영
        try {
            localStorage.setItem(
                "cart",
                JSON.stringify(updated.map(({ productId, quantity }) => ({ productId, quantity })))
            );
        } catch (error) {
            console.error("장바구니 저장에 실패했습니다:", error);
            alert("장바구니 저장에 실패했습니다. 다시 시도해주세요.");
        }
        setSelected(selected.filter((id) => id !== productId));
    };

    // 체크박스 선택/해제
    const handleSelect = (productId: number) => {
        setSelected((prev) =>
            prev.includes(productId)
                ? prev.filter((id) => id !== productId)
                : [...prev, productId]
        );
    };

    // 구매하기 (실시간 재고 확인)
    const handlePurchase = async () => {
        if (selected.length === 0) {
            alert("상품을 선택해주세요.");
            return;
        }

        const selectedItems = cartItems.filter((item) => selected.includes(item.productId));

        // 실시간 재고 및 상태 검증
        for (const item of selectedItems) {
            try {
                const res = await fetch(`http://localhost:8080/api/products/${item.productId}`);
                if (!res.ok) {
                    alert(`${item.productName}의 정보를 가져올 수 없습니다.`);
                    return;
                }

                const data = await res.json();
                const currentProduct = data.data;

                if (currentProduct.status === 'OUT_OF_STOCK') {
                    alert(`${item.productName}은(는) 재고가 소진되었습니다.`);
                    return;
                }

                if (currentProduct.status === 'NOT_FOR_SALE') {
                    alert(`${item.productName}은(는) 판매가 중지되었습니다.`);
                    return;
                }

                if (item.quantity > currentProduct.totalQuantity) {
                    alert(`${item.productName}의 재고가 부족합니다. (재고: ${currentProduct.totalQuantity}개, 요청: ${item.quantity}개)`);
                    return;
                }
            } catch (error) {
                console.error(`${item.productName} 재고 확인 중 오류:`, error);
                alert(`${item.productName}의 재고를 확인할 수 없습니다.`);
                return;
            }
        }

        const purchaseCartItems = selectedItems.map(({ productId, quantity }) => ({ productId, quantity }));

        if (!purchaseCartItems || purchaseCartItems.length === 0) {
            alert("선택한 상품을 찾을 수 없습니다.");
            return;
        }
        const purchaseData = JSON.stringify(purchaseCartItems);

        router.push(`/purchase?cartItems=${encodeURIComponent(purchaseData)}`);
    };

    // 선택된 상품만 합산
    const selectedItems = cartItems.filter(item => selected.includes(item.productId));
    const totalPrice = selectedItems.reduce((sum, item) => sum + item.price * item.quantity, 0);

    return (
        <div className="bg-gray-200 pt-20 min-h-screen w-full px-4">
            <div className="max-w-4xl mx-auto">
                <h1 className="text-3xl font-bold text-gray-900 mb-8">장바구니</h1>
                {cartItems.length === 0 ? (
                    <div className="bg-white rounded-xl shadow-lg p-8 text-center">
                        <p className="text-gray-500 text-lg mb-4">장바구니가 비어있습니다.</p>
                        <button
                            onClick={() => router.push("/products/list")}
                            className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg transition"
                        >
                            상품 목록으로 가기
                        </button>
                    </div>
                ) : (
                    <>
                        <div className="bg-white rounded-xl shadow-lg p-8 mb-6">
                            {/* 전체 선택/해제 체크박스 */}
                            <div className="flex items-center mb-4">
                                <input
                                    type="checkbox"
                                    id="selectAll"
                                    checked={selected.length === cartItems.length && cartItems.length > 0}
                                    onChange={() => {
                                        if (selected.length === cartItems.length) {
                                            setSelected([]);
                                        } else {
                                            setSelected(cartItems.map(item => item.productId));
                                        }
                                    }}
                                    className="mr-2 w-5 h-5 accent-blue-600"
                                />
                                <label htmlFor="selectAll" className="text-gray-700 font-medium cursor-pointer">
                                    전체 선택
                                </label>
                                <span className="ml-2 text-gray-500 text-sm">
                                    ({selected.length} / {cartItems.length})
                                </span>
                            </div>
                            {cartItems.map((item) => (
                                <div key={item.productId} className="flex items-center border-b border-gray-200 py-4 last:border-b-0">
                                    <input
                                        type="checkbox"
                                        checked={selected.includes(item.productId)}
                                        onChange={() => handleSelect(item.productId)}
                                        className="mr-4 w-5 h-5 accent-blue-600 disabled:opacity-50 disabled:cursor-not-allowed"
                                        disabled={item.status === 'OUT_OF_STOCK' || item.status === 'NOT_FOR_SALE'}
                                    />
                                    <img
                                        src={item.imageUrl}
                                        alt={item.productName}
                                        className="w-20 h-20 object-cover rounded-lg mr-4 cursor-pointer"
                                        onClick={() => router.push(`/products/${item.productId}`)}
                                    />
                                    <div className="flex-1">
                                        <h3
                                            className="text-lg font-semibold text-blue-700 hover:underline cursor-pointer"
                                            onClick={() => router.push(`/products/${item.productId}`)}
                                        >
                                            {item.productName}
                                        </h3>
                                        <p className="text-gray-600">{item.price.toLocaleString()}원</p>
                                        <div className="flex items-center space-x-2 mt-1">
                                            <span className="text-sm text-gray-500">재고: {item.totalQuantity}개</span>
                                            {item.status === 'OUT_OF_STOCK' && (
                                                <span className="text-xs bg-red-100 text-red-800 px-2 py-1 rounded">재고소진</span>
                                            )}
                                            {item.status === 'NOT_FOR_SALE' && (
                                                <span className="text-xs bg-gray-100 text-gray-800 px-2 py-1 rounded">판매중지</span>
                                            )}
                                            {item.quantity > item.totalQuantity && (
                                                <span className="text-xs bg-orange-100 text-orange-800 px-2 py-1 rounded">재고부족</span>
                                            )}
                                        </div>
                                    </div>
                                    <div className="flex items-center space-x-2 mr-4">
                                        <button
                                            onClick={() => updateQuantity(item.productId, item.quantity - 1)}
                                            className="w-8 h-8 bg-gray-200 hover:bg-gray-300 rounded-full flex items-center justify-center disabled:opacity-50 disabled:cursor-not-allowed"
                                            disabled={item.quantity <= 1}
                                        >
                                            -
                                        </button>
                                        <span className="w-12 text-center">{item.quantity}</span>
                                        <button
                                            onClick={() => updateQuantity(item.productId, item.quantity + 1)}
                                            className="w-8 h-8 bg-gray-200 hover:bg-gray-300 rounded-full flex items-center justify-center disabled:opacity-50 disabled:cursor-not-allowed"
                                            disabled={item.quantity >= item.totalQuantity || item.status !== 'ON_SALE'}
                                        >
                                            +
                                        </button>
                                    </div>
                                    <div className="text-right mr-4">
                                        <p className="text-lg font-semibold text-gray-900">
                                            {(item.price * item.quantity).toLocaleString()}원
                                        </p>
                                    </div>
                                    <button
                                        onClick={() => removeItem(item.productId)}
                                        className="text-red-500 hover:text-red-700 px-3 py-1 rounded"
                                    >
                                        삭제
                                    </button>
                                </div>
                            ))}
                        </div>
                        <div className="bg-white rounded-xl shadow-lg p-8">
                            <div className="flex justify-between items-center mb-6">
                                <h2 className="text-2xl font-bold text-gray-900">총 결제금액</h2>
                                <p className="text-3xl font-bold text-blue-600">
                                    {totalPrice.toLocaleString()}원
                                </p>
                            </div>
                            <div className="flex space-x-4">
                                <button
                                    onClick={() => router.push("/products/list")}
                                    className="flex-1 bg-gray-400 hover:bg-gray-500 text-white py-3 rounded-lg transition"
                                >
                                    계속 쇼핑
                                </button>
                                <button
                                    onClick={handlePurchase}
                                    className="flex-1 bg-blue-600 hover:bg-blue-700 text-white py-3 rounded-lg transition"
                                >
                                    구매하기
                                </button>
                            </div>
                        </div>
                    </>
                )}
            </div>
        </div>
    );
} 