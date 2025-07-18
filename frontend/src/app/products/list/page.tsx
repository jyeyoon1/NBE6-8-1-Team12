'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';

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

interface PageResponseDto {
  content: Product[];
  PageNumber: number;
  PageSize: number;
  totalPages: number;
  totalElements: boolean;
  isLast: boolean;
}

export default function ProductListPage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const router = useRouter();
  const [error, setError] = useState<string | null>(null);

  // 상품 목록을 불러오는 useEffect
  useEffect(() => {
    const fetchProducts = async () => {
      try {
        setLoading(true);
        setError(null);

        const response = await fetch('/api/products', {
          method: 'GET',
          credentials: 'include',
          headers: { 'Content-Type': 'application/json' },
        });

        if (!response.ok) {
          if (response.status === 401 || response.status === 403) {
            router.push('/member/login');
            return;
          }
          throw new Error(`서버 오류: ${response.status}`);
        }


        const json: RsData<PageResponseDto> = await response.json();

        if (json && typeof json === 'object' &&
          'data' in json &&
          Array.isArray(json.data.content)
        ) {
          setProducts(json.data.content);
        } else {
          console.error('상품 목록이 배열이 아닙니다:', json);
          setError('상품 데이터 형식이 올바르지 않습니다.');

          // 컴포넌트가 마운트될 때 상품 목록을 불러옵니다.
          fetchProducts();;
        }, []);

  if (loading) {
    return (
      <div className="bg-gray-200 pt-20 min-h-screen w-full flex items-center justify-center px-4">
        <div className="text-lg">로딩 중...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-gray-200 pt-20 min-h-screen flex items-center justify-center px-4">
        <div className="text-red-500 text-lg">{error}</div>
      </div>
    );
  }

  return (
    <div className="bg-gray-200 pt-20 min-h-screen w-full flex items-center justify-center px-4">
      <div className="bg-white rounded-xl shadow-lg w-full max-w-4xl p-10">
        <h1 className="text-3xl font-bold text-center mb-8">상품 목록</h1>

        <div className="flex justify-end mb-6">
          <a
            href="/products/form"
            className="bg-gray-800 text-white px-4 py-2 rounded hover:bg-gray-700 transition-colors"
          >
            상품 추가
          </a>
        </div>

        {products.length === 0 ? (
          <p className="text-gray-500 text-center">등록된 상품이 없습니다.</p>
        ) : (
          <ul className="space-y-4">
            {products.map((product) => (
              <li key={product.id}>
                <a
                  href={`/api/products/${product.id}`}
                  className="flex items-center p-4 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors"
                >
                  <img
                    src={product.imageUrl}
                    alt={product.productName}
                    className="w-20 h-20 rounded-md object-cover bg-gray-200 mr-6"
                  />
                  <div className="flex-grow">
                    <div className="text-lg font-semibold">{product.productName}</div>
                    <div className="text-sm text-gray-500 mb-1">{product.description}</div>
                    <div className="text-base font-bold text-gray-700">
                      {product.price.toLocaleString()}원
                    </div>
                  </div>
                </a>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}