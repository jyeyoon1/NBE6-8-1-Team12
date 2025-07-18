'use client';

import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';

interface Product {
    id: number;
    productName: string;
    description: string;
    price: number;
    totalQuantity: number;
    imageUrl: string;
}

export default function ProductDetailPage() {
    const { id } = useParams();
    const router = useRouter();
    const [product, setProduct] = useState<Product | null>(null);

    useEffect(() => {
        if (!id) return;

        fetch(`/api/products/${id}`)
            .then(response => response.json())
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

  const handleDelete = async () => {
    if (!confirm('정말 삭제하시겠습니까?')) return;

    try {
      const response = await fetch(`/api/products/${product?.id}`, {
        method: 'DELETE',
      });

      const result = await response.json();

      if (result.resultCode && result.resultCode.startsWith('200')) {
        alert('상품이 삭제되었습니다.');
        router.push('/products/list');
      } else {
        alert('삭제에 실패했습니다.');
      }
    } catch (error) {
      alert('삭제 중 오류가 발생했습니다.');
    }
  };
    

    if (!product) {
        return <div className="text-center mt-20">상품 정보를 불러오는 중...</div>;
    }

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
                onClick={() => router.push(`/products/edit/${product.id}`)}
                className="flex-1 text-center bg-yellow-400 hover:bg-yellow-500 text-white  cursor-pointer py-3 rounded-lg transition"
              >
                수정
              </button>
              <button
                onClick={handleDelete}
                className="flex-1 bg-red-500 hover:bg-red-600 text-white py-3 rounded-lg  cursor-pointer transition"
              >
                삭제
              </button>
            </div>
          </div>
        </div>
    );

}