import { useEffect, useState } from 'react';

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

  useEffect(() => {
    fetch('http://localhost:8080/api/products/list', {
      method: 'GET',
      credentials: 'include', // 세션 로그인 유지용
    })
      .then((res) => res.json())
      .then((data) => setProducts(data))
      .catch((err) => console.error('상품 목록 불러오기 실패:', err));
  }, []);

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">상품 목록</h1>
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
    </div>
  );
}