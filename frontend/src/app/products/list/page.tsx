"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

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
        setLoading(true); // 로딩 시작
        setError(null); // 이전 에러 초기화

        const response = await fetch("/api/products", {
          method: "GET",
          credentials: "include",
          headers: { "Content-Type": "application/json" },
        });

        if (!response.ok) {
          if (response.status === 401 || response.status === 403) {
            router.push("/member/login");
            return;
          }
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
  }, [router]); // `router`를 의존성 배열에 추가 (내부에서 사용하므로)

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
                  href={`/api/products/${product.id}`} // Next.js 내부 라우팅을 사용한다면 `/products/${product.id}`처럼 변경하는 것을 고려해보세요.
                  className="flex items-center p-4 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors"
                >
                  <img
                    src={product.imageUrl}
                    alt={product.productName}
                    className="w-20 h-20 rounded-md object-cover bg-gray-200 mr-6"
                  />
                  <div className="flex-grow">
                    <div className="text-lg font-semibold">
                      {product.productName}
                    </div>
                    <div className="text-sm text-gray-500 mb-1">
                      {product.description}
                    </div>
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
