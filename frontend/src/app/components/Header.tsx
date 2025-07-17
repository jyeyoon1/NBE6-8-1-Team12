'use client';

import Link from 'next/link';
import Image from 'next/image';
import { useState, useEffect } from 'react';

// 예시: 로그인 상태 확인 (실제로는 세션, 쿠키, auth 라이브러리 등으로 구현해야 함)
const mockAuthCheck = () => {
  // true: 로그인 상태, false: 비로그인 상태
  return false;
};

export default function Header() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    // 실제론 쿠키, 세션, fetch 등으로 로그인 여부 확인
    setIsAuthenticated(mockAuthCheck());
  }, []);

  return (
    <header className="w-full fixed top-0 left-0 bg-white/70 text-[#4a3b31] shadow-md z-50 backdrop-blur-sm">
      <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center gap-4">
        {/* 로고 + 설명 */}
        <div className="flex items-center space-x-3">
          <Image
            src="https://i.postimg.cc/PJDhy8d9/nbe6-8-team12-caffe-logo.png"
            alt="Caffe Logo"
            width={40}
            height={40}
            className="object-contain"
          />
          <h5 className="text-base">Caffe 메뉴 서비스 입니다.</h5>
        </div>

        {/* 네비게이션 */}
        <div className="flex items-center text-sm font-medium gap-4">
          {!isAuthenticated ? (
            <>
              <Link href="/" className="hover:text-gray-500 transition-colors">홈</Link>
              <span className="text-gray-400">|</span>
              <Link href="/order/form" className="hover:text-gray-500 transition-colors">주문 내역</Link>
              <span className="text-gray-400">|</span>
              <Link href="/member/login" className="hover:text-gray-500 transition-colors">관리자 로그인</Link>
            </>
          ) : (
            <>
              <Link href="/products/list" className="hover:text-gray-500 transition-colors">상품 목록</Link>
              <span className="text-gray-400">|</span>
              <Link href="/products/add" className="hover:text-gray-500 transition-colors">상품 등록</Link>
              <span className="text-gray-400">|</span>
              <Link href="/orders" className="hover:text-gray-500 transition-colors">주문 목록</Link>
              <span className="text-gray-400">|</span>
              <Link href="/logout" className="hover:text-gray-500 transition-colors">로그아웃</Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
}