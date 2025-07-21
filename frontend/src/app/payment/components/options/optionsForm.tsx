"use client";

import React, { useEffect, useState } from "react";
import { PaymentOptionGroup, PaymentOption } from "@/payment/types/paymentData";

export default function OptionsForm({ onUpdate, onCancel, apiUrl }: { onUpdate: (option: PaymentOption) => void, onCancel: () => void, apiUrl: string }) {
    const [groupOptions, setGroupOptions] = useState<PaymentOptionGroup[]>([]);
    const [selectedGroup, setSelectedGroup] = useState<PaymentOptionGroup | null>(null);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchAllOptions = async () => {
            setIsLoading(true);
            try {
                const response = await fetch(`${apiUrl}/api/v1/payments/options`);
                if (!response.ok) {
                    setError('옵션 데이터를 불러오는 중 오류가 발생했습니다.');
                }
                const data = await response.json();
                setGroupOptions(data);
                console.log(data);
            } catch (error) {
                setError(error instanceof Error ? error.message : '결제 옵션을 불러올 수 없습니다.');
            } finally {
                setIsLoading(false);
            }
        };
        fetchAllOptions();
    }, [apiUrl]);

    const handleGroupSelect = (group: PaymentOptionGroup) => {
        console.log(group);
        setSelectedGroup(group);
    };

    if (isLoading) {
        return <div>로딩 중...</div>;
    }
    if (error) {
        return <div className="text-red-500 text-center">{error}</div>;
    }

    return (
        <div className="w-full animate-fade-in">
            <h1 className="text-lg font-semibold mb-4">결제 수단 변경</h1>
            {!selectedGroup ? (
                //상위 그룹 선택
                <div className="space-y-2">
                    {groupOptions.map((group) => (
                        <button key={group.optionType} onClick={() => handleGroupSelect(group)} className="w-full text-left p-3 border rounded-lg hover:bg-gray-100">
                            {group.optionType}
                        </button>
                    ))}
                </div>
            ) : (
                //하위 옵션 선택
                <div className="space-y-2">
                    <button onClick={() => setSelectedGroup(null)} className="text-sm text-blue-600 mb-2">뒤로가기</button>
                    {selectedGroup.options.map((opt: PaymentOption) => (
                        <button key={opt.id} onClick={() => onUpdate(opt)} className="w-full text-left p-3 border rounded-lg hover:bg-gray-100">
                            {opt.name}
                        </button>
                    ))}
                </div>
            )}
            <button onClick={onCancel} className="w-full mt-4 text-sm text-center text-gray-600 hover:underline">변경 취소</button>
        </div>
    );
}