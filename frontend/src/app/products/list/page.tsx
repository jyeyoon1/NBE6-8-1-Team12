
"use client";

import { useEffect, useState } from 'react';
import { useSearchParams, useRouter } from 'next/navigation';
import Link from 'next/link';
import { useAuth } from '@/contexts/AuthContext';

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
  pageNumber: number;
  pageSize: number;
  totalPages: number;
  totalElements: number;
  isLast: boolean;
}

export default function ProductListPage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const router = useRouter();
  const [error, setError] = useState<string | null>(null);
  const { isAuthenticated } = useAuth();

  const [pageInfo, setPageInfo] = useState({
    pageNumber: 0,
    totalPages: 0,
    totalElements: 0,
    isLast: true
  });

  const searchParams = useSearchParams();
  const currentPage = parseInt(searchParams.get('page') || '0');

  // 상품 목록을 불러오는 useEffect
  useEffect(() => {
    const fetchProducts = async () => {
      try {
        setLoading(true); // 로딩 시작
        setError(null); // 이전 에러 초기화

        const response = await fetch(`http://localhost:8080/api/products?page=${currentPage}&size=10`, {
          method: "GET",
          // credentials: "include", // 로그인이 필요없으므로 제거할 수 있음
          headers: { "Content-Type": "application/json" },
        });

        if (!response.ok) {
          // 로그인 리다이렉트 로직 제거
          throw new Error(`서버 오류: ${response.status}`);
        }

        const json: RsData<PageResponseDto> = await response.json();

        if (
          json &&
          typeof json === "object" &&
          "data" in json &&
          Array.isArray(json.data.content)
        ) {
          setProducts(json.data.content);
          setPageInfo({
            pageNumber: json.data.pageNumber,
            totalPages: json.data.totalPages,
            totalElements: json.data.totalElements,
            isLast: json.data.isLast
          });
        } else {
          console.error("상품 목록이 배열이 아닙니다:", json);
          setError("상품 데이터 형식이 올바르지 않습니다.");
        }
      } catch (err) {
        if (err instanceof Error) {
          setError(err.message);
        } else {
          setError("알 수 없는 오류가 발생했습니다.");
        }
      } finally {
        setLoading(false); // 성공 또는 실패와 관계없이 로딩 종료
      }
    };

    fetchProducts();
  }, [router, currentPage]); // `router`를 의존성 배열에 추가 (내부에서 사용하므로)

  // --- 여기부터가 컴포넌트의 렌더링 로직입니다. ---

  // 로딩 중일 때 UI
  if (loading) {
    return (
      <div className="bg-gray-200 pt-20 min-h-screen w-full flex items-center justify-center px-4">
        <div className="text-lg">로딩 중...</div>
      </div>
    );
  }

  // 에러가 발생했을 때 UI
  if (error) {
    return (
      <div className="bg-gray-200 pt-20 min-h-screen flex items-center justify-center px-4">
        <div className="text-red-500 text-lg">{error}</div>
      </div>
    );
  }

  // 정상적으로 데이터를 불러왔을 때 UI
  return (
    <div className="bg-gray-200 pt-20 min-h-screen w-full flex items-center justify-center px-4">
      <div className="bg-white rounded-xl shadow-lg w-full max-w-4xl p-10">
        <h1 className="text-3xl font-bold text-center mb-8">상품 목록</h1>

        {/* 상품 추가 및 구매 페이지 버튼 */}
        <div className="flex justify-end mb-6">
          {isAuthenticated && (
            <a
              href="/products/form"
              className="bg-gray-800 text-white px-4 py-2 rounded hover:bg-gray-700 transition-colors mr-4"
            >
              상품 추가
            </a>
          )}
          <a
            href="/purchase"
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-500 transition-colors"
          >
            구매 페이지
          </a>
        </div>

        {products.length === 0 ? (
          <p className="text-gray-500 text-center">등록된 상품이 없습니다.</p>
        ) : (
          <ul className="space-y-4">
            {products.map((product) => (
              <li key={product.id}>
                <Link href={`/products/${product.id}`}>
                  <div className="flex items-center p-4 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors">
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
                  </div>
                </Link>
              </li>
            ))}
          </ul>
        )}
        {/* 페이징 UI */}
        <div className="flex justify-center items-center mt-8 space-x-2">
          {/* 이전 버튼 */}
          <button
            onClick={() => {
              if (pageInfo.pageNumber > 0) {
                router.push(`/products/list?page=${pageInfo.pageNumber - 1}`);
              }
            }}
            disabled={pageInfo.pageNumber === 0}
            className={`px-3 py-2 rounded ${pageInfo.pageNumber === 0
              ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
              : 'bg-gray-300 text-gray-700 hover:bg-gray-400'
              }`}
          >
            이전
          </button>

          {/* 현재 페이지 정보 */}
          <span className="px-3 py-2 bg-blue-500 text-white rounded font-bold">
            {pageInfo.pageNumber + 1}
          </span>

          {/* 다음 버튼 */}
          <button
            onClick={() => {
              if (!pageInfo.isLast) {
                router.push(`/products/list?page=${pageInfo.pageNumber + 1}`);
              }
            }}
            disabled={pageInfo.isLast}
            className={`px-3 py-2 rounded ${pageInfo.isLast
              ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
              : 'bg-gray-300 text-gray-700 hover:bg-gray-400'
              }`}
          >
            다음
          </button>
        </div>
      </div>
    </div>
  );
}