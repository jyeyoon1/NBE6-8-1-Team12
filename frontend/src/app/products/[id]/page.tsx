'use client';

import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { useRequireAuth } from "@/hooks/useRequireAuth";
import { useAuth } from '@/contexts/AuthContext';

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
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState<Product | null>(null);
  const { isAuthenticated } = useAuth(); // 로그인 상태 받아오기

  useEffect(() => {
    if (!id) return;

    fetch(`/api/products/${id}`)
      .then((res) => res.json())
      .then((data) => {
        if (data.resultCode && data.resultCode.startsWith('200')) {
          setProduct(data.data as Product);
          setFormData(data.data as Product);
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
    } catch {
      alert('삭제 중 오류가 발생했습니다.');
    }
  };

  const handleSave = async () => {
    if (!formData) return;

    try {
      const response = await fetch(`/api/products/${formData.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        alert('상품이 수정되었습니다.');
        const updated = await response.json();
        setProduct(updated.data);
        setIsEditing(false);
      } else {
        alert('수정 실패');
      }
    } catch {
      alert('수정 중 오류가 발생했습니다.');
    }
  };

  if (!product || !formData) {
    return <div className="text-center mt-20">상품 정보를 불러오는 중...</div>;
  }

  return (
    <div className="bg-gray-200 pt-20 min-h-screen w-full flex items-center justify-center px-4">
      <div className="bg-white max-w-2xl w-full rounded-xl shadow-lg p-8">
        <img
          src={formData.imageUrl}
          alt="상품 이미지"
          className="w-full max-h-96 object-cover rounded-xl mb-6"
        />

        {isEditing ? (
          <>
            <div>
              <input
                type="text"
                value={formData.productName}
                onChange={(e) => setFormData({ ...formData, productName: e.target.value })}
                className="w-full border px-2 py-1 rounded mb-1"
              />
              <p className="text-sm text-gray-500 mb-2">상품의 이름을 입력해주세요. (예: Columbia Nariñó)</p>
            </div>

            <div>
              <textarea
                value={formData.description}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                className="w-full border px-2 py-1 rounded mb-1"
              />
              <p className="text-sm text-gray-500 mb-2">상품에 대한 간단한 설명을 입력해주세요. 최대 1000자까지 가능합니다.</p>
            </div>

            <div>
              <input
                type="number"
                value={formData.price}
                onChange={(e) => setFormData({ ...formData, price: Number(e.target.value) })}
                className="w-full border px-2 py-1 rounded mb-1"
              />
              <p className="text-sm text-gray-500 mb-2">상품 가격을 숫자로 입력해주세요. 0원 이상이어야 합니다.</p>
            </div>

            <div>
              <input
                type="number"
                value={formData.totalQuantity}
                onChange={(e) => setFormData({ ...formData, totalQuantity: Number(e.target.value) })}
                className="w-full border px-2 py-1 rounded mb-1"
              />
              <p className="text-sm text-gray-500 mb-2">현재 보유 중인 상품 수량을 입력하세요. 0 이상이어야 합니다.</p>
            </div>

            <div>
              <input
                type="text"
                value={formData.imageUrl}
                onChange={(e) => setFormData({ ...formData, imageUrl: e.target.value })}
                className="w-full border px-2 py-1 rounded mb-1"
              />
              <p className="text-sm text-gray-500 mb-2">상품 이미지를 보여줄 URL을 입력해주세요.</p>
            </div>

            <div className="flex space-x-4 mt-4">
              <button
                onClick={handleSave}
                className="flex-1 bg-green-600 hover:bg-green-700 text-white py-3 rounded-lg transition"
              >
                저장
              </button>
              <button
                onClick={() => {
                  setFormData(product);
                  setIsEditing(false);
                }}
                className="flex-1 bg-gray-400 hover:bg-gray-500 text-white py-3 rounded-lg transition"
              >
                취소
              </button>
            </div>
          </>
        ) : (
          <>
            {/* 상품 정보 */}
            <div className="text-3xl font-bold text-gray-900">{product.productName}</div>
            <div className="text-2xl font-extrabold text-gray-800 mt-4">
              {product.price.toLocaleString()}원
            </div>
            <div className="text-lg text-gray-600 mt-4">{product.description}</div>
            <div className="text-sm text-gray-500 mt-1">재고: {product.totalQuantity}개</div>

            <div className="flex justify-between mt-10 space-x-4">
              <button
                onClick={() => router.push('/products/list')}
                className={`${!isAuthenticated ? 'w-full' : 'flex-1'} text-center bg-gray-400 hover:bg-gray-500 text-white py-3 cursor-pointer rounded-lg transition`}
              >
                목록으로
              </button>

              {isAuthenticated && (
                <button
                  onClick={() => setIsEditing(true)}
                  className="flex-1 text-center bg-yellow-400 hover:bg-yellow-500 text-white cursor-pointer py-3 rounded-lg transition"
                >
                  수정
                </button>
              )}
            </div>
          </>
        )}
      </div>
    </div>
  );
}