"use client";
import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";

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

  useEffect(() => {
    if (!id) return;
    fetch(`/api/products/${id}`)
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

  const handlePurchase = () => {
    router.push(`/purchase?id=${product?.id}&quantity=1`);
  };

  const handleAddToCart = () => {
    if (!product) return;
    const cartStr = localStorage.getItem('cart');
    let cart = cartStr ? JSON.parse(cartStr) : [];
    const exists = cart.some((item: any) => item.productId === product.id);
    if (exists) {
      alert('이미 장바구니에 존재하는 상품입니다.');
      return;
    }
    cart.push({
      productId: product.id,
      quantity: 1
    });
    localStorage.setItem('cart', JSON.stringify(cart));
    alert('장바구니에 추가되었습니다.');
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
        <div className="flex justify-between mt-10 space-x-4">
          <button
            onClick={() => router.push('/products/list')}
            className="flex-1 text-center bg-gray-400 hover:bg-gray-500 text-white py-3 cursor-pointer rounded-lg transition"
          >
            목록으로
          </button>
          <button
            onClick={handlePurchase}
            className="flex-1 text-center bg-blue-600 hover:bg-blue-700 text-white py-3 cursor-pointer rounded-lg transition"
          >
            바로 구매
          </button>
          <button
            onClick={handleAddToCart}
            className="flex-1 text-center bg-green-600 hover:bg-green-700 text-white py-3 cursor-pointer rounded-lg transition"
          >
            장바구니
          </button>
        </div>
      </div>
    </div>
  );
}