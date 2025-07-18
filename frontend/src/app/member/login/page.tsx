'use client'; // 꼭 필요함 (Client Component로 선언)

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';

export default function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMsg, setErrorMsg] = useState('');
    const { setIsAuthenticated } = useAuth();
    const router = useRouter();

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();

        const formData = new URLSearchParams();
        formData.append('username', username);
        formData.append('password', password);

        const response = await fetch('/api/member/login', {
            method: 'POST',
            body: formData,
            credentials: 'include', // 세션 기반 로그인 유지
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
        });

        if (response.ok) {
            setIsAuthenticated(true); // 로그인 성공 시 인증 상태 업데이트
            router.push('/products/list'); // 상품 목록 페이지로 이동
        } else {
            setErrorMsg('ID 혹은 비밀번호를 잘못 입력하셨거나 등록되지 않은 ID 입니다.');
        }
    };

    // UI 렌더링하는 부분
    return (
        <div className="bg-gray-200 pt-20 min-h-screen w-full flex items-center justify-center px-4">
          <div className="bg-white rounded-2xl shadow-lg w-full max-w-md p-10">
            <div className="mb-5 text-center">
              <h5 className="font-bold text-xl">관리자 로그인</h5>
              <hr className="mt-3 border-gray-300" />
            </div>

            {errorMsg && (
              <div className="bg-red-200 text-red-800 text-xs text-center px-3 py-1 mb-4 rounded-sm">
                {errorMsg}
              </div>
            )}

            <form onSubmit={handleLogin}>
              <input
                type="text"
                name="username"
                placeholder="ID를 입력하세요"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
                className="w-full mb-4 px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-700"
              />
              <input
                type="password"
                name="password"
                placeholder="비밀번호를 입력하세요"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                className="w-full mb-6 px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-700"
              />

              <button
                type="submit"
                className="w-full bg-gray-800 hover:bg-gray-900 text-white py-3 rounded-lg text-lg cursor-pointer font-semibold transition-colors"
              >
                로그인
              </button>
            </form>
          </div>
        </div>
    );

}