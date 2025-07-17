'use client';

import Link from 'next/link';
import Image from 'next/image';
import { useAuth } from '@/contexts/AuthContext';

export default function Header() {
  const { isAuthenticated, isLoading, logout } = useAuth();

  return (
    <header className="w-full fixed top-0 left-0 bg-white/70 text-[#4a3b31] shadow-md z-50 backdrop-blur-sm">
      <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center gap-4">
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

        <div className="flex items-center text-sm font-medium gap-4">
          {isLoading ? (
            // 로딩 중일 때 스켈레톤 UI
            <div className="flex items-center gap-4">
              <div className="w-8 h-4 bg-gray-200 animate-pulse rounded"></div>
              <span className="text-gray-400">|</span>
              <div className="w-12 h-4 bg-gray-200 animate-pulse rounded"></div>
              <span className="text-gray-400">|</span>
              <div className="w-16 h-4 bg-gray-200 animate-pulse rounded"></div>
            </div>
          ) : !isAuthenticated ? (
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
              <button type="button" onClick={logout} className="hover:text-gray-500 transition-colors cursor-pointer">로그아웃</button>
            </>
          )}
        </div>
      </div>
    </header>
  );
}