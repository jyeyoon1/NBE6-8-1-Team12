'use client';

import { useEffect, useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { useAuth } from "@/contexts/AuthContext";

import { PageResponseDto, PurchaseAdmDto } from '@/purchase/types/purchase-response'

export default function PurchaseListPage() {
    const router = useRouter();
    const { isAuthenticated, isLoading } = useAuth();

    const searchParams = useSearchParams();
    const currentPage = parseInt(searchParams.get("page") || "0");

    const [purchases, setPurchases] = useState<PurchaseAdmDto[]>([]);
    const [pageInfo, setPageInfo] = useState({
        pageNumber: 0,
        totalPages: 0,
        totalElements: 0,
        isLast: true,
    });

    // 관리자 로그인
    useEffect(() => {
        if (!isLoading && !isAuthenticated) {
          router.push('/login');
        }
    }, [isAuthenticated, isLoading ]);

    useEffect(() => {
        const fetchPurchases = async () => {
            try {
                const res = await fetch(`http://localhost:8080/api/purchases?page=${currentPage}&size=5`);
                
                if (!res.ok) throw new Error('구매 목록 불러오기 실패');
                
                const json: PageResponseDto<PurchaseAdmDto> = await res.json();
                setPurchases(json.content);
                setPageInfo({
                    pageNumber: json.pageNumber,
                    totalPages: json.totalPages,
                    totalElements: json.totalElements,
                    isLast: json.isLast,
                });

            } catch(err) {
                console.error('주문 목록 조회 실패:', err);
            }
        };
        fetchPurchases();
    }, [currentPage]);

    if (!isAuthenticated) {
        return (
            <div className="p-8 max-w-lg mx-auto">
                <p className="text-gray-500 font-medium">접근 권한이 없습니다.</p>
                <p className="text-gray-500 font-medium">로그인 후 이용해주세요.</p>
                <button onClick={() => router.push('/member/login')} className="mt-4 px-4 py-2 bg-gray-800 text-white rounded hover:bg-gray-700 transition-colors">
                    로그인 페이지로 이동
                </button>
            </div>
        );
    }

    if (!purchases) return <div>Loading...</div>;

    return (
        <div
            className="bg-gray-200 min-h-screen w-full flex items-center justify-center px-4"
            style={{ paddingTop: 20, paddingBottom: 20 }}
        >
            <div
                className="bg-white rounded-xl shadow-lg"
                style={{
                    width: "1020px",
                    minWidth: "1020px",
                    maxWidth: "1020px",
                    padding: "2.5rem",
                }}
            >
                <h1 className="text-3xl font-bold text-center mb-8">주문 목록</h1>
                <div>
                    <table
                        className="w-full leading-normal"
                        style={{ tableLayout: "fixed" }}
                    >
                        <colgroup>
                            <col style={{ width: "80px" }} />
                            <col style={{ width: "180px" }} />
                            <col style={{ width: "120px" }} />
                            <col style={{ width: "220px" }} />
                            <col style={{ width: "80px" }} />
                            <col style={{ width: "140px" }} />
                            <col style={{ width: "120px" }} />
                        </colgroup>
                        <thead>
                            <tr className="bg-gray-100 text-gray-600 uppercase text-sm">
                                <th className="px-2 py-3 text-xs font-bold text-center" style={{ fontSize: "13px", textAlign: "center" }}>주문번호</th>
                                <th className="px-2 py-3 text-xs font-bold text-center" style={{ textAlign: "center" }}>구매자 이메일</th>
                                <th className="px-2 py-3 text-xs font-bold text-center" style={{ textAlign: "center" }}>
                                    <span className="block">구매일시</span>
                                </th>
                                <th className="px-2 py-3 text-xs font-bold text-center" style={{ textAlign: "center" }}>구매 제품</th>
                                <th className="px-2 py-3 text-xs font-bold text-center" style={{ textAlign: "center" }}>
                                    <span className="block">총 구매</span>
                                    <span className="block">개수</span>
                                </th>
                                <th className="px-2 py-3 text-xs font-bold text-center" style={{ textAlign: "center" }}>총 결제금액</th>
                                <th className="px-2 py-3 text-xs font-bold text-center" style={{ textAlign: "center" }}>상태</th>
                            </tr>
                        </thead>
                        <tbody>
                            {purchases.length === 0 ? (
                                <tr>
                                    <td colSpan={7} className="text-center py-8 text-gray-500">
                                        구매 내역이 없습니다.
                                    </td>
                                </tr>
                            ) : (
                                purchases.slice(0, 10).map((purchase, idx) => {
                                    // 구매일시 줄바꿈: 날짜\n시간
                                    let dateStr: React.ReactNode = "-";
                                    if (purchase.purchaseDate) {
                                        const dateObj = new Date(purchase.purchaseDate);
                                        const datePart = dateObj.toLocaleDateString('ko-KR', {
                                            year: 'numeric',
                                            month: '2-digit',
                                            day: '2-digit',
                                        });
                                        const timePart = dateObj.toLocaleTimeString('ko-KR', {
                                            hour: '2-digit',
                                            minute: '2-digit',
                                        });
                                        dateStr = (
                                            <>
                                                <span>{datePart}</span>
                                                <br />
                                                <span>{timePart}</span>
                                            </>
                                        );
                                    }
                                    // 행간 줄 색상: 짝수행/홀수행 구분
                                    const rowBg =
                                        idx % 2 === 0
                                            ? "bg-white"
                                            : "bg-gray-50";
                                    return (
                                        <tr
                                            key={purchase.purchaseId}
                                            className={`border-b border-gray-200 hover:bg-gray-100 ${rowBg}`}
                                        >
                                            {/* 주문번호 */}
                                            <td className="px-2 py-4 text-xs font-bold text-center" style={{ fontSize: "13px" }}>
                                                {purchase.purchaseId}
                                            </td>
                                            {/* 구매자 이메일 */}
                                            <td className="px-2 py-4 text-sm text-left">
                                                {purchase.userEmail}
                                            </td>
                                            {/* 구매일시 */}
                                            <td className="px-2 py-4 text-xs text-left" style={{ fontSize: "13px", whiteSpace: "pre-line" }}>
                                                {dateStr}
                                            </td>
                                            {/* 구매제품 */}
                                            <td
                                                className="px-2 py-4 text-sm break-words text-left"
                                                style={{
                                                    maxWidth: 200,
                                                    whiteSpace: "normal",
                                                    overflow: "hidden",
                                                    textOverflow: "ellipsis",
                                                    wordBreak: "break-all",
                                                }}
                                                title={purchase.summaryName ?? '-'}
                                            >
                                                {purchase.summaryName ?? '-'}
                                            </td>
                                            {/* 총 구매 개수 */}
                                            <td className="px-2 py-4 text-sm text-right">
                                                {purchase.totalQuantity ?? '-'}
                                            </td>
                                            {/* 총 결제금액 */}
                                            <td className="px-2 py-4 text-sm text-right">
                                                {purchase.totalPrice?.toLocaleString() ?? '-'}원
                                            </td>
                                            {/* 상태 */}
                                            <td className="px-2 py-4 text-sm text-center">
                                                {(() => {
                                                    let statusLabel = '';
                                                    switch (purchase.purchaseStatus) {
                                                        case 'TEMPORARY':
                                                            statusLabel = '임시 저장';
                                                            break;
                                                        case 'PURCHASED':
                                                            statusLabel = '주문 완료';
                                                            break;
                                                        case 'CANCELED':
                                                            statusLabel = '주문 취소';
                                                            break;
                                                        case 'FAILED':
                                                            statusLabel = '주문 오류';
                                                            break;
                                                        default:
                                                            statusLabel = purchase.purchaseStatus;
                                                    }
                                                    const statusClass =
                                                        purchase.purchaseStatus === 'PURCHASED'
                                                            ? 'bg-green-100 text-green-600'
                                                            : purchase.purchaseStatus === 'CANCELED'
                                                            ? 'bg-red-100 text-red-600'
                                                            : purchase.purchaseStatus === 'FAILED'
                                                            ? 'bg-yellow-100 text-yellow-600'
                                                            : 'bg-gray-100 text-gray-600';
                                                    return (
                                                        <span
                                                            className={`px-2 py-1 font-semibold rounded-full leading-tight ${statusClass}`}
                                                        >
                                                            {statusLabel}
                                                        </span>
                                                    );
                                                })()}
                                            </td>
                                        </tr>
                                    );
                                })
                            )}
                        </tbody>
                    </table>

                    {/* 페이징 UI */}
                    <div className="flex justify-center items-center mt-8 space-x-2">
                        <button
                            onClick={() => {
                            if (pageInfo.pageNumber > 0) {
                                router.push(`/purchase/list?page=${pageInfo.pageNumber - 1}`);
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
                        <span className="px-3 py-2 bg-blue-500 text-white rounded font-bold">
                            {pageInfo.pageNumber + 1}
                        </span>
                        <button
                            onClick={() => {
                            if (!pageInfo.isLast) {
                                router.push(`/purchase/list?page=${pageInfo.pageNumber + 1}`);
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
        </div>
    );
}