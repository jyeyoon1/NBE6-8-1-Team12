"use client";

import Link from "next/link";
import Image from "next/image";
import { useAuth } from "@/contexts/AuthContext";

export default function Header() {
  const { isAuthenticated, isLoading, logout } = useAuth();

  // 로그인 여부에 따라 이동할 링크 설정
  const homeLink = isAuthenticated ? "/products/list" : "/";

  return (
    <header className="w-full fixed top-0 left-0 bg-white/70 text-[#4a3b31] shadow-md z-50 backdrop-blur-sm">
      <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center gap-4">
        <Link href={homeLink}>
          <div className="flex items-center space-x-3 cursor-pointer">
            <Image
              src="/team12.png"
              alt="Caffe Logo"
              width={40}
              height={40}
              className="object-contain"
            />
            <h5 className="text-base">Team | 12</h5>
          </div>
        </Link>
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
              <Link href="/products/list" className="hover:text-gray-500 transition-colors">
                상품 목록
              </Link>
              <span className="text-gray-400">|</span>
              <Link
                href="/cart"
                className="hover:text-gray-500 transition-colors"
              >
                장바구니
              </Link>
              <span className="text-gray-400">|</span>
              <Link
                href="/purchase/lookup"
                className="hover:text-gray-500 transition-colors"
              >
                주문 내역
              </Link>
              <span className="text-gray-400">|</span>
              <Link
                href="/member/login"
                className="hover:text-gray-500 transition-colors"
              >
                관리자 로그인
              </Link>
            </>
          ) : (
            <>
              <Link
                href="/products/list"
                className="hover:text-gray-500 transition-colors"
              >
                상품 목록
              </Link>
              <span className="text-gray-400">|</span>
              <Link
                href="/products/form"
                className="hover:text-gray-500 transition-colors"
              >
                상품 등록
              </Link>
              <span className="text-gray-400">|</span>
              <Link
                href="/purchase/list"
                className="hover:text-gray-500 transition-colors"
              >
                주문 목록
              </Link>
              <span className="text-gray-400">|</span>
              <Link
                href="/shipping/list"
                className="hover:text-gray-500 transition-colors"
              >
                배송 목록
              </Link>
              <span className="text-gray-400">|</span>
              <Link
                href="/payment"
                className="hover:text-gray-500 transition-colors"
              >
                결제 내역
              </Link>
              <span className="text-gray-400">|</span>
              <button
                type="button"
                onClick={logout}
                className="hover:text-gray-500 transition-colors cursor-pointer"
              >
                로그아웃
              </button>
            </>
          )}
        </div>
      </div>
    </header>
  );
}
