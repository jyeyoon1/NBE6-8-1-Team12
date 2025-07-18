"use client";

export default function PgForm({ optionName }: { optionName: string }) {
  return (
    <div className="text-center p-4 border border-gray-300 rounded-lg">
      <p className="font-semibold">{optionName}(으)로 결제를 진행합니다.</p>
      <p className="text-sm text-gray-500 mt-2">'완료하기' 버튼을 눌러주세요.</p>
    </div>
  );
}