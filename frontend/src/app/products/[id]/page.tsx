"use client";
import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { useAuth } from "@/contexts/AuthContext";

interface Product {
  id: number;
  productName: string;
  description: string;
  price: number;
  totalQuantity: number;
  imageUrl: string;
  status: 'ON_SALE' | 'OUT_OF_STOCK' | 'NOT_FOR_SALE';
}

export default function ProductDetailPage() {
  const { id } = useParams();
  const router = useRouter();
  const [product, setProduct] = useState<Product | null>(null);
  const [quantity, setQuantity] = useState(1);
  const { isAdmin } = useAuth();

  useEffect(() => {
    if (!id) return;
    fetch(`http://localhost:8080/api/products/${id}`)
      .then(res => res.json())
      .then(data => {
        if (data.resultCode && data.resultCode.startsWith('200')) {
          setProduct(data.data as Product);
        } else {
          alert('상품을 찾을 수 없습니다.');
          router.push('/products/list');
        }
      })
      .catch(() => {
        alert('오류가 발생했습니다.');
        router.push('/products/list');
      });
  }, [id, router]);

  if (!product) return <div className="text-center mt-20">상품 정보를 불러오는 중...</div>;

  const handleQuantityChange = (val: number) => {
    if (!product) return;
    if (val < 1) setQuantity(1);
    else if (val > product.totalQuantity) {
      setQuantity(product.totalQuantity);
      alert(`재고가 부족합니다. 최대 ${product.totalQuantity}개까지 구매 가능합니다.`);
    }
    else setQuantity(val);
  };

  const handlePurchase = async () => {
    if (!product) return;

    try {
      // 실시간 재고 확인
      const res = await fetch(`http://localhost:8080/api/products/${product.id}`);
      if (!res.ok) {
        alert('상품 정보를 가져올 수 없습니다.');
        return;
      }

      const data = await res.json();
      const currentProduct = data.data;

      if (currentProduct.status === 'OUT_OF_STOCK') {
        alert('재고가 소진된 상품입니다.');
        return;
      }

      if (currentProduct.status === 'NOT_FOR_SALE') {
        alert('판매가 중지된 상품입니다.');
        return;
      }

      if (quantity > currentProduct.totalQuantity) {
        alert(`재고가 부족합니다. 최대 ${currentProduct.totalQuantity}개까지 구매 가능합니다.`);
        return;
      }

      router.push(`/purchase?id=${product?.id}&quantity=${quantity}`);
    } catch (error) {
      console.error('재고 확인 중 오류:', error);
      alert('재고를 확인할 수 없습니다. 다시 시도해주세요.');
    }
  };

  const handleAddToCart = async () => {
    if (!product) return;

    try {
      // 실시간 재고 확인
      const res = await fetch(`http://localhost:8080/api/products/${product.id}`);
      if (!res.ok) {
        alert('상품 정보를 가져올 수 없습니다.');
        return;
      }

      const data = await res.json();
      const currentProduct = data.data;

      if (currentProduct.status === 'OUT_OF_STOCK') {
        alert('재고가 소진된 상품입니다.');
        return;
      }

      if (currentProduct.status === 'NOT_FOR_SALE') {
        alert('판매가 중지된 상품입니다.');
        return;
      }

      if (quantity > currentProduct.totalQuantity) {
        alert(`재고가 부족합니다. 최대 ${currentProduct.totalQuantity}개까지 구매 가능합니다.`);
        return;
      }

      const cartStr = localStorage.getItem('cart');
      let cart = cartStr ? JSON.parse(cartStr) : [];
      const exists = cart.some((item: any) => item.productId === product.id);
      if (exists) {
        alert('이미 장바구니에 존재하는 상품입니다.');
        return;
      }
      cart.push({
        productId: product.id,
        quantity: quantity
      });
      localStorage.setItem('cart', JSON.stringify(cart));
      alert('장바구니에 추가되었습니다.');
    } catch (error) {
      console.error('재고 확인 중 오류:', error);
      alert('재고를 확인할 수 없습니다. 다시 시도해주세요.');
    }
  };

  return (
    <div className="bg-gray-200 pt-20 min-h-screen w-full flex items-center justify-center px-4">
      <div className="bg-white max-w-2xl w-full rounded-xl shadow-lg p-8">
        <img
          src={product.imageUrl}
          alt="상품 이미지"
          className="w-full max-h-96 object-cover rounded-xl mb-6"
        />
        <div className="text-3xl font-bold text-gray-900">{product.productName}</div>
        <div className="text-2xl font-extrabold text-gray-800 mt-4">
          {product.price.toLocaleString()}원
        </div>
        <div className="text-lg text-gray-600 mt-4">{product.description}</div>
        <div className="text-sm text-gray-500 mt-1">재고: {product.totalQuantity}개</div>
        {/* 수량 선택 및 구매/장바구니 버튼: 일반 사용자만 노출 */}
        {!isAdmin && (
          <>
            <div className="flex items-center mt-4 space-x-2">
              <span className="text-gray-700">수량:</span>
              <button
                type="button"
                className="px-2 py-1 bg-gray-300 rounded"
                onClick={() => handleQuantityChange(quantity - 1)}
                disabled={quantity <= 1}
              >
                -
              </button>
              <input
                type="number"
                min={1}
                max={product.totalQuantity}
                value={quantity}
                onChange={e => handleQuantityChange(Number(e.target.value))}
                className="w-16 text-center border rounded"
              />
              <button
                type="button"
                className="px-2 py-1 bg-gray-300 rounded"
                onClick={() => handleQuantityChange(quantity + 1)}
                disabled={quantity >= product.totalQuantity}
              >
                +
              </button>
            </div>
            <div className="flex justify-between mt-10 space-x-4">
              <button
                onClick={() => router.push('/products/list')}
                className="flex-1 text-center bg-gray-400 hover:bg-gray-500 text-white py-3 cursor-pointer rounded-lg transition"
              >
                목록으로
              </button>
              <button
                onClick={handlePurchase}
                className={`flex-1 text-center py-3 rounded-lg transition ${product.status === 'ON_SALE' && quantity <= product.totalQuantity
                  ? 'bg-blue-600 hover:bg-blue-700 text-white cursor-pointer'
                  : 'bg-gray-300 text-gray-500 cursor-not-allowed'
                  }`}
                disabled={product.status !== 'ON_SALE' || quantity > product.totalQuantity}
              >
                {product.status === 'OUT_OF_STOCK' ? '재고소진' :
                  product.status === 'NOT_FOR_SALE' ? '판매중지' :
                    quantity > product.totalQuantity ? '재고부족' : '바로 구매'}
              </button>
              <button
                onClick={handleAddToCart}
                className={`flex-1 text-center py-3 rounded-lg transition ${product.status === 'ON_SALE' && quantity <= product.totalQuantity
                  ? 'bg-green-600 hover:bg-green-700 text-white cursor-pointer'
                  : 'bg-gray-300 text-gray-500 cursor-not-allowed'
                  }`}
                disabled={product.status !== 'ON_SALE' || quantity > product.totalQuantity}
              >
                {product.status === 'OUT_OF_STOCK' ? '재고소진' :
                  product.status === 'NOT_FOR_SALE' ? '판매중지' :
                    quantity > product.totalQuantity ? '재고부족' : '장바구니'}
              </button>
            </div>
          </>
        )}
        {isAdmin && (
          <div className="flex justify-between mt-10 space-x-4">
            <button
              onClick={() => router.push('/products/list')}
              className="flex-1 text-center bg-gray-400 hover:bg-gray-500 text-white py-3 cursor-pointer rounded-lg transition"
            >
              목록으로
            </button>
          </div>
        )}
      </div>
    </div>
  );
}