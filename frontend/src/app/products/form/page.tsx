"use client";

import { useState, useEffect } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { useRequireAuth } from "@/hooks/useRequireAuth";

export default function ProductFormPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const { isAuthenticated, isLoading } = useRequireAuth();

  const productId = searchParams.get('id');
  const isEditMode = !!productId;

  const [form, setForm] = useState({
    productName: "",
    description: "",
    price: "",
    totalQuantity: "",
    imageUrl: "",
    status: "ON_SALE"
  });

  const [errorMsg, setErrorMsg] = useState("");
  const [isLoadingProduct, setIsLoadingProduct] = useState(false);

  // 기존 상품 정보 불러오기 (수정 모드일 때)
  useEffect(() => {
    if (isEditMode && productId) {
      const fetchProduct = async () => {
        setIsLoadingProduct(true);
        try {
          const response = await fetch(`http://localhost:8080/api/products/${productId}`);
          if (response.ok) {
            const data = await response.json();
            const product = data.data;
            setForm({
              productName: product.productName,
              description: product.description,
              price: product.price.toString(),
              totalQuantity: product.totalQuantity.toString(),
              imageUrl: product.imageUrl,
              status: product.status || "ON_SALE"
            });
          } else {
            setErrorMsg("상품 정보를 불러올 수 없습니다.");
          }
        } catch (error) {
          setErrorMsg("상품 정보를 불러오는 중 오류가 발생했습니다.");
        } finally {
          setIsLoadingProduct(false);
        }
      };
      fetchProduct();
    }
  }, [isEditMode, productId]);

  // 인증 상태 확인 후 로딩 중이면 로딩 메시지 표시
  if (isLoading || isLoadingProduct) return <div className="bg-gray-200 flex items-center justify-center w-screen h-screen">로딩 중...</div>;

  // 인증되지 않은 상태면 아무것도 렌더링하지 않음
  if (!isAuthenticated) return null;

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const productData = {
      productName: form.productName.trim(),
      description: form.description.trim(),
      price: parseInt(form.price),
      totalQuantity: parseInt(form.totalQuantity),
      imageUrl: form.imageUrl.trim(),
      status: form.status
    };

    try {
      const url = isEditMode
        ? `http://localhost:8080/api/products/${productId}`
        : "http://localhost:8080/api/products";

      const method = isEditMode ? "PUT" : "POST";

      const response = await fetch(url, {
        method: method,
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(productData),
      });

      if (response.ok) {
        const data = await response.json();
        setForm({
          productName: data.data.productName,
          description: data.data.description,
          price: data.data.price.toString(),
          totalQuantity: data.data.totalQuantity.toString(),
          imageUrl: data.data.imageUrl,
          status: data.data.status || "ON_SALE",
        });
        alert(isEditMode ? "상품이 성공적으로 수정되었습니다." : "상품이 성공적으로 추가되었습니다.");
        router.push("/products/list");
      } else {
        const errorData = await response.json();
        setErrorMsg(errorData.msg || (isEditMode ? "상품 수정에 실패했습니다." : "상품 등록에 실패했습니다."));
      }
    } catch (error: any) {
      setErrorMsg("네트워크 오류가 발생했습니다: " + error.message);
    }
  };

  return (
    <div className="bg-gray-200 pt-20 min-h-screen w-full flex items-center justify-center px-4">
      <div className="bg-white p-10 rounded-xl shadow-lg w-full max-w-lg">
        <h1 className="text-3xl font-bold text-center mb-8">{isEditMode ? "상품 수정" : "상품 등록"}</h1>

        {errorMsg && (
          <div className="bg-red-200 text-red-800 text-xs text-center px-3 py-1 mb-4 rounded-sm">
            {errorMsg}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="mb-6">
            <label
              htmlFor="productName"
              className="block mb-2 font-semibold text-gray-700"
            >
              상품명
            </label>
            <input
              type="text"
              id="productName"
              name="productName"
              placeholder="예: Columbia Nariñó"
              value={form.productName}
              onChange={handleChange}
              required
              className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-gray-600"
            />
          </div>

          <div className="mb-6">
            <label
              htmlFor="description"
              className="block mb-2 font-semibold text-gray-700"
            >
              설명
            </label>
            <input
              type="text"
              id="description"
              name="description"
              placeholder="예: 커피콩"
              value={form.description}
              onChange={handleChange}
              required
              className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-gray-600"
            />
          </div>

          <div className="mb-6">
            <label
              htmlFor="price"
              className="block mb-2 font-semibold text-gray-700"
            >
              가격 (원)
            </label>
            <input
              type="number"
              id="price"
              name="price"
              placeholder="예: 5000"
              value={form.price}
              onChange={handleChange}
              required
              className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-gray-600"
            />
          </div>

          <div className="mb-6">
            <label
              htmlFor="totalQuantity"
              className="block mb-2 font-semibold text-gray-700"
            >
              재고 수량
            </label>
            <input
              type="number"
              id="totalQuantity"
              name="totalQuantity"
              placeholder="예: 100"
              value={form.totalQuantity}
              onChange={handleChange}
              required
              className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-gray-600"
            />
          </div>

          <div className="mb-8">
            <label
              htmlFor="imageUrl"
              className="block mb-2 font-semibold text-gray-700"
            >
              이미지 URL
            </label>
            <input
              type="url"
              id="imageUrl"
              name="imageUrl"
              placeholder="예: https://i.imgur.com/HKOFQYa.jpeg"
              value={form.imageUrl}
              onChange={handleChange}
              required
              className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-gray-600"
            />
          </div>

          <button
            type="submit"
            className="w-full bg-gray-800 text-white py-3 rounded-md hover:bg-gray-700 transition-colors cursor-pointer font-semibold"
          >
            {isEditMode ? "상품 수정" : "상품 추가"}
          </button>
        </form>
      </div>
    </div>
  );
}
