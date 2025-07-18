"use client";

export default function PgForm({ paymentInfo }) {
  return (
    <div className="text-center p-4 border border-gray-300 rounded-lg">
      <p className="font-semibold">{paymentInfo.optionName}(으)로 결제를 진행합니다.</p>
    </div>
  );
}