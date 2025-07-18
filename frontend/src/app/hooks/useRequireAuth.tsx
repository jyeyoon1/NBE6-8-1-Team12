"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/contexts/AuthContext";

/**
 * 인증이 필요한 페이지에서 호출
 * - 인증 안 된 상태면 로그인 페이지로 리디렉션
 * - 인증 상태와 로딩 상태를 반환
 */
export const useRequireAuth = () => {
  const router = useRouter();
  const { isAuthenticated, isLoading } = useAuth();

  useEffect(() => {
    if (!isLoading && !isAuthenticated) {
      router.replace("/member/login");
    }
  }, [isAuthenticated, isLoading, router]);

  return { isAuthenticated, isLoading };
};