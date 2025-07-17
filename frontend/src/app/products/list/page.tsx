'use client'; // 꼭 필요함 (Client Component로 선언)

import { useEffect, useState } from 'react';

interface RsData<T> {
  resultCode: string;
  statusCode: number;
  msg: string;
  data: T;
}

interface Product {
  id: number;
  productName: string;
  price: number;
  totalQuantity: number;
  description: string;
  imageUrl: string;
}

export default function ProductListPage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        setLoading(true);
        setError(null);

        const response = await fetch('http://localhost:8080/api/products/list', {
          method: 'GET',
          credentials: 'include', // 세션 로그인 유지용
          headers: {
            'Content-Type': 'application/json',
          },
        });

        console.log('응답 상태:', response.status);
        console.log('응답 헤더:', response.headers);

        if (!response.ok) {
          if (response.status === 401) {
            throw new Error('인증이 필요합니다. 로그인해주세요.');
          } else if (response.status === 403) {
            throw new Error('접근 권한이 없습니다.');
          } else {
            throw new Error(`서버 오류: ${response.status}`);
          }
        }

        const json = await response.json();
        console.log('받은 데이터:', json);

        // RsData 구조인지 확인
        if (json && typeof json === 'object' && 'data' in json) {
          if (Array.isArray(json.data)) {
            setProducts(json.data);
          } else {
            console.error('상품 목록이 배열이 아닙니다:', json);
            setError('상품 데이터 형식이 올바르지 않습니다.');
          }
        } else {
          // 직접 배열이 반환된 경우
          if (Array.isArray(json)) {
            setProducts(json);
          } else {
            console.error('예상하지 못한 응답 형식:', json);
            setError('예상하지 못한 응답 형식입니다.');
          }
        }
      } catch (err) {
        console.error('상품 목록 불러오기 실패:', err);
        setError(err instanceof Error ? err.message : '알 수 없는 오류가 발생했습니다.');
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  if (loading) {
    return (
      <div className="p-6 flex justify-center items-center min-h-screen">
        <div className="text-lg">로딩 중...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-6 flex justify-center items-center min-h-screen">
        <div className="text-red-500 text-lg">{error}</div>
      </div>
    );
  }

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">상품 목록</h1>
      {products.length === 0 ? (
        <p className="text-gray-500">등록된 상품이 없습니다.</p>
      ) : (
        <div className="grid grid-cols-2 gap-6">
          {products.map((product) => (
            <div key={product.id} className="border rounded-lg p-4 shadow-md">
              <img
                src={product.imageUrl}
                alt={product.productName}
                className="w-full h-48 object-cover mb-2"
              />
              <h2 className="text-lg font-semibold">{product.productName}</h2>
              <p className="text-gray-600">{product.description}</p>
              <p className="text-blue-600 font-bold mt-1">{product.price.toLocaleString()}원</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}